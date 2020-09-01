package com.market.server.config;

import com.market.server.handler.SocketHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;

@Configuration
/*
WebSocket이란 클라이언트와 서버의 연결은 유지한 채로, 연결이 끊기기 전까지
지속적으로 양방향 통신을 할 수 있는 프로토콜이다.
실시간 서비스나, 짧은 시간에 많은 양의 데이터를 보내야 할 때 적합한 기술이다.
하지만, 구버전의 익스폴로러 지원이 안되는 단점도 있다.
"미지원 브라우저" 문제를 해결 하기위해 나온 기술이 Socket.io와 SockJS다.
@EnableWebSocket: WebSocketConfigurer인터페이스 를 구현하여 가져온 구성한다.
*/
@EnableWebSocket
public class WebSocketConfig implements WebSocketConfigurer {

    @Autowired
    SocketHandler socketHandler;

    // WebSocketHandlerRegistry 에서 handshake와 통신을 담당할 endpoint를 지정한다.
    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        registry.addHandler(socketHandler, "/chating/{roomNumber}");
    }

    /*
    ThreadPoolTaskScheduler 는 ScheduledExecutorService에 작업을 위임 하고
    TaskExecutor 인터페이스를 구현하므로 내부 스레드 관리에 매우 적합하다.
    따라서 단일 인스턴스가 @Scheduled 주석 뿐 아니라 비동기식 잠재적 실행을 처리 할 수 있다.
    작업 스케쥴러를 구현하려면 해당 스케쥴을 관리할 org.springframework.scheduling.TaskScheduler 빈이 등록되어 있어야 한다.
    쓰레드 풀이 필요하다면 ThreadPoolTaskScheduler 빈을 등록하고 단일 쓰레드로 충분하다면 ConcurrentTaskScheduler 빈을 등록한다.
    */
    @Bean
    public TaskScheduler taskScheduler() {
        ThreadPoolTaskScheduler taskScheduler = new ThreadPoolTaskScheduler();
        taskScheduler.setPoolSize(10);
        taskScheduler.initialize();
        return taskScheduler;
    }
}
