package com.clip.assessment;

import com.clip.assessment.entity.Transaction;
import com.clip.assessment.exception.ProcessException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.clip.assessment.service.TransactionService;

import java.util.*;

@SpringBootApplication
@EnableAutoConfiguration
public class Application implements CommandLineRunner {

    private static final Logger LOGGER = LoggerFactory.getLogger(Application.class);

    @Autowired
    private TransactionService transactionService;

    public static void main(String[] args) {
        SpringApplication sa = new SpringApplication(Application.class);
        sa.setLogStartupInfo(false);
        sa.run(args);
    }

    @Override
    public void run(String... args) throws Exception {
        Integer userId;
        String command;

        if(valideArgs(args)) {
            userId = Integer.valueOf(args[0]);
            command = args[1];

            switch(command) {
                case "add":
                    addTransaction(args);
                break;
                case "show":
                    showTransaction(userId, args);
                break;
                case "list":
                    listTransactions(userId);
                break;
                case "sum":
                    sumTransactions(userId);
                break;
                default:
                    LOGGER.info("Invalid operation");
            }
        } else {

        }
    }

    private Boolean valideArgs(String... args) {
        return args!= null && args.length >= 2;
    }

    private void addTransaction(String... args) {
        try {
            if(args.length < 2) {
                LOGGER.error("Missing transaction data");
            }
            ObjectMapper mapper = new ObjectMapper();
            Transaction result = transactionService.addTransaction(args[2]);
            LOGGER.info(mapper.writeValueAsString(result));
        } catch (ProcessException  e) {
            LOGGER.error(e.getMessage());
        } catch (JsonProcessingException e) {
            LOGGER.error("Error adding the transaction");
        }

    }

    private void showTransaction(Integer userId, String... args) {
        try {
            if(args.length < 2) {
                LOGGER.error("Missing transaction ID");
            }
            ObjectMapper mapper = new ObjectMapper();
            Transaction result = transactionService.showTransaction(userId, args[2]);
            if(result == null) {
                LOGGER.info("Transaction not found");
            } else {
                LOGGER.info(mapper.writeValueAsString(result));
            }

        } catch (ProcessException  e) {
            LOGGER.error(e.getMessage());
        } catch (JsonProcessingException e) {
            LOGGER.error("Error adding the transaction");
        }

    }

    private void listTransactions(Integer userId) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            List<Transaction> result = transactionService.listTransactions(userId);
            if(result == null || result.isEmpty()) {
                LOGGER.info("Transaction not found");
            } else {
                LOGGER.info(mapper.writeValueAsString(result));
            }

        } catch (ProcessException  e) {
            LOGGER.error(e.getMessage());
        } catch (JsonProcessingException e) {
            LOGGER.error("Error adding the transaction");
        }

    }

    private void sumTransactions(Integer userId) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            Double result = transactionService.sumTransactions(userId);
            Map<String, Object> mapResult = new LinkedHashMap<>(2);
            mapResult.put("user_id", userId);
            mapResult.put("sum", result);
            LOGGER.info(mapper.writeValueAsString(mapResult));

        } catch (ProcessException  e) {
            LOGGER.error(e.getMessage());
        } catch (JsonProcessingException e) {
            LOGGER.error("Error adding the transaction");
        }

    }

}
