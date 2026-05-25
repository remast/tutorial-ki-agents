package remast.winkelshop.boundary;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import remast.winkelshop.common.CategoryListResponse;
import remast.winkelshop.control.CategoryControl;
import remast.winkelshop.entity.CategoryEntity;

import java.util.List;

@RestController
@RequestMapping("/categories")
@RequiredArgsConstructor
public class CategoryController {

    private final CategoryControl categoryControl;

    @GetMapping
    public CategoryListResponse getCategories() {
        List<CategoryEntity> categories = categoryControl.getAllCategories();
        return new CategoryListResponse(categories, new CategoryListResponse.Meta(categories.size()));
    }
}
