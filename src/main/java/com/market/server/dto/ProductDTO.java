package com.market.server.dto;

import lombok.*;

import java.util.Date;

@Getter
@Setter
@ToString
@AllArgsConstructor
public class ProductDTO {
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
}
