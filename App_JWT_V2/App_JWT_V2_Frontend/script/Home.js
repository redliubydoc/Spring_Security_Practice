const logOutBtn = document.querySelector("#btn-logout");

function doPostLogOutActivity() {
  localStorage.clear();
  console.log("logout successful, navigating to login page");
  Util.navigateTo(window.location.origin);
}

function populatePage(userDetails) {
  document.querySelector("#welcome-msg").innerText = `Welcome ${localStorage.getItem("userId")}!`;
  document.querySelector("#userId").innerText = userDetails.userId;
  document.querySelector("#userRole").innerText = localStorage.getItem("userRole");
  document.querySelector("#firstName").innerText = userDetails.firstName;
  document.querySelector("#lastName").innerText = userDetails.lastName;
  document.querySelector("#emailId").innerText = userDetails.emailId;
}

async function doRefresh() { 

  let refreshed = false;

  try {
    const refreshToken = localStorage.getItem("refreshToken");

    if (refreshToken) {
      const response = await AuthNService.doRefresh(refreshToken);
      if (response.status === 200) {
        const jsonObj = await response.json();

        if (jsonObj.isError === false) {

          localStorage.setItem("accessToken", jsonObj.data.accessToken);
          localStorage.setItem("refreshToken", jsonObj.data.refreshToken);

          if (localStorage.getItem("accessToken") && localStorage.getItem("refreshToken")) {
            refreshed = true;
          }
        }
      }
      else if (response.status === 401) {
        console.log("invalid refresh token, login again");
        doPostLogOutActivity();
      }
      else {
        console.log(`http status code ${response.status}`);
      }
    }
  }
  catch (e) {
    console.log(e);
  }

  return refreshed;
}

window.addEventListener("load", function() {

  const accessToken = localStorage.getItem("accessToken");
  const refreshToken = localStorage.getItem("refreshToken");
  const userRole = localStorage.getItem("userRole");

  if (accessToken && refreshToken && userRole) {
    if (userRole === AppConstant.UserRole.USER) {
      Util.showLoadingAnimation();

      UserDetailsService.getUserDetails(accessToken)
        .then(function(response) {
          if (response.status === 200) {
            response.json()
              .then(function(jsonObj) {
                if (jsonObj.isError === false) {
                  populatePage(jsonObj.data);
                  Util.hideLoadingAnimation();
                }
              });
          }
          else if (response.status === 401) {
            response.json()
              .then(function(jsonObj) {
                if (jsonObj.isError === true && jsonObj.error.code === AppConstant.ErrorCodes.EXPIRED_ACCESS_TOKEN) {
                  
                  console.log("accessToken has expired, refreshing accessToken");
  
                  doRefresh()
                    .then(function(refreshed) {
                      if (refreshed) {
                        console.log("refreshing accessToken successful, reloading page");
                        location.reload();
                      }
                      else {
                        console.log("refreshing accessToken failed");
                      }
                    })
                    .catch(function(e) {
                      console.log(e);
                    })
                }
              });
          }
          else {
            console.log(`http status code ${response.status}`);
          }
        })
        .catch(function(e) {
          console.log(e);
        });
    }
    else if (userRole === AppConstant.UserRole.ADMIN) {
      Util.showLoadingAnimation();

      UserDetailsService.getAdminDetails(accessToken)
        .then(function(response) {
          if (response.status === 200) {
            response.json()
              .then(function(jsonObj) {
                if (jsonObj.isError === false) {
                  populatePage(jsonObj.data);
                  Util.hideLoadingAnimation();
                }
              });
          }
          else if (response.status === 401) {
            response.json()
              .then(function(jsonObj) {
                if (jsonObj && jsonObj.error && jsonObj.error.code === AppConstant.ErrorCodes.EXPIRED_ACCESS_TOKEN) {
                  
                  console.log("accessToken has expired, refreshing accessToken");
  
                  doRefresh()
                    .then(function(refreshed) {
                      if (refreshed) {
                        console.log("refreshing accessToken successful, reloading page");
                        location.reload();
                      }
                      else {
                        console.log("refreshing accessToken failed");
                      }
                    })
                    .catch(function(e) {
                      console.log(e);
                    })
                }
              });
          }
          else {
            console.log(`http status code ${response.status}`);
          }
        })
        .catch(function(e) {
          console.log(e);
        });
    }
  }
  else {
    Util.navigateTo(window.location.origin);
  }
});

logOutBtn.addEventListener("click", function() {

  const accessToken = localStorage.getItem("accessToken");
  const refreshToken = localStorage.getItem("refreshToken");

  Util.showLoadingAnimation();

  setTimeout(function() {
    AuthNService.doLogOut(accessToken, refreshToken)
      .then(function (response) {

        /**
         * Http status code 200 implies successful log out
         */

        if (response.status === 200) {
          doPostLogOutActivity();
          Util.hideLoadingAnimation();
        }
        else if (response.status === 401) {
          response.json()
            .then(function(jsonObj) {

              if (jsonObj.isError && (jsonObj.error.code === AppConstant.ErrorCodes.INVALID_REFRESH_TOKEN
                || jsonObj.error.code === AppConstant.ErrorCodes.INVALID_ACCESS_TOKEN)) {

                doPostLogOutActivity();
                Util.hideLoadingAnimation();
              }
              else if (jsonObj.isError && jsonObj.error.code === AppConstant.ErrorCodes.EXPIRED_ACCESS_TOKEN) {
                console.log("accessToken has expired, refreshing accessToken");
                doRefresh()
                  .then(function(refreshed) {
                    if (refreshed) {
                      console.log("refreshing accessToken successful, reloading page");
                      logOutBtn.click();
                    }
                    else {
                      console.log("refreshing accessToken failed");
                    }
                  })
                  .catch(function(e) {
                    console.log(e);
                  })
              }
            });
        }
        else {
          throw new Error();
        }
        
      })
      .catch(function(e) {
        console.log(e); 
        Util.hideLoadingAnimation();
      });
  }, 1000);
});