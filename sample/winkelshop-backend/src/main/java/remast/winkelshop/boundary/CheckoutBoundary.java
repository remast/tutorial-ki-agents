package remast.winkelshop.boundary;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
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
@RequestMapping("/v1/checkout")
public class CheckoutBoundary {

    private final CheckoutControl checkoutControl;

    public CheckoutBoundary(CheckoutControl checkoutControl) {
        this.checkoutControl = checkoutControl;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public DataResponse<CheckoutResponse> checkout(Authentication authentication,
                                                   @Valid @RequestBody CheckoutRequest request) {
        String userId = ((AuthenticatedUser) authentication.getPrincipal()).getUserId();
        ShippingAddress address = new ShippingAddress(
                request.getShippingAddress().getFullName(),
                request.getShippingAddress().getStreet(),
                request.getShippingAddress().getCity(),
                request.getShippingAddress().getPostalCode(),
                request.getShippingAddress().getCountry()
        );
        Order order = checkoutControl.checkout(userId, address, request.getPaymentMethod());
        return new DataResponse<>(new CheckoutResponse(order.getOrderId(), order.getStatus(), order.getTotal(), order.getCurrency().name(), order.getCreatedAt()));
    }

    public static class CheckoutRequest {
        @Valid
        private ShippingAddressRequest shippingAddress;
        @NotBlank
        private String paymentMethod;

        public ShippingAddressRequest getShippingAddress() {
            return shippingAddress;
        }

        public void setShippingAddress(ShippingAddressRequest shippingAddress) {
            this.shippingAddress = shippingAddress;
        }

        public String getPaymentMethod() {
            return paymentMethod;
        }

        public void setPaymentMethod(String paymentMethod) {
            this.paymentMethod = paymentMethod;
        }
    }

    public static class ShippingAddressRequest {
        @NotBlank
        private String fullName;
        @NotBlank
        private String street;
        @NotBlank
        private String city;
        @NotBlank
        private String postalCode;
        @NotBlank
        private String country;

        public String getFullName() {
            return fullName;
        }

        public void setFullName(String fullName) {
            this.fullName = fullName;
        }

        public String getStreet() {
            return street;
        }

        public void setStreet(String street) {
            this.street = street;
        }

        public String getCity() {
            return city;
        }

        public void setCity(String city) {
            this.city = city;
        }

        public String getPostalCode() {
            return postalCode;
        }

        public void setPostalCode(String postalCode) {
            this.postalCode = postalCode;
        }

        public String getCountry() {
            return country;
        }

        public void setCountry(String country) {
            this.country = country;
        }
    }

    public static class CheckoutResponse {
        private String orderId;
        private String status;
        private double total;
        private String currency;
        private Instant placedAt;

        public CheckoutResponse(String orderId, String status, double total, String currency, Instant placedAt) {
            this.orderId = orderId;
            this.status = status;
            this.total = total;
            this.currency = currency;
            this.placedAt = placedAt;
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

        public Instant getPlacedAt() {
            return placedAt;
        }

        public void setPlacedAt(Instant placedAt) {
            this.placedAt = placedAt;
        }
    }
}
