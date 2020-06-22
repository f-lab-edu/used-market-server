package com.market.Server.service;

import com.market.Server.advice.exception.DuplicateIdException;
import com.market.Server.mapper.UserProfileMapper;
import com.market.Server.dto.UserDTO;
import com.market.Server.utils.SHA256Util;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Log4j2
// https://logging.apache.org/log4j/2.x/
public class UserServiceImpl implements UserService {

    @Autowired
    private final UserProfileMapper userProfileMapper;

    public UserServiceImpl(UserProfileMapper userProfileMapper){
        this.userProfileMapper=userProfileMapper;
    }


    public UserDTO getUserInfo(String userId) {
        return userProfileMapper.getUserProfile(userId);
    }

    /**
     * 회원 가입 메서드.
     *
     * @param UserDTO 저장할 회원의 정보
     * @return
     *
     * - 고객 회원가입 메서드 비밀번호를 암호화하여 세팅한다. MyBatis에서 insert return값은 성공시 1이 리턴된다. return값은 검사하여 null값이면
     * true, null이 아닐시 insert에 실패한 것이니 false를 반환한다
     */
    @Override
    public void register(UserDTO userDTO) {
        // id 중복체크
        boolean duplIdResult = isDuplicatedId(userDTO.getId());
        if (duplIdResult) {
            throw new DuplicateIdException("중복된 아이디입니다.");
        }

        userDTO.setPw(SHA256Util.encryptSHA256(userDTO.getPw()));
        int insertCount = userProfileMapper.register(userDTO);

        if (insertCount != 1) {
            log.error("insertMember ERROR! {}", userDTO);
            throw new RuntimeException(
                    "insertUser ERROR! 회원가입 메서드를 확인해주세요\n" + "Params : " + userDTO);
        }
    }

    /**
     * 고객 로그인 메서드.
     *
     * @param id 고객 아이디
     * @param password 고객 비밀번호
     * @return
     */
    public UserDTO login(String id, String password) {
        String cryptoPassword = SHA256Util.encryptSHA256(password);
        UserDTO memberInfo = userProfileMapper.findByIdAndPassword(id, cryptoPassword);
        return memberInfo;
    }

    /**
     * 회원가입시 아이디 중복 체크를 진행한다.
     *
     * @param id 중복체크를 진행할 아이디
     * @return true : 중복된 아이디 false : 중복되지 않은 아이디(생성 가능한 아이디)
     */
    public boolean isDuplicatedId(String id) {
        return userProfileMapper.idCheck(id) == 1;
    }



}
