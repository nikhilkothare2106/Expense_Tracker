package com.example.consumer;

import com.example.model.UserDto;
import com.example.repositories.UserRepo;
import com.example.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class AuthServiceConsumer {

    @Autowired
    private UserRepo userRepo;

    @Autowired
    private UserService userService;

    @KafkaListener(topics = "${spring.kafka.topic.name}",groupId = "${spring.kafka.consumer.group-id}")
    public void listen(UserDto userDto) {
        try {
            userService.createOrUpdateUser(userDto);
        }
        catch (Exception e) {
            e.printStackTrace();
            System.out.println("AuthServiceConsumer: Exception is thrown while consuming kafka event");
        }
    }
}
