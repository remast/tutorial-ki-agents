package remast.winkelshop.control;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import remast.winkelshop.entity.CategoryEntity;
import remast.winkelshop.entity.CategoryRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CategoryControl {

    private final CategoryRepository categoryRepository;

    public List<CategoryEntity> getAllCategories() {
        return categoryRepository.findAll();
    }
}
