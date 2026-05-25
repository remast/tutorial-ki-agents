package remast.winkelshop.common;

import lombok.Builder;

import java.util.List;
import java.util.function.Function;

@Builder
public record PagedResponse<T>(
        List<T> content,
        PageableDetails pageable,
        boolean last,
        int totalPages,
        long totalElements,
        int size,
        int number,
        SortDetails sort,
        boolean first,
        int numberOfElements,
        boolean empty
) {

    public <R> PagedResponse<R> map(Function<T, R> mapper) {
        return PagedResponse.<R>builder()
                .content(content.stream().map(mapper).toList())
                .pageable(pageable)
                .last(last)
                .totalPages(totalPages)
                .totalElements(totalElements)
                .size(size)
                .number(number)
                .sort(sort)
                .first(first)
                .numberOfElements(numberOfElements)
                .empty(empty)
                .build();
    }

    public static <T> PagedResponse<T> of(List<T> all, int page, int size) {
        int safePage = Math.max(page, 0);
        int safeSize = Math.max(size, 1);
        int totalElements = all.size();
        int from = Math.min(safePage * safeSize, totalElements);
        int to = Math.min(from + safeSize, totalElements);
        List<T> content = all.subList(from, to);
        int totalPages = totalElements == 0 ? 1 : (int) Math.ceil((double) totalElements / safeSize);

        return PagedResponse.<T>builder()
                .content(content)
                .pageable(new PageableDetails(safePage, safeSize, new SortDetails(false, true, true), (long) safePage * safeSize, true, false))
                .last(safePage >= totalPages - 1)
                .totalPages(totalPages)
                .totalElements(totalElements)
                .size(safeSize)
                .number(safePage)
                .sort(new SortDetails(false, true, true))
                .first(safePage == 0)
                .numberOfElements(content.size())
                .empty(content.isEmpty())
                .build();
    }

    public static <T> PagedResponse<T> fromContentAndTotal(List<T> content, int page, int size, long totalElements) {
        int safePage = Math.max(page, 0);
        int safeSize = Math.max(size, 1);
        int totalPages = totalElements == 0 ? 1 : (int) Math.ceil((double) totalElements / safeSize);

        return PagedResponse.<T>builder()
                .content(content)
                .pageable(new PageableDetails(safePage, safeSize, new SortDetails(false, true, true), (long) safePage * safeSize, true, false))
                .last(safePage >= totalPages - 1)
                .totalPages(totalPages)
                .totalElements(totalElements)
                .size(safeSize)
                .number(safePage)
                .sort(new SortDetails(false, true, true))
                .first(safePage == 0)
                .numberOfElements(content.size())
                .empty(content.isEmpty())
                .build();
    }

    public record PageableDetails(
            int pageNumber,
            int pageSize,
            SortDetails sort,
            long offset,
            boolean paged,
            boolean unpaged
    ) {
    }

    public record SortDetails(boolean sorted, boolean unsorted, boolean empty) {
    }
}
