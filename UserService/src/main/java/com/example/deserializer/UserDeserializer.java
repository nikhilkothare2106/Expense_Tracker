package com.example.deserializer;

import com.example.entities.User;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.kafka.common.serialization.Deserializer;

import java.io.IOException;

public class UserDeserializer implements Deserializer<User> {

    @Override
    public User deserialize(String s, byte[] bytes) {
        ObjectMapper mapper = new ObjectMapper();
        User user = null;
        try {
            user = mapper.readValue(bytes,User.class);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return user;
    }
}
