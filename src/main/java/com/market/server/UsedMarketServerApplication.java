package com.market.server;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.session.data.redis.config.annotation.web.http.EnableRedisHttpSession;

@SpringBootApplication
@EnableRedisHttpSession // session을 redis에 저장한다, 여러 WAS를 사용하더라도 세션 정보를 하나의 redis에서 관리할 수 있다.
@EnableCaching // Spring에서 Caching을 사용하겠다고 선언한다.
public class UsedMarketServerApplication {

    public static void main(String[] args) {
        SpringApplication.run(UsedMarketServerApplication.class, args);
    }

}
