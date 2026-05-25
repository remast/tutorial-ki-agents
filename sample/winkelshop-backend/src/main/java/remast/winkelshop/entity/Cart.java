package remast.winkelshop.entity;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class Cart {
    private final String userId;
    private final List<CartItem> items = new ArrayList<>();
}
