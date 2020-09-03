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
    private ChattingMapper chattingMapper;

    // 채팅방 등록
    public void register(RoomDTO roomDTO) {
        if (roomDTO != null) {
            chattingMapper.register(roomDTO);
        } else {
            log.error("register ERROR! {}", roomDTO);
            throw new RuntimeException("register ERROR! 채팅방 등록 메서드를 확인해주세요\n" + "Params : " + roomDTO);
        }
    }

    // 채팅방 목록 찾기
    public List<RoomDTO> getAllRooms(RoomDTO roomDTO) {
        List<RoomDTO> roomDTOList = null;
        if (roomDTO == null)
            roomDTOList = chattingMapper.selectRooms("NEWEST", 30, 0);
        else
            roomDTOList = chattingMapper.selectRooms(roomDTO.getSortStatus().toString(), roomDTO.getSearchCount(), roomDTO.getPagingStartOffset());
        return roomDTOList;
    }

    public Integer getLastRoomNumber() {
        Integer result = -2;
        result = chattingMapper.getLastRoomNumber();
        if (result == null)
            result = 0;
        return result;
    }

}

