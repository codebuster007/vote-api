package com.voterapp.voter.vapp.dto.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AccessLevel;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.time.Instant;
import java.util.List;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL;

@Data
@Accessors(chain = true)
@JsonInclude(NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class PollDto implements Serializable {

    @Setter(AccessLevel.NONE)
    @Getter(AccessLevel.NONE)
    private static final long serialVersionUID = -2327894120477011575L;

    private Long pollId;
    private String question;
    private List<OptionDto> options;
    private Long totalVotes;
    private String createdBy;
    private Instant createdAt;
    private Instant expirationDateTime;
    private Boolean isExpired;

//    private Long selectedChoice;

}
