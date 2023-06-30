/**
 * For login Page
 */
const usernameInp = document.querySelector("#ip-username");
const passwordInp = document.querySelector("#ip-password");
const logInBtn = document.querySelector("#btn-login");

logInBtn.addEventListener("click", function (e) {

  e.preventDefault();

  const username = usernameInp.value;
  const password = passwordInp.value;

  if (username && password) {

    Util.showLoadingAnimation();

    setTimeout(function() {
      AuthNService.doSignOn(username, password)
        .then(function (response) {

          /**
           * Http status code 200 implies successful sign on
           */

          if (response.status === 200) {

            response.json()
              .then(function(jsonObj) {

                if (jsonObj.isError === false) {

                  localStorage.setItem("accessToken", jsonObj.data.accessToken);
                  localStorage.setItem("refreshToken", jsonObj.data.refreshToken);
                  localStorage.setItem("userId", jsonObj.data.userId);
                  localStorage.setItem("userRole", jsonObj.data.userRole);

                  if (localStorage.getItem("accessToken") 
                      && localStorage.getItem("refreshToken")
                      && localStorage.getItem("userRole")) {
                    
                    const pageNavigator = document.querySelector("#page-navigator");
                    const userRole = localStorage.getItem("userRole");
  
                    if (userRole === AppConstant.UserRole.USER) {
                      Util.navigateTo(`${window.location.origin}\\user.html`);
                    }
                    else if (userRole === AppConstant.UserRole.ADMIN) {
                      Util.navigateTo(`${window.location.origin}\\admin.html`);
                    }
                  }
                }
              });
          } 
          else if (response.status === 401) {
            console.log("authentication exception");
            Util.hideLoadingAnimation();
          } 
          else if (response.status === 403) {
            console.log("authorization exception");
            Util.hideLoadingAnimation();
          } 
          else {
            throw new Error();
          }
        }).catch(function (e) {
          console.log(e);
          Util.hideLoadingAnimation();
      });
    }, 1000);
  }
  else {
    console.log("empty username or password.");
  }
});
