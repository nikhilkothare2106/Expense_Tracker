package com.example.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserEventDto{

    private String userId;
    private String firstName; // first_name

    private String lastName; //last_name

    private Long phoneNumber;

    private String email; // email
    private String profilePic;
}
