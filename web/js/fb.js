window.fbAsyncInit = function() {
  FB.init({
	appId      : '1682532568656067',
	cookie     : true,  
	xfbml      : true,  
	version    : 'v2.2'
  });

  FB.getLoginStatus(function(response) {
	statusChangeCallback(response);
  });
};

(function(d, s, id) {
	var js, fjs = d.getElementsByTagName(s)[0];
	if (d.getElementById(id)) return;
	js = d.createElement(s); js.id = id;
	js.src = "//connect.facebook.net/en_US/sdk.js";
	fjs.parentNode.insertBefore(js, fjs);
}(document, 'script', 'facebook-jssdk'));

function checkLoginState() {
	FB.getLoginStatus(function(response) {
	  statusChangeCallback(response);
	});
}

function statusChangeCallback(response) {
	console.log('statusChangeCallback');
	console.log(response);

	if (response.status === 'connected') {
	  console.log('Successful userId for: ' + response.authResponse.userID);
	  console.log('Successful accessToken for: ' + response.authResponse.accessToken);
	  var controller = $('#loginController');
	  var account = {
		  userID :  response.authResponse.userID,
		  accessToken : response.authResponse.accessToken,
		  service : 'fb'
	  }
	  
	  controller.accountHolder(account);
	  $("#home").click();
	} else if (response.status === 'not_authorized') {
	  document.getElementById('status').innerHTML = 'Please log ' +
		'into this app.';
	} else {
	  document.getElementById('status').innerHTML = 'Please log ' +
		'into Facebook.';
	}
}