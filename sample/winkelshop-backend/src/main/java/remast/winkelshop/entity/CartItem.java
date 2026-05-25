package remast.winkelshop.entity;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CartItem {
    private String itemId;
    private String productId;
    private int quantity;
    private double unitPrice;

    public double getLineTotal() {
        return unitPrice * quantity;
    }
}
