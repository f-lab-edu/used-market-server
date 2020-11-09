package com.market.server.service.Impl;

import com.market.server.dto.UserDTO;
import com.market.server.mapper.UserProfileMapper;
import com.market.server.utils.SHA256Util;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Date;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.BDDMockito.given;

@RunWith(MockitoJUnitRunner.class)
class UserServiceImplTest {
    /*
     * '@Mock'이 붙은 목 객체를 해당 어노테이션이 선언된 객체에 주입할 수 있다.
     * Dao객체를 주입하기 위해서는 Dao에 '@Mock'을, Service에' @InjectMocks'를 붙여주어야한다.
     */
    @InjectMocks
    UserServiceImpl userService;

    @Mock
    UserProfileMapper userProfileMapper;

    // 새로운 멤버 객체를 생성하여 반환한다.
    public UserDTO generateUser() {
        MockitoAnnotations.initMocks(this); // mock all the field having @Mock annotation
        UserDTO userDTO = new UserDTO();
        userDTO.setId("textUserId");
        userDTO.setPassword(SHA256Util.encryptSHA256("testPassword"));
        userDTO.setName("testUserName");
        userDTO.setPhone("010-1111-2222");
        userDTO.setAddress("testAdress");
        userDTO.setStatus(UserDTO.Status.DEFAULT);
        userDTO.setCreatetime(new Date());
        userDTO.setUpdatetime(new Date());
        userDTO.setAddmin(false);
        return userDTO;
    }

    @Test
    public void getUserInfo() {
        UserDTO userDTO = generateUser();
        assertTrue("textUserId".equals(userDTO.getId()));
        assertTrue("testUserName" == userDTO.getName());
    }

    @Test
    void register() {
        UserDTO userDTO = generateUser();
        userProfileMapper.register(userDTO);

    }

    @Test
    void login() {
        UserDTO userDTO = generateUser();
        given(userProfileMapper.findByIdAndPassword("textUserId",
                SHA256Util.encryptSHA256("testPassword")))
                .willReturn(userDTO);
        assertThat(userService.login("textUserId", "testPassword")).isEqualTo(userDTO);
    }

    @Test
    void isDuplicatedId() {
        UserDTO userDTO = generateUser();
        given(userProfileMapper.idCheck("textUserId"))
                .willReturn(1);
        given(userProfileMapper.idCheck("textUserId2"))
                .willReturn(0);
        userService.login(userDTO.getId(), userDTO.getPassword());
    }

    @Test
    void updatePassword() {
        UserDTO userDTO = generateUser();
        given(userProfileMapper.updatePassword(userDTO))
                .willReturn(1);
        given(userProfileMapper.findByIdAndPassword(userDTO.getId(), SHA256Util.encryptSHA256("testPassword")))
                .willReturn(userDTO);

        userService.updatePassword(userDTO.getId(), "testPassword", "1234");
    }

    @Test
    void updateAddress() {
        UserDTO userDTO = generateUser();
        given(userProfileMapper.updateAddress(userDTO))
                .willReturn(1);
        given(userProfileMapper.getUserProfile(userDTO.getId()))
                .willReturn(userDTO);

        userService.updateAddress(userDTO.getId(), "testAdress22");
    }

    @Test
    void deleteId() {
        UserDTO userDTO = generateUser();
        given(userProfileMapper.findByIdAndPassword("textUserId",
                SHA256Util.encryptSHA256("testPassword")))
                .willReturn(userDTO);

        userService.deleteId(userDTO.getId(),"testPassword");
    }
}