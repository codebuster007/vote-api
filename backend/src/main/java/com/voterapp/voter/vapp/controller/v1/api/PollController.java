package com.voterapp.voter.vapp.controller.v1.api;

import com.voterapp.voter.vapp.controller.v1.request.PollDurationRequest;
import com.voterapp.voter.vapp.controller.v1.request.PollRequest;
import com.voterapp.voter.vapp.controller.v1.request.VoteRequest;
import com.voterapp.voter.vapp.dto.model.PollDto;
import com.voterapp.voter.vapp.dto.model.UserDto;
import com.voterapp.voter.vapp.dto.model.VoteDto;
import com.voterapp.voter.vapp.dto.response.Response;
import com.voterapp.voter.vapp.security.UserPrincipal;
import com.voterapp.voter.vapp.service.PollService;
import com.voterapp.voter.vapp.service.VoteService;
import org.modelmapper.ModelMapper;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.time.Duration;
import java.time.Instant;

@RestController
@RequestMapping("/api/v1/poll")
public class PollController {

    private final PollService pollService;

    private final VoteService voteService;

    private final ModelMapper modelMapper;

    public PollController(PollService pollService,
                          VoteService voteService,
                          ModelMapper modelMapper) {
        this.pollService = pollService;
        this.voteService = voteService;
        this.modelMapper = modelMapper;
    }

    @PostMapping("/create")
    public Response<?> createPoll(@RequestBody @Valid PollRequest pollRequest){
        PollDto pollDto = modelMapper.map(pollRequest, PollDto.class);
        Instant now = Instant.now();
        Instant exDateTime =
                now.plus(Duration.ofDays(pollRequest
                        .getExpirationDateTime()
                        .getDays()))
                        .plus(Duration.ofHours(pollRequest
                                .getExpirationDateTime()
                                .getHours())
                        .plus(Duration.ofMinutes(pollRequest
                                .getExpirationDateTime()
                                .getMinutes())));

        pollDto.setExpirationDateTime(exDateTime);

        return Response.ok().setPayload(pollService.createPoll(pollDto));
    }

    @GetMapping("/all")
    public Response<?> getPolls(){
        return Response.ok().setPayload(pollService.findAllPolls());
    }

    @GetMapping("/{id}")
    public Response<?> getPollById(@PathVariable("id") Long id){
        return Response.ok().setPayload(pollService.getPollById(id));
    }

    @PostMapping("/{pollId}/votes")
    public Response<?> castVote(@AuthenticationPrincipal UserPrincipal currentUser,
                                @PathVariable Long pollId,
                                @Valid @RequestBody VoteRequest voteRequest) {

        UserDto userDto = modelMapper.map(currentUser, UserDto.class);

        VoteDto voteDto = voteService.castVote(pollId, voteRequest.getOptionId(), userDto);
        return Response.ok().setPayload(voteDto);

    }

    @PutMapping("/{id}")
    public Response<?> updatePollTime(@PathVariable("id") Long pollId, @RequestBody PollDurationRequest newPollTime) {
        Instant now = Instant.now();
        Instant newTime = now
                .plus(Duration.ofDays(newPollTime.getDays())
                .plus(Duration.ofHours(newPollTime.getHours()))
                .plus(Duration.ofMinutes(newPollTime.getMinutes())));

        return Response.ok().setPayload(pollService.alterPollTime(pollId, newTime));
    }

    @PostMapping("/{pollId}/end")
    public Response<?> endPolling(@PathVariable("pollId") Long pollId){
        return Response.ok().setPayload(pollService.endPoll(pollId));
    }

    @GetMapping("/users/{currentuser}")
    public Response<?> getPollsByCreator(@PathVariable("currentuser") String currentUser){

        return Response.ok().setPayload(pollService.getPollsByCreator(currentUser));
    }

    @DeleteMapping("/{pollId}")
    public  Response<?> deletePoll(@PathVariable("pollId") Long id){
        pollService.deletePoll(id);

        return Response.ok();
    }
}
