package com.example.service;

import com.example.dto.ExpenseDto;
import com.example.entities.Expense;
import com.example.repository.ExpenseRepo;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.logging.log4j.util.Strings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class ExpenseService {

    @Autowired
    private ExpenseRepo expenseRepo;

    private ObjectMapper mapper = new ObjectMapper();

    public boolean createExpense(ExpenseDto expenseDto) {
        setCurrency(expenseDto);
        try{
            expenseRepo.save(mapper.convertValue(expenseDto,Expense.class));
            return true;
        }
        catch(Exception e){
            return false;
        }
    }

    public boolean updateExpense(ExpenseDto expenseDto) {
        Optional<Expense> byUserIdAndExternalId = expenseRepo.findByUserIdAndExternalId(
                expenseDto.getUserId(), expenseDto.getExternalId());

        if (byUserIdAndExternalId.isEmpty()) {
            return false;
        }

        Expense expense = byUserIdAndExternalId.get();

        // Update only non-null fields from DTO

            expense.setAmount(expenseDto.getAmount());


        if (expenseDto.getMerchant() != null) {
            expense.setMerchant(expenseDto.getMerchant());
        }

        if (expenseDto.getCurrency() != null) {
            expense.setCurrency(expenseDto.getCurrency());
        }

        // Save the updated expense
        expenseRepo.save(expense);

        return true;
    }

    public List<ExpenseDto> getExpenses(String userId){
        List<Expense> byUserId = expenseRepo.findByUserId(userId);
        return mapper.convertValue(byUserId, new TypeReference<List<ExpenseDto>>() {});
    }


    private void setCurrency(ExpenseDto expenseDto){
        if(Objects.isNull(expenseDto.getCurrency())){
            expenseDto.setCurrency("inr");
        }
    }
}
