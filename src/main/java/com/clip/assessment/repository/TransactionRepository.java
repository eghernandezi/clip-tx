package com.clip.assessment.repository;

import com.clip.assessment.entity.Transaction;
import com.clip.assessment.entity.TransactionFile;
import com.clip.assessment.exception.PersistenceException;

import java.util.List;

public interface TransactionRepository {

    void saveTransaction(Transaction tx) throws PersistenceException;

    Transaction getTransaction(Integer userId, String id) throws PersistenceException;

    List<Transaction> findTransactions(Integer userId) throws PersistenceException;

    List<TransactionFile> findTransactions() throws PersistenceException;
}
