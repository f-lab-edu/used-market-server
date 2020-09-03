package com.market.server.controller;
import com.market.server.dto.RoomDTO;
import com.market.server.service.ChattingService;
import io.swagger.annotations.Api;
import lombok.extern.log4j.Log4j2;
import org.springframework.ui.Model;
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
    public List<RoomDTO> createRoom(RoomDTO roomDTO, Model model) {
        model.addAttribute("roomDTO", roomDTO);
        String roomName = roomDTO.getRoomName();

        RoomDTO room = null;
        if (roomName != null && !roomName.trim().equals("")) {
            int roomNumber = 0;
            roomNumber = chattingService.getLastRoomNumber();
            room = RoomDTO.builder()
                    .roomNumber(++roomNumber)
                    .roomName(roomName)
                    .build();
            chattingService.register(room);
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
    public ModelAndView moveRoom(RoomDTO roomDTO, Model model) {
        model.addAttribute("roomDTO", roomDTO);
        ModelAndView mv = new ModelAndView();
        int roomNumber = roomDTO.getRoomNumber();

        List<RoomDTO> new_list = chattingService.getAllRooms(null).stream().filter(o -> o.getRoomNumber() == roomNumber).collect(Collectors.toList());
        if (new_list != null && new_list.size() > 0) {
            mv.addObject("roomName", roomDTO.getRoomName());
            mv.addObject("roomNumber", roomDTO.getRoomNumber());
            mv.setViewName("chat");
        } else {
            mv.setViewName("room");
        }
        return mv;
    }
}