angular
	.module('answermeApp')
	.controller('loginController', ['$scope', 'accountService', '$location', '$http', 'cfg',
		function($scope, accountService, $location, $http, cfg) {
	function createAccount(name, extUserId, token, loginService) {
		return {
			name: name,
			extUserId: extUserId,
			token: token,
			loginService: loginService,
			client: 'web'
		}
	}
	function callbackStatus(response) {
		console.log(response)
	}

	function signin(account) {
		$http.defaults.headers.common['Access-Control-Allow-Origin'] = '*';
		$http.defaults.headers.common['Access-Control-Allow-Methods'] = 'POST, GET, OPTIONS, DELETE';
		$http.defaults.headers.common['Access-Control-Max-Age'] = '3600';
		$http.defaults.headers.common['Access-Control-Allow-Headers'] = 'x-requested-with';

		$http.defaults.headers.post['dataType'] = 'json'

		$http({
		  	method: 'POST',
		  	headers: {'Content-Type': 'application/json', 'Access-Control-Allow-Origin': '*'},
		  	url: cfg.BACKEND_ADDRESS + '/user/check',
		  	dataType: 'json',
		  	data: {request: account}
		  }).then(function(response) {
		  	console.log(response.data.response)
		  	if(response.data.response.message == 'ok') 
		  		$location.path("/home");
		  	if(response.data.response.message == 'firstlogin') 
		  		$location.path("/signup");
		  	
		  	}, 
		  	callbackStatus);
	}



	$scope.statusChangeCallback = function (response) {
		if (response.status === 'connected') {
		  var name = '';
		  var account = createAccount('', response.authResponse.userID, response.authResponse.accessToken, 'fb');

		  FB.api('/me', function(response) {
			account.name = response.name 
			});

		  accountService.set(account);
		  signin(account);
		}
	}
	
	$scope.checkLoginState = function () {
		FB.getLoginStatus(function(response) {
		  statusChangeCallback(response);
		});
	}
	
	$scope.fb_login = function() {
		FB.login( function() {}, { scope: 'email,public_profile' } );
	}

	window.fbAsyncInit = function() {
	  FB.init({
		appId      : '1682532568656067',
		cookie     : true,  
		xfbml      : true,  
		version    : 'v2.2'
	  });

	  FB.getLoginStatus(function(response) {
		$scope.statusChangeCallback(response);
	  });
	};

	(function(d, s, id) {
		var js, fjs = d.getElementsByTagName(s)[0];
		if (d.getElementById(id)) return;
		js = d.createElement(s); js.id = id;
		js.src = "//connect.facebook.net/en_US/sdk.js";
		fjs.parentNode.insertBefore(js, fjs);
	}(document, 'script', 'facebook-jssdk'));
	
	//google
	
	  $scope.googleUser = {};
	  $scope.startApp = function() {
		gapi.load('auth2', function(){
			auth2 = gapi.auth2.init({
			client_id: '374086716369-10kasah024t0g0a1e8nae2e1sb7jf2ua.apps.googleusercontent.com',
			cookiepolicy: 'single_host_origin',
		  });
		  $scope.attachSignin(document.getElementById('googleb'));
		});
	  };
	  
	$scope.attachSignin = function(element) {
		auth2.attachClickHandler(element, {}, function(googleUser) {
			console.log(googleUser);

			var id_token = googleUser.getAuthResponse().id_token;
			var profile = googleUser.getBasicProfile();
			console.log(profile.getId());
			var account = createAccount(profile.getName(), profile.getId(), id_token, 'gp', 'web');
			accountService.set(account);
			signin(account);
		});
	}

	$scope.justGo = function() {
		var account = createAccount('test user', '666', '666000', 'tt', 'web');
		accountService.set(account);
		signin(account);
	}

	$scope.startApp();
}]);