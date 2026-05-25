package remast.winkelshop.order.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

@Table("cart_items")
public record CartItemEntity(
        @Id Long id,
        @Column("item_id") String itemId,
        @Column("user_id") String userId,
        @Column("product_id") String productId,
        int quantity,
        @Column("unit_price") double unitPrice,
        @Column("created_at") java.time.Instant createdAt
) {
}
