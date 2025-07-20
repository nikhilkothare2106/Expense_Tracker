package com.example.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Expense {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String externalId;
    private String amount;
    private String userId;
    private String merchant;
    private String currency;
    private Timestamp createdAt;
    private Timestamp modifiedAt;

    @PrePersist
    @PreUpdate
    private void generateExternalIdAndTimestamp(){
        if(this.externalId == null){
            this.externalId = UUID.randomUUID().toString();
        }
        if(this.createdAt == null){
            this.createdAt = new Timestamp(System.currentTimeMillis());
            this.modifiedAt = this.createdAt;
        }
        else{
            this.modifiedAt = new Timestamp(System.currentTimeMillis());
        }
    }
}
