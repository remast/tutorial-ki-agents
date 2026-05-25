package remast.winkelshop.entity;

import lombok.Builder;

import java.time.Instant;
import java.util.List;

@Builder
public record Order(
        String orderId,
        String userId,
        String status,
        List<OrderItem> items,
        ShippingAddress shippingAddress,
        String paymentMethod,
        double total,
        Currency currency,
        Instant createdAt
) {
}
