package com.market.server.dto;

import lombok.*;
import java.util.Date;

@Getter
@Setter
@ToString
@NoArgsConstructor
public class ProductDTO {
    public static final int DEFAULT_SEARCH_CATEGORY_ID = 1;
    public static final int DEFAULT_PRODUCT_CACHE_COUNT = 2000;
    public static final String DEFAULT_SEARCH_CATEGORY_NAME = CategoryDTO.SortStatus.CATEGORIES.toString();
    public static final String DEFAULT_PRODUCT_SEARCH_CACHE_KEY = "noLogin";

    public enum Status {
        NEW, OLD, ECT
    }

    private int id;
    private long price;
    private int accountId;
    private String title;
    private String contents;
    private Status status;
    private boolean istrade;
    private Date createtime;
    private Date updatetime;
    private long deliveryprice;
    private int dibcount;
    private int categoryId;
    private int fileId;

    @Builder
    public ProductDTO(@NonNull int id, long price, int accountId, String title, String contents, Status status, boolean istrade, Date updatetime, long deliveryprice, int dibcount, int categoryId, int fileId) {
        this.id = id;
        this.price = price;
        this.accountId = accountId;
        this.title = title;
        this.contents = contents;
        this.status = status;
        this.istrade = istrade;
        this.createtime = new Date();
        this.updatetime = updatetime;
        this.deliveryprice = deliveryprice;
        this.dibcount = dibcount;
        this.categoryId = categoryId;
        this.fileId = fileId;
    }
}
