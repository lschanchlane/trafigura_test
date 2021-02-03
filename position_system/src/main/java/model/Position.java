package model;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
public class Position {

    @Override
    public String toString() {
        return "Position{" +
                "securityCode='" + securityCode + '\'' +
                ", qty='" + qty + '\'' +
                '}';
    }

    private String securityCode;
    private String qty;
}
