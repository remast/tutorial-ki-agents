package remast.winkelshop.boundary;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import remast.winkelshop.common.DataResponse;
import remast.winkelshop.control.CartControl;
import remast.winkelshop.entity.CartItem;
import remast.winkelshop.security.AuthenticatedUser;

@RestController
@RequestMapping("/v1/cart")
public class CartBoundary {

    private final CartControl cartControl;

    public CartBoundary(CartControl cartControl) {
        this.cartControl = cartControl;
    }

    @PostMapping("/items")
    @ResponseStatus(HttpStatus.CREATED)
    public DataResponse<AddCartItemResponse> addItem(Authentication authentication,
                                                     @Valid @RequestBody AddCartItemRequest request) {
        CartItem item = cartControl.addItem(getUser(authentication), request.getProductId(), request.getQuantity());
        AddCartItemResponse response = new AddCartItemResponse(
                item.getItemId(),
                item.getProductId(),
                item.getQuantity(),
                item.getUnitPrice(),
                item.getLineTotal()
        );
        return new DataResponse<>(response);
    }

    @GetMapping
    public DataResponse<CartControl.CartView> getCart(Authentication authentication) {
        return new DataResponse<>(cartControl.getCart(getUser(authentication)));
    }

    @PatchMapping("/items/{itemId}")
    public DataResponse<UpdateCartItemResponse> updateItem(Authentication authentication,
                                                           @PathVariable String itemId,
                                                           @Valid @RequestBody UpdateCartItemRequest request) {
        CartItem item = cartControl.updateItemQuantity(getUser(authentication), itemId, request.getQuantity());
        return new DataResponse<>(new UpdateCartItemResponse(item.getItemId(), item.getQuantity(), item.getUnitPrice(), item.getLineTotal()));
    }

    @DeleteMapping("/items/{itemId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteItem(Authentication authentication, @PathVariable String itemId) {
        cartControl.removeItem(getUser(authentication), itemId);
    }

    private String getUser(Authentication authentication) {
        AuthenticatedUser user = (AuthenticatedUser) authentication.getPrincipal();
        return user.getUserId();
    }

    public static class AddCartItemRequest {
        @NotBlank
        private String productId;
        @Min(1)
        private int quantity;

        public String getProductId() {
            return productId;
        }

        public void setProductId(String productId) {
            this.productId = productId;
        }

        public int getQuantity() {
            return quantity;
        }

        public void setQuantity(int quantity) {
            this.quantity = quantity;
        }
    }

    public static class UpdateCartItemRequest {
        @Min(1)
        private int quantity;

        public int getQuantity() {
            return quantity;
        }

        public void setQuantity(int quantity) {
            this.quantity = quantity;
        }
    }

    public static class AddCartItemResponse {
        private String itemId;
        private String productId;
        private int quantity;
        private double unitPrice;
        private double lineTotal;

        public AddCartItemResponse(String itemId, String productId, int quantity, double unitPrice, double lineTotal) {
            this.itemId = itemId;
            this.productId = productId;
            this.quantity = quantity;
            this.unitPrice = unitPrice;
            this.lineTotal = lineTotal;
        }

        public String getItemId() {
            return itemId;
        }

        public void setItemId(String itemId) {
            this.itemId = itemId;
        }

        public String getProductId() {
            return productId;
        }

        public void setProductId(String productId) {
            this.productId = productId;
        }

        public int getQuantity() {
            return quantity;
        }

        public void setQuantity(int quantity) {
            this.quantity = quantity;
        }

        public double getUnitPrice() {
            return unitPrice;
        }

        public void setUnitPrice(double unitPrice) {
            this.unitPrice = unitPrice;
        }

        public double getLineTotal() {
            return lineTotal;
        }

        public void setLineTotal(double lineTotal) {
            this.lineTotal = lineTotal;
        }
    }

    public static class UpdateCartItemResponse {
        private String itemId;
        private int quantity;
        private double unitPrice;
        private double lineTotal;

        public UpdateCartItemResponse(String itemId, int quantity, double unitPrice, double lineTotal) {
            this.itemId = itemId;
            this.quantity = quantity;
            this.unitPrice = unitPrice;
            this.lineTotal = lineTotal;
        }

        public String getItemId() {
            return itemId;
        }

        public void setItemId(String itemId) {
            this.itemId = itemId;
        }

        public int getQuantity() {
            return quantity;
        }

        public void setQuantity(int quantity) {
            this.quantity = quantity;
        }

        public double getUnitPrice() {
            return unitPrice;
        }

        public void setUnitPrice(double unitPrice) {
            this.unitPrice = unitPrice;
        }

        public double getLineTotal() {
            return lineTotal;
        }

        public void setLineTotal(double lineTotal) {
            this.lineTotal = lineTotal;
        }
    }
}
