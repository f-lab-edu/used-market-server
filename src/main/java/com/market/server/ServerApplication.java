package com.market.server;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.EnableAspectJAutoProxy;


@SpringBootApplication
@EnableCaching // Spring에서 Caching을 사용하겠다고 선언한다.
/*
최상위 클래스에 적용해야 AOP를 찾을 수 있도록 만들어준다.
XML 기반의 ApplicationContext 설정에서의 <aop:aspectj-autoproxy />와 동일한 기능을 한다.
*/
@EnableAspectJAutoProxy
public class ServerApplication {

    public static void main(String[] args) {
        SpringApplication.run(ServerApplication.class, args);
    }

}
