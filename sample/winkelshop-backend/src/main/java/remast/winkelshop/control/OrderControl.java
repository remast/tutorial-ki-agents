package remast.winkelshop.control;

import org.springframework.stereotype.Service;
import remast.winkelshop.common.PagedResponse;
import remast.winkelshop.entity.Order;
import remast.winkelshop.entity.SeedData;

@Service
public class OrderControl {

    private final SeedData seedData;

    public OrderControl(SeedData seedData) {
        this.seedData = seedData;
    }

    public Order getOrder(String userId, String orderId) {
        return seedData.findOrderForUser(userId, orderId)
                .orElseThrow(() -> new NotFoundException("order not found"));
    }

    public PagedResponse<Order> getOrders(String userId, int page, int size) {
        return PagedResponse.of(seedData.findOrdersForUserSorted(userId), page, size);
    }
}
