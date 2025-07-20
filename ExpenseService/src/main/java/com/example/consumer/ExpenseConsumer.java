package com.example.consumer;

import com.example.dto.ExpenseDto;
import com.example.service.ExpenseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class ExpenseConsumer {

    @Autowired
    private ExpenseService expenseService;

    @KafkaListener(topics = "${spring.kafka.topic.name}",groupId = "${spring.kafka.consumer.group-id}")
    public void listen(ExpenseDto expenseDto){
        try {
            expenseService.createExpense(expenseDto);
        }
        catch (Exception e) {
            System.out.println("Exception in listening the event.");
            e.printStackTrace();
        }
    }
}
