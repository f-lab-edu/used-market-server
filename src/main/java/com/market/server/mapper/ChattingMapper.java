package com.market.server.mapper;

import com.market.server.dto.CategoryDTO;
import com.market.server.dto.ProductDTO;
import com.market.server.dto.RoomDTO;

import java.util.List;

public interface ChattingMapper {
    public int register(RoomDTO roomDTO);
    public List<RoomDTO> selectRooms(String sortStatus, int searchCount, int pagingStartOffset);
    public Integer getLastRoomNumber();
}
