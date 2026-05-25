package remast.winkelshop.entity;

public record Product(
        String id,
        String name,
        String description,
        double price,
        Currency currency,
        boolean inStock,
        int stock,
        String categoryId,
        String imageUrl
) {
}
