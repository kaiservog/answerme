angular
	.module('answermeApp')
	.controller('answerController', ['$scope', '$http', '$location', '$interval', 'accountService', 'questionService', 'accountService', 'cfg',
		function($scope, $http, $location, $interval, accountService, questionService, accountService, cfg) {
		$scope.appendAnswer = function() {
			$interval.cancel($scope.acceptTimeout);
			$('#answer').removeClass('ng-hide');
			$('#bt-answer').addClass('hidden');
			$('#bt-ignore').addClass('hidden');

			$scope.countering = function() {
				$scope.counter--;			
			}
			$scope.counteringId = $interval($scope.countering, 60000);

			var timeout = setTimeout($scope.goHome, 900000);
		}

		$scope.accept = function() {
			$interval.cancel($scope.acceptTimeout);
			$('#clockAccept').addClass('hidden');
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
			$interval.cancel($scope.acceptTimeout);
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
			$interval.cancel($scope.acceptTimeout);
			$location.path('/home');
		}

		$scope.question = questionService.get();
		$scope.accountHolder = accountService.get();

		$scope.counter = 15;

		$scope.counterAccept = 30;
		$scope.counteringAccept = function() {
			$scope.counterAccept--;
		}
		$scope.counteringAcceptId = $interval($scope.counteringAccept, 1000);
		$scope.acceptTimeout = $interval($scope.goHome, 30000, 1);



}]);
