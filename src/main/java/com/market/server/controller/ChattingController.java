package com.market.server.controller;
import com.market.server.dto.RoomDTO;
import com.market.server.service.ChattingService;
import io.swagger.annotations.Api;
import lombok.extern.log4j.Log4j2;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;
import java.util.stream.Collectors;

@Api(tags = {"6. chatting"})
@RestController
@Log4j2
public class ChattingController {

    private final ChattingService chattingService;

    public ChattingController(ChattingService chattingService) {
        this.chattingService = chattingService;
    }

    /**
     * 대기방 페이지
     *
     * @return
     */
    @GetMapping("/waiting-room")
    public ModelAndView room() {
        ModelAndView mv = new ModelAndView();
        mv.setViewName("room");
        return mv;
    }

    /**
     * 채팅방 생성하기
     *
     * @param params
     * @return
     */
    @PostMapping("/rooms")
    @ResponseBody
    public List<RoomDTO> createRoom(RoomDTO roomDTO) {
        String roomName = roomDTO.getRoomName();
        RoomDTO room = null;

        if (roomName != null) {
            int roomNumber = chattingService.getLastRoomNumber();
            room = RoomDTO.builder()
                    .roomNumber(++roomNumber)
                    .roomName(roomName)
                    .build();
            chattingService.register(room);
        }
        else{
            throw new NullPointerException("채팅방이 존재 하는지 확인 부탁 드립니다.");
        }

        return chattingService.getAllRooms(room);
    }

    /**
     * 채팅방 정보가져오기
     *
     * @param params
     * @return
     */
    @GetMapping("/rooms")
    @ResponseBody
    public List<RoomDTO> getRoom(@RequestBody RoomDTO roomDTO) {
        return chattingService.getAllRooms(roomDTO);
    }

    /**
     * 채팅방 이동
     *
     * @return
     */
    @GetMapping("/chatting")
    public ModelAndView C(RoomDTO roomDTO) {
        ModelAndView modelAndView = new ModelAndView();
        int roomNumber = roomDTO.getRoomNumber();

        List<RoomDTO> roomsDTOList = chattingService.getAllRooms(null).stream().filter(o -> o.getRoomNumber() == roomNumber).collect(Collectors.toList());
        if (roomsDTOList != null && roomsDTOList.size() > 0) {
            modelAndView.addObject("roomName", roomDTO.getRoomName());
            modelAndView.addObject("roomNumber", roomDTO.getRoomNumber());
            modelAndView.setViewName("chat");
        } else {
            modelAndView.setViewName("room");
        }
        return modelAndView;
    }
}