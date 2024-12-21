package com.ev.ocpp16.websocket.config;

import static com.ev.ocpp16.websocket.Constants.REGIST_PATH;

import org.springframework.context.annotation.Configuration;
import org.springframework.lang.NonNull;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;

import com.ev.ocpp16.websocket.OcppWebSocketHandler;
import com.ev.ocpp16.websocket.interceptor.AuthHandshakeInterceptor;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@Configuration
@EnableWebSocket
public class WebSocketConfig implements WebSocketConfigurer {

    private final OcppWebSocketHandler ocppWebSocketHandler;
    private final AuthHandshakeInterceptor authHandshakeInterceptor;

    @Override
    public void registerWebSocketHandlers(@NonNull WebSocketHandlerRegistry registry) {
        registry.addHandler(ocppWebSocketHandler, REGIST_PATH)
                .setAllowedOrigins("*")
                .addInterceptors(authHandshakeInterceptor);
    }
}
