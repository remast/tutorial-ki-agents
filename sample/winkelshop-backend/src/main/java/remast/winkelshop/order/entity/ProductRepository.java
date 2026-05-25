package remast.winkelshop.order.entity;

import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.Repository;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ProductRepository extends Repository<ProductEntity, UUID> {

    Optional<ProductEntity> findById(UUID id);

    @Query("""
            SELECT * FROM products
            WHERE (:search IS NULL OR :search = '' OR lower(name) LIKE lower(concat('%', :search, '%')))
              AND (:category IS NULL OR :category = '' OR category_id = :category)
              AND (:minPrice IS NULL OR price >= :minPrice)
              AND (:maxPrice IS NULL OR price <= :maxPrice)
              AND (:inStock IS NULL OR in_stock = :inStock)
            ORDER BY name
            LIMIT :size OFFSET :offset
            """)
    List<ProductEntity> search(@Param("search") String search,
                               @Param("category") UUID category,
                               @Param("minPrice") Double minPrice,
                               @Param("maxPrice") Double maxPrice,
                               @Param("inStock") Boolean inStock,
                               @Param("size") int size,
                               @Param("offset") long offset);

    @Query("""
            SELECT count(*) FROM products
            WHERE (:search IS NULL OR :search = '' OR lower(name) LIKE lower(concat('%', :search, '%')))
              AND (:category IS NULL OR :category = '' OR category_id = :category)
              AND (:minPrice IS NULL OR price >= :minPrice)
              AND (:maxPrice IS NULL OR price <= :maxPrice)
              AND (:inStock IS NULL OR in_stock = :inStock)
            """)
    long countSearch(@Param("search") String search,
                     @Param("category") UUID category,
                     @Param("minPrice") Double minPrice,
                     @Param("maxPrice") Double maxPrice,
                     @Param("inStock") Boolean inStock);
}
