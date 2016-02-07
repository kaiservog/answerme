angular
	.module('answermeApp')
	.controller('homeController', ['$scope', '$http', '$location', 'accountService', 'questionService', function($scope, $http, $location, accountService, questionService) {
		$scope.accountHolder = accountService.get();
		$scope.msgs = [];

		$scope.delay = 5000;
		$scope.alert = function(text, severity) {
			var div = $('<div>').html(text);
			div.addClass('alert');
			div.addClass('alert-' + severity);
			$('#messageContainer').append(div);
			setTimeout(function() {
				div.remove();	
			}, 10000)
		}

		$scope.jumpTopic = function(event) {
			if(event.which === 32) {
				$('#question').focus();
			}
		}

		$scope.ask = function() {
			var askPackage = {
				userId: $scope.accountHolder.userId,
				token: $scope.accountHolder.token,
				loginService: $scope.accountHolder.service,
				topic: $('#topic').val().trim(),
				question: $('#question').val().trim()
			}

			$http.defaults.headers.post['dataType'] = 'json'

			$http({
			  	method: 'POST',
			  	headers: {'Content-Type': 'application/json', 'Access-Control-Allow-Origin': '*'},
			  	url: 'https://localhost:4567/question/add',
			  	dataType: 'json',
			  	data: {request: askPackage}
			  }).then(function(response) {
			  	$('#topic').val('');
			  	$('#question').val('');
			  	$('#topic').focus();
			  }, function(response){
			  	console.log(response);
			  	$scope.alert('Something weird happened', 'danger');
			  });
		} 

		var checkId = setInterval(checkQuestions, $scope.delay);


		function checkQuestions() {
			var userId = $scope.accountHolder.userId;
			var loginService = $scope.accountHolder.service;
			var token = $scope.accountHolder.token;

			$http.defaults.headers.post['dataType'] = 'json'

			$http({
			  	method: 'GET',
			  	headers: {'Content-Type': 'application/json', 'Access-Control-Allow-Origin': '*'},
			  	url: 'https://localhost:4567/question/find/' + userId + '/' + token + '/' + loginService + '/java',
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
	}]);