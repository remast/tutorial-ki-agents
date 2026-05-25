package remast.winkelshop.boundary;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import remast.winkelshop.common.DataResponse;
import remast.winkelshop.common.PagedResponse;
import remast.winkelshop.control.ProductControl;
import remast.winkelshop.entity.Product;

import java.util.List;

@RestController
@RequestMapping("/v1/products")
public class ProductBoundary {

    private final ProductControl productControl;

    public ProductBoundary(ProductControl productControl) {
        this.productControl = productControl;
    }

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
        PagedResponse<Product> products = productControl.searchProducts(search, category, minPrice, maxPrice, inStock, page, size);
        PagedResponse<ProductListItem> response = new PagedResponse<>();
        List<ProductListItem> content = products.getContent().stream()
                .map(product -> new ProductListItem(
                        product.getId(),
                        product.getName(),
                        product.getPrice(),
                        product.getCurrency().name(),
                        product.isInStock(),
                        product.getCategoryId()))
                .toList();
        response.setContent(content);
        response.setPageable(products.getPageable());
        response.setLast(products.isLast());
        response.setTotalPages(products.getTotalPages());
        response.setTotalElements(products.getTotalElements());
        response.setSize(products.getSize());
        response.setNumber(products.getNumber());
        response.setSort(products.getSort());
        response.setFirst(products.isFirst());
        response.setNumberOfElements(products.getNumberOfElements());
        response.setEmpty(products.isEmpty());
        return response;
    }

    @GetMapping("/{productId}")
    public DataResponse<Product> getProduct(@PathVariable String productId) {
        return new DataResponse<>(productControl.getProduct(productId));
    }

    public static class ProductListItem {
        private String id;
        private String name;
        private double price;
        private String currency;
        private boolean inStock;
        private String categoryId;

        public ProductListItem(String id, String name, double price, String currency, boolean inStock, String categoryId) {
            this.id = id;
            this.name = name;
            this.price = price;
            this.currency = currency;
            this.inStock = inStock;
            this.categoryId = categoryId;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public double getPrice() {
            return price;
        }

        public void setPrice(double price) {
            this.price = price;
        }

        public String getCurrency() {
            return currency;
        }

        public void setCurrency(String currency) {
            this.currency = currency;
        }

        public boolean isInStock() {
            return inStock;
        }

        public void setInStock(boolean inStock) {
            this.inStock = inStock;
        }

        public String getCategoryId() {
            return categoryId;
        }

        public void setCategoryId(String categoryId) {
            this.categoryId = categoryId;
        }
    }
}
