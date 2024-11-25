package com.ev.ocpp16.websocket.interceptor;

import static com.ev.ocpp16.websocket.utils.Constants.MDC_KEY;
import static com.ev.ocpp16.websocket.utils.Constants.PATH_INFO;
import static com.ev.ocpp16.websocket.utils.Constants.REGIST_PATH;
import static com.ev.ocpp16.websocket.utils.Constants.SESSION_KEY;
import static com.ev.ocpp16.websocket.utils.Constants.USER_TYPE_ADMIN;
import static com.ev.ocpp16.websocket.utils.Constants.USER_TYPE_USER;

import java.util.Map;

import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.HandshakeInterceptor;
import org.springframework.web.util.UriTemplate;

import com.ev.ocpp16.domain.chargepoint.service.ChargerService;
import com.ev.ocpp16.websocket.dto.PathInfo;
import com.ev.ocpp16.websocket.dto.PathValidationResult;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
public class AuthHandshakeInterceptor implements HandshakeInterceptor {

    private final ChargerService chargerService;

    private static final String PATH_SEPARATOR = ":";
    private static final String MDC_SEPARATOR = "-";

    @Override
    public boolean beforeHandshake(ServerHttpRequest request, ServerHttpResponse response, WebSocketHandler wsHandler,
            Map<String, Object> attributes) throws Exception {
        PathValidationResult validationResult = validatePath(request);
        if (!validationResult.isValid()) {
            return false;
        }

        if (!isValidConnection(validationResult.getChgrId(), validationResult.getUserType(), request)) {
            return false;
        }

        setAttributes(attributes, validationResult);
        return true;
    }

    private PathValidationResult validatePath(ServerHttpRequest request) {
        UriTemplate uriTemplate = new UriTemplate(REGIST_PATH);
        Map<String, String> pathVariables = uriTemplate.match(request.getURI().getPath());

        if (pathVariables == null || pathVariables.isEmpty()) {
            return PathValidationResult.invalid();
        }

        return PathValidationResult.builder()
                .userType(pathVariables.get("userType"))
                .version(pathVariables.get("version"))
                .siteId(Long.parseLong(pathVariables.get("siteId")))
                .chgrId(Long.parseLong(pathVariables.get("chgrId")))
                .valid(true)
                .build();
    }

    private boolean isValidConnection(Long chgrId, String userType, ServerHttpRequest request) {
        return isValidUserType(userType) && isChargerActiveTrue(chgrId) && isSecureRequest(request);
    }

    private boolean isValidUserType(String userType) {
        return USER_TYPE_ADMIN.equals(userType) || USER_TYPE_USER.equals(userType);
    }

    private boolean isChargerActiveTrue(Long chgrId) {
        return chargerService.isChgrActiveTrue(chgrId);
    }

    private boolean isSecureRequest(ServerHttpRequest request) {
        return true;
    }

    private void setAttributes(Map<String, Object> attributes, PathValidationResult result) {
        attributes.put(PATH_INFO, buildPathInfoFromSession(result));
        attributes.put(SESSION_KEY, buildSessionKey(result));
        attributes.put(MDC_KEY, buildMdcKey(result));
    }

    public PathInfo buildPathInfoFromSession(PathValidationResult result) {
        return PathInfo.builder()
                .userType(result.getUserType())
                .version(result.getVersion())
                .siteId(result.getSiteId())
                .chgrId(result.getChgrId())
                .build();
    }

    private String buildSessionKey(PathValidationResult result) {
        return String.join(PATH_SEPARATOR,
                result.getUserType(),
                result.getVersion(),
                String.valueOf(result.getSiteId()),
                String.valueOf(result.getChgrId()));
    }

    private String buildMdcKey(PathValidationResult result) {
        return String.join(MDC_SEPARATOR,
                result.getUserType(),
                result.getVersion(),
                String.valueOf(result.getSiteId()),
                String.valueOf(result.getChgrId()));
    }

    @Override
    public void afterHandshake(ServerHttpRequest request, ServerHttpResponse response, WebSocketHandler wsHandler,
            @Nullable Exception exception) {
    }

}
