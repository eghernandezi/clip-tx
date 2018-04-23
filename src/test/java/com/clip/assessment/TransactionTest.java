package com.clip.assessment;

import com.clip.assessment.entity.Transaction;
import com.clip.assessment.exception.PersistenceException;
import com.clip.assessment.exception.ProcessException;
import com.clip.assessment.service.TransactionService;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
public class TransactionTest {

    private static final Logger LOGGER = LoggerFactory.getLogger(TransactionTest.class);

    private static final Integer USER_ID = 345;

    @Autowired
    private TransactionService transactionService;

    @Test
    public void addTransaction_success() {
        try {
            Transaction tx = transactionService.addTransaction("{ \"amount\": 1.23, \"description\": \"Joes Tacos\", \"date\":\"2018-12-30\", \"user_id\": 345 }");
            Assert.assertNotNull(tx);
            Assert.assertNotNull(tx.getId());
        } catch (ProcessException e) {
            LOGGER.error(e.getMessage(), e);
            Assert.fail(e.getMessage());
        }
    }

    @Test
    public void showTransaction_found() {
        try {
            Transaction newTx = transactionService.addTransaction("{ \"amount\": 1.23, \"description\": \"Joes Tacos\", \"date\":\"2018-12-30\", \"user_id\": 345 }");
            Transaction tx =  transactionService.showTransaction(USER_ID, newTx.getId());
            Assert.assertNotNull(tx);
            Assert.assertEquals(newTx.getId(), tx.getId());
            Assert.assertEquals(newTx.getDescription(), tx.getDescription());
            Assert.assertEquals(newTx.getDate(), tx.getDate());
            Assert.assertEquals(newTx.getAmount(), tx.getAmount());
            Assert.assertEquals(newTx.getUserId(), tx.getUserId());
        } catch (ProcessException e) {
            LOGGER.error(e.getMessage(), e);
            Assert.fail(e.getMessage());
        }

    }

    @Test
    public void showTransaction_notfound() {
        try {
            Transaction tx =  transactionService.showTransaction(USER_ID, "X");
            Assert.assertNull(tx);
        } catch (ProcessException e) {
            LOGGER.error(e.getMessage(), e);
            Assert.fail(e.getMessage());
        }

    }

    @Test
    public void listTransactions_found() {
        try {
            List<Transaction> txs =  transactionService.listTransactions(USER_ID);
            Assert.assertNotNull(txs);
            Assert.assertFalse(txs.isEmpty());
        } catch (ProcessException e) {
            LOGGER.error(e.getMessage(), e);
            Assert.fail(e.getMessage());
        }

    }

    @Test
    public void listTransactions_notfound() {
        try {
            List<Transaction> txs =  transactionService.listTransactions(77777);
            Assert.assertNotNull(txs);
            Assert.assertTrue(txs.isEmpty());
        } catch (ProcessException e) {
            LOGGER.error(e.getMessage(), e);
            Assert.fail(e.getMessage());
        }

    }

    @Test
    public void sumTransactions() {
        try {
            Double result =  transactionService.sumTransactions(USER_ID);
            List<Transaction> txs =  transactionService.listTransactions(USER_ID);
            Double expected = txs.stream().mapToDouble(tx -> tx.getAmount()).sum();
            Assert.assertNotNull(result);
            Assert.assertEquals(result, expected);
        } catch (ProcessException e) {
            LOGGER.error(e.getMessage(), e);
            Assert.fail(e.getMessage());
        }
    }
}
