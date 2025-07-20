package com.example.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.*;

import java.sql.Timestamp;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class ExpenseDto {

    private String externalId;
    @NonNull
    private String amount;
    private String userId;
    private String merchant;
    private String currency;
    private Timestamp createdAt;
    private Timestamp modifiedAt;

    public ExpenseDto(String json){
        try{
            ObjectMapper mapper = new ObjectMapper();
            ExpenseDto expenseDto = mapper.readValue(json, ExpenseDto.class);
            this.externalId = expenseDto.getExternalId();
            this.amount = expenseDto.getAmount();
            this.userId = expenseDto.getUserId();
            this.merchant = expenseDto.getMerchant();
            this.currency = expenseDto.getCurrency();
            this.createdAt = expenseDto.getCreatedAt();
            this.modifiedAt = expenseDto.getModifiedAt();
        } catch (Exception e) {
            throw new RuntimeException("Failed to deserialize ExpenseDto from json.", e);
        }
    }

}
