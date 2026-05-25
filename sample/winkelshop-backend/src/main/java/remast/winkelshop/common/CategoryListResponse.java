package remast.winkelshop.common;

import java.util.List;

public record CategoryListResponse(List<?> data, Meta meta) {
    public record Meta(int count) {
    }
}
