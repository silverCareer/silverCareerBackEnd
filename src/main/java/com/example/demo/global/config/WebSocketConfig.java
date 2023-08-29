package com.example.demo.global.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.*;

@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        System.out.println("여기까지1");
        try {
            registry.addEndpoint("/ws").setAllowedOrigins("https://www.silvercareer.shop").withSockJS();
            System.out.println("여기까지2");
            //        https://43.201.132.241:3000 멘토 수정용
            // http://localhost:3000 로컬 테스트용
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public void configureMessageBroker(MessageBrokerRegistry registry) {
        registry.setApplicationDestinationPrefixes("/app");
        registry.enableSimpleBroker("/topic");
    }
}