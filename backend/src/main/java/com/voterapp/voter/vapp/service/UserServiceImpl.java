package com.voterapp.voter.vapp.service;

import com.voterapp.voter.vapp.dto.model.UserDto;
import com.voterapp.voter.vapp.exception.EntityType;
import com.voterapp.voter.vapp.exception.ExceptionType;
import com.voterapp.voter.vapp.exception.VAppException;
import com.voterapp.voter.vapp.model.User;
import com.voterapp.voter.vapp.repository.UserRepository;
import org.modelmapper.ModelMapper;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.voterapp.voter.vapp.exception.EntityPropertyType.EMAIL;
import static com.voterapp.voter.vapp.exception.EntityPropertyType.USERNAME;
import static com.voterapp.voter.vapp.exception.ExceptionType.*;

@Service
public class UserServiceImpl implements UserService {

    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    private final UserRepository userRepository;

    private final ModelMapper modelMapper;

    public UserServiceImpl(BCryptPasswordEncoder bCryptPasswordEncoder,
                           UserRepository userRepository,
                           ModelMapper modelMapper) {
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
        this.userRepository = userRepository;
        this.modelMapper = modelMapper;
    }

    /**
     * Register a new User
     *
     * @param userDto
     * @return UserDto
     */
    @Override
    public UserDto signUp(UserDto userDto) {
        User userEmail = userRepository.findByEmail(userDto.getEmail());
        User userUsername = userRepository.findByUsername(userDto.getUsername());

        if(userEmail == null && userUsername == null){
            userEmail = modelMapper.map(userDto, User.class);
            userEmail.setEncryptedPassword(bCryptPasswordEncoder.encode(userDto.getPassword()));

            return modelMapper.map(userRepository.save(userEmail), UserDto.class);
        }


        if(userEmail != null){
            throw exception(DUPLICATE_ENTITY, EMAIL.getValue(), userDto.getEmail());
        }

        throw exception(DUPLICATE_ENTITY, USERNAME.getValue(), userDto.getUsername());
    }

    /**
     * Update User Profile
     *
     * @param userDto
     * @return UserDto
     */
    @Override
    public UserDto updateProfile(String username, UserDto userDto) {

        Optional<User> user = Optional.ofNullable(userRepository.findByUsername(username));

        if(user.isPresent()){
            User userModel = user.get();
            userModel.setFirstName(userDto.getFirstName())
                    .setLastName(userDto.getLastName())
                    .setEmail(userDto.getEmail());
            return modelMapper.map(userRepository.save(userModel), UserDto.class);
        }
        throw exception(ENTITY_NOT_FOUND, USERNAME.getValue(),  username);
    }

    /**
     * Search an existing user by email
     *
     * @param email
     * @return UserDto
     */
    @Override
    public UserDto findUserByEmail(String email) {
        Optional<User> user = Optional.ofNullable(userRepository.findByEmail(email));

        if(user.isPresent()){
            return modelMapper.map(user.get(), UserDto.class);
        }
        throw exception(ENTITY_NOT_FOUND, EMAIL.getValue(),  email);
    }

    /**
     * Search an existing user by username
     *
     * @param username
     * @return UserDto
     */
    @Override
    public UserDto findUserByUsername(String username) {
        Optional<User> user = Optional.ofNullable(userRepository.findByUsername(username));

        if(user.isPresent()){
            return modelMapper.map(user.get(), UserDto.class);
        }
        throw exception(ENTITY_NOT_FOUND, USERNAME.getValue(), username);
    }

    /**
     * Returns all users with provided username
     *
     * @param username
     * @return List<UserDto>
     */
    @Override
    public List<UserDto> findAllUserByUsername(String username) {
        Optional<List<User>> users = Optional.ofNullable(userRepository.findByUsernameContainingIgnoreCase(username));

        if (users.isPresent()){
            return users.get()
                    .stream()
                    .map(user -> modelMapper.map(user, UserDto.class))
                    .collect(Collectors.toList());
        }
        throw exception(ENTITY_NOT_FOUND, USERNAME.getValue(), username);
    }


    /**
     * Change Password
     *
     * @param userDto
     * @param newPassword
     * @return UserDto
     */
    @Override
    public UserDto changePassword(UserDto userDto, String newPassword) {
        Optional<User> user = Optional.ofNullable(userRepository.findByEmail(userDto.getEmail()));
        if(user.isPresent()){
            User userModel = user.get();
            userModel.setEncryptedPassword(bCryptPasswordEncoder.encode(newPassword));

            return modelMapper.map(userRepository.save(userModel), UserDto.class);
        }
        throw exception(ENTITY_NOT_FOUND, EMAIL.getValue(),  userDto.getEmail());
    }

    /**
     * Returns a new {@link RuntimeException}
     *
     * @param exceptionType
     * @param args
     * @return RuntimeException
     */
    private RuntimeException exception(ExceptionType exceptionType, String... args){
        return VAppException.throwException(EntityType.USER, exceptionType, args);
    }
}
