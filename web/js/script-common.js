var answermeApp = angular.module('answermeApp', ['ngRoute']);
answermeApp.service('accountService', function() {
	 var savedData = {}
	 function set(data) {
	   savedData = data;
	 }
	 function get() {
	  return savedData;
	 }

	 return {
	  set: set,
	  get: get
	 }
});

answermeApp.config(function($routeProvider, $locationProvider) {
  $routeProvider
	.when('/', {
		controller: 'loginController',
		templateUrl: 'login.html'
	}).when('/home', {
		controller: 'homeController',
		templateUrl: 'home.html'
	}).when('/login', {
		controller: 'loginController',
		templateUrl: 'login.html'
	}).otherwise({
		redirectTo: '/login'
	});
});

answermeApp.controller('loginController', ['$scope', 'accountService', '$location', '$http', function($scope, accountService, $location, $http) {
	function createAccount(name, userId, token, service, client) {
		return {
			name: name,
			userId: userId,
			token: token,
			service: service,
			client: client
		}
	}
	function callbackStatus(response) {console.log(response)}

	function signin(account) {
		$http.defaults.headers.common['Access-Control-Allow-Origin'] = '*';
		$http.defaults.headers.common['Access-Control-Allow-Methods'] = 'POST, GET, OPTIONS, DELETE';
		$http.defaults.headers.common['Access-Control-Max-Age'] = '3600';
		$http.defaults.headers.common['Access-Control-Allow-Headers'] = 'x-requested-with';

		$http.defaults.headers.post['dataType'] = 'json'

		$http({
		  	method: 'POST',
		  	headers: {'Content-Type': 'application/json', 'Access-Control-Allow-Origin': '*'},
		  	url: 'https://localhost:4567/user/check',
		  	dataType: 'json',
		  	data: {request: account}
		  }).then(callbackStatus, callbackStatus);
	}

	$scope.statusChangeCallback = function (response) {
		if (response.status === 'connected') {
		  var name = '';
		  var account = createAccount(undefined, response.authResponse.userID, response.authResponse.accessToken, 'fb', 'web');

		  FB.api('/me', function(response) {
			account.name = response.name 
			});

		  accountService.set(account);
		  $location.path("/home");
		  $scope.$apply();
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
			signin(account);
		});
	}

	$scope.startApp();
}])
.controller('MainCtrl', ['$route', '$routeParams', '$location', function($route, $routeParams, $location) {
    this.$route = $route;
    this.$location = $location;
    this.$routeParams = $routeParams;
}])
.controller('homeController', ['$scope', 'accountService', function($scope, accountService) {
	$scope.accountHolder = accountService.get()
	$scope.msgs = [];
}]);