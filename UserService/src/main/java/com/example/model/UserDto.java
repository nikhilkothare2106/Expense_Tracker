package com.example.model;

import com.example.entities.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserDto {

    private String userId;
    private String firstName;
    private String lastName;
    private Long phoneNumber;
    private String email;
    private String profilePic;

    public User transformToUserInfo(User user) {
        if (userId != null) user.setUserId(userId);
        if (firstName != null) user.setFirstName(firstName);
        if (lastName != null) user.setLastName(lastName);
        if (phoneNumber != null) user.setPhoneNumber(phoneNumber);
        if (email != null) user.setEmail(email);
        if (profilePic != null) user.setProfilePic(profilePic);

        return user;
    }
}
