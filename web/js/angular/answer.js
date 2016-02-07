angular
	.module('answermeApp')
	.controller('answerController', ['$scope', '$http', '$location', 'accountService', 'questionService', 'accountService', function($scope, $http, $location, accountService, questionService, accountService) {
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
}]);