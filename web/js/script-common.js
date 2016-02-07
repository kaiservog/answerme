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
	}).service('questionService', function() {
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
	}).when('/signup', {
		controller: 'signupController',
		templateUrl: 'signup.html'
	}).when('/answer', {
		controller: 'answerController',
		templateUrl: 'answer.html'
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
		  	url: 'https://localhost:4567/user/check',
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
			accountService.set(account);
			signin(account);
		});
	}

	$scope.justGo = function() {
		var account = createAccount('test user', '666', '666', 'tt', 'web');
		accountService.set(account);
		$location.path("/home");
	}

	$scope.startApp();
}])
.controller('MainCtrl', ['$route', '$routeParams', '$location', 'questionService',
	function($route, $routeParams, $location) {
    this.$route = $route;
    this.$location = $location;
    this.$routeParams = $routeParams;
}])
.controller('homeController', ['$scope', '$http', '$location', 'accountService', 'questionService',
		function($scope, $http, $location, accountService, questionService) {
	$scope.accountHolder = accountService.get();
	$scope.msgs = [];

	$scope.delay = 5000;

	var checkId = setInterval(checkQuestions, $scope.delay);


	function checkQuestions() {
		var userId = $scope.accountHolder.userId;

		$http.defaults.headers.post['dataType'] = 'json'

		$http({
		  	method: 'GET',
		  	headers: {'Content-Type': 'application/json', 'Access-Control-Allow-Origin': '*'},
		  	url: 'https://localhost:4567/question/find/' + userId + '/java',
		  	dataType: 'json'
		  }).then(function(response) {
		  		console.log('find');
		  		console.log(response);
		  		if(response.data.response.message=='ok') {
		  			questionService.set({
		  				topic: response.data.response.question.topic,
		  				question: response.data.response.question.question

		  			});
		  			clearInterval(checkId);
		  			$location.path('/answer');
		  		}
		  		if(response.data.response.message == 'notFound') {
					$scope.delay = response.data.response.next;
		  		}
		  			
		  		if(response.data.response.message=='error') {
		  			$scope.delay = 10000;
		  		}
		  		
		  	}, function(response){console.log(response)});
	}


}])
.controller('answerController', ['$scope', '$http', '$location', 'accountService', 'questionService', 'accountService',
	function($scope, $http, $location, accountService, questionService, accountService) {
		$scope.question = questionService.get();
		$scope.accountHolder = accountService.get();
		$scope.appendAnswer = function() {
			$('#answer').removeClass('hidden');
			$('#bt-answer').addClass('hidden');
			$('#bt-ignore').addClass('hidden');
		}
		$scope.goHome = function() {
			$location.path('/home');
		}
	
}])
.controller('signupController', ['$scope', '$http', '$location', 'accountService', function($scope, $http, $location, accountService) {
	$scope.apply = function () {
		var topics = $('#topics').val();

		var request = {
			userId: accountService.get().userId,
			service: accountService.get().service,
			topics: topics
		}

		$http.defaults.headers.common['Access-Control-Allow-Origin'] = '*';
		$http.defaults.headers.common['Access-Control-Allow-Methods'] = 'POST, GET, OPTIONS, DELETE';
		$http.defaults.headers.common['Access-Control-Max-Age'] = '3600';
		$http.defaults.headers.common['Access-Control-Allow-Headers'] = 'x-requested-with';

		$http.defaults.headers.post['dataType'] = 'json'

		$http({
		  	method: 'POST',
		  	headers: {'Content-Type': 'application/json', 'Access-Control-Allow-Origin': '*'},
		  	url: 'https://localhost:4567/user/update',
		  	dataType: 'json',
		  	data: {request: request}
		  }).then(function(response) {
		  	if(response.data.response.message=='ok') $location.path("/home");
		  	}, function(response){console.log(response)});
	}

	$scope.workTopics = function (field) {
		var val = $('#topics').val();

		$scope.topics = splitTopics(val).map(mapTopics);
	}

	function mapTopics(element ) {
		var letter = element.charAt(0);
		color = colorTable[letter.toLowerCase()];
		if(color == undefined) color = 'default';
		var cElement =  element.charAt(0).toUpperCase() + element.slice(1);
		return { text: cElement, color: color};
	}

	function splitTopics(rawTopics) {
		var okRawTopics = rawTopics.replace("  ", " ");
		var topics = okRawTopics.split(" ");
		return topics;
	}

	var colorTable = {
		a: 'default',
		b: 'primary',
		c: 'success',
		d: 'info',
		e: 'warning',
		f: 'danger',
		g: 'default',
		h: 'primary',
		i: 'success',
		j: 'info',
		k: 'warning',
		l: 'danger',
		m: 'warning',
		n: 'primary',
		o: 'success',
		p: 'info',
		q: 'danger',
		r: 'info',
		s: 'info',
		t: 'danger',
		u: 'success',
		w: 'primary',
		x: 'primary',
		y: 'success',
		z: 'info'
	}
}]);