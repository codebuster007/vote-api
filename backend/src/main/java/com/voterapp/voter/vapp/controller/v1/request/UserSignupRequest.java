package com.voterapp.voter.vapp.controller.v1.request;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotEmpty;

@Getter
@Setter
@NoArgsConstructor
@Accessors(chain = true)
@JsonIgnoreProperties(ignoreUnknown = true)
public class UserSignupRequest {

    @NotEmpty(message = "{constraints.NotEmpty.message}")
    private String email;

    @NotEmpty(message = "{constraints.NotEmpty.message}")
    private String firstName;

    @NotEmpty(message = "{constraints.NotEmpty.message}")
    private String lastName;

    @NotEmpty(message = "{constraints.NotEmpty.message}")
    private String username;

    @NotEmpty(message = "{constraints.NotEmpty.message}")
    private String password;
}
