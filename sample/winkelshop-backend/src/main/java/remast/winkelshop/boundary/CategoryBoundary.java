package remast.winkelshop.boundary;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import remast.winkelshop.common.CategoryListResponse;
import remast.winkelshop.control.CategoryControl;
import remast.winkelshop.entity.Category;

import java.util.List;

@RestController
@RequestMapping("/v1/categories")
public class CategoryBoundary {

    private final CategoryControl categoryControl;

    public CategoryBoundary(CategoryControl categoryControl) {
        this.categoryControl = categoryControl;
    }

    @GetMapping
    public CategoryListResponse getCategories() {
        List<Category> categories = categoryControl.getAllCategories();
        return new CategoryListResponse(categories, new CategoryListResponse.Meta(categories.size()));
    }
}
