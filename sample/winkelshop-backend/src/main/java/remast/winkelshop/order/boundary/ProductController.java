package remast.winkelshop.order.boundary;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import remast.winkelshop.order.control.ProductService;
import remast.winkelshop.order.entity.ProductEntity;

@RestController
@RequestMapping("/products")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;

    @GetMapping
    public PagedResponse<ProductListItem> searchProducts(
            @RequestParam(required = false) String search,
            @RequestParam(required = false) String category,
            @RequestParam(required = false) Double minPrice,
            @RequestParam(required = false) Double maxPrice,
            @RequestParam(required = false) Boolean inStock,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size
    ) {
        return productService.searchProducts(search, category, minPrice, maxPrice, inStock, page, size)
                .map(product -> new ProductListItem(
                        product.id(),
                        product.name(),
                        product.price(),
                        product.currency(),
                        product.inStock(),
                        product.categoryId()
                ));
    }

    @GetMapping("/{productId}")
    public DataResponse<ProductEntity> getProduct(@PathVariable String productId) {
        return new DataResponse<>(productService.getProduct(productId));
    }

    public record ProductListItem(
            String id,
            String name,
            double price,
            String currency,
            boolean inStock,
            String categoryId
    ) {
    }
}
