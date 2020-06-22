package com.market.Server.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class UserDTO {
    public enum Status {
        DEFAULT, DELETED
    }

    private String id;
    private String pw;
    private String name;
    private String phone;
    private String address;
    private Status status;


    public UserDTO(String id, String pw, String name, String phone, String address, Status status) {
        this.id = id;
        this.pw =pw;
        this.name = name;
        this.phone = phone;
        this.address = address;
        this.status = status;
    }

    /**
     * 회원가입 전 필수 데이터중 null값이 있는지 검사한다.
     * null값이 존재하여 회원가입 진행이 불가능 하다면 false를 반환한다.
     * 검사 후 이상이 없다면 true를 반환한다.
     * @param UserDTO 검사할 회원의 정보
     * @return
     */
    public static boolean hasNullDataBeforeSignup(UserDTO userDTO) {
        return userDTO.getId() == null || userDTO.getPw() == null
                || userDTO.getName() == null || userDTO.getPhone() == null
                || userDTO.getAddress() == null;
    }
}
