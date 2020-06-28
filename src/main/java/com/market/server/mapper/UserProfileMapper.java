package com.market.server.mapper;

import com.market.server.dto.UserDTO;
import org.apache.ibatis.annotations.*;

@Mapper
public interface UserProfileMapper {

    @Select("SELECT id,pw,name,phone,address,status FROM UserProfile WHERE id=#{id}")
    public UserDTO getUserProfile(@Param("id") String id);

    @Insert("INSERT INTO UserProfile VALUES(#{id}, #{pw}, #{name}, #{phone}, #{address})")
    int insertUserProfile(@Param("id") String id, @Param("pw") String pw, @Param("name") String name, @Param("phone") String phone, @Param("address") String address);

    @Update("UPDATE UserProfile SET pw=#{pw}, name=#{name}, phone=#{phone}, address=#{address} WHERE id=#{id}")
    int updateUserProfile(@Param("id") String id, @Param("pw") String pw, @Param("name") String name, @Param("phone") String phone, @Param("address") String address);

    @Delete("DELETE FROM UserProfile WHERE id=#{id}")
    int deleteUserProfile(@Param("id") String id);

    public int register(UserDTO userDTO);

    public UserDTO findByIdAndPassword(@Param("id") String id, @Param("pw") String pw);

    int idCheck(String id);
}
