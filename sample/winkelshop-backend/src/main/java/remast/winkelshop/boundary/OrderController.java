package remast.winkelshop.boundary;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import remast.winkelshop.common.DataResponse;
import remast.winkelshop.common.PagedResponse;
import remast.winkelshop.control.OrderControl;
import remast.winkelshop.entity.OrderEntity;
import remast.winkelshop.security.AuthenticatedUser;

import java.time.Instant;

@RestController
@RequestMapping("/orders")
@RequiredArgsConstructor
public class OrderController {

    private final OrderControl orderControl;

    @GetMapping("/{orderId}")
    public DataResponse<OrderControl.OrderDetails> getOrder(Authentication authentication, @PathVariable String orderId) {
        String userId = ((AuthenticatedUser) authentication.getPrincipal()).userId();
        return new DataResponse<>(orderControl.getOrder(userId, orderId));
    }

    @GetMapping
    public PagedResponse<OrderHistoryItem> getOrders(Authentication authentication,
                                                     @RequestParam(defaultValue = "0") int page,
                                                     @RequestParam(defaultValue = "20") int size) {
        String userId = ((AuthenticatedUser) authentication.getPrincipal()).userId();
        return orderControl.getOrders(userId, page, size)
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
