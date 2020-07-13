package com.market.server.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import java.util.Date;

@Getter
@Setter
@ToString
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

    public ProductDTO() {
    }

    public ProductDTO(int id, long price, int accountId, String title, String contents, Status status, boolean istrade, Date createtime, Date updatetime, long deliveryprice, int dibcount) {
        this.id = id;
        this.price = price;
        this.accountId = accountId;
        this.title = title;
        this.contents = contents;
        this.status = status;
        this.istrade = istrade;
        this.createtime = createtime;
        this.updatetime = updatetime;
        this.deliveryprice = deliveryprice;
        this.dibcount = dibcount;
    }
}
