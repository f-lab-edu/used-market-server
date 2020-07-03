package com.market.server.controller;

import com.market.server.dto.UserDTO;
import com.market.server.service.ResponseService;
import com.market.server.service.UserServiceImpl;
import com.market.server.utils.SessionUtil;
import com.sun.istack.internal.NotNull;
import io.swagger.annotations.Api;
import lombok.*;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;


@Api(tags = {"2. users"})
@RestController
@RequestMapping("/users")
@Log4j2
public class UserController {

    private final UserServiceImpl userService;
    private static LoginResponse loginResponse = null;
    private static final ResponseEntity<LoginResponse> LOGIN_FAIL_RESPONSE = new ResponseEntity<LoginResponse>(loginResponse, HttpStatus.BAD_REQUEST);

    @Autowired
    public UserController(UserServiceImpl userService, ResponseService responseService) {
        this.userService = userService;
    }

    /**
     * 로그인한 사용자가 마이페이지를 눌렀을 때 보여줄 사용자 정보를 반환한다.
     *
     * @param session 현재 사용자의 세션
     * @return UserDTO
     */
    @GetMapping("myInfo")
    public UserInfoResponse memberInfo(HttpSession session) {
        String id = SessionUtil.getLoginMemberId(session);
        UserDTO memberInfo = userService.getUserInfo(id);
        return new UserInfoResponse(memberInfo);
    }


    /**
     * 유저가 입력한 정보로 회원가입을 진행한다. 보낸 값들 중 NULL값이 있으면 "NULL_ARGUMENT" 를 리턴한다. 회원가입 요청을 보내기 전 먼저 ID 중복체크를
     * 진행한다. ID 중복시 403 상태코드를 반환한다. 회원가입 성공시 201 상태코드를 반환한다.
     *
     * @param userDTO 회원가입을 요청한 정보
     * @return
     */
    @PostMapping("signUp")
    @ResponseStatus(HttpStatus.CREATED)
    public void signUp(@RequestBody @NotNull UserDTO userDTO) {
        if (UserDTO.hasNullDataBeforeSignup(userDTO)) {
            throw new NullPointerException("회원가입시 필수 데이터를 모두 입력해야 합니다.");
        }
        userService.register(userDTO);
    }

    /**
     * 회원 로그인을 진행한다. Login 요청시 id, password가 NULL일 경우 NullPointerException을 throw한다.
     */
    @PostMapping("signIn")
    public ResponseEntity<LoginResponse> login(@RequestBody @NotNull UserLoginRequest loginRequest,
                                               HttpSession session) {
        ResponseEntity<LoginResponse> responseEntity = null;
        String id = loginRequest.getId();
        String password = loginRequest.getPassword();
        LoginResponse loginResponse;
        UserDTO userInfo = userService.login(id, password);

        if (userInfo == null) {
            // ID, Password에 맞는 정보가 없을 때
            return LOGIN_FAIL_RESPONSE;
        } else if (UserDTO.Status.DEFAULT == userInfo.getStatus()) {
            // 성공시 세션에 ID를 저장
            loginResponse = LoginResponse.success(userInfo);
            SessionUtil.setLoginMemberId(session, id);
            responseEntity = new ResponseEntity<LoginResponse>(loginResponse, HttpStatus.OK);
        } else {
            // 예상하지 못한 오류일 경우
            throw new RuntimeException("Login Error! 유저 정보가 없거나 지워진 유저 정보입니다.");
        }

        return responseEntity;
    }

    /**
     * 회원 로그아웃 메서드.
     *
     * @param session 현제 접속한 세션
     * @return 로그인 하지 않았을 시 401코드를 반환하고 result:NO_LOGIN 반환 로그아웃 성공시 200 코드를 반환
     * @author jun
     */
    @GetMapping("logout")
    public void logout(HttpSession session) {
        SessionUtil.logoutMember(session);
    }

    @Getter
    @AllArgsConstructor
    private static class UserInfoResponse {
        private UserDTO userDTO;
    }

    // -------------- response 객체 --------------

    @Getter
    // 필드값을 모두 포함한 생성자를 자동 생성해준다.
    @AllArgsConstructor
    // 생성자를 자동 생성하지만, 필드명 위에 @NonNull로 표기된 경우만 생성자의 매개변수로 받는다.
    @RequiredArgsConstructor
    private static class LoginResponse {
        enum LoginStatus {
            SUCCESS, FAIL, DELETED
        }

        @NonNull
        private LoginStatus result;
        private UserDTO userDTO;

        // success의 경우 memberInfo의 값을 set해줘야 하기 때문에 new 하도록 해준다.
        private static final LoginResponse FAIL = new LoginResponse(LoginStatus.FAIL);

        private static LoginResponse success(UserDTO userDTO) {
            return new LoginResponse(LoginStatus.SUCCESS, userDTO);
        }

    }

    // -------------- request 객체 --------------

    @Setter
    @Getter
    private static class UserLoginRequest {
        @NonNull
        private String id;
        @NonNull
        private String password;
    }
}
