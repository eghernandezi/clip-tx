package com.clip.assessment.repository.impl;

import com.clip.assessment.entity.Transaction;
import com.clip.assessment.entity.TransactionFile;
import com.clip.assessment.exception.PersistenceException;
import com.clip.assessment.repository.TransactionRepository;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Repository;

import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

@Repository
public class TxFileRepository implements TransactionRepository {
    private static final String FILE_NAME = "transactions.txt";

    @Override
    public void saveTransaction(Transaction tx) throws PersistenceException {
        ObjectMapper mapper = new ObjectMapper();
        try {
            Map<Integer, List<Transaction>> transactions = transformListToMap(findTransactions());

            tx.setId(UUID.randomUUID().toString());
            if(transactions.containsKey(tx.getUserId())) {
                transactions.get(tx.getUserId()).add(tx);
            } else {
                List<Transaction> userTxs = new ArrayList<>(1);
                userTxs.add(tx);
                transactions.put(tx.getUserId(), userTxs);
            }
            mapper.writeValue(new File(getFileName()), transformMapToList(transactions));
        } catch (IOException e) {
            throw  new PersistenceException(e);
        }
    }

    @Override
    public Transaction getTransaction(Integer userId, String id) throws PersistenceException {
        Optional<Transaction> result = findTransactions(userId).stream()
                    .filter(tx -> tx.getId().equals(id))
                    .findFirst();
        if(result.isPresent()) {
            return result.get();
        }

        return null;
    }

    @Override
    public List<Transaction> findTransactions(Integer userId) throws PersistenceException {
        Optional<TransactionFile> result =  findTransactions().stream()
                                                .filter(fileTx -> fileTx.getUserId().equals(userId))
                                                .findFirst();

        if(result.isPresent()) {
            return result.get().getTransactions();
        }

        return new ArrayList<>();

    }
    public List<TransactionFile> findTransactions() throws PersistenceException {
        ObjectMapper mapper = new ObjectMapper();
        try {
            return mapper.readValue(new File(getFileName()), new TypeReference<List<TransactionFile>>(){});
        } catch(JsonMappingException me) {
            return new ArrayList<>();
        }catch (IOException e) {
            throw new PersistenceException(e);
        }
    }

    private Map<Integer, List<Transaction>> transformListToMap(List<TransactionFile> txList){
        return txList.stream().collect(
                                    Collectors.toMap(
                                            TransactionFile::getUserId, TransactionFile::getTransactions));
    }

    private List<TransactionFile> transformMapToList(Map<Integer, List<Transaction>> txMap) {
        List<TransactionFile> txList = new ArrayList<TransactionFile>(txMap.size());
        txMap.forEach((k, v) -> {
            txList.add(new TransactionFile(k, v));
        });

        return txList;
    }

    private String getFileName() {
        StringBuilder file = new StringBuilder();
        file.append(System.getProperty("user.dir"));
        file.append(File.separator);
        file.append(FILE_NAME);
        return file.toString();
    }
}
