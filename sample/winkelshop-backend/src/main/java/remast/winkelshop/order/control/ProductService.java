package remast.winkelshop.order.control;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import remast.winkelshop.order.boundary.PagedResponse;
import remast.winkelshop.order.entity.ProductEntity;
import remast.winkelshop.order.entity.ProductRepository;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;

    public PagedResponse<ProductEntity> searchProducts(String search,
                                                       UUID category,
                                                       Double minPrice,
                                                       Double maxPrice,
                                                       Boolean inStock,
                                                       int page,
                                                       int size) {
        var safePage = Math.max(page, 0);
        var safeSize = Math.max(size, 1);
        var offset = (long) safePage * safeSize;

        var content = productRepository.search(search, category, minPrice, maxPrice, inStock, safeSize, offset);

        var total = productRepository.countSearch(search, category, minPrice, maxPrice, inStock);
        return PagedResponse.fromContentAndTotal(content, safePage, safeSize, total);
    }

    public ProductEntity getProduct(UUID productId) {
        return productRepository.findById(productId)
                .orElseThrow();
    }
}
