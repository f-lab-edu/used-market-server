package com.market.Server.controller;

import com.market.Server.model.response.CommonResult;
import com.market.Server.dto.UserDTO;
import com.market.Server.service.ResponseService;
import com.market.Server.service.UserService;
import com.market.Server.service.UserServiceImpl;
import com.sun.istack.internal.NotNull;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiParam;
import lombok.NonNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
    public void signUp(@RequestBody @NotNull UserDTO userDTO) {
        if(UserDTO.hasNullDataBeforeSignup(userDTO)){
            throw new NullPointerException("회원가입시 필수 데이터를 모두 입력해야 합니다.");
        }
        userService.register(userDTO);
    }
    /**
     * 회원 로그인을 진행한다. Login 요청시 id, password가 NULL일 경우 NullPointerException을 throw한다.
     */
//    @PostMapping("login")
//    public ResponseEntity<LoginResponse> login(@RequestBody @NonNull MemberLoginRequest loginRequest,
//                                               HttpSession session) {
//        ResponseEntity<LoginResponse> responseEntity = null;
//        String id = loginRequest.getId();
//        String password = loginRequest.getPassword();
//        LoginResponse loginResponse;
//        MemberDTO memberInfo = memberService.login(id, password);
//
//        if (memberInfo == null) {
//            // ID, Password에 맞는 정보가 없을 때
//            loginResponse = LoginResponse.FAIL;
//            responseEntity = new ResponseEntity<MemberController.LoginResponse>(loginResponse,
//                    HttpStatus.UNAUTHORIZED);
//        } else if (MemberDTO.Status.DEFAULT.equals(memberInfo.getStatus())) {
//            // 성공시 세션에 ID를 저장
//            loginResponse = LoginResponse.success(memberInfo);
//            SessionUtil.setLoginMemberId(session, id);
//            responseEntity = new ResponseEntity<LoginResponse>(loginResponse, HttpStatus.OK);
//        } else {
//            // 예상하지 못한 오류일 경우
//            log.error("login ERROR" + responseEntity);
//            throw new RuntimeException("login ERROR!");
//        }
//
//        return responseEntity;
//    }
}
