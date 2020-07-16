package com.market.server.mapper;

import com.market.server.dto.UserDTO;
import org.apache.ibatis.annotations.*;

@Mapper
public interface UserProfileMapper {
    public UserDTO getUserProfile(@Param("id") String id);

    int insertUserProfile(@Param("id") String id, @Param("password") String password, @Param("name") String name, @Param("phone") String phone, @Param("address") String address);

    int updateUserProfile(@Param("id") String id, @Param("password") String password, @Param("name") String name, @Param("phone") String phone, @Param("address") String address);

    int deleteUserProfile(@Param("id") String id);

    public int register(UserDTO userDTO);

    public UserDTO findByIdAndPassword(@Param("id") String id, @Param("password") String password);

    int idCheck(String id);

    public int updatePassword(UserDTO userDTO);

    public int updateAddress(UserDTO userDTO);
}
