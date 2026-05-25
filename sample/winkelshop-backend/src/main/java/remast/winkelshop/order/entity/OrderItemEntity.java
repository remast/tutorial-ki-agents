package remast.winkelshop.order.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

@Table("order_items")
public record OrderItemEntity(
        @Id Long id,
        @Column("order_id") String orderId,
        @Column("product_id") String productId,
        String name,
        int quantity,
        @Column("unit_price") double unitPrice
) {
}
