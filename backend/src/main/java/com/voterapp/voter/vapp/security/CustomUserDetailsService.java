package com.voterapp.voter.vapp.security;

import com.voterapp.voter.vapp.dto.model.UserDto;
import com.voterapp.voter.vapp.service.UserService;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final UserService userService;

    public CustomUserDetailsService(UserService userService) {
        this.userService = userService;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserDto userDto = userService.findUserByUsername(username);

        if(userDto != null){
            return buildUserForAuthentication(userDto, new ArrayList<>());
        }else{
            throw new UsernameNotFoundException("user with username " + username + " does not exist");
        }
    }

    private UserDetails buildUserForAuthentication(UserDto user, List<GrantedAuthority> authorities) {
        return new UserPrincipal()
                .setPassword(user.getPassword())
                .setUsername(user.getUsername())
                .setAuthorities(authorities);
    }
}
