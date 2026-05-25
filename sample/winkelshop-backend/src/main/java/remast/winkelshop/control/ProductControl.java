package remast.winkelshop.control;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import remast.winkelshop.common.PagedResponse;
import remast.winkelshop.entity.ProductEntity;
import remast.winkelshop.entity.ProductRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductControl {

    private final ProductRepository productRepository;

    public PagedResponse<ProductEntity> searchProducts(String search,
                                                       String category,
                                                       Double minPrice,
                                                       Double maxPrice,
                                                       Boolean inStock,
                                                       int page,
                                                       int size) {
        int safePage = Math.max(page, 0);
        int safeSize = Math.max(size, 1);
        long offset = (long) safePage * safeSize;

        List<ProductEntity> content = productRepository.search(search, category, minPrice, maxPrice, inStock, safeSize, offset);

        long total = productRepository.countSearch(search, category, minPrice, maxPrice, inStock);
        return PagedResponse.fromContentAndTotal(content, safePage, safeSize, total);
    }

    public ProductEntity getProduct(String productId) {
        return productRepository.findById(productId)
                .orElseThrow();
    }
}
