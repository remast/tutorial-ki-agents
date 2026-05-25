package remast.winkelshop.entity;

import org.springframework.data.jdbc.repository.query.Modifying;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.Repository;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface CartItemRepository extends Repository<CartItemEntity, Long> {

    List<CartItemEntity> findByUserIdOrderByCreatedAtAsc(String userId);

    Optional<CartItemEntity> findByUserIdAndItemId(String userId, String itemId);

    CartItemEntity save(CartItemEntity item);

    void delete(CartItemEntity item);

    @Modifying
    @Query("DELETE FROM cart_items WHERE user_id = :userId")
    int deleteAllByUserId(@Param("userId") String userId);
}
