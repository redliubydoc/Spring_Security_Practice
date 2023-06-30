const UserDetailsService = {

  getUserDetails: function (accessToken) {
    return fetch(AppConstant.ResourceServer.USER_DETAILS_ENDPOINT_URL, {
      headers: {
        "Content-Type": "application/json",
        "Authorization": `Bearer ${accessToken}`
      }
    });
  },

  getAdminDetails: function(accessToken) {
    return fetch(AppConstant.ResourceServer.ADMIN_DETAILS_ENDPOINT_URL, {
      headers: {
        "Content-Type": "application/json",
        "Authorization": `Bearer ${accessToken}`
      }
    });
  }
};