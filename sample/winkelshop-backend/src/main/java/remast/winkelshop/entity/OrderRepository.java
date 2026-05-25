package remast.winkelshop.entity;

import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.Repository;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface OrderRepository extends Repository<OrderEntity, String> {

    OrderEntity save(OrderEntity order);

    Optional<OrderEntity> findByIdAndUserId(String id, String userId);

    @Query("""
            SELECT * FROM orders
            WHERE user_id = :userId
            ORDER BY created_at DESC
            LIMIT :size OFFSET :offset
            """)
    List<OrderEntity> findPageByUserId(@Param("userId") String userId,
                                       @Param("size") int size,
                                       @Param("offset") long offset);

    long countByUserId(String userId);
}
