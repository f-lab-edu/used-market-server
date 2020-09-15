package com.market.server.service;

import com.market.server.dto.RoomDTO;
import com.market.server.mapper.ChattingMapper;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Log4j2
public class ChattingService {
    @Autowired
    private ChattingMapper chattingRoomMapper;

    private static final String DEFAULT_ROOMS_SORT_STATUS = "NEWEST";
    private static final int DEFAULT_SEARCH_COUNT = 30;
    private static final int DEFAULT_PAGING_OFFSET = 0;

    // 채팅방 등록
    public void register(RoomDTO roomDTO) {
        if (roomDTO != null) {
            chattingRoomMapper.register(roomDTO);
        } else {
            log.error("register ERROR! {}", roomDTO);
            throw new RuntimeException("register ERROR! 채팅방 등록 메서드를 확인해주세요\n" + "Params : " + roomDTO);
        }
    }

    // 채팅방 목록 찾기
    public List<RoomDTO> getAllRooms(RoomDTO roomDTO) {
        List<RoomDTO> roomDTOList = null;
        if (roomDTO == null)
            roomDTOList = chattingRoomMapper.selectRooms(DEFAULT_ROOMS_SORT_STATUS, DEFAULT_SEARCH_COUNT, DEFAULT_PAGING_OFFSET);
        else
            roomDTOList = chattingRoomMapper.selectRooms(roomDTO.getSortStatus().toString(), roomDTO.getSearchCount(), roomDTO.getPagingStartOffset());
        return roomDTOList;
    }

    public Integer getLastRoomNumber() {
        Integer result = chattingRoomMapper.getLastRoomNumber();
        if (result == null)
            result = 0;
        return result;
    }

}

