package com.example.deserializer;

import com.example.model.UserDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.kafka.common.serialization.Deserializer;

import java.io.IOException;

public class UserDeserializer implements Deserializer<UserDto> {

    @Override
    public UserDto deserialize(String s, byte[] bytes) {

        ObjectMapper mapper = new ObjectMapper();
        UserDto userDto = null;
        try {
            userDto = mapper.readValue(bytes, UserDto.class);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return userDto;
    }
}
