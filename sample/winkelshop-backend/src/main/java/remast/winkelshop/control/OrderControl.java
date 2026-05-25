package remast.winkelshop.control;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import remast.winkelshop.common.PagedResponse;
import remast.winkelshop.entity.OrderEntity;
import remast.winkelshop.entity.OrderItemEntity;
import remast.winkelshop.entity.OrderItemRepository;
import remast.winkelshop.entity.OrderRepository;

@Service
@RequiredArgsConstructor
public class OrderControl {

    private final OrderRepository orderRepository;
    private final OrderItemRepository orderItemRepository;

    public OrderDetails getOrder(String userId, String orderId) {
        OrderEntity order = orderRepository.findByIdAndUserId(orderId, userId)
                .orElseThrow();
        var items = orderItemRepository.findByOrderId(order.id());
        return new OrderDetails(order, items);
    }

    public PagedResponse<OrderEntity> getOrders(String userId, int page, int size) {
        int safePage = Math.max(page, 0);
        int safeSize = Math.max(size, 1);
        long offset = (long) safePage * safeSize;

        var content = orderRepository.findPageByUserId(userId, safeSize, offset);

        long total = orderRepository.countByUserId(userId);
        return PagedResponse.fromContentAndTotal(content, safePage, safeSize, total);
    }

    public record OrderDetails(OrderEntity order, java.util.List<OrderItemEntity> items) {
    }
}
