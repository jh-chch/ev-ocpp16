package com.ev.ocpp16.websocket.utils;

public interface Constants {
    String REGIST_PATH = "/{userType}/{version}/{siteId}/{chgrId}";

    String PATH_INFO = "pathInfo";
    String SESSION_KEY = "sessionKey";
    String MDC_KEY = "mdcKey";

    String USER_TYPE_ADMIN = "wsadmin";
    String USER_TYPE_USER = "wsuser";
}
