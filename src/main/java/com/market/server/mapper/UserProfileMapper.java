package com.market.server.mapper;

import com.market.server.dto.UserDTO;
import org.apache.ibatis.annotations.*;

@Mapper
public interface UserProfileMapper {

    @Select("SELECT id, password, name, phone, address, status FROM account WHERE id=#{id}")
    public UserDTO getUserProfile(@Param("id") String id);

    @Insert("INSERT INTO account VALUES(#{id}, #{password}, #{name}, #{phone}, #{address})")
    int insertUserProfile(@Param("id") String id, @Param("password") String password, @Param("name") String name, @Param("phone") String phone, @Param("address") String address);

    @Update("UPDATE account SET password=#{password}, name=#{name}, phone=#{phone}, address=#{address} WHERE id=#{id}")
    int updateUserProfile(@Param("id") String id, @Param("password") String password, @Param("name") String name, @Param("phone") String phone, @Param("address") String address);

    @Delete("DELETE FROM account WHERE id=#{id}")
    int deleteUserProfile(@Param("id") String id);

    public int register(UserDTO userDTO);

    public UserDTO findByIdAndPassword(@Param("id") String id, @Param("password") String password);

    int idCheck(String id);

    public int updatePassword(UserDTO userDTO);

    public int updateAddress(UserDTO userDTO);
}
