package com.voterapp.voter.vapp.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.voterapp.voter.vapp.dto.model.OptionDto;
import lombok.Data;
import lombok.experimental.Accessors;

import java.time.Instant;
import java.util.List;

@Data
@Accessors(chain = true)
public class PollResponse {
    private Long id;
    private String question;
    private List<OptionDto> options;
    private String createdBy;
    private Instant creationDateTime;
    private Instant expirationDateTime;
    private Boolean isExpired;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Long selectedChoice;
    private Long totalVotes;
}
