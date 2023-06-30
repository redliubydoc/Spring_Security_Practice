package com.raj;

public class AppConstants {
    public static final String JWT_SIGNING_KEY = "_A3GhQMmfixjef5G9bFNKu7XhY7i1Tf5gyuWHrFIVTBk4t9APCX8Foq1SJWgCspLy3MuLgrI7js-0JS65M78dg";
    public static final int MAX_CONCURRENT_LOGIN_PER_USER = 5;

    /**
     * Token Related
     */
    public static final int ERR_CODE_INVALID_REFRESH_TOKEN = -1;
    public static final int ERR_CODE_INVALID_ACCESS_TOKEN = -2;
    public static final int ERR_CODE_EXPIRED_ACCESS_TOKEN = -3;
    public static final int ERR_CODE_USER_NOT_FOUND = -5;
    public static final int ERR_CODE_INVALID_PASSWORD = -6;
}
