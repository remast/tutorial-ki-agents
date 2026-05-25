package remast.winkelshop.common;

import java.util.List;

public class CategoryListResponse {

    private List<?> data;
    private Meta meta;

    public CategoryListResponse() {
    }

    public CategoryListResponse(List<?> data, Meta meta) {
        this.data = data;
        this.meta = meta;
    }

    public List<?> getData() {
        return data;
    }

    public void setData(List<?> data) {
        this.data = data;
    }

    public Meta getMeta() {
        return meta;
    }

    public void setMeta(Meta meta) {
        this.meta = meta;
    }

    public static class Meta {
        private int count;

        public Meta() {
        }

        public Meta(int count) {
            this.count = count;
        }

        public int getCount() {
            return count;
        }

        public void setCount(int count) {
            this.count = count;
        }
    }
}
