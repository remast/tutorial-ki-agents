package remast.winkelshop.control;

import org.springframework.stereotype.Service;
import remast.winkelshop.entity.Cart;
import remast.winkelshop.entity.Currency;
import remast.winkelshop.entity.Order;
import remast.winkelshop.entity.OrderItem;
import remast.winkelshop.entity.Product;
import remast.winkelshop.entity.SeedData;
import remast.winkelshop.entity.ShippingAddress;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Service
public class CheckoutControl {

    private final SeedData seedData;
    private final CartControl cartControl;

    public CheckoutControl(SeedData seedData, CartControl cartControl) {
        this.seedData = seedData;
        this.cartControl = cartControl;
    }

    public Order checkout(String userId, ShippingAddress shippingAddress, String paymentMethod) {
        Cart cart = cartControl.consumeCart(userId);
        List<OrderItem> items = cart.getItems().stream().map(cartItem -> {
            Product product = seedData.findProductById(cartItem.getProductId())
                    .orElseThrow(() -> new NotFoundException("product not found"));
            return new OrderItem(cartItem.getProductId(), product.getName(), cartItem.getQuantity(), cartItem.getUnitPrice());
        }).toList();

        double total = items.stream().mapToDouble(OrderItem::getLineTotal).sum();

        Order order = new Order();
        order.setOrderId(seedData.nextOrderId());
        order.setUserId(userId);
        order.setStatus("placed");
        order.setItems(items);
        order.setShippingAddress(shippingAddress);
        order.setPaymentMethod(paymentMethod);
        order.setTotal(total);
        order.setCurrency(Currency.GALLEON);
        order.setCreatedAt(Instant.now());

        seedData.getOrdersByUserId()
                .computeIfAbsent(userId, ignored -> new ArrayList<>())
                .add(order);

        return order;
    }
}
