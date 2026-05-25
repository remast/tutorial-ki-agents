package remast.winkelshop.control;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import remast.winkelshop.common.PagedResponse;
import remast.winkelshop.entity.*;

import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderControl {

    private final OrderRepository orderRepository;
    private final OrderItemRepository orderItemRepository;

    public Order getOrder(String userId, String orderId) {
        var order = orderRepository.findByIdAndUserId(orderId, userId)
                .orElseThrow();
        List<OrderItem> items = orderItemRepository.findByOrderId(order.id()).stream()
                .map(item -> new OrderItem(item.productId(), item.name(), item.quantity(), item.unitPrice()))
                .toList();
        return mapOrder(order, items);
    }

    public PagedResponse<Order> getOrders(String userId, int page, int size) {
        int safePage = Math.max(page, 0);
        int safeSize = Math.max(size, 1);
        long offset = (long) safePage * safeSize;

        List<Order> content = orderRepository.findPageByUserId(userId, safeSize, offset).stream()
                .map(order -> mapOrder(order, orderItemRepository.findByOrderId(order.id()).stream()
                        .map(item -> new OrderItem(item.productId(), item.name(), item.quantity(), item.unitPrice()))
                        .toList()))
                .toList();

        long total = orderRepository.countByUserId(userId);
        return PagedResponse.fromContentAndTotal(content, safePage, safeSize, total);
    }

    private Order mapOrder(OrderEntity order, List<OrderItem> items) {
        return Order.builder()
                .orderId(order.id())
                .userId(order.userId())
                .status(order.status())
                .items(items)
                .shippingAddress(new ShippingAddress(
                        order.shippingFullName(),
                        order.shippingStreet(),
                        order.shippingCity(),
                        order.shippingPostalCode(),
                        order.shippingCountry()
                ))
                .paymentMethod(order.paymentMethod())
                .total(order.total())
                .currency(Currency.valueOf(order.currency()))
                .createdAt(order.createdAt())
                .build();
    }
}
