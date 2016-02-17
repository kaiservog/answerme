angular
	.module('answermeApp')
	.controller('MainCtrl', ['$route', '$routeParams', '$location', 'questionService', function($route, $routeParams, $location) {
    this.$route = $route;
    this.$location = $location;
    this.$routeParams = $routeParams;
}])