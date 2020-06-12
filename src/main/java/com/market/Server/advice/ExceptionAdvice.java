package com.market.Server.advice;

import com.market.Server.advice.exception.CUserNotFoundException;
import com.market.Server.model.response.CommonResult;
import com.market.Server.service.ResponseService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.servlet.http.HttpServletRequest;

// 이 어노테이션은 초기화 되지않은 final 필드나,
// @NonNull 이 붙은 필드에 대해 생성자를 생성해 줍니다.
// 주로 의존성 주입(Dependency Injection) 편의성을 위해서 사용되곤 합니다.
@RequiredArgsConstructor

// ControllerAdvice의 annotation은 @ControllerAdvice @RestControllerAdvice 두가지가 있습니다. 예외 발생 시 json형태로 결과를 반환하려면
// @RestControllerAdvice를 클래스에 선언하면 됩니다. annotation에 추가로 패키지를 적용하면 위에서 설명한 것처럼 특정 패키지 하위의 Controller에만 로직이 적용되게도 할 수 있습니다.
// ex) @RestControllerAdvice(basePackages = “com.rest.api”)
// 아무것도 적용하지 않으면 프로젝트의 모든 Controller에 로직이 적용됩니다.
@RestControllerAdvice

public class ExceptionAdvice {
    // final 의 장점은 누군가가 Controller 내부에서 service 객체를 바꿔치기 할 수 없다는 점이다.
    // 기능이 바뀌지 않을 것임을 의미합니다. 복잡한 기능을 갖춘 소프트웨어를 디자인할 때 이러한 설계가 중요합니다.
    private final ResponseService responseService;
    private final MessageSource messageSource;

    // Exception이 발생하면 해당 Handler로 처리하겠다고 명시하는 annotation입니다. 괄호안에는 어떤 Exception이 발생할때 handler를 적용할 것인지
    // Exception Class를 인자로 넣습니다. 예제에서는 Exception.class를 지정하였는데 Exception.class는 최상위 예외처리 객체이므로 다른
    // ExceptionHandler에서 걸러지지 않은 예외가 있으면 최종으로 이 handler를 거쳐 처리됩니다. 그래서 메서드 명도 defaultException이라 명명하였습니다.
    @ExceptionHandler(Exception.class)

    // 해당 Exception이 발생하면 Response에 출력되는 HttpStatus Code가 500으로 내려가도록 설정합니다.
    // 참고로 성공 시엔 HttpStatus code가 200으로 내려갑니다. 실습에서 HttpStatus Code의 역할은 성공이냐(200)
    // 아니냐 정도의 의미만 있고 실제 사용하는 성공 실패 여부는 json으로 출력되는 정보를 이용합니다.
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    protected CommonResult defaultException(HttpServletRequest request, Exception e) {
        return responseService.getFailResult(Integer.valueOf(getMessage("unKnown.code")), getMessage("unKnown.msg"));
    }

    @ExceptionHandler(CUserNotFoundException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    protected CommonResult userNotFoundException(HttpServletRequest request, CUserNotFoundException e) {
        // 예외 처리의 메시지를 MessageSource에서 가져오도록 수정
        return responseService.getFailResult(Integer.valueOf(getMessage("userNotFound.code")), getMessage("userNotFound.msg"));
    }

    // code정보에 해당하는 메시지를 조회합니다.
    private String getMessage(String code) {
        return getMessage(code, null);
    }

    // code정보, 추가 argument로 현재 locale에 맞는 메시지를 조회합니다.
    private String getMessage(String code, Object[] args) {
        return messageSource.getMessage(code, args, LocaleContextHolder.getLocale());
    }
}
