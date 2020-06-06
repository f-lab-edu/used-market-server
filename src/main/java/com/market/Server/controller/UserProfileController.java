package com.market.Server.controller;

import com.market.Server.mapper.UserProfileMapper;
import com.market.Server.model.response.CommonResult;
import com.market.Server.model.response.ListResult;
import com.market.Server.model.response.SingleResult;
import com.market.Server.repository.UserProfile;
import com.market.Server.service.ResponseService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Api(tags = {"1. user"})
@RequiredArgsConstructor
@RestController
public class UserProfileController {

    private final UserProfileMapper  mapper;
    private final ResponseService responseService;

    @ApiOperation(value = "회원 한명 조회", notes = "특정 회원을 조회한다")
    @GetMapping("/user/{id}")
    public SingleResult<UserProfile> getUserProfile(@ApiParam(value = "회원아이디", required = true) @PathVariable ("id") String id) throws Exception {
        return responseService.getSingleResult(mapper.getUserProfile(id));
    }

    @ApiOperation(value = "회원 모두 조회", notes = "모든 회원을 조회한다")
    @GetMapping("/user/all")
    public ListResult<UserProfile> getUserProfileList() {
        return  responseService.getListResult(mapper.getUserProfileList());
    }

    @ApiOperation(value = "회원 입력", notes = "회원을 입력한다.")
    @PutMapping("/user/{id}")
    public SingleResult<Integer> putUserProfile(@ApiParam(value = "회원아이디", required = true) @PathVariable("id") String id,
                                                @ApiParam(value = "회원이름", required = true) @RequestParam("name") String name,
                                                @ApiParam(value = "회원폰번호", required = true) @RequestParam("phone") String phone,
                                                @ApiParam(value = "회원주소", required = true) @RequestParam("address") String address) {
       return responseService.getSingleResult(mapper.updateUserProfile(id,name,phone,address));
    }

    @PostMapping("/user/{id}")
    public SingleResult<Integer> postUserProfile(@ApiParam(value = "회원아이디", required = true) @PathVariable("id") String id,
                                                 @ApiParam(value = "회원이름", required = true) @RequestParam("name") String name,
                                                 @ApiParam(value = "회원폰번호", required = true) @RequestParam("phone") String phone,
                                                 @ApiParam(value = "회원주소", required = true) @RequestParam("address") String address)  {
        return responseService.getSingleResult(mapper.insertUserProfile(id,name,phone,address));
    }

    @DeleteMapping("/user/{id}")
    public CommonResult deleteUserProfile(@ApiParam(value = "회원아이디", required = true) @PathVariable("id") String id) {
        mapper.deleteUserProfile(id);
        // 성공 결과 정보만 필요한경우 getSuccessResult()를 이용하여 결과를 출력한다.
        return responseService.getSuccessResult();
    }

}
