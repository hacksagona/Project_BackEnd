package com.project.hack.chat.config;


import org.springframework.boot.autoconfigure.websocket.servlet.WebSocketMessagingAutoConfiguration;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer{

    @Override
    public void configureMessageBroker(MessageBrokerRegistry registry) {
        //메시지 구독하는 요청은 /sub
        registry.enableSimpleBroker("/sub");
        //prefix 시작하는 /pub
        registry.setApplicationDestinationPrefixes("/pub");
    }

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        //endpoint, 개발 서버 접속 주소 : 토큰을 가지고 http://localhost:8080/ws-sotmp로 접속하면
        // Handshake 일어나면서 HTTP->WS 프로토콜 변경. "Welcom to SockJs!"가 출력됨

        registry.addEndpoint("/ws-stomp").setAllowedOriginPatterns("*")
                .withSockJS(); //sock.js를 통하여 낮은 버전의 브라우저에서도 websocket이 동작할 수 있게
    }
}
