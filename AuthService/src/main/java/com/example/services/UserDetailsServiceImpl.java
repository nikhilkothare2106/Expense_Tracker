package com.example.services;

import com.example.entities.User;
import com.example.eventproducer.UserProducer;
import com.example.model.UserEventDto;
import com.example.repositories.UserRepository;
import com.example.request.SignupRequest;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.UUID;

@Service
@AllArgsConstructor
@Data
public class UserDetailsServiceImpl implements UserDetailsService {
    @Autowired
    private  final UserRepository userRepository;

    @Autowired
    private final BCryptPasswordEncoder passwordEncoder;

    @Autowired
    private UserProducer userProducer;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username);
        if(user == null){
            throw new UsernameNotFoundException("could not found username: " + username);
        }
        return new CustomUserDetails(user);
    }

    public User checkIfUserAlreadyExist(SignupRequest signupRequest){
        return userRepository.findByUsername(signupRequest.getUsername());
    }

    public Boolean signupUser(SignupRequest signupRequest) {
        if (checkIfUserAlreadyExist(signupRequest) != null) {
            return false;
        }

        String encodedPassword = passwordEncoder.encode(signupRequest.getPassword());
        String userId = UUID.randomUUID().toString();
        User build = User.builder()
                .userId(userId)
                .username(signupRequest.getUsername())
                .password(encodedPassword)
                .roles(new HashSet<>())
                .build();
//        userRepository.save(build);


        //push event


        UserEventDto eventDto = UserEventDto.builder()
                .firstName(signupRequest.getFirstName())
                .lastName(signupRequest.getLastName())
                .phoneNumber(signupRequest.getPhoneNumber())
                .email(signupRequest.getEmail())
                .build();
        userProducer.sendEventToKafka(eventDto);

        return true;
    }

}
