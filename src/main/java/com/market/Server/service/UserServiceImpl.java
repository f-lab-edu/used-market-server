package com.market.Server.service;

import com.market.Server.mapper.UserProfileMapper;
import com.market.Server.dto.UserDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private final UserProfileMapper userProfileMapper;

    public UserServiceImpl(UserProfileMapper userProfileMapper){
        this.userProfileMapper=userProfileMapper;
    }

    // 회원 가입 처리
    @Override
    public void register(UserDTO userDTO) {
        userProfileMapper.register(userDTO);
    }

}
