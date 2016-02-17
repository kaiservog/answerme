angular
	.module('answermeApp')
	.controller('answerController', ['$scope', '$http', '$location', '$interval', 'accountService', 'questionService', 'accountService', 'cfg',
		function($scope, $http, $location, $interval, accountService, questionService, accountService, cfg) {
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

		$scope.accept = function() {
			$http.defaults.headers.post['dataType'] = 'json'

			var request = {
				token: accountService.get().token,
				loginService: accountService.get().loginService,
				questionId : questionService.get().id
			}

			$http({
			  	method: 'POST',
			  	headers: {'Content-Type': 'application/json', 'Access-Control-Allow-Origin': '*'},
			  	url: cfg.BACKEND_ADDRESS + '/question/accept',
			  	dataType: 'json',
			  	data : {request: request}
			  }).then(function(response) {
			  	console.log(response);
			  	$scope.appendAnswer();
			  }, function(response){
			  	console.log(response);
			  });
		}

		$scope.reject = function() {
			$http.defaults.headers.post['dataType'] = 'json'

			var request = {
				token: accountService.get().token,
				loginService: accountService.get().loginService,
				questionId : questionService.get().id
			}

			$http({
			  	method: 'POST',
			  	headers: {'Content-Type': 'application/json', 'Access-Control-Allow-Origin': '*'},
			  	url: cfg.BACKEND_ADDRESS + '/question/reject',
			  	dataType: 'json',
			  	data : {request: request}
			  }).then(function(response) {
			  	console.log(response);
				$location.path('/home');  	
			  }, function(response){
			  	console.log(response);
			  });
		}

		$scope.answer = function() {
			$http.defaults.headers.post['dataType'] = 'json'
			var answer = $('#answer-text').val();
			var request = {
				token: accountService.get().token,
				loginService: accountService.get().loginService,
				questionId : questionService.get().id,
				answer : answer
			}

			$http({
			  	method: 'POST',
			  	headers: {'Content-Type': 'application/json', 'Access-Control-Allow-Origin': '*'},
			  	url: cfg.BACKEND_ADDRESS + '/question/answer',
			  	dataType: 'json',
			  	data : {request: request}
			  }).then(function(response) {
			  	console.log(response);
				$location.path('/home');  	
			  }, function(response){
			  	console.log(response);
			  });
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
