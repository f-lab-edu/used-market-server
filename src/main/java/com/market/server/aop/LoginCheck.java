package com.market.server.aop;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/*
어노테이션은 주석이기 때문에 컴파일시 사라진다.
하지만 retention을 통해, 런타임때까지 주석을 남기겠다 라고 컴파일러에게 알려준다.
*/
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD) //어디에 어노테이션을 적용할 지 타겟을 정한다.
public @interface LoginCheck {
    public static enum UserType {
        user, admin
    }

    UserType type();
}
