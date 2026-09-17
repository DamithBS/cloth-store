package com.example.clothes_store.Controller;

import com.example.clothes_store.Controller.DTO.Request.UserRequest;
import com.example.clothes_store.Security.JwtUtil;
import com.example.clothes_store.Service.UserService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collections;
import java.util.Map;

@RestController
@RequestMapping("api/v1/user")
@AllArgsConstructor
@Slf4j
public class UserController {


    private final UserService userService;

    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;

    @PostMapping("/register")
    public ResponseEntity<String> createUser(
            @RequestBody UserRequest userRequest
            ){
        userService.create(userRequest);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body("User created successfully");
    }


    @Transactional(readOnly = true)
    @PostMapping("/login")
    public ResponseEntity<Map<String,String>> login(@RequestBody UserRequest userRequest){
        Authentication authentication =authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(userRequest.getUserName(),userRequest.getPassword())
        );
        log.info(authentication.toString());

        UserDetails userDetails= new User(userRequest.getUserName(),"",authentication.getAuthorities());
        String token = jwtUtil.generateToken(userDetails);

        return ResponseEntity.ok(Collections.singletonMap("token",token));
    }
}
