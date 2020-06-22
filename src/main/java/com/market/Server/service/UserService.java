package com.market.Server.service;

import com.market.Server.dto.UserDTO;

public interface UserService {

    // 회원 가입 처리
    void register(UserDTO userProfile);

}
