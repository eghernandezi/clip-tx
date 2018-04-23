package com.clip.assessment.entity;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;
import java.util.stream.Collectors;

public class TransactionFile {

    private Integer userId;

    private List<Transaction> transactions;

    public TransactionFile() {}

    public TransactionFile(Integer userId, List<Transaction> transactions) {
        this.userId = userId;
        this.transactions = transactions;
    }

    @JsonProperty("user_id")
    public Integer getUserId() {
        return userId;
    }

    public List<Transaction> getTransactions() {
        return transactions;
    }

}
