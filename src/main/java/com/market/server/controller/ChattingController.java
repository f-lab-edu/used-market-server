package com.market.server.controller;
import io.swagger.annotations.Api;
import org.json.simple.JSONObject;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Api(tags = {"6. chatting"})
@RestController
public class ChattingController {

    @RequestMapping("/chatting")
    public JSONObject chat(JSONObject jsonStr) {
        return jsonStr;
    }

//    @RequestMapping("/chat")
//    public ModelAndView chat() {
//        ModelAndView mv = new ModelAndView();
//        mv.setViewName("chat");
//        return mv;
//    }
}
