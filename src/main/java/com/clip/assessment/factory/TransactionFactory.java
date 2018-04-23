package com.clip.assessment.factory;

import com.clip.assessment.entity.Transaction;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.util.UUID;

public class TransactionFactory {

    public static Transaction createTransaction(String json) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        return mapper.readValue(json, Transaction.class);
    }
}
