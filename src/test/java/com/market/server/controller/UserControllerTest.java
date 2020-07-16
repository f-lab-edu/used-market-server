package com.market.server.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.market.server.dto.UserDTO;
import com.market.server.service.Impl.UserServiceImpl;
import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.hamcrest.CoreMatchers.is;

/*
 * @SpringBootTest - 통합테스트 환경을 제공하는 spring-boot-test 어노테이션. 실제 구동되는 어플리케이션과 동일한 어플리케이션 컨텍스트를 제공함. 대신
 * 어플리케이션의 설정을 모두 로드하기 때문에 성능, 속도면에서 느리다. 제공되는 의존성 : JUnit, Spring Test, Spring Boot Test, AssertJ,
 * Hamcrest(matcher object), Mockito, JSONassert. JsonPath
 *
 * @RunWith - JUnit에 내장된 러너 대신 어노테이션에 제공된 러너를 사용한다. SpringBootTest를 사용시 이 어노테이션을 같이 사용해야 한다.
 *
 * @AutoConfigureMockMvc - Mock 테스트시 필요한 의존성을 제공. Service에서 호출하는 Bean을 주입해준다. 간단히 컨트롤러 클래스만 테스트 하고
 * 싶다면 Mockup Test를 사용할 수 있는데 service 객체에 @MockBean 어노테이션을 적용하는 것으로 이 어노테이션을 대체할 수 있다.
 */
@SpringBootTest
@RunWith(SpringRunner.class)
@AutoConfigureMockMvc
@Log4j2
class UserControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private MockHttpSession mockSession;
    @Autowired
    private ObjectMapper mapper;

    @Autowired
    private UserServiceImpl userService;

    private String token;

    @Test
    @Transactional // 테스트 종료 후 롤백한다.
    void signUpTest()  throws Exception {
        UserDTO memberInfo = new UserDTO();
        memberInfo.setId("testID99999");
        memberInfo.setPassword("testPassword001");
        memberInfo.setName("testName");
        memberInfo.setPhone("010-1234-1234");
        memberInfo.setAddress("abc");
        memberInfo.setStatus(UserDTO.Status.DEFAULT);

        mockMvc
                .perform(post("/users/signUp")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(memberInfo)))
                .andDo(print())
                .andExpect(status().isCreated());
    }

    @Test
    void signInTest() throws Exception {
        UserDTO memberInfo = new UserDTO();
        memberInfo.setId("topojs8");
        memberInfo.setPassword("111");

        MvcResult result = mockMvc
                .perform(post("/users/signIn")
                        .session(mockSession)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(memberInfo)))
                .andDo(print()).andExpect(status().isOk())
                .andReturn();
    }

    @Test
    @Transactional
    void updateUserPassword() throws Exception {
        MultiValueMap<String, String> info = new LinkedMultiValueMap<>();

        info.add("password", "111");
        info.add("id", "topojs8");

        mockMvc.perform(put("/users/updatePassword")
                .session(mockSession)
                .params(info))
                .andExpect(status().isOk())
                .andDo(print());
    }


}