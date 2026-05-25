package remast.winkelshop.entity;

public record OrderItem(String productId, String name, int quantity, double unitPrice) {
    public double lineTotal() {
        return unitPrice * quantity;
    }
}
