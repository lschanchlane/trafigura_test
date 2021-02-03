package positionsystem;

import model.Position;
import model.Transaction;
import positionsystem.service.PositionCalcsService;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class PositionCalculatorImpl implements PositionCalculator {

    public List<Position> calculatePositions(List<Transaction> transactions) {
        PositionCalcsService positionCalcsService = new PositionCalcsService();
        Map<String,Long> mapSecCodeTransactions  = positionCalcsService.applyTransactionAction(transactions).stream().
                collect(Collectors.groupingBy(t -> t.getTrade().getSecurityCode(), Collectors.summingLong(Transaction::getQuantity)));
        List<Position> positionList = mapSecCodeTransactions.entrySet().stream()
                .map(t -> new Position(t.getKey(),
                t.getValue().toString())).collect(Collectors.toList());
        System.out.println(positionList);
        return positionList;
    }
}
