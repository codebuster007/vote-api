package com.voterapp.voter.vapp.service;

import com.voterapp.voter.vapp.dto.model.OptionDto;
import com.voterapp.voter.vapp.dto.model.PollDto;
import com.voterapp.voter.vapp.dto.model.UserDto;
import com.voterapp.voter.vapp.dto.model.VoteDto;
import com.voterapp.voter.vapp.exception.EntityType;
import com.voterapp.voter.vapp.exception.ExceptionType;
import com.voterapp.voter.vapp.exception.VAppException;
import com.voterapp.voter.vapp.model.*;
import com.voterapp.voter.vapp.repository.OptionRepository;
import com.voterapp.voter.vapp.repository.PollRepository;
import com.voterapp.voter.vapp.repository.UserRepository;
import com.voterapp.voter.vapp.repository.VoteRepository;
import org.modelmapper.ModelMapper;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.voterapp.voter.vapp.exception.EntityPropertyType.USERNAME;
import static com.voterapp.voter.vapp.exception.EntityType.*;
import static com.voterapp.voter.vapp.exception.ExceptionType.*;

@Service
public class VoteServiceImpl implements VoteService {

    private final PollRepository pollRepository;

    private final UserRepository userRepository;

    private final OptionRepository optionRepository;

    private final VoteRepository voteRepository;

    private final ModelMapper modelMapper;

    public VoteServiceImpl(PollRepository pollRepository,
                           UserRepository userRepository,
                           OptionRepository optionRepository,
                           VoteRepository voteRepository,
                           ModelMapper modelMapper) {
        this.pollRepository = pollRepository;
        this.userRepository = userRepository;
        this.optionRepository = optionRepository;
        this.voteRepository = voteRepository;
        this.modelMapper = modelMapper;
    }

    @Override
    public VoteDto castVote(Long pollId, Long optionId, UserDto currentUser) {
        Poll poll = pollRepository.findById(pollId)
                .orElseThrow(() -> exception(POLL, ENTITY_NOT_FOUND, String.valueOf(pollId)));

        if(poll.getExpirationDateTime().isBefore(Instant.now())){
            throw exception(POLL, POLL_TIME_EXPIRED);
        }

        User user = Optional.of(userRepository.findByUsername(currentUser.getUsername()))
                .orElseThrow(() -> exception(USER, ENTITY_NOT_FOUND, USERNAME.getValue(), currentUser.getUsername()));

        Option selectedOption = optionRepository.findById(optionId)
                .orElseThrow(() -> exception(OPTION, ENTITY_NOT_FOUND, String.valueOf(optionId)));

        if(!poll.getOptions().contains(selectedOption)){
            throw exception(OPTION, ENTITY_NOT_FOUND, String.valueOf(optionId));
        }

        Vote  vote = new Vote()
                .setPoll(poll)
                .setOption(selectedOption)
                .setUser(user);

        try{
            vote = voteRepository.save(vote);
        }catch (DataIntegrityViolationException e){
            throw VAppException.throwException("User with id - {0} has already voted on Poll with id - {1}",
                    String.valueOf(user.getUserId()),
                    String.valueOf(pollId));
        }

        return convertToVoteDto(poll, user, selectedOption, vote);
    }

    private VoteDto convertToVoteDto(Poll poll, User user, Option selectedOption, Vote vote) {
        List<OptionVoteCount> optionVoteCounts = voteRepository.countByPollIdGroupByOptionId(poll.getPollId());
        Map<Long, Long> optionsVotesMap = optionVoteCounts.stream()
                .collect(Collectors.toMap(OptionVoteCount::getOptionId, OptionVoteCount::getVoteCount));

        PollDto pollDto = modelMapper.map(poll, PollDto.class);

        pollDto.getOptions().forEach(oDto -> {
            Long count = optionsVotesMap.get(oDto.getOptionId());
            oDto.setVoteCount(count == null ? 0L : count);
        });
        pollDto.setTotalVotes(optionVoteCounts.stream()
                    .map(OptionVoteCount::getVoteCount)
                    .reduce(0L, Long::sum))
                .setIsExpired(poll.getExpirationDateTime().isBefore(Instant.now()));

        OptionDto optionDto = new OptionDto()
                .setOptionId(selectedOption.getOptionId())
                .setVoteCount(optionsVotesMap.get(selectedOption.getOptionId()));

        UserDto userDto = new UserDto()
                .setFirstName(user.getFirstName())
                .setLastName(user.getLastName())
                .setUsername(user.getUsername());

        return new VoteDto()
                .setSelectedOption(selectedOption.getOptionId())
                .setPoll(pollDto)
                .setOption(optionDto)
                .setUser(userDto);
    }


    /**
     * Returns a new {@link RuntimeException}
     *
     * @param entityType
     * @param exceptionType
     * @param args
     * @return RuntimeException
     */
    private RuntimeException exception(EntityType entityType, ExceptionType exceptionType, String... args){
        return VAppException.throwException(entityType, exceptionType, args);
    }
}
