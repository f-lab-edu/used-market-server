package com.market.server.aop;

import com.market.server.utils.SessionUtil;
import lombok.extern.log4j.Log4j2;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.aspectj.lang.annotation.Aspect;

import javax.servlet.http.HttpSession;


/*
자동으로 탐지하기 위해서는 별도의 @Component 어노테이션을 추가해야 한다.
(아니면 대신 스프링의 컴포넌트 스캐너의 규칙마다 제한을 하는 커스텀 스테레오타입 어노테이션을 추가해야 한다.)
*/
@Component
/*
해당 빈이 Aspect로 작동한다.
@Aspect가 명시된 빈에는 어드바이스(Advice)라 불리는 메써드를 작성할 수 있다.
대상 스프링 빈의 메써드의 호출에 끼어드는 시점과 방법에 따라
@Before, @After, @AfterReturning, @AfterThrowing, @Around
*/
@Aspect
/*
클래스 레벨에 @Order를 명시하여 @Aspect 빈 간의 작동 순서를 정할 수 있다.
int 타입의 정수로 순서를 정할 수 있는데 값이 낮을수록 우선순위가 높다.
기본값은 가장 낮은 우선순위를 가지는 Ordered.LOWEST_PRECEDENCE이다.
*/
@Order(Ordered.LOWEST_PRECEDENCE)
/*
Login Check할때 aop의 Aspect 애노테이션을 이용하여
로그인 체크 중복되는 코드를 제거하기 위해 어드바이스(Advice)를 정의하는 class 입니다.
*/
@Log4j2
public class LoginCheckAspect {
    private int idIndex = 0;

    @Around("@annotation(com.market.server.aop.LoginCheck) && @ annotation(loginCheck)")
    public Object adminLoginCheck(ProceedingJoinPoint proceedingJoinPoint, LoginCheck loginCheck) throws Throwable {
        HttpSession session = ((ServletRequestAttributes) (RequestContextHolder.currentRequestAttributes())).getRequest().getSession();
        String id = null;

        String userType = loginCheck.type().toString();
        switch (userType) {
            case "ADMIN": {
                id = SessionUtil.getLoginAdminId(session);
                break;
            }
            case "USER": {
                id = SessionUtil.getLoginMemberId(session);
                break;
            }
        }

        Object[] modifiedArgs = proceedingJoinPoint.getArgs();

        if (id != null)
            modifiedArgs[idIndex] = id;
        else
        {
            log.debug(proceedingJoinPoint.toString() + "accountName :" + id);
            throw new HttpStatusCodeException(HttpStatus.UNAUTHORIZED, "로그인한 id값을 확인해주세요.") {};
        }

        return proceedingJoinPoint.proceed(modifiedArgs);
    }

}