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

answermeApp.controller('loginController', ['$scope', 'accountService', '$location', function($scope, accountService, $location) {
	$scope.statusChangeCallback = function (response) {
		console.log('statusChangeCallback');
		console.log(response);

		if (response.status === 'connected') {
		  console.log('Successful userId for: ' + response.authResponse.userID);
		  console.log('Successful accessToken for: ' + response.authResponse.accessToken);

		  var account = {
			  userID :  response.authResponse.userID,
			  accessToken : response.authResponse.accessToken,
			  service : 'fb'
		  }
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
				var account = {
				  userID :  'teste',
				  accessToken : 'teste',
				  service : 'gp'
				}
			}
		, undefined);
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
	$scope.msgs = [
		{
			question: {
				topic: 'Java',
				text: 'How to create classpath for maven?',
				time: '15 mins ago',
				avatar: 'http://placehold.it/50/55C1E7/fff'
			},
			answer: {
				text: 'try MVN_HOME on control panel > add envirement variable',
				time: '10 mins ago',
				avatar: 'http://placehold.it/50/FA6F57/fff',
				status: 'Approved'
			}
			
		},
		{
			question: {
				topic: 'Java',
				text: 'How to create classpath for maven?',
				time: '15 mins ago',
				avatar: 'http://placehold.it/50/55C1E7/fff'
			},
			answer: {
				text: 'try MVN_HOME on control panel > add envirement variable',
				time: '10 mins ago',
				avatar: 'http://placehold.it/50/FA6F57/fff',
				status: 'Approved'
			}
			
		},
		{
			question: {
				topic: 'Java',
				text: 'How to create classpath for maven?',
				time: '15 mins ago',
				avatar: 'http://placehold.it/50/55C1E7/fff'
			},
			answer: {
				text: 'try MVN_HOME on control panel > add envirement variable',
				time: '10 mins ago',
				avatar: 'http://placehold.it/50/FA6F57/fff',
				status: 'Approved'
			}
			
		},
		{
			question: {
				topic: 'Java',
				text: 'How to create classpath for maven?',
				time: '15 mins ago',
				avatar: 'http://placehold.it/50/55C1E7/fff'
			},
			answer: {
				text: 'try MVN_HOME on control panel > add envirement variable',
				time: '10 mins ago',
				avatar: 'http://placehold.it/50/FA6F57/fff',
				status: 'Approved'
			}
			
		},
		{
			question: {
				topic: 'Java',
				text: 'How to create classpath for maven?',
				time: '15 mins ago',
				avatar: 'http://placehold.it/50/55C1E7/fff'
			},
			answer: {
				text: 'try MVN_HOME on control panel > add envirement variable',
				time: '10 mins ago',
				avatar: 'http://placehold.it/50/FA6F57/fff',
				status: 'Approved'
			}
			
		},
		{
			question: {
				topic: 'Java',
				text: 'How to create classpath for maven?',
				time: '15 mins ago',
				avatar: 'http://placehold.it/50/55C1E7/fff'
			},
			answer: {
				text: 'try MVN_HOME on control panel > add envirement variable',
				time: '10 mins ago',
				avatar: 'http://placehold.it/50/FA6F57/fff',
				status: 'Approved'
			}
			
		},
		{
			question: {
				topic: 'Java',
				text: 'How to create classpath for maven?',
				time: '15 mins ago',
				avatar: 'http://placehold.it/50/55C1E7/fff'
			},
			answer: {
				text: 'try MVN_HOME on control panel > add envirement variable',
				time: '10 mins ago',
				avatar: 'http://placehold.it/50/FA6F57/fff',
				status: 'Approved'
			}
			
		},
		{
			question: {
				topic: 'Java',
				text: 'How to create classpath for maven?',
				time: '15 mins ago',
				avatar: 'http://placehold.it/50/55C1E7/fff'
			},
			answer: {
				text: 'try MVN_HOME on control panel > add envirement variable',
				time: '10 mins ago',
				avatar: 'http://placehold.it/50/FA6F57/fff',
				status: 'Approved'
			}
			
		}
	];
}]);