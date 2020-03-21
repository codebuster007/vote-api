package com.voterapp.voter.vapp.service;

import com.voterapp.voter.vapp.dto.model.PollDto;
import com.voterapp.voter.vapp.exception.EntityType;
import com.voterapp.voter.vapp.exception.ExceptionType;
import com.voterapp.voter.vapp.exception.VAppException;
import com.voterapp.voter.vapp.model.OptionVoteCount;
import com.voterapp.voter.vapp.model.Poll;
import com.voterapp.voter.vapp.repository.PollRepository;
import com.voterapp.voter.vapp.repository.UserRepository;
import com.voterapp.voter.vapp.repository.VoteRepository;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.voterapp.voter.vapp.exception.EntityPropertyType.USERNAME;
import static com.voterapp.voter.vapp.exception.EntityType.POLL;
import static com.voterapp.voter.vapp.exception.EntityType.USER;
import static com.voterapp.voter.vapp.exception.ExceptionType.ENTITY_NOT_FOUND;
import static com.voterapp.voter.vapp.exception.ExceptionType.POLL_TIME_EXPIRED;

@Service
public class PollServiceImpl implements PollService {

    private final PollRepository pollRepository;

    private final VoteRepository voteRepository;

    private final UserRepository userRepository;

    private final ModelMapper modelMapper;

    public PollServiceImpl(PollRepository pollRepository,
                           VoteRepository voteRepository,
                           UserRepository userRepository,
                           ModelMapper modelMapper) {
        this.pollRepository = pollRepository;
        this.voteRepository = voteRepository;
        this.userRepository = userRepository;
        this.modelMapper = modelMapper;
    }

    @Override
    public List<PollDto> findAllPolls() {
        return pollRepository.findAll()
                .stream()
                .map(poll -> {
                    PollDto pollDto = modelMapper.map(poll, PollDto.class);
                    pollDto.setIsExpired(poll.getExpirationDateTime().isBefore(Instant.now()));
                    insertPollVoteData(pollDto);
                    return pollDto;
                })
                .collect(Collectors.toList());
    }

    @Override
    public PollDto createPoll(PollDto pollDto) {
        Duration timeDiff = Duration.between(Instant.now(), pollDto.getExpirationDateTime());

        if(timeDiff.toMinutes() > Duration.ofMinutes(6L).toMinutes()){
            pollDto.getOptions().forEach(optionDto -> optionDto.setPoll(pollDto));

            Poll poll = modelMapper.map(pollDto, Poll.class);

            PollDto returnPollDto = modelMapper.map(pollRepository.save(poll), PollDto.class);
            insertPollVoteData(returnPollDto);
            return returnPollDto;
        }
        throw VAppException.throwException("Poll Time cannot be less than 5 minutes");
    }

    @Override
    public PollDto endPoll(Long pollId) {
        Poll  poll = pollRepository.findById(pollId).orElseThrow(() ->
                exception(POLL, ENTITY_NOT_FOUND, String.valueOf(pollId)));

        if(poll.getExpirationDateTime().isBefore(Instant.now())){
            throw exception(POLL, POLL_TIME_EXPIRED, "Create a new Poll Session!");
        }

        poll.setExpirationDateTime(Instant.now());

        PollDto pollDto = modelMapper.map(pollRepository.save(poll), PollDto.class);
        pollDto.setIsExpired(poll.getExpirationDateTime().isBefore(Instant.now()));

        insertPollVoteData(pollDto);

        return pollDto;
    }

    @Override
    public PollDto alterPollTime(Long pollId, Instant newPollExpTime) {
        Poll  poll = pollRepository.findById(pollId).orElseThrow(() ->
                exception(POLL, ENTITY_NOT_FOUND, String.valueOf(pollId)));

        if(poll.getExpirationDateTime().isBefore(Instant.now())){
            throw exception(POLL, POLL_TIME_EXPIRED, "Create a new Poll Session!");
        }
        // make sure new time is not less than the current time(now)
        if(newPollExpTime.isBefore(Instant.now())){
            throw VAppException.throwException("new Poll Time cannot be less than elapsed time");
        }

        // new time can only be adjusted to 5minutes greater or equal to now.
        Duration timeDiff = Duration.between(newPollExpTime,
                Instant.now());

        if(timeDiff.toMinutes() < 5){
            throw VAppException.throwException("new Poll Time cannot be less than five minutes from now");
        }
        poll.setExpirationDateTime(newPollExpTime);

        PollDto pollDto = modelMapper.map(pollRepository.save(poll), PollDto.class);
        pollDto.setIsExpired(poll.getExpirationDateTime().isBefore(Instant.now()));

        insertPollVoteData(pollDto);

        return pollDto;
    }

    @Override
    public void deletePoll(Long id) {
        Optional<Poll> poll = pollRepository.findById(id);
        if(poll.isPresent()){
            pollRepository.delete(poll.get());
            return;
        }
        throw exception(POLL, ENTITY_NOT_FOUND, String.valueOf(id));
    }

    @Override
    public PollDto getPollById(Long id) {
        Optional<Poll> poll = pollRepository.findById(id);
        if(poll.isPresent()){
            PollDto pollDto = modelMapper.map(poll.get(), PollDto.class);
            pollDto.setIsExpired(poll.get().getExpirationDateTime().isBefore(Instant.now()));

            insertPollVoteData(pollDto);

            return pollDto;
        }
        throw exception(POLL, ENTITY_NOT_FOUND, String.valueOf(id));
    }

    @Override
    public List<PollDto> getPollsByCreator(String username) {
        Optional.ofNullable(userRepository.findByUsername(username))
                .orElseThrow(() -> exception(USER, ENTITY_NOT_FOUND, USERNAME.getValue(), username));

        List<Poll>  polls = pollRepository.findAllByCreatedBy(username);

        List<PollDto> pollDtos = new ArrayList<>();

        polls.forEach(poll -> pollDtos.add(modelMapper.map(poll, PollDto.class)));

        pollDtos.forEach(pollDto -> {
            pollDto.setIsExpired(pollDto.getExpirationDateTime().isBefore(Instant.now()));
            insertPollVoteData(pollDto);
        });

        return pollDtos;
    }

    private void insertPollVoteData(PollDto pollDto) {

        List<OptionVoteCount> optionVoteCounts = voteRepository.countByPollIdGroupByOptionId(pollDto.getPollId());
        Map<Long, Long> optionsVotesMap = optionVoteCounts.stream()
                .collect(Collectors.toMap(OptionVoteCount::getOptionId, OptionVoteCount::getVoteCount));

        pollDto.getOptions().forEach(oDto -> {
            Long count = optionsVotesMap.get(oDto.getOptionId());
            oDto.setVoteCount(count == null ? 0L : count);
        });

        pollDto.setTotalVotes(optionVoteCounts.stream()
                .map(OptionVoteCount::getVoteCount)
                .reduce(0L, Long::sum));
    }


    /**
     * Returns a new {@link RuntimeException}
     *
     * @param entityType
     * @param args
     * @return RuntimeException
     */
    private RuntimeException exception(EntityType entityType, ExceptionType exceptionType, String... args){
        return VAppException.throwException(entityType, exceptionType, args);
    }
}
