const AppConstant = {
  AuthNServer: {
    BASE_URL: "http://localhost:9090",
    SIGN_ON_ENDPOINT_URL: "http://localhost:9090/authn/so",
    REFRESH_ENDPOINT_URL: "http://localhost:9090/authn/refresh",
    LOG_OUT_ENDPOINT_URL: "http://localhost:9090/authn/lo"
  },
  ResourceServer: {
    BASE_URL: "http://localhost:9095",
    USER_DETAILS_ENDPOINT_URL: "http://localhost:9095/user/details",
    ADMIN_DETAILS_ENDPOINT_URL: "http://localhost:9095/admin/details"
  },
  UserRole: {
    USER: "USER",
    ADMIN: "ADMIN"
  },
  ErrorCodes: {
    INVALID_REFRESH_TOKEN: -1,
    INVALID_ACCESS_TOKEN: -2,
    EXPIRED_ACCESS_TOKEN: -3,
    USER_NOT_FOUND: -5,
    INVALID_PASSWORD: -6
  }
};
