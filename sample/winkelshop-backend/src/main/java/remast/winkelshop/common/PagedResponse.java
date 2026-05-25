package remast.winkelshop.common;

import java.util.List;

public class PagedResponse<T> {

    private List<T> content;
    private PageableDetails pageable;
    private boolean last;
    private int totalPages;
    private long totalElements;
    private int size;
    private int number;
    private SortDetails sort;
    private boolean first;
    private int numberOfElements;
    private boolean empty;

    public static <T> PagedResponse<T> of(List<T> all, int page, int size) {
        int safePage = Math.max(page, 0);
        int safeSize = Math.max(size, 1);
        int totalElements = all.size();
        int from = Math.min(safePage * safeSize, totalElements);
        int to = Math.min(from + safeSize, totalElements);
        List<T> content = all.subList(from, to);
        int totalPages = totalElements == 0 ? 1 : (int) Math.ceil((double) totalElements / safeSize);

        PagedResponse<T> response = new PagedResponse<>();
        response.setContent(content);
        response.setPageable(new PageableDetails(safePage, safeSize));
        response.setLast(safePage >= totalPages - 1);
        response.setTotalPages(totalPages);
        response.setTotalElements(totalElements);
        response.setSize(safeSize);
        response.setNumber(safePage);
        response.setSort(new SortDetails());
        response.setFirst(safePage == 0);
        response.setNumberOfElements(content.size());
        response.setEmpty(content.isEmpty());
        return response;
    }

    public List<T> getContent() {
        return content;
    }

    public void setContent(List<T> content) {
        this.content = content;
    }

    public PageableDetails getPageable() {
        return pageable;
    }

    public void setPageable(PageableDetails pageable) {
        this.pageable = pageable;
    }

    public boolean isLast() {
        return last;
    }

    public void setLast(boolean last) {
        this.last = last;
    }

    public int getTotalPages() {
        return totalPages;
    }

    public void setTotalPages(int totalPages) {
        this.totalPages = totalPages;
    }

    public long getTotalElements() {
        return totalElements;
    }

    public void setTotalElements(long totalElements) {
        this.totalElements = totalElements;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public SortDetails getSort() {
        return sort;
    }

    public void setSort(SortDetails sort) {
        this.sort = sort;
    }

    public boolean isFirst() {
        return first;
    }

    public void setFirst(boolean first) {
        this.first = first;
    }

    public int getNumberOfElements() {
        return numberOfElements;
    }

    public void setNumberOfElements(int numberOfElements) {
        this.numberOfElements = numberOfElements;
    }

    public boolean isEmpty() {
        return empty;
    }

    public void setEmpty(boolean empty) {
        this.empty = empty;
    }

    public static class PageableDetails {
        private int pageNumber;
        private int pageSize;
        private SortDetails sort;
        private long offset;
        private boolean paged;
        private boolean unpaged;

        public PageableDetails() {
        }

        public PageableDetails(int pageNumber, int pageSize) {
            this.pageNumber = pageNumber;
            this.pageSize = pageSize;
            this.sort = new SortDetails();
            this.offset = (long) pageNumber * pageSize;
            this.paged = true;
            this.unpaged = false;
        }

        public int getPageNumber() {
            return pageNumber;
        }

        public void setPageNumber(int pageNumber) {
            this.pageNumber = pageNumber;
        }

        public int getPageSize() {
            return pageSize;
        }

        public void setPageSize(int pageSize) {
            this.pageSize = pageSize;
        }

        public SortDetails getSort() {
            return sort;
        }

        public void setSort(SortDetails sort) {
            this.sort = sort;
        }

        public long getOffset() {
            return offset;
        }

        public void setOffset(long offset) {
            this.offset = offset;
        }

        public boolean isPaged() {
            return paged;
        }

        public void setPaged(boolean paged) {
            this.paged = paged;
        }

        public boolean isUnpaged() {
            return unpaged;
        }

        public void setUnpaged(boolean unpaged) {
            this.unpaged = unpaged;
        }
    }

    public static class SortDetails {
        private boolean sorted;
        private boolean unsorted;
        private boolean empty;

        public SortDetails() {
            this.sorted = false;
            this.unsorted = true;
            this.empty = true;
        }

        public boolean isSorted() {
            return sorted;
        }

        public void setSorted(boolean sorted) {
            this.sorted = sorted;
        }

        public boolean isUnsorted() {
            return unsorted;
        }

        public void setUnsorted(boolean unsorted) {
            this.unsorted = unsorted;
        }

        public boolean isEmpty() {
            return empty;
        }

        public void setEmpty(boolean empty) {
            this.empty = empty;
        }
    }
}
