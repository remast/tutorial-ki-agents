package remast.winkelshop.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

@Table("products")
public record ProductEntity(
        @Id String id,
        String name,
        String description,
        double price,
        String currency,
        @Column("in_stock") boolean inStock,
        int stock,
        @Column("category_id") String categoryId,
        @Column("image_url") String imageUrl
) {
}
