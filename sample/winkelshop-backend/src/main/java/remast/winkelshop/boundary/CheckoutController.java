package remast.winkelshop.boundary;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import remast.winkelshop.common.DataResponse;
import remast.winkelshop.control.CheckoutControl;
import remast.winkelshop.entity.Order;
import remast.winkelshop.entity.ShippingAddress;
import remast.winkelshop.security.AuthenticatedUser;

import java.time.Instant;

@RestController
@RequestMapping("/checkout")
@RequiredArgsConstructor
public class CheckoutController {

    private final CheckoutControl checkoutControl;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public DataResponse<CheckoutResponse> checkout(Authentication authentication,
                                                   @Valid @RequestBody CheckoutRequest request) {
        String userId = ((AuthenticatedUser) authentication.getPrincipal()).userId();
        ShippingAddressRequest shippingAddress = request.shippingAddress();
        ShippingAddress address = new ShippingAddress(
                shippingAddress.fullName(),
                shippingAddress.street(),
                shippingAddress.city(),
                shippingAddress.postalCode(),
                shippingAddress.country()
        );
        Order order = checkoutControl.checkout(userId, address, request.paymentMethod());
        return new DataResponse<>(new CheckoutResponse(order.orderId(), order.status(), order.total(), order.currency().name(), order.createdAt()));
    }

    public record CheckoutRequest(@Valid ShippingAddressRequest shippingAddress, @NotBlank String paymentMethod) {
    }

    public record ShippingAddressRequest(
            @NotBlank String fullName,
            @NotBlank String street,
            @NotBlank String city,
            @NotBlank String postalCode,
            @NotBlank String country
    ) {
    }

    public record CheckoutResponse(String orderId, String status, double total, String currency, Instant placedAt) {
    }
}
