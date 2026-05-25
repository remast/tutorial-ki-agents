package remast.winkelshop.order.boundary;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import remast.winkelshop.order.control.CartService;
import remast.winkelshop.order.entity.CartItemEntity;
import remast.winkelshop.auth.boundary.User;

@RestController
@RequestMapping("/cart")
@RequiredArgsConstructor
public class CartController {

    private final CartService cartService;

    @PostMapping("/items")
    @ResponseStatus(HttpStatus.CREATED)
    public DataResponse<CartItemEntity> addItem(User user,
                                                @Valid @RequestBody AddCartItemRequest request) {
        var item = cartService.addItem(user.userId(), request.productId(), request.quantity());
        return new DataResponse<>(item);
    }

    @GetMapping
    public DataResponse<CartService.CartView> getCart(User user) {
        return new DataResponse<>(cartService.getCart(user.userId()));
    }

    @PatchMapping("/items/{itemId}")
    public DataResponse<CartItemEntity> updateItem(User user,
                                                   @PathVariable String itemId,
                                                   @Valid @RequestBody UpdateCartItemRequest request) {
        var item = cartService.updateItemQuantity(user.userId(), itemId, request.quantity());
        return new DataResponse<>(item);
    }

    @DeleteMapping("/items/{itemId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteItem(User user, @PathVariable String itemId) {
        cartService.removeItem(user.userId(), itemId);
    }

    public record AddCartItemRequest(@NotBlank String productId, @Min(1) int quantity) {
    }

    public record UpdateCartItemRequest(@Min(1) int quantity) {
    }

}
