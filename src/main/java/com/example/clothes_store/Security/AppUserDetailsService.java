package com.example.clothes_store.Security;

import com.example.clothes_store.Model.Entity.User;
import com.example.clothes_store.Repository.UserRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@AllArgsConstructor
public class AppUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    @Transactional(readOnly = true)
    public UserDetails loadUserByUsername(String userName) throws UsernameNotFoundException{

        log.info("authentication user with username [{}]", userName);
        User user = userRepository.findByUserName(userName).orElseThrow(
                ()-> new UsernameNotFoundException("No user found with username  [" + userName + "]")
        );

        return org.springframework.security.core.userdetails.User.builder()
                .username(user.getUserName())
                .password(user.getPassword())
                .authorities(
                        List.of(user.getRole())
                                .stream()
                                .map(role -> new SimpleGrantedAuthority("ROLE_"+role.name()))
                                .toList()
                )
                .build();
    }
}
