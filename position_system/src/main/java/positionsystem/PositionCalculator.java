package positionsystem;

import model.Position;
import model.Transaction;

import java.util.List;

public interface PositionCalculator {

    public List<Position> calculatePositions(List<Transaction> transactions);

}
