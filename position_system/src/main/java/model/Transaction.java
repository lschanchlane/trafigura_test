package model;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Objects;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Transaction {
    private String transactionId;
    private Trade trade;
    private long quantity;
    private TransactionAction transactionAction;
    private TransactionType transactionType;


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Transaction that = (Transaction) o;
        return quantity == that.quantity &&
                transactionId.equals(that.transactionId) &&
                trade.equals(that.trade) &&
                transactionAction == that.transactionAction &&
                transactionType == that.transactionType;
    }

    @Override
    public int hashCode() {
        return Objects.hash(transactionId);
    }
}
