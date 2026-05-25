package remast.winkelshop.control;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import remast.winkelshop.entity.CartItemEntity;
import remast.winkelshop.entity.Currency;
import remast.winkelshop.entity.OrderEntity;
import remast.winkelshop.entity.OrderItemEntity;
import remast.winkelshop.entity.OrderItemRepository;
import remast.winkelshop.entity.OrderRepository;
import remast.winkelshop.entity.ProductEntity;
import remast.winkelshop.entity.ProductRepository;

import java.time.Instant;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class CheckoutControl {

    private final CartControl cartControl;
    private final OrderRepository orderRepository;
    private final OrderItemRepository orderItemRepository;
    private final ProductRepository productRepository;

    @Transactional
    public CheckoutResult checkout(String userId,
                                   String paymentMethod,
                                   String shippingFullName,
                                   String shippingStreet,
                                   String shippingCity,
                                   String shippingPostalCode,
                                   String shippingCountry) {
        List<CartItemEntity> cartItems = cartControl.consumeCart(userId);
        Map<String, ProductEntity> products = new HashMap<>();
        double total = cartItems.stream()
                .map(item -> {
                    ProductEntity product = productRepository.findById(item.productId())
                            .orElseThrow();
                    products.put(item.productId(), product);
                    return item.unitPrice() * item.quantity();
                })
                .mapToDouble(Double::doubleValue)
                .sum();
        String orderId = "ord_" + Instant.now().toEpochMilli();
        Instant createdAt = Instant.now();

        OrderEntity orderEntity = orderRepository.save(new OrderEntity(
                orderId,
                userId,
                "placed",
                paymentMethod,
                total,
                Currency.GALLEON.name(),
                shippingFullName,
                shippingStreet,
                shippingCity,
                shippingPostalCode,
                shippingCountry,
                createdAt
        ));

        List<OrderItemEntity> savedItems = cartItems.stream()
                .map(item -> {
                    return orderItemRepository.save(new OrderItemEntity(
                            null,
                            orderEntity.id(),
                            item.productId(),
                            products.get(item.productId()).name(),
                            item.quantity(),
                            item.unitPrice()
                    ));
                })
                .toList();

        return new CheckoutResult(orderEntity, savedItems);
    }

    public record CheckoutResult(OrderEntity order, List<OrderItemEntity> items) {
    }
}
