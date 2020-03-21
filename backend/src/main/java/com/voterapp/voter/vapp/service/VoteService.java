package com.voterapp.voter.vapp.service;

import com.voterapp.voter.vapp.dto.model.UserDto;
import com.voterapp.voter.vapp.dto.model.VoteDto;

public interface VoteService {

    VoteDto castVote(Long pollId, Long optionId, UserDto currentUser);
}
