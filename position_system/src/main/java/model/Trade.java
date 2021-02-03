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
public class Trade {

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Trade trade = (Trade) o;
        return tradeVersion == trade.tradeVersion &&
                tradeId.equals(trade.tradeId) &&
                securityCode.equals(trade.securityCode);
    }

    @Override
    public int hashCode() {
        return Objects.hash(tradeId, tradeVersion);
    }

    private String tradeId;
    private long tradeVersion;
    private String securityCode;
}
