angular
	.module('answermeApp')
	.controller('answerController', ['$scope', '$http', '$location', '$interval', 'accountService', 'questionService', 'accountService', 
		function($scope, $http, $location, $interval, accountService, questionService, accountService) {
		$scope.appendAnswer = function() {
			$interval.cancel($scope.answerTimeout);
			$('#answer').removeClass('ng-hide');
			$('#bt-answer').addClass('hidden');
			$('#bt-ignore').addClass('hidden');

			$scope.countering = function() {
				$scope.counter--;			
			}
			$scope.counteringId = $interval($scope.countering, 1000);

			var timeout = setTimeout($scope.goHome, 15000);
		}

		$scope.goHome = function() {
			if($scope.counteringId != undefined) $interval.cancel($scope.counterId);
			$location.path('/home');
		}

		$scope.question = questionService.get();
		$scope.accountHolder = accountService.get();

		$scope.counter = 15;
		$scope.answerTimeout = $interval($scope.goHome, 5000, 1);

}]);
