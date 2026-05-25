package remast.winkelshop.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

@Table("users")
public record UserRecord(
        @Id String id,
        String name,
        String email,
        String password,
        String role,
        @Column("created_at") java.time.Instant createdAt
) {
}
