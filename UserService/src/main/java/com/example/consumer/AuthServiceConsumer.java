package com.example.consumer;

import com.example.entities.User;
import com.example.repositories.UserRepo;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class AuthServiceConsumer {

    @Autowired
    private UserRepo userRepo;

    @KafkaListener(topics = "${spring.kafka.topic.name}",groupId = "${spring.kafka.consumer.group-id}")
    public void listen(ConsumerRecord<?, ?> record) {
        try {
            String json = record.value().toString();
            System.out.println(json);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
}
