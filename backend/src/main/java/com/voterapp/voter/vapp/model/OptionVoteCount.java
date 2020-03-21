package com.voterapp.voter.vapp.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

@Setter
@Getter
@Accessors(chain = true)
@AllArgsConstructor
public class OptionVoteCount {

    private Long optionId;
    private Long voteCount;
}
