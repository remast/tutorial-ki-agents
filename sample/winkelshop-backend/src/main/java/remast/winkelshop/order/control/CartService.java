package remast.winkelshop.order.control;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import remast.winkelshop.order.entity.CartItemEntity;
import remast.winkelshop.order.entity.CartItemRepository;
import remast.winkelshop.order.entity.Currency;
import remast.winkelshop.order.entity.ProductRepository;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CartService {

    private final ProductRepository productRepository;
    private final CartItemRepository cartItemRepository;

    public CartItemEntity addItem(String userId, String productId, int quantity) {
        var product = productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("product not found"));
        if (!product.inStock()) {
            throw new RuntimeException("product is out of stock");
        }

        var saved = cartItemRepository.save(new CartItemEntity(
                null,
                "item_" + UUID.randomUUID().toString().replace("-", "").substring(0, 8),
                userId,
                productId,
                quantity,
                product.price(),
                Instant.now()
        ));
        return saved;
    }

    public CartView getCart(String userId) {
        var lines = cartItemRepository.findByUserIdOrderByCreatedAtAsc(userId).stream()
                .map(item -> {
                    var product = productRepository.findById(item.productId())
                            .orElseThrow();
                    return new CartLine(item.itemId(), item.productId(), product.name(), item.quantity(), item.unitPrice());
                })
                .toList();

        var subtotal = lines.stream().mapToDouble(CartLine::lineTotal).sum();
        return new CartView(lines, subtotal, Currency.GALLEON);
    }

    public CartItemEntity updateItemQuantity(String userId, String itemId, int quantity) {
        var existing = cartItemRepository.findByUserIdAndItemId(userId, itemId)
                .orElseThrow();
        var updated = cartItemRepository.save(new CartItemEntity(
                existing.id(),
                existing.itemId(),
                existing.userId(),
                existing.productId(),
                quantity,
                existing.unitPrice(),
                existing.createdAt()
        ));
        return updated;
    }

    public void removeItem(String userId, String itemId) {
        var existing = cartItemRepository.findByUserIdAndItemId(userId, itemId)
                .orElseThrow();
        cartItemRepository.delete(existing);
    }

    public List<CartItemEntity> consumeCart(String userId) {
        var items = cartItemRepository.findByUserIdOrderByCreatedAtAsc(userId);
        cartItemRepository.deleteAllByUserId(userId);
        return items;
    }

    public record CartView(List<CartLine> items, double subtotal, Currency currency) {
    }

    public record CartLine(String itemId, String productId, String name, int quantity, double unitPrice) {
        public double lineTotal() {
            return unitPrice * quantity;
        }
    }
}
