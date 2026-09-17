package com.example.clothes_store.Service.impl;

import com.example.clothes_store.Controller.DTO.Request.AddressRequest;
import com.example.clothes_store.Controller.DTO.Request.UserRequest;
import com.example.clothes_store.Exception.UserAlreadyExistsException;
import com.example.clothes_store.Model.Entity.Address;
import com.example.clothes_store.Model.Entity.User;
import com.example.clothes_store.Model.Eum.Role;
import com.example.clothes_store.Repository.UserRepository;
import com.example.clothes_store.Service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    

    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    @Override
    public void create(UserRequest userRequest){

        String userEmail= userRequest.getEmail().trim();

        if (userRepository.existsByEmailIgnoreCase(userEmail)){
            throw new UserAlreadyExistsException("This user Already sing in..");
        }

        User user = new User();
        user.setName(userRequest.getName());
        user.setUserName(userRequest.getUserName());
        user.setEmail(userRequest.getEmail());
        user.setPassword(passwordEncoder.encode(userRequest.getPassword()));
        user.setPhone(userRequest.getPhone());
        user.setRole(userRequest.getRole() != null ? userRequest.getRole(): Role.USER );
        user.setCreatedAt(LocalDateTime.now());


        if (userRequest.getAddresses() != null){
            for (AddressRequest addressRequest : userRequest.getAddresses()){

                Address address = new Address();

                address.setStreet(addressRequest.getStreet());
                address.setCity(addressRequest.getCity());
                address.setCountry(addressRequest.getCountry());
                address.setDistrict(addressRequest.getDistrict());
                address.setPostal_code(addressRequest.getPostal_code());
                address.setIsDefault(addressRequest.getIsDefault() != null ? addressRequest.getIsDefault():false);
                address.setUser(user);

                user.getAddresses().add(address);
            }
        }

        userRepository.save(user);
    }
}
