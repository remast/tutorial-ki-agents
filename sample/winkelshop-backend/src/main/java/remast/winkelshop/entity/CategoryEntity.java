package remast.winkelshop.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

@Table("categories")
public record CategoryEntity(
        @Id String id,
        String name,
        String description
) {
}
