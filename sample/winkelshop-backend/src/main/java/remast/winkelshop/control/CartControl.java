package remast.winkelshop.control;

import org.springframework.stereotype.Service;
import remast.winkelshop.entity.Cart;
import remast.winkelshop.entity.CartItem;
import remast.winkelshop.entity.Currency;
import remast.winkelshop.entity.Product;
import remast.winkelshop.entity.SeedData;

import java.util.List;

@Service
public class CartControl {

    private final SeedData seedData;

    public CartControl(SeedData seedData) {
        this.seedData = seedData;
    }

    public CartItem addItem(String userId, String productId, int quantity) {
        Product product = seedData.findProductById(productId)
                .orElseThrow(() -> new NotFoundException("product not found"));
        if (!product.isInStock()) {
            throw new ConflictException("product is out of stock");
        }

        Cart cart = getOrCreateCart(userId);
        CartItem item = new CartItem(seedData.nextCartItemId(), productId, quantity, product.getPrice());
        cart.getItems().add(item);
        return item;
    }

    public CartView getCart(String userId) {
        Cart cart = getOrCreateCart(userId);
        List<CartLine> lines = cart.getItems().stream().map(item -> {
            Product product = seedData.findProductById(item.getProductId())
                    .orElseThrow(() -> new NotFoundException("product not found"));
            return new CartLine(item.getItemId(), item.getProductId(), product.getName(), item.getQuantity(), item.getUnitPrice());
        }).toList();

        double subtotal = lines.stream().mapToDouble(CartLine::getLineTotal).sum();
        return new CartView(lines, subtotal, Currency.GALLEON);
    }

    public CartItem updateItemQuantity(String userId, String itemId, int quantity) {
        CartItem item = getOrCreateCart(userId).getItems().stream()
                .filter(current -> current.getItemId().equals(itemId))
                .findFirst()
                .orElseThrow(() -> new NotFoundException("cart item not found"));
        item.setQuantity(quantity);
        return item;
    }

    public void removeItem(String userId, String itemId) {
        Cart cart = getOrCreateCart(userId);
        boolean removed = cart.getItems().removeIf(item -> item.getItemId().equals(itemId));
        if (!removed) {
            throw new NotFoundException("cart item not found");
        }
    }

    public Cart consumeCart(String userId) {
        Cart cart = getOrCreateCart(userId);
        if (cart.getItems().isEmpty()) {
            throw new ConflictException("cart is empty");
        }
        Cart snapshot = new Cart(userId);
        snapshot.setItems(List.copyOf(cart.getItems()));
        cart.getItems().clear();
        return snapshot;
    }

    private Cart getOrCreateCart(String userId) {
        return seedData.getCartsByUserId().computeIfAbsent(userId, Cart::new);
    }

    public static class CartView {
        private List<CartLine> items;
        private double subtotal;
        private Currency currency;

        public CartView(List<CartLine> items, double subtotal, Currency currency) {
            this.items = items;
            this.subtotal = subtotal;
            this.currency = currency;
        }

        public List<CartLine> getItems() {
            return items;
        }

        public void setItems(List<CartLine> items) {
            this.items = items;
        }

        public double getSubtotal() {
            return subtotal;
        }

        public void setSubtotal(double subtotal) {
            this.subtotal = subtotal;
        }

        public Currency getCurrency() {
            return currency;
        }

        public void setCurrency(Currency currency) {
            this.currency = currency;
        }
    }

    public static class CartLine {
        private String itemId;
        private String productId;
        private String name;
        private int quantity;
        private double unitPrice;

        public CartLine(String itemId, String productId, String name, int quantity, double unitPrice) {
            this.itemId = itemId;
            this.productId = productId;
            this.name = name;
            this.quantity = quantity;
            this.unitPrice = unitPrice;
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

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
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
            return unitPrice * quantity;
        }
    }
}
