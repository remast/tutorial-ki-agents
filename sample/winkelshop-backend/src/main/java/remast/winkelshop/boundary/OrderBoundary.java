package remast.winkelshop.boundary;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.security.core.Authentication;
import remast.winkelshop.common.DataResponse;
import remast.winkelshop.common.PagedResponse;
import remast.winkelshop.control.OrderControl;
import remast.winkelshop.entity.Order;
import remast.winkelshop.security.AuthenticatedUser;

@RestController
@RequestMapping("/v1/orders")
public class OrderBoundary {

    private final OrderControl orderControl;

    public OrderBoundary(OrderControl orderControl) {
        this.orderControl = orderControl;
    }

    @GetMapping("/{orderId}")
    public DataResponse<Order> getOrder(Authentication authentication, @PathVariable String orderId) {
        String userId = ((AuthenticatedUser) authentication.getPrincipal()).getUserId();
        return new DataResponse<>(orderControl.getOrder(userId, orderId));
    }

    @GetMapping
    public PagedResponse<OrderHistoryItem> getOrders(Authentication authentication,
                                                     @RequestParam(defaultValue = "0") int page,
                                                     @RequestParam(defaultValue = "20") int size) {
        String userId = ((AuthenticatedUser) authentication.getPrincipal()).getUserId();
        PagedResponse<Order> orders = orderControl.getOrders(userId, page, size);
        PagedResponse<OrderHistoryItem> response = new PagedResponse<>();
        response.setContent(orders.getContent().stream()
                .map(order -> new OrderHistoryItem(order.getOrderId(), order.getStatus(), order.getTotal(), order.getCurrency().name(), order.getCreatedAt()))
                .toList());
        response.setPageable(orders.getPageable());
        response.setLast(orders.isLast());
        response.setTotalPages(orders.getTotalPages());
        response.setTotalElements(orders.getTotalElements());
        response.setSize(orders.getSize());
        response.setNumber(orders.getNumber());
        response.setSort(orders.getSort());
        response.setFirst(orders.isFirst());
        response.setNumberOfElements(orders.getNumberOfElements());
        response.setEmpty(orders.isEmpty());
        return response;
    }

    public static class OrderHistoryItem {
        private String orderId;
        private String status;
        private double total;
        private String currency;
        private java.time.Instant createdAt;

        public OrderHistoryItem(String orderId, String status, double total, String currency, java.time.Instant createdAt) {
            this.orderId = orderId;
            this.status = status;
            this.total = total;
            this.currency = currency;
            this.createdAt = createdAt;
        }

        public String getOrderId() {
            return orderId;
        }

        public void setOrderId(String orderId) {
            this.orderId = orderId;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public double getTotal() {
            return total;
        }

        public void setTotal(double total) {
            this.total = total;
        }

        public String getCurrency() {
            return currency;
        }

        public void setCurrency(String currency) {
            this.currency = currency;
        }

        public java.time.Instant getCreatedAt() {
            return createdAt;
        }

        public void setCreatedAt(java.time.Instant createdAt) {
            this.createdAt = createdAt;
        }
    }
}
