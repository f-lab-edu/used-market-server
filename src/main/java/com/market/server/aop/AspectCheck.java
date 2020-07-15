package com.market.server.aop;

import com.market.server.utils.SessionUtil;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
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
//@Order(Ordered.HIGHEST_PRECEDENCE)
public class AspectCheck {

    /**
     * 로그인 체크 AOP 적용
     * Pointcut JoinPoints
     * <p>
     * execution(public * *(..)) public 메소드 실행
     * <p>
     * execution(* set*(..)) 이름이 set으로 시작하는 모든 메소드명 실행
     * <p>
     * execution(* get*(..)) 이름이 get으로 시작하는 모든 메소드명 실행
     * <p>
     * execution(* com.xyz.service.AccountService.*(..)) AccountService 인터페이스의 모든 메소드 실행
     * <p>
     * execution(* com.xyz.service.*.*(..)) service 패키지의 모든 메소드 실행
     * <p>
     * execution(* com.xyz.service..*.*(..)) service 패키지와 하위 패키지의 모든 메소드 실행
     * <p>
     * within(com.xyz.service.*) service 패키지 내의 모든 결합점 (클래스 포함)
     * <p>
     * within(com.xyz.service..*) service 패키지 및 하위 패키지의 모든 결합점 (클래스 포함)
     * <p>
     * bean(*Repository) 이름이 “Repository”로 끝나는 모든 빈
     * <p>
     * bean(*) 모든 빈 bean(account*) 이름이 'account'로 시작되는 모든 빈
     * <p>
     * bean(*dataSource) || bean(*DataSource) 이름이 “dataSource” 나 “DataSource” 으로 끝나는 모든 빈
     */
    //@Before("execution(* com.market.server.controller.*.*(..))")
    //1. 파라미터에서 어떤 파라미터에 우리가 만든 어노테이션이 붙어있는지 알기
    //2. 파라미터의 값을 우리가 조회한 유저의 아이디로 변경
    @Around("@annotation(com.market.server.aop.LoginCheck)")
    public Object loginCheck(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
        HttpSession session = ((ServletRequestAttributes) (RequestContextHolder.currentRequestAttributes())).getRequest().getSession();
        String Id = SessionUtil.getLoginMemberId(session);
        if (Id == null) {
            throw new HttpStatusCodeException(HttpStatus.UNAUTHORIZED, "LOGIN_FAIL") {
            };
        }
        int index = 0;
        Object[] modifiedArgs = proceedingJoinPoint.getArgs();

        for (Object arg : proceedingJoinPoint.getArgs()) {
            if (arg instanceof String) {    // Check on what basis argument have to be modified.
                modifiedArgs[index]=Id;
            }
            index++;
        }
        return proceedingJoinPoint.proceed(modifiedArgs);  //Continue with the method with modified arguments.
    }
}