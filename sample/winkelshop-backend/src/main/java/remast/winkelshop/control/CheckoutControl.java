package remast.winkelshop.control;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import remast.winkelshop.entity.*;

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
    public Order checkout(String userId, ShippingAddress shippingAddress, String paymentMethod) {
        Cart cart = cartControl.consumeCart(userId);
        Map<String, ProductEntity> products = new HashMap<>();
        List<OrderItem> items = cart.getItems().stream()
                .map(item -> {
                    ProductEntity product = productRepository.findById(item.getProductId())
                            .orElseThrow();
                    products.put(item.getProductId(), product);
                    return new OrderItem(item.getProductId(), product.name(), item.getQuantity(), item.getUnitPrice());
                })
                .toList();

        double total = items.stream().mapToDouble(OrderItem::lineTotal).sum();
        String orderId = "ord_" + Instant.now().toEpochMilli();
        Instant createdAt = Instant.now();

        OrderEntity orderEntity = orderRepository.save(new OrderEntity(
                orderId,
                userId,
                "placed",
                paymentMethod,
                total,
                Currency.GALLEON.name(),
                shippingAddress.fullName(),
                shippingAddress.street(),
                shippingAddress.city(),
                shippingAddress.postalCode(),
                shippingAddress.country(),
                createdAt
        ));

        List<OrderItem> enrichedItems = cart.getItems().stream()
                .map(item -> {
                    OrderItemEntity saved = orderItemRepository.save(new OrderItemEntity(
                            null,
                            orderEntity.id(),
                            item.getProductId(),
                            products.get(item.getProductId()).name(),
                            item.getQuantity(),
                            item.getUnitPrice()
                    ));
                    return new OrderItem(saved.productId(), saved.name(), saved.quantity(), saved.unitPrice());
                })
                .toList();

        return Order.builder()
                .orderId(orderEntity.id())
                .userId(orderEntity.userId())
                .status(orderEntity.status())
                .items(enrichedItems)
                .shippingAddress(new ShippingAddress(
                        orderEntity.shippingFullName(),
                        orderEntity.shippingStreet(),
                        orderEntity.shippingCity(),
                        orderEntity.shippingPostalCode(),
                        orderEntity.shippingCountry()
                ))
                .paymentMethod(orderEntity.paymentMethod())
                .total(orderEntity.total())
                .currency(Currency.valueOf(orderEntity.currency()))
                .createdAt(orderEntity.createdAt())
                .build();
    }
}
