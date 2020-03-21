package com.voterapp.voter.vapp.controller.v1.api;

import com.voterapp.voter.vapp.controller.v1.request.UserSignupRequest;
import com.voterapp.voter.vapp.dto.model.UserDto;
import com.voterapp.voter.vapp.dto.response.Response;
import com.voterapp.voter.vapp.service.UserService;
import org.modelmapper.ModelMapper;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/v1")
public class UserController {

    private final UserService userService;

    private final ModelMapper modelMapper;

    public UserController(UserService userService,
                          ModelMapper modelMapper) {
        this.userService = userService;
        this.modelMapper = modelMapper;
    }

    @PostMapping("/auth/signup")
    public Response<?> signup(@RequestBody @Valid UserSignupRequest userSignupRequest){
        return Response.ok().setPayload(registerUser(userSignupRequest));
    }

    private UserDto registerUser(UserSignupRequest userSignupRequest) {
        UserDto registeredUser = modelMapper.map(userSignupRequest, UserDto.class);

        return userService.signUp(registeredUser);
    }

    @GetMapping("/users/{username}")
    public Response<?> getUser(@PathVariable("username")  String username){
        UserDto foundUser = userService.findUserByUsername(username);
        return Response.ok().setPayload(foundUser);
    }

    @PutMapping("/users/{username}")
    public Response<?> updateUser(@PathVariable("username") String username,
                               @RequestBody UserSignupRequest userSignupRequest){

        UserDto newUser = modelMapper.map(userSignupRequest, UserDto.class);
        UserDto updatedUser = userService.updateProfile(username, newUser);

        return Response.ok().setPayload(updatedUser);
    }

    @GetMapping("/users")
    public Response<?> getAllUsersWithUsername(
            @RequestParam(value = "username", defaultValue = "") String username){

        List<UserDto> userDtos = userService.findAllUserByUsername(username);

        return Response.ok().setPayload(userDtos);
    }

}
