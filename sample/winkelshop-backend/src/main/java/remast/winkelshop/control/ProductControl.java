package remast.winkelshop.control;

import org.springframework.stereotype.Service;
import remast.winkelshop.common.PagedResponse;
import remast.winkelshop.entity.Product;
import remast.winkelshop.entity.SeedData;

import java.util.List;

@Service
public class ProductControl {

    private final SeedData seedData;

    public ProductControl(SeedData seedData) {
        this.seedData = seedData;
    }

    public PagedResponse<Product> searchProducts(String search,
                                                 String category,
                                                 Double minPrice,
                                                 Double maxPrice,
                                                 Boolean inStock,
                                                 int page,
                                                 int size) {
        List<Product> filtered = seedData.getProducts().stream()
                .filter(product -> search == null || search.isBlank() || containsIgnoreCase(product.getName(), search))
                .filter(product -> category == null || category.isBlank() || product.getCategoryId().equals(category))
                .filter(product -> minPrice == null || product.getPrice() >= minPrice)
                .filter(product -> maxPrice == null || product.getPrice() <= maxPrice)
                .filter(product -> inStock == null || product.isInStock() == inStock)
                .toList();

        return PagedResponse.of(filtered, page, size);
    }

    public Product getProduct(String productId) {
        return seedData.findProductById(productId)
                .orElseThrow(() -> new NotFoundException("product not found"));
    }

    private boolean containsIgnoreCase(String value, String query) {
        return value.toLowerCase().contains(query.toLowerCase());
    }
}
