package com.market.server.aop;

import com.market.server.utils.SessionUtil;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
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
public class LoginCheckAspect {
    private static Logger logger = LogManager.getLogger(LoginCheckAspect.class);

    @Around("@annotation(com.market.server.aop.LoginCheck)")
    public Object loginCheck(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
        HttpSession session = ((ServletRequestAttributes) (RequestContextHolder.currentRequestAttributes())).getRequest().getSession();
        String Id = SessionUtil.getLoginMemberId(session);
        if (Id == null) {
            logger.debug("api method name : {} accountName : {}", proceedingJoinPoint.toString(), Id);
            throw new HttpStatusCodeException(HttpStatus.UNAUTHORIZED, "로그인한 id값을 확인해주세요." + Id) {};
        }
        int index = 0;
        Object[] modifiedArgs = proceedingJoinPoint.getArgs();
        
        for (Object arg : proceedingJoinPoint.getArgs()) {
            if(arg == null) // Parameter 값에 값이 없어도 Id값 맵핑
                modifiedArgs[index] = Id; 
            if (arg instanceof String) {    // accountId String타입 체크 , 추가로 파라미터에 String타입이 올시 변경 필요
                modifiedArgs[index]=Id;
            }
            index++;
        }
        return proceedingJoinPoint.proceed(modifiedArgs);
    }
}