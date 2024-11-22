package com.ev.ocpp16.websocket.dto;

import static com.ev.ocpp16.websocket.utils.Constants.PATH_INFO;

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
    private final Long siteId;
    private final Long chgrId;

    public static PathInfo getPathInfoFromSession(WebSocketSession session) {
        return (PathInfo) session.getAttributes().get(PATH_INFO);
    }
}