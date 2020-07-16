package com.market.server.service.Impl;

import com.market.server.advice.exception.DuplicateIdException;
import com.market.server.mapper.UserProfileMapper;
import com.market.server.dto.UserDTO;
import com.market.server.service.UserService;
import com.market.server.utils.SHA256Util;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Date;

@Service
/*
logback : log4j2 전에 개발된 로깅프로그램으로 log4j에서 향상된 성능과 필터링 옵션을 제공합니다. slf4j도 지원합니다. 그리고 자동 리로드도 가능합니다.
log4j2 : logback과 동일하게 자동 리로드 기능과 필터리 기능을 제공합니다. logback과 차이점은 Apache에 따르면 멀티 쓰레드 환경에서 비동기 로거(Async Logger) 의 경우
logback보다 몇 배나 되는 처리량을 갖고 있다고 합니다. 그리고 람다 표현식과 사용자 정의 로그 레벨도 지원합니다.
*/
@Log4j2
// https://logging.apache.org/log4j/2.x/
public class UserServiceImpl implements UserService {

    @Autowired
    private final UserProfileMapper userProfileMapper;

    public UserServiceImpl(UserProfileMapper userProfileMapper) {
        this.userProfileMapper = userProfileMapper;
    }

    @Override
    public UserDTO getUserInfo(String userId) {
        return userProfileMapper.getUserProfile(userId);
    }

    /**
     * 회원 가입 메서드.
     *
     * @param userDTO 저장할 회원의 정보
     * @return - 고객 회원가입 메서드 비밀번호를 암호화하여 세팅한다. MyBatis에서 insert return값은 성공시 1이 리턴된다. return값은 검사하여 null값이면
     * true, null이 아닐시 insert에 실패한 것이니 false를 반환한다
     */
    @Override
    public void register(UserDTO userDTO) {
        // id 중복체크
        boolean duplIdResult = isDuplicatedId(userDTO.getId());
        if (duplIdResult) {
            throw new DuplicateIdException("중복된 아이디입니다.");
        }
        userDTO.setCreatetime(new Date());
        userDTO.setPassword(SHA256Util.encryptSHA256(userDTO.getPassword()));
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
     * @param id       고객 아이디
     * @param password 고객 비밀번호
     * @return
     */
    @Override
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
    @Override
    public boolean isDuplicatedId(String id) {
        return userProfileMapper.idCheck(id) == 1;
    }


    /**
     * 유저 비밀번호 변경 메서드.
     */
    @Override
    public void updatePassword(String id, String beforePassword, String afterPassword) {
        String cryptoPassword = SHA256Util.encryptSHA256(beforePassword);
        UserDTO memberInfo = userProfileMapper.findByIdAndPassword(id, cryptoPassword);

        if (memberInfo != null) {
            memberInfo.setPassword(SHA256Util.encryptSHA256(afterPassword));
            int insertCount = userProfileMapper.updatePassword(memberInfo);
        } else {
            log.error("updatePasswrod ERROR! {}", memberInfo);
            throw new IllegalArgumentException("updatePasswrod ERROR! 비밀번호 변경 메서드를 확인해주세요\n" + "Params : " + memberInfo);
        }
    }

    /**
     * 유저 주소 변경 메서드.
     */
    @Override
    public void updateAddress(String id, String newAddress) {
        UserDTO memberInfo = userProfileMapper.getUserProfile(id);
        memberInfo.setAddress(newAddress);

        if (memberInfo != null) {
            userProfileMapper.updateAddress(memberInfo);
        } else {
            log.error("updateAddress ERROR! {}", memberInfo);
            throw new RuntimeException("updateAddress ERROR! 주소 변경 메서드를 확인해주세요\n" + "Params : " + memberInfo);
        }
    }

    @Override
    public void deleteId(String id, String passWord) {
        String cryptoPassword = SHA256Util.encryptSHA256(passWord);
        UserDTO memberInfo = userProfileMapper.findByIdAndPassword(id, cryptoPassword);

        if (memberInfo != null) {
            userProfileMapper.deleteUserProfile(memberInfo.getId());
        } else {
            log.error("deleteId ERROR! {}", memberInfo);
            throw new RuntimeException("deleteId ERROR! id 삭제 메서드를 확인해주세요\n" + "Params : " + memberInfo);
        }
    }

}
