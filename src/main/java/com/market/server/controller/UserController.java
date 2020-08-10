package com.market.server.controller;

import com.market.server.aop.LoginCheck;
import com.market.server.dto.UserDTO;
import com.market.server.service.ResponseService;
import com.market.server.service.Impl.UserServiceImpl;
import com.market.server.utils.SessionUtil;
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
    private static final ResponseEntity<LoginResponse> FAIL_RESPONSE = new ResponseEntity<LoginResponse>(HttpStatus.BAD_REQUEST);
    private LoginResponse loginResponse;

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
    public void signUp(@RequestBody UserDTO userDTO) {
        if (UserDTO.hasNullDataBeforeSignup(userDTO)) {
            throw new NullPointerException("회원가입시 필수 데이터를 모두 입력해야 합니다.");
        }
        userService.register(userDTO);
    }

    /**
     * 회원 로그인을 진행한다. Login 요청시 id, password가 NULL일 경우 NullPointerException을 throw한다.
     */
    @PostMapping("signIn")
    public HttpStatus login(@RequestBody UserLoginRequest loginRequest,
                                               HttpSession session) {
        ResponseEntity<LoginResponse> responseEntity = null;
        String id = loginRequest.getId();
        String password = loginRequest.getPassword();
        UserDTO userInfo = userService.login(id, password);

        if (userInfo == null) {
            return HttpStatus.NOT_FOUND;
        } else if (userInfo != null) {
            loginResponse = LoginResponse.success(userInfo);
            if (userInfo.getStatus() == (UserDTO.Status.ADMIN))
                SessionUtil.setLoginAdminId(session, id);
            else
                SessionUtil.setLoginMemberId(session, id);

            responseEntity = new ResponseEntity<LoginResponse>(loginResponse, HttpStatus.OK);
        } else {
            throw new RuntimeException("Login Error! 유저 정보가 없거나 지워진 유저 정보입니다.");
        }

        return HttpStatus.OK;
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

    /**
     * 회원 비밀번호 수정 메서드.
     */
    @PatchMapping("password")
    public ResponseEntity<LoginResponse> updateUserPassword(@RequestBody UserUpdatePasswordRequest userUpdatePasswordRequest,
                                                            HttpSession session) {
        ResponseEntity<LoginResponse> responseEntity = null;
        String Id = SessionUtil.getLoginMemberId(session);
        String beforePassword = userUpdatePasswordRequest.getBeforePassword();
        String afterPassword = userUpdatePasswordRequest.getAfterPassword();

        try {
            userService.updatePassword(Id, beforePassword, afterPassword);
            ResponseEntity.ok(new ResponseEntity<LoginResponse>(loginResponse, HttpStatus.OK));
        } catch (IllegalArgumentException e) {
            log.error("updatePassword 실패" , e);
            responseEntity = FAIL_RESPONSE;
        }
        return responseEntity;
    }

    /**
     * 회원 주소수정 메서드.
     */
    @PatchMapping("myInfo/address")
    public ResponseEntity<LoginResponse> updateAddress(@RequestBody UserUpdateAddressRequest userUpdateAddressRequestu,
                                                       HttpSession session) {
        ResponseEntity<LoginResponse> responseEntity = null;
        String Id = SessionUtil.getLoginMemberId(session);
        String newAddress = userUpdateAddressRequestu.getNewAddress();

        try {
            userService.updateAddress(Id, newAddress);
            responseEntity = new ResponseEntity<LoginResponse>(loginResponse, HttpStatus.OK);
        } catch (RuntimeException e) {
            log.info("updateAddress 실패");
            responseEntity = FAIL_RESPONSE;
        }
        return responseEntity;
    }

    /**
     * 회원 ID 삭제 메서드.
     */
    @DeleteMapping
    public ResponseEntity<LoginResponse> deleteId(@RequestBody UserDeleteId userDeleteId,
                                                       HttpSession session) {
        ResponseEntity<LoginResponse> responseEntity = null;
        String Id = SessionUtil.getLoginMemberId(session);

        try {
            userService.deleteId(Id, userDeleteId.getPassword());
            responseEntity = new ResponseEntity<LoginResponse>(loginResponse, HttpStatus.OK);
        } catch (RuntimeException e) {
            log.info("deleteID 실패");
            responseEntity = FAIL_RESPONSE;
        }
        return responseEntity;
    }

    /**
     * 회원 로그아웃 메서드.
     */
    @DeleteMapping("session/clear")
    @LoginCheck(type = LoginCheck.UserType.USER)
    public void deleteSession(String accountId, HttpSession session) {
        SessionUtil.clear(session);

    }

    // -------------- response 객체 --------------

    @Getter
    @AllArgsConstructor
    private static class UserInfoResponse {
        private UserDTO userDTO;
    }

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

    @Setter
    @Getter
    private static class UserUpdatePasswordRequest {
        @NonNull
        private String beforePassword;
        @NonNull
        private String afterPassword;
    }

    @Setter
    @Getter
    private static class UserUpdateAddressRequest {
        @NonNull
        private String newAddress;
    }

    @Setter
    @Getter
    private static class UserDeleteId {
        @NonNull
        private String id;
        @NonNull
        private String password;
    }
}
