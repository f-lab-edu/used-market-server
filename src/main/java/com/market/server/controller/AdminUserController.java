package com.market.server.controller;

import com.market.server.advice.exception.CUserNotFoundException;
import com.market.server.aop.LoginCheck;
import com.market.server.mapper.UserProfileMapper;
import com.market.server.model.response.CommonResult;
import com.market.server.model.response.SingleResult;
import com.market.server.dto.UserDTO;
import com.market.server.service.ResponseService;
import com.market.server.utils.SessionUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.Optional;


@Api(tags = {"1. admin"})
@RequiredArgsConstructor
@RestController
public class AdminUserController {

    private final UserProfileMapper mapper;
    private final ResponseService responseService;

    @ApiOperation(value = "회원 한명 언어별 조회", notes = "userId로 회원을 조회한다")
    @GetMapping(value = "/users/{id}")
    @LoginCheck(type = LoginCheck.UserType.ADMIN)
    public SingleResult<UserDTO> findUserById(String accountId, @ApiParam(value = "회원ID", required = true) @PathVariable("id") String id, @ApiParam(value = "언어", defaultValue = "ko") @RequestParam String lang) {
        return responseService.getSingleResult(Optional.ofNullable(mapper.getUserProfile(id)).orElseThrow(CUserNotFoundException::new));
    }

    @ApiOperation(value = "회원 수정", notes = "회원을 수정한다..")
    @PutMapping("/users/{id}")
    @LoginCheck(type = LoginCheck.UserType.ADMIN)
    public SingleResult<Integer> putUserProfile(String accountId, @ApiParam(value = "회원아이디", required = true) @PathVariable("id") String id,
                                                @ApiParam(value = "회원비번", required = true) @RequestParam("pw") String pw,
                                                @ApiParam(value = "회원이름", required = true) @RequestParam("name") String name,
                                                @ApiParam(value = "회원폰번호", required = true) @RequestParam("phone") String phone,
                                                @ApiParam(value = "회원주소", required = true) @RequestParam("address") String address) {
        return responseService.getSingleResult(mapper.updateUserProfile(id, pw, name, phone, address));
    }

    @ApiOperation(value = "회원 입력", notes = "회원을 입력한다..")
    @PostMapping("/users/{id}")
    @LoginCheck(type = LoginCheck.UserType.ADMIN)
    public SingleResult<Integer> postUserProfile(String accountId, @ApiParam(value = "회원아이디", required = true) @PathVariable("id") String id,
                                                 @ApiParam(value = "회원비번", required = true) @RequestParam("pw") String pw,
                                                 @ApiParam(value = "회원이름", required = true) @RequestParam("name") String name,
                                                 @ApiParam(value = "회원폰번호", required = true) @RequestParam("phone") String phone,
                                                 @ApiParam(value = "회원주소", required = true) @RequestParam("address") String address) {
        return responseService.getSingleResult(mapper.insertUserProfile(id, pw, name, phone, address));
    }

    @DeleteMapping("/users/{id}")
    @LoginCheck(type = LoginCheck.UserType.ADMIN)
    public CommonResult deleteUserProfile(String accountId, @ApiParam(value = "회원아이디", required = true) @PathVariable("id") String id) {
        mapper.deleteUserProfile(id);
        // 성공 결과 정보만 필요한경우 getSuccessResult()를 이용하여 결과를 출력한다.
        return responseService.getSuccessResult();
    }

    @DeleteMapping("SessionAll")
    @LoginCheck(type = LoginCheck.UserType.ADMIN)
    public void deleteSessionAll(String accountId, HttpSession session) {
        SessionUtil.clear(session);

    }
}
