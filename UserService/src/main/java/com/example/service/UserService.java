package com.example.service;

import com.example.entities.User;
import com.example.model.UserDto;
import com.example.repositories.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.function.Supplier;
import java.util.function.UnaryOperator;

@Service
public class UserService {

    @Autowired
    private UserRepo userRepo;

    public UserDto createOrUpdateUser(UserDto userDto) {
        UnaryOperator<User> updateUser = (user) -> {
            User updatedUser = userDto.transformToUserInfo(user);
            updatedUser.setId(user.getId()); // preserve DB id
            return userRepo.save(updatedUser);
        };

        Supplier<User> createUser = () -> userRepo.save(userDto.transformToUserInfo(new User()));

        User user = userRepo.findByUserId(userDto.getUserId())
                .map(updateUser)
                .orElseGet(createUser);
        return UserDto.builder()
                .userId(user.getUserId())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .phoneNumber(user.getPhoneNumber())
                .email(user.getEmail())
                .profilePic(user.getProfilePic())
                .build();
    }

    public UserDto getUser(UserDto userInfoDto) throws Exception {
        Optional<User> userDtoOpt = userRepo.findByUserId(userInfoDto.getUserId());
        if (userDtoOpt.isEmpty()) {
            throw new Exception("User not found");
        }
        User user = userDtoOpt.get();
        return UserDto.builder()
                .userId(user.getUserId())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .phoneNumber(user.getPhoneNumber())
                .email(user.getEmail())
                .profilePic(user.getProfilePic())
                .build();
    }
}
