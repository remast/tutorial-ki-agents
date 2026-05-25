package remast.winkelshop.boundary;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
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
import remast.winkelshop.entity.CartItemEntity;
import remast.winkelshop.security.AuthenticatedUser;

@RestController
@RequestMapping("/cart")
@RequiredArgsConstructor
public class CartController {

    private final CartControl cartControl;

    @PostMapping("/items")
    @ResponseStatus(HttpStatus.CREATED)
    public DataResponse<CartItemEntity> addItem(Authentication authentication,
                                                @Valid @RequestBody AddCartItemRequest request) {
        CartItemEntity item = cartControl.addItem(getUser(authentication), request.productId(), request.quantity());
        return new DataResponse<>(item);
    }

    @GetMapping
    public DataResponse<CartControl.CartView> getCart(Authentication authentication) {
        return new DataResponse<>(cartControl.getCart(getUser(authentication)));
    }

    @PatchMapping("/items/{itemId}")
    public DataResponse<CartItemEntity> updateItem(Authentication authentication,
                                                   @PathVariable String itemId,
                                                   @Valid @RequestBody UpdateCartItemRequest request) {
        CartItemEntity item = cartControl.updateItemQuantity(getUser(authentication), itemId, request.quantity());
        return new DataResponse<>(item);
    }

    @DeleteMapping("/items/{itemId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteItem(Authentication authentication, @PathVariable String itemId) {
        cartControl.removeItem(getUser(authentication), itemId);
    }

    private String getUser(Authentication authentication) {
        return ((AuthenticatedUser) authentication.getPrincipal()).userId();
    }

    public record AddCartItemRequest(@NotBlank String productId, @Min(1) int quantity) {
    }

    public record UpdateCartItemRequest(@Min(1) int quantity) {
    }

}
