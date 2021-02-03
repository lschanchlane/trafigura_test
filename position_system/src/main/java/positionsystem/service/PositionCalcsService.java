package positionsystem.service;

import model.Transaction;
import model.TransactionAction;
import model.TransactionType;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

public class PositionCalcsService {

    private Transaction switchSign(Transaction transaction){
        if(transaction.getTransactionType().equals(TransactionType.SELL)){
            transaction.setQuantity(-(transaction.getQuantity()));
        }
        return transaction;
    }

    private List<Transaction> processTransactions(List<Transaction> transactions){
        List<Transaction> finalTransactions = new LinkedList<>();
        Transaction greatestTran =
                transactions
                        .stream()
                        .sorted((f1, f2) -> Long.compare(f2.getTrade().getTradeVersion(), f1.getTrade().getTradeVersion()))
                        .findFirst()
                        .map(k -> switchSign(k))
                        .get();
        TransactionAction transactionAction =  greatestTran.getTransactionAction();
        if(transactionAction.equals(TransactionAction.UPDATE)){
            finalTransactions.add(greatestTran);
        } else if(transactionAction.equals(TransactionAction.CANCEL)){
            greatestTran.setQuantity(0L);
            finalTransactions.add(greatestTran);
        }else {
            finalTransactions.add(greatestTran);
        }
        return finalTransactions;
    }
    public List<Transaction> applyTransactionAction(List<Transaction> transactionList) {
        Map<String, List<Transaction>> groupedBySecCode = transactionList.stream().collect(Collectors.groupingBy(f -> f.getTrade().getSecurityCode()));
        Map<String, List<Transaction>> mapTradeIdTransactions =  transactionList
                .stream()
                .collect(Collectors.groupingBy(f -> f.getTrade().getTradeId()));
        List<Transaction> transactions =
                mapTradeIdTransactions
                        .entrySet()
                        .stream()
                        .map(e -> processTransactions(e.getValue()))
                        .flatMap(List::stream)
                        .collect(Collectors.toList());
        transactions.removeIf(Objects::isNull);

        return transactions;
    }
}
