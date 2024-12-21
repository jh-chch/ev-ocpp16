package com.ev.ocpp16.websocket.dto;

import static com.ev.ocpp16.websocket.Constants.PATH_INFO;

import org.springframework.web.socket.WebSocketSession;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
@EqualsAndHashCode
@Builder
public class PathInfo {
    private final String userType;
    private final String version;
    private final String siteIdentifier;
    private final String chargerIdentifier;

    public static PathInfo from(WebSocketSession session) {
        if (session == null) {
            throw new IllegalArgumentException("Session cannot be null");
        }

        Object pathInfo = session.getAttributes().get(PATH_INFO);
        if (pathInfo == null) {
            throw new IllegalStateException("PathInfo not found in session");
        }
        return (PathInfo) pathInfo;
    }
}