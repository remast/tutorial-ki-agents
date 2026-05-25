package remast.winkelshop.control;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import remast.winkelshop.entity.CategoryRepository;
import remast.winkelshop.entity.Category;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CategoryControl {

    private final CategoryRepository categoryRepository;

    public List<Category> getAllCategories() {
        return categoryRepository.findAll().stream()
                .map(record -> new Category(record.id(), record.name(), record.description()))
                .toList();
    }
}
