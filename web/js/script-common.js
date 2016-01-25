var answermeApp = angular.module('answermeApp', ['ngRoute']);

answermeApp.factory('accountService', function() {
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

answermeApp.controller('loginController', ['$scope', function($scope) {
	$scope.accountHolder = accountService.set
}])
.controller('MainCtrl', ['$route', '$routeParams', '$location',
  function($route, $routeParams, $location) {
    this.$route = $route;
    this.$location = $location;
    this.$routeParams = $routeParams;
}])
.controller('homeController', ['$scope', function($scope) {
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