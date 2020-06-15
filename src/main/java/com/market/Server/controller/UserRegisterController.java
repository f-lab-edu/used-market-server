package com.market.Server.controller;

import com.market.Server.model.response.CommonResult;
import com.market.Server.dto.UserDTO;
import com.market.Server.service.ResponseService;
import com.market.Server.service.UserService;
import com.market.Server.service.UserServiceImpl;
import com.sun.istack.internal.NotNull;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpSession;


@Api(tags = {"2. users"})
@Controller
@RequestMapping("/user")
public class UserRegisterController {

    private UserServiceImpl userService ;
    private final ResponseService responseService;

    @Autowired
    public UserRegisterController(UserServiceImpl userService, ResponseService responseService) {
        this.userService=userService;
        this.responseService=responseService;
    }


    /**
     * 유저가 입력한 정보로 회원가입을 진행한다. 보낸 값들 중 NULL값이 있으면 "NULL_ARGUMENT" 를 리턴한다. 회원가입 요청을 보내기 전 먼저 ID 중복체크를
     * 진행한다. ID 중복시 403 상태코드를 반환한다. 회원가입 성공시 201 상태코드를 반환한다.
     *
     * @param UserDTO 회원가입을 요청한 정보
     * @return
     *
     */
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @RequestMapping(value = "/register", method = RequestMethod.POST)
    public void signUp(@RequestBody @NotNull UserDTO userDTO,
                                      RedirectAttributes redirectAttributes) {
        userService.register(userDTO);
        redirectAttributes.addFlashAttribute("msg","REGISTERD");
    }

}
