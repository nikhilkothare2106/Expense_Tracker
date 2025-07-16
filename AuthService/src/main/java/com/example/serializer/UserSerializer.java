package com.example.serializer;

import com.example.model.UserEventDto;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.kafka.common.serialization.Serializer;

public class UserSerializer implements Serializer<UserEventDto> {

    @Override
    public byte[] serialize(String s, UserEventDto userDto) {
        ObjectMapper mapper = new ObjectMapper();
        byte[] bytes = null;
        try {
            bytes = mapper.writeValueAsString(userDto).getBytes();

        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        return bytes;
    }
}
