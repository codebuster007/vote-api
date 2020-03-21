package com.voterapp.voter.vapp.controller.v1.request;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

@Getter
@Setter
@Accessors(chain = true)
@JsonIgnoreProperties(ignoreUnknown = true)
public class OptionRequest {

    @NotEmpty(message = "{constraints.NotEmpty.message}")
    @Size(max = 40)
    private String optionText;
}
