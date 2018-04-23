package com.clip.assessment.service;

import com.clip.assessment.entity.Transaction;
import com.clip.assessment.entity.TransactionFile;
import com.clip.assessment.exception.PersistenceException;
import com.clip.assessment.exception.ProcessException;
import com.clip.assessment.factory.TransactionFactory;
import com.clip.assessment.repository.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;

@Service
public class TransactionService {

    @Autowired
    private TransactionRepository txRepo;

    public Transaction addTransaction(String jsonTx) throws ProcessException {
        try {
            Transaction newxTx = TransactionFactory.createTransaction(jsonTx);
            if(newxTx.validate()) {
                txRepo.saveTransaction(newxTx);
            } else {
                throw new ProcessException("Required field");
            }
            return newxTx;
        } catch (IOException | PersistenceException e) {
           throw  new ProcessException(e);
        }
    }

    public Transaction showTransaction(Integer userId, String id) throws ProcessException {
        try {
            return txRepo.getTransaction(userId, id);
        } catch (PersistenceException e) {
            throw  new ProcessException(e);
        }
    }

    public List<Transaction> listTransactions(Integer userId) throws ProcessException {
        try {
            return txRepo.findTransactions(userId);
        } catch (PersistenceException e) {
            throw  new ProcessException(e);
        }
    }

    public Double sumTransactions(Integer userId) throws ProcessException {
        List<Transaction> transactions = listTransactions(userId);
        return transactions.stream().mapToDouble(tx -> tx.getAmount()).sum();
    }
}
