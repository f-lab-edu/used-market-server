package com.market.server.service;

import com.market.server.dto.UserDTO;

public interface UserService {

    // 회원 가입 처리
    void register(UserDTO userProfile);

    UserDTO login(String id, String password);

    boolean isDuplicatedId(String id);

    UserDTO getUserInfo(String userId);
}
