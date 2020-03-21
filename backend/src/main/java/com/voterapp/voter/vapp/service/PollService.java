package com.voterapp.voter.vapp.service;

import com.voterapp.voter.vapp.dto.model.PollDto;

import java.time.Instant;
import java.util.List;

public interface PollService {

    List<PollDto> findAllPolls();
    PollDto createPoll(PollDto pollDto);
    PollDto endPoll(Long pollId);
    PollDto alterPollTime(Long pollId, Instant newPollExpTime);
    void deletePoll(Long id);
    PollDto getPollById(Long id);
    List<PollDto> getPollsByCreator(String username);
}
