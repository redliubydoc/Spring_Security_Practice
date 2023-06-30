const Util = {

  showLoadingAnimation: function() {
    const loadingAnimationElement = document.querySelector(".overlay");
    if (loadingAnimationElement.classList.contains("hide")) {
      loadingAnimationElement.classList.remove("hide");
    }
  },

  hideLoadingAnimation: function() {
    const loadingAnimationElement = document.querySelector(".overlay");
    if (!loadingAnimationElement.classList.contains("hide")) {
      loadingAnimationElement.classList.add("hide");
    }
  },

  navigateTo: function(href) {
    const pageNavigator = document.querySelector("#page-navigator");
    pageNavigator.setAttribute("href", href);
    pageNavigator.click();
  }
};