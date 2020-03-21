package com.voterapp.voter.vapp.controller.v1.request;

import lombok.Data;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotNull;

@Data
@Accessors(chain = true)
public class VoteRequest {
    @NotNull
    private Long optionId;
}
