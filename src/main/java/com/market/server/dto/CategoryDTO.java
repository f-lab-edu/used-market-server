package com.market.server.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@AllArgsConstructor
public class CategoryDTO {
    public enum SortStatus {
        CATEGORIES, SALESCOUNTS, NEWEST, OLDEST, HIGHPRICE, LOWPRICE, GRADE
    }
    private int id;
    private String name;
    private SortStatus sortStatus;

}
