package com.market.server.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
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
    private int writerId;
    private String title;
    private String contents;
    private Status status;
    private boolean istrade;
    private Date createtime;
    private Date updatetime;
    private long deliveryprice;
    private int dibcount;
}
