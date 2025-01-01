package com.ev.ocpp16.websocket;

public interface Constants {
    // String REGIST_PATH = "/{userType}/{version}/{siteIdentifier}/{chargerIdentifier}";
    String REGIST_PATH = "/{userType}/{chargerIdentifier}";

    String PATH_INFO = "pathInfo";
    String SESSION_KEY = "sessionKey";
    String MDC_KEY = "mdcKey";

    String USER_TYPE_ADMIN = "wsadmin";
    String USER_TYPE_USER = "wsuser";

    int CALL_CLIENT_TO_SERVER = 2;
    int CALL_RESULT_SERVER_TO_CLIENT = 3;
    int CALL_ERROR_SERVER_TO_CLIENT = 4;
}
