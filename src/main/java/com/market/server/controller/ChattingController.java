package com.market.server.controller;

import com.market.server.dto.RoomDTO;
import com.market.server.service.ChattingService;
import io.swagger.annotations.Api;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

@Api(tags = {"6. chatting"})
@RestController
@Log4j2
public class ChattingController {

    static int roomNumber = 0;
    private ChattingResponse chattingResponse;
    private final ChattingService chattingService;

    @Autowired
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
    public @ResponseBody
    List<RoomDTO> createRoom(@RequestParam HashMap<Object, Object> params, ChattingRequest chattingRequest) {
        String roomName = (String) params.get("roomName");
        RoomDTO roomDTO = null;
        if (roomName != null && !roomName.trim().equals("")) {
            roomDTO = RoomDTO.builder()
                    .roomNumber(++roomNumber)
                    .roomName(roomName)
                    .build();
            chattingService.register(roomDTO);
        }

        return chattingService.getRooms(roomDTO);
    }

    /**
     * 채팅방 정보가져오기
     *
     * @param params
     * @return
     */
    @GetMapping("/rooms")
    public @ResponseBody
    List<RoomDTO> getRoom(@RequestParam HashMap<Object, Object> params, @RequestBody RoomDTO roomDTO) {
        return chattingService.getRooms(roomDTO);
    }

    /**
     * 채팅방 이동
     *
     * @return
     */
    @GetMapping("/chatting")
    public ModelAndView moveRoom(@RequestParam HashMap<Object, Object> params) {
        ModelAndView mv = new ModelAndView();
        int roomNumber = Integer.parseInt((String) params.get("roomNumber"));

        List<RoomDTO> new_list = chattingService.getRooms(null).stream().filter(o->o.getRoomNumber()==roomNumber).collect(Collectors.toList());
        if(new_list != null && new_list.size() > 0) {
            mv.addObject("roomName", params.get("roomName"));
            mv.addObject("roomNumber", params.get("roomNumber"));
            mv.setViewName("chat");
        }else {
            mv.setViewName("room");
        }
        return mv;
    }

    // -------------- response 객체 --------------

    @Getter
    @AllArgsConstructor
    private static class ChattingResponse {
        private List<RoomDTO> roomDTOList;
    }

    // -------------- request 객체 --------------

    @Setter
    @Getter
    private static class ChattingRequest {
        private int roomNumber;
        private String roomName;
        private RoomDTO.Status status;
        private Date updatetime;
    }
}