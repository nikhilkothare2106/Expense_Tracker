package com.example.consumer;

import com.example.dto.ExpenseDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.kafka.common.serialization.Deserializer;

import java.io.IOException;

public class ExpenseDeserializer implements Deserializer<ExpenseDto> {

    @Override
    public ExpenseDto deserialize(String s, byte[] bytes) {
        ObjectMapper mapper = new ObjectMapper();
        ExpenseDto expenseDto = null;
        try {
            expenseDto = mapper.readValue(bytes, ExpenseDto.class);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return expenseDto;
    }
}
