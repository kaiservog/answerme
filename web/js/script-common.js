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

answermeApp.constant('cfg', {
	'BACKEND_ADDRESS' : 'http://localhost:4567' 
});