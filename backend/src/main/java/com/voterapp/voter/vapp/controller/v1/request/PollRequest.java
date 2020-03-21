package com.voterapp.voter.vapp.controller.v1.request;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@Accessors(chain = true)
@JsonIgnoreProperties(ignoreUnknown = true)
public class PollRequest {

    @NotBlank(message = "{constraints.NotEmpty.message}")
    @Size(max = 40)
    private String question;

    @NotEmpty(message = "{constraints.NotEmpty.message}")
    @Size(min = 2, max = 6)
    @Valid
    private List<OptionRequest> options;

    @NotNull
    @Valid
    private PollDurationRequest expirationDateTime;

}
