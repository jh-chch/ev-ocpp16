package com.ev.ocpp16.websocket.interceptor;

import static com.ev.ocpp16.websocket.utils.Constants.MDC_KEY;
import static com.ev.ocpp16.websocket.utils.Constants.PATH_INFO;
import static com.ev.ocpp16.websocket.utils.Constants.SESSION_KEY;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

import java.net.URI;
import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.socket.WebSocketHandler;

import com.ev.ocpp16.domain.chargepoint.service.ChargerService;
import com.ev.ocpp16.websocket.dto.PathInfo;

@ExtendWith(MockitoExtension.class)
class AuthHandshakeInterceptorTest {

    @Mock
    private ServerHttpRequest request;

    @Mock
    private ServerHttpResponse response;

    @Mock
    private WebSocketHandler wsHandler;

    @Mock
    private ChargerService chargerService;

    @InjectMocks
    private AuthHandshakeInterceptor interceptor;

    private Map<String, Object> attributes;

    @BeforeEach
    void setUp() {
        attributes = new HashMap<>();
    }

    @Test
    void 유효한_경로() throws Exception {
        when(request.getURI()).thenReturn(new URI("/wsuser/1.6/123/456"));
        when(chargerService.isChgrActiveTrue(456L)).thenReturn(true);

        boolean result = interceptor.beforeHandshake(request, response, wsHandler, attributes);

        assertTrue(result);

        assertThat(attributes.get(SESSION_KEY)).isEqualTo("wsuser:1.6:123:456");
        assertThat(attributes.get(MDC_KEY)).isEqualTo("wsuser-1.6-123-456");
        assertThat(attributes.get(PATH_INFO)).isEqualTo(PathInfo.builder()
                .userType("wsuser")
                .version("1.6")
                .siteId(123L)
                .chgrId(456L)
                .build());
    }

    @Test
    void 유효하지_않은_경로() throws Exception {
        when(request.getURI()).thenReturn(new URI("/user/1.6/123/456"));

        boolean result = interceptor.beforeHandshake(request, response, wsHandler, attributes);

        assertFalse(result);
    }

}