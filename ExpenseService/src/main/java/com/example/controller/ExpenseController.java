package com.example.controller;

import com.example.dto.ExpenseDto;
import com.example.service.ExpenseService;
import jakarta.websocket.server.PathParam;
import lombok.NonNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class ExpenseController {

    @Autowired
    public ExpenseService expenseService;

    @GetMapping("/expense")
    public ResponseEntity<List<ExpenseDto>> getExpenses(
            @PathParam("userId") @NonNull String userId
    ){
        try{
            List<ExpenseDto> expenseDtoList = expenseService.getExpenses(userId);
            return ResponseEntity.ok(expenseDtoList);
        }
        catch (Exception e){
            return ResponseEntity.notFound().build();
        }
    }
}
