package com.market.server.service;

import com.market.server.dto.UserDTO;

public interface UserService {

    // 회원 가입 처리
    void register(UserDTO userProfile);

    UserDTO login(String id, String password);

    boolean isDuplicatedId(String id);

    UserDTO getUserInfo(String userId);

    void updatePassword(String id, String beforePassword, String afterPassword);

    void updateAddress(String id, String newAddress);

    void deleteId(String id, String passWord);
}
