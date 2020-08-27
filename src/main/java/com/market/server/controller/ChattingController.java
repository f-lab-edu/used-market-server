package com.market.server.controller;
import com.market.server.dto.RoomDTO;
import io.swagger.annotations.Api;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

@Api(tags = {"6. chatting"})
@RestController
public class ChattingController {

    List<RoomDTO> roomDTOList = new ArrayList<RoomDTO>();
    static int roomNumber = 0;

    /**
     * 대기방 페이지
     * @return
     */
    @GetMapping("/waiting-rooms")
    public ModelAndView room() {
        ModelAndView mv = new ModelAndView();
        mv.setViewName("room");
        return mv;
    }

    /**
     * 채팅방 생성하기
     * @param params
     * @return
     */
    @PostMapping("/rooms")
    public @ResponseBody
    List<RoomDTO> createRoom(@RequestParam HashMap<Object, Object> params){
        String roomName = (String) params.get("roomName");
        if(roomName != null && !roomName.trim().equals("")) {
            RoomDTO roomDTO = new RoomDTO();
            roomDTO.setRoomNumber(++roomNumber);
            roomDTO.setRoomName(roomName);
            roomDTOList.add(roomDTO);
        }
        return roomDTOList;
    }

    /**
     * 채팅방 정보가져오기
     * @param params
     * @return
     */
    @GetMapping("/rooms")
    public @ResponseBody List<RoomDTO> getRoom(@RequestParam HashMap<Object, Object> params){
        return roomDTOList;
    }

    /**
     * 채팅방 이동
     * @return
     */
    @GetMapping("/moving/rooms")
    public ModelAndView moveRoom(@RequestParam HashMap<Object, Object> params) {
        ModelAndView mv = new ModelAndView();
        int roomNumber = Integer.parseInt((String) params.get("roomNumber"));

        List<RoomDTO> new_list = roomDTOList.stream().filter(o->o.getRoomNumber()==roomNumber).collect(Collectors.toList());
        if(new_list != null && new_list.size() > 0) {
            mv.addObject("roomName", params.get("roomName"));
            mv.addObject("roomNumber", params.get("roomNumber"));
            mv.setViewName("chat");
        }else {
            mv.setViewName("room");
        }
        return mv;
    }
}
