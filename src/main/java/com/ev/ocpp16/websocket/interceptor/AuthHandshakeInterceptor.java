package com.ev.ocpp16.websocket.interceptor;

import static com.ev.ocpp16.websocket.Constants.MDC_KEY;
import static com.ev.ocpp16.websocket.Constants.PATH_INFO;
import static com.ev.ocpp16.websocket.Constants.REGIST_PATH;
import static com.ev.ocpp16.websocket.Constants.SESSION_KEY;
import static com.ev.ocpp16.websocket.Constants.USER_TYPE_ADMIN;
import static com.ev.ocpp16.websocket.Constants.USER_TYPE_USER;

import java.util.Map;

import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.HandshakeInterceptor;
import org.springframework.web.util.UriTemplate;

import com.ev.ocpp16.application.ChargingManageService;
import com.ev.ocpp16.domain.chargingManagement.exception.ChargerNotFoundException;
import com.ev.ocpp16.websocket.dto.PathInfo;
import com.ev.ocpp16.websocket.dto.PathValidationResult;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
public class AuthHandshakeInterceptor implements HandshakeInterceptor {

    private final ChargingManageService chargingManageService;

    private static final String PATH_SEPARATOR = ":";
    private static final String MDC_SEPARATOR = "-";

    @Override
    public boolean beforeHandshake(ServerHttpRequest request, ServerHttpResponse response, WebSocketHandler wsHandler,
            Map<String, Object> attributes) throws Exception {
        PathValidationResult validationResult = validatePath(request);
        if (!validationResult.isValid()) {
            return false;
        }

        if (!isValidConnection(validationResult.getChargerIdentifier(), validationResult.getUserType(), request)) {
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
                .siteIdentifier(pathVariables.get("siteIdentifier"))
                .chargerIdentifier(pathVariables.get("chargerIdentifier"))
                .valid(true)
                .build();
    }

    private boolean isValidConnection(String chargerIdentifier, String userType, ServerHttpRequest request) {
        return isValidUserType(userType) && isChargerActiveTrue(chargerIdentifier) && isSecureRequest(request);
    }

    private boolean isValidUserType(String userType) {
        return USER_TYPE_ADMIN.equals(userType) || USER_TYPE_USER.equals(userType);
    }

    private boolean isChargerActiveTrue(String chargerIdentifier) {
        try {
            chargingManageService.validateChargerForCharging(chargerIdentifier);
            return true;
        } catch (ChargerNotFoundException e) {
            return false;
        }
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
                .siteIdentifier(result.getSiteIdentifier())
                .chargerIdentifier(result.getChargerIdentifier())
                .build();
    }

    private String buildSessionKey(PathValidationResult result) {
        return String.join(PATH_SEPARATOR,
                result.getUserType(),
                result.getVersion(),
                String.valueOf(result.getSiteIdentifier()),
                String.valueOf(result.getChargerIdentifier()));
    }

    private String buildMdcKey(PathValidationResult result) {
        return String.join(MDC_SEPARATOR,
                result.getUserType(),
                result.getVersion(),
                String.valueOf(result.getSiteIdentifier()),
                String.valueOf(result.getChargerIdentifier()));
    }

    @Override
    public void afterHandshake(ServerHttpRequest request, ServerHttpResponse response, WebSocketHandler wsHandler,
            @Nullable Exception exception) {
    }

}
