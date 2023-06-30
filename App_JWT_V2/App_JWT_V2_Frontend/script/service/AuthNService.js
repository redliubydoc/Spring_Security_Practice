const AuthNService = {
  doSignOn: function (username, password) {

    const payload = {
      "username": username,
      "password": password
    };

    return fetch(AppConstant.AuthNServer.SIGN_ON_ENDPOINT_URL, {
      headers: {
        "Content-Type": "application/json"
      },
      method: "post",
      body: JSON.stringify(payload)
    });
  },

  doRefresh: function(refreshToken) {

    const payload = {
      "refreshToken": refreshToken
    };

    return fetch(AppConstant.AuthNServer.REFRESH_ENDPOINT_URL, {
      headers: {
        "Content-Type": "application/json"
      },
      method: "post",
      body: JSON.stringify(payload)
    });
  },

  doLogOut: function(accessToken, refreshToken) {
    const payload = {
      "accessToken": accessToken,
      "refreshToken": refreshToken
    };

    return fetch(AppConstant.AuthNServer.LOG_OUT_ENDPOINT_URL, {
      headers: {
        "Content-Type": "application/json"
      },
      method: "post",
      body: JSON.stringify(payload)
    });
  }
};
