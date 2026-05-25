package remast.winkelshop.order.control;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import remast.winkelshop.order.boundary.PagedResponse;
import remast.winkelshop.order.entity.ProductEntity;
import remast.winkelshop.order.entity.ProductRepository;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;

    public PagedResponse<ProductEntity> searchProducts(int page,
                                                       int size) {
        var safePage = Math.max(page, 0);
        var safeSize = Math.max(size, 1);
        var pageRequest = PageRequest.of(safePage, safeSize, Sort.by("name").ascending());
        var result = productRepository.findAll(pageRequest);

        return PagedResponse.fromContentAndTotal(result.getContent(), safePage, safeSize, result.getTotalElements());
    }

    public ProductEntity getProduct(UUID productId) {
        return productRepository.findById(productId)
                .orElseThrow();
    }
}
