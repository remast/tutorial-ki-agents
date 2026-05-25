package remast.winkelshop.control;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import remast.winkelshop.common.PagedResponse;
import remast.winkelshop.entity.ProductRepository;
import remast.winkelshop.entity.Currency;
import remast.winkelshop.entity.Product;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductControl {

    private final ProductRepository productRepository;

    public PagedResponse<Product> searchProducts(String search,
                                                 String category,
                                                 Double minPrice,
                                                 Double maxPrice,
                                                 Boolean inStock,
                                                 int page,
                                                 int size) {
        int safePage = Math.max(page, 0);
        int safeSize = Math.max(size, 1);
        long offset = (long) safePage * safeSize;

        List<Product> content = productRepository.search(search, category, minPrice, maxPrice, inStock, safeSize, offset)
                .stream()
                .map(record -> new Product(
                        record.id(),
                        record.name(),
                        record.description(),
                        record.price(),
                        Currency.valueOf(record.currency()),
                        record.inStock(),
                        record.stock(),
                        record.categoryId(),
                        record.imageUrl()
                ))
                .toList();

        long total = productRepository.countSearch(search, category, minPrice, maxPrice, inStock);
        return PagedResponse.fromContentAndTotal(content, safePage, safeSize, total);
    }

    public Product getProduct(String productId) {
        return productRepository.findById(productId)
                .map(record -> new Product(
                        record.id(),
                        record.name(),
                        record.description(),
                        record.price(),
                        Currency.valueOf(record.currency()),
                        record.inStock(),
                        record.stock(),
                        record.categoryId(),
                        record.imageUrl()
                ))
                .orElseThrow(() -> new NotFoundException("product not found"));
    }
}
