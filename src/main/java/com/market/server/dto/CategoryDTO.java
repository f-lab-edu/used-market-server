package com.market.server.dto;
import lombok.*;

@Getter
@Setter
@ToString
@NoArgsConstructor
public class CategoryDTO {
    public static final int SEARCH_COUNT = 10;
    public static final int PAGING_OFFSET = 0;
    public enum SortStatus {
        CATEGORIES, NEWEST, OLDEST, HIGHPRICE, LOWPRICE, GRADE
    }
    private int id;
    private String name;
    private SortStatus sortStatus;
    private int searchCount;
    private int pagingStartOffset;

    @Builder
    public CategoryDTO(@NonNull int id, String name, SortStatus sortStatus, int searchCount, int pagingStartOffset) {
        this.id = id;
        this.name = name;
        this.sortStatus = sortStatus;
        this.searchCount = searchCount;
        this.pagingStartOffset = pagingStartOffset;
    }

}
