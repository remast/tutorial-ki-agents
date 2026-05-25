package remast.winkelshop.control;

import org.springframework.stereotype.Service;
import remast.winkelshop.entity.Category;
import remast.winkelshop.entity.SeedData;

import java.util.List;

@Service
public class CategoryControl {

    private final SeedData seedData;

    public CategoryControl(SeedData seedData) {
        this.seedData = seedData;
    }

    public List<Category> getAllCategories() {
        return seedData.getCategories();
    }
}
