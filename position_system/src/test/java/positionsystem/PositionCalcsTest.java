package positionsystem;

import model.*;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

public class PositionCalcsTest {
    @Test
    /*
     * Simple insert transactions
     */
    public void testInsertTransaction(){

        List<Transaction> transactions  = getInsertTransactions();
        PositionCalculator positionCalculator = new PositionCalculatorImpl();
        List<Position> positionList  = positionCalculator.calculatePositions(transactions);
        Position RELposition = positionList.stream().filter(f -> f.getSecurityCode().equals("REL")).findFirst().get();
        Assertions.assertEquals("50",RELposition.getQty());
        Position ITCposition = positionList.stream().filter(f -> f.getSecurityCode().equals("ITC")).findFirst().get();
        Assertions.assertEquals("-40",ITCposition.getQty());


    }

    @Test
    /*
     * Single Qty Update for Multiple transactions with Different Trade ID
     */
    public void testUpdateTransaction(){
        List<Transaction> updateTransactions = getUpdateTransactions();
        List<Transaction> insertTransactions  = getInsertTransactions();

        List<Transaction> transactions = new ArrayList<>();
        transactions.addAll(updateTransactions);
        transactions.addAll(insertTransactions);

        PositionCalculator positionCalculator = new PositionCalculatorImpl();
        List<Position> positionList = positionCalculator.calculatePositions(transactions);

        Position RELposition = positionList.stream().filter(f -> f.getSecurityCode().equals("REL")).findFirst().get();
        Assertions.assertEquals("60",RELposition.getQty());

    }

    @Test
    public void testMultipleInsertSameSecurityTransactions(){
        List<Transaction> updateTransactions = getSameSecurityInsertTransactions();

        List<Transaction> transactions = new ArrayList<>();
        transactions.addAll(updateTransactions);

        PositionCalculator positionCalculator = new PositionCalculatorImpl();
        List<Position> positionList = positionCalculator.calculatePositions(transactions);
        Position RELposition = positionList.stream().filter(f -> f.getSecurityCode().equals("REL")).findFirst().get();
        Assertions.assertEquals("90",RELposition.getQty());

    }

    @Test
    public void testUpdateTransactionReverseOrder(){
        List<Transaction> updateTransactions = getUpdateTransactionReverse();

        List<Transaction> transactions = new ArrayList<>();
        transactions.addAll(updateTransactions);

        PositionCalculator positionCalculator = new PositionCalculatorImpl();
        List<Position> positionList = positionCalculator.calculatePositions(transactions);

        Position RELposition = positionList.stream().filter(f -> f.getSecurityCode().equals("REL")).findFirst().get();
        Assertions.assertEquals("33",RELposition.getQty());

    }

    @Test
    /*
     * Multiple Trades with Different Trade ID
     */
    public void testUpdateTransactionMultipleTradesDiffTradeId(){
        List<Transaction> updateTransactions = getDiffTradeIdsTransactions();

        List<Transaction> transactions = new ArrayList<>();
        transactions.addAll(updateTransactions);

        PositionCalculator positionCalculator = new PositionCalculatorImpl();
        List<Position> positionList = positionCalculator.calculatePositions(transactions);
        Position INFposition = positionList.stream().filter(f -> f.getSecurityCode().equals("INF")).findFirst().get();
        Assertions.assertEquals("66",INFposition.getQty());

        Position RELposition = positionList.stream().filter(f -> f.getSecurityCode().equals("REL")).findFirst().get();
        Assertions.assertEquals("-33",RELposition.getQty());

    }

    @Test
    /*
     *  Same trade ID but different Sec Code
     */
    public void testUpdateTransactionMultipleTradesSameTradeIdUpdateSecCode(){
        List<Transaction> updateTransactions = getSameTradeIdsUpdateSecCodeInTransactions();
        List<Transaction> transactions = new ArrayList<>();
        transactions.addAll(updateTransactions);

        PositionCalculator positionCalculator = new PositionCalculatorImpl();
        List<Position> positionList = positionCalculator.calculatePositions(transactions);

        Position INFposition = positionList.stream().filter(f -> f.getSecurityCode().equals("INF")).findFirst().get();
        Assertions.assertEquals("96",INFposition.getQty());
    }

    @Test
    /*
     * Test cancelled transactions
     */
    public void testCancelTransactions(){
        List<Transaction> cancelTransactions = getCancelTransaction();
        PositionCalculator positionCalculator = new PositionCalculatorImpl();
        List<Position> positionList = positionCalculator.calculatePositions(cancelTransactions);
        Position RELposition = positionList.stream().filter(f -> f.getSecurityCode().equals("REL")).findFirst().get();
        Assertions.assertEquals("0",RELposition.getQty());
    }


    private List<Transaction> getInsertTransactions(){
        List<Transaction> transactions = new ArrayList<Transaction>();

        //insert
        Transaction transaction1  = createTransaction("REL", 50, 1, "1", "1",
                TransactionAction.INSERT, TransactionType.BUY);
        transactions.add(transaction1);
        Transaction transaction2  = createTransaction("ITC", 40, 1, "2", "2",
                TransactionAction.INSERT, TransactionType.SELL);
        transactions.add(transaction2);
        Transaction transaction3  = createTransaction("INF", 70, 1, "3", "3",
                TransactionAction.INSERT, TransactionType.BUY);
        transactions.add(transaction3);
        return transactions;
    }

    private List<Transaction> getSameSecurityInsertTransactions(){
        List<Transaction> transactions = new ArrayList<Transaction>();

        //insert
        Transaction transaction1  = createTransaction("REL", 50, 1, "1", "1",
                TransactionAction.INSERT, TransactionType.BUY);
        transactions.add(transaction1);
        Transaction transaction2  = createTransaction("REL", 40, 1, "2", "2",
                TransactionAction.INSERT, TransactionType.BUY);
        transactions.add(transaction2);
        return transactions;
    }

    private List<Transaction> getUpdateTransactions(){
        List<Transaction> transactions = new ArrayList<Transaction>();

        //insert
        Transaction transaction1  = createTransaction("REL", 60, 2, "1", "1",
                TransactionAction.UPDATE, TransactionType.BUY);
        transactions.add(transaction1);

        return transactions;
    }

    private List<Transaction> getUpdateTransactionReverse(){
        List<Transaction> transactions = new ArrayList<Transaction>();

        //insert
        Transaction transaction1  = createTransaction("REL", 33, 2, "1", "1",
                TransactionAction.UPDATE, TransactionType.BUY);
        Transaction transaction2  = createTransaction("REL", 66, 1, "1", "2",
                TransactionAction.INSERT, TransactionType.BUY);
        transactions.add(transaction1);
        transactions.add(transaction2);

        return transactions;
    }

    private List<Transaction> getCancelTransaction(){
        List<Transaction> transactions = new ArrayList<Transaction>();

        //insert
        Transaction transaction1  = createTransaction("REL", 33, 1, "1", "1",
                TransactionAction.INSERT, TransactionType.BUY);
        Transaction transaction2  = createTransaction("REL", 99, 2, "1", "2",
                TransactionAction.CANCEL, TransactionType.BUY);
        transactions.add(transaction1);
        transactions.add(transaction2);

        return transactions;
    }

    private List<Transaction> getDiffTradeIdsTransactions(){
        List<Transaction> transactions = new ArrayList<Transaction>();

        //insert
        Transaction transaction1  = createTransaction("REL", -33, 1, "1", "1",
                TransactionAction.UPDATE, TransactionType.BUY);
        Transaction transaction2  = createTransaction("INF", 66, 1, "2", "1",
                TransactionAction.INSERT, TransactionType.BUY);
        transactions.add(transaction1);
        transactions.add(transaction2);

        return transactions;
    }

    private List<Transaction> getSameTradeIdsUpdateSecCodeInTransactions(){
        List<Transaction> transactions = new ArrayList<Transaction>();

        //insert
        Transaction transaction1  = createTransaction("REL", 36, 1, "1", "1",
                TransactionAction.UPDATE, TransactionType.BUY);
        Transaction transaction2  = createTransaction("INF", 96, 2, "1", "2",
                TransactionAction.INSERT, TransactionType.BUY);
        transactions.add(transaction1);
        transactions.add(transaction2);

        return transactions;
    }

    private Transaction createTransaction(String securityCode, long qty, long tradeVersion,
                                          String tradeId, String transactionId, TransactionAction action, TransactionType type){
        Transaction transaction = new Transaction();
        transaction.setTransactionId(transactionId);
        transaction.setTransactionAction(action);
        transaction.setTransactionType(type);
        transaction.setQuantity(qty);
        Trade trade = new Trade();
        trade.setSecurityCode(securityCode);
        trade.setTradeVersion(tradeVersion);
        trade.setTradeId(tradeId);
        transaction.setTrade(trade);
        return transaction;
    }


}

