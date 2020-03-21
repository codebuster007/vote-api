package com.voterapp.voter.vapp.service;

import com.voterapp.voter.vapp.dto.model.UserDto;

import java.util.List;

public interface UserService {

    /**
     * Register a new user
     *
     * @param userDto
     * @return
     */
    UserDto signUp(UserDto userDto);

    /**
     * Update an existing user profile
     *
     * @param username
     * @param userDto
     * @return
     */
    UserDto updateProfile(String username, UserDto userDto);

    /**
     * Find an existing user by email
     *
     * @param email
     * @return
     */
    UserDto findUserByEmail(String email);

    /**
     * Find an existing user by username
     *
     * @param username
     * @return
     */
    UserDto findUserByUsername(String username);

    /**
     * Find all existing users by username
     * @param username
     * @return List<UserDto>
     */
    List<UserDto> findAllUserByUsername(String username);

    /**
     * Change the password of an existing user
     * @param userDto
     * @param newPassword
     * @return
     */
    UserDto changePassword(UserDto userDto, String newPassword);
}
