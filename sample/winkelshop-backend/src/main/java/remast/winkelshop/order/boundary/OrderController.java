package remast.winkelshop.order.boundary;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import remast.winkelshop.order.control.OrderService;
import remast.winkelshop.auth.boundary.AuthenticatedUser;

import java.time.Instant;

@RestController
@RequestMapping("/orders")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    @GetMapping("/{orderId}")
    public DataResponse<OrderService.OrderDetails> getOrder(Authentication authentication, @PathVariable String orderId) {
        var userId = ((AuthenticatedUser) authentication.getPrincipal()).userId();
        return new DataResponse<>(orderService.getOrder(userId, orderId));
    }

    @GetMapping
    public PagedResponse<OrderHistoryItem> getOrders(Authentication authentication,
                                                     @RequestParam(defaultValue = "0") int page,
                                                     @RequestParam(defaultValue = "20") int size) {
        var userId = ((AuthenticatedUser) authentication.getPrincipal()).userId();
        return orderService.getOrders(userId, page, size)
                .map(order -> new OrderHistoryItem(
                        order.id(),
                        order.status(),
                        order.total(),
                        order.currency(),
                        order.createdAt()
                ));
    }

    public record OrderHistoryItem(String orderId, String status, double total, String currency, Instant createdAt) {
    }
}
