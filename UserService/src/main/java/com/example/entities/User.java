package com.example.entities;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.*;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
@ToString
public class User {

    Long id;

    @Id
    private String userId;
    private String firstName;
    private String lastName;
    private Long phoneNumber;
    private String email;
//    private String profilePic;
}
