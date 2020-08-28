package com.market.server.dto;

import lombok.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class RoomDTO {
    public enum Status {
        CREATE, UPDATE, DELETE
    }
    public enum SortStatus {
        NEWEST, OLDEST
    }
    private int roomNumber;
    private String roomName;
    private Status status;
    private Date createtime;
    private Date updatetime;
    private List<RoomDTO> roomDTOList;
    private SortStatus sortStatus;
    private int searchCount;
    private int pagingStartOffset;

    @Override
    public String toString() {
        return "Room [roomNumber=" + roomNumber + ", roomName=" + roomName + "]";
    }

    @Builder
    public RoomDTO(@NonNull int roomNumber, String roomName) {
        this.roomNumber = roomNumber;
        this.roomName = roomName;
        this.createtime = new Date();
        roomDTOList = new ArrayList<RoomDTO>();
        SortStatus sortStatus = SortStatus.NEWEST;
        searchCount = 10;
        pagingStartOffset = 0;
    }
}
