package remast.winkelshop.order.entity;

import org.springframework.data.repository.Repository;

import java.util.List;

public interface CategoryRepository extends Repository<CategoryEntity, String> {
    List<CategoryEntity> findAll();
}
