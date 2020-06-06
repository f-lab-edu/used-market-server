package com.market.Server.controller;

import com.market.Server.mapper.UserProfileMapper;
import com.market.Server.repository.UserProfile;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Api(tags = {"1. user"})
@RestController
public class UserProfileController {

    private UserProfileMapper  mapper;

    public UserProfileController(UserProfileMapper mapper)
    {
       this.mapper=mapper;
    }

    @GetMapping("/user/{id}")
    public UserProfile getUserProfile(@PathVariable ("id") String id) {
        return mapper.getUserProfile(id);
    }

    @ApiOperation(value = "회원 조회", notes = "모든 회원을 조회한다")
    @GetMapping("/user/all")
    public List<UserProfile> getUserProfileList() {
        return mapper.getUserProfileList();
    }

    @ApiOperation(value = "회원 입력", notes = "회원을 입력한다.")
    @PutMapping("/user/{id}")
    public void putUserProfile(@ApiParam(value = "회원아이디", required = true) @PathVariable("id") String id,
                               @ApiParam(value = "회원이름", required = true) @RequestParam("name") String name,
                               @ApiParam(value = "회원폰번호", required = true) @RequestParam("phone") String phone,
                               @ApiParam(value = "회원주소", required = true) @RequestParam("address") String address) {
        mapper.insertUserProfile(id,name,phone,address);
    }

    @PostMapping("/user/{id}")
    public void postUserProfile(@PathVariable("id") String id, @RequestParam("name") String name, @RequestParam("phone") String phone, @RequestParam("address") String address) {
        mapper.updateUserProfile(id,name,phone,address);

    }

    @DeleteMapping("/user/{id}")
    public void deleteUserProfile(@PathVariable("id") String id) {
        mapper.deleteUserProfile(id);
    }

}
