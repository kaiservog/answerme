angular
	.module('answermeApp')
	.controller('homeController', ['$scope', '$http', '$location', 'accountService', 'questionService', 'cfg',
		function($scope, $http, $location, accountService, questionService, cfg) {
		$scope.accountHolder = accountService.get();
		$scope.msgs = [];

		$scope.delay = 5000;
		$scope.alert = function(text, severity) {
			var div = $('<div>').html(text);
			div.addClass('alert');
			div.addClass('alert-' + severity);
			$('#messageContainer-body').append(div);
			setTimeout(function() {
				div.remove();	
			}, 10000)
		}
		
		$scope.messageContainerHider = function() {
			var container = $('#messageContainer');
			var containerBody = $('#messageContainer-body');
			
			if(containerBody.children().size() > 0) {
				container.removeClass('hidden');
			}else {
				container.addClass('hidden');
			}
		}

		$scope.jumpTopic = function(event) {
			if(event.which === 32) {
				$('#question').focus();
			}
		}

		$scope.updateModalShow = function() {
			$http.defaults.headers.post['dataType'] = 'json'

			$http({
			  	method: 'GET',
			  	headers: {'Content-Type': 'application/json', 'Access-Control-Allow-Origin': '*'},
			  	url: cfg.BACKEND_ADDRESS + '/user/get/' + $scope.accountHolder.extUserId + '/' + $scope.accountHolder.loginService,
			  	dataType: 'json'
			  }).then(function(response) {
			  	console.log(response);
			  	$scope.rawTopics = '';
			  	response.data.response.user.topics.forEach(function(elm, index) {
			  		$scope.rawTopics = $scope.rawTopics + elm.name + ' ';
			  	});
			  	$('#topics').val($scope.rawTopics);
			  	$scope.workTopics();
			  }, function(response){
			  	console.log(response);
			  	$scope.alert('Something weird happened', 'danger');
			  });

			  $('#update-modal').modal('show');
		}

		$scope.ask = function() {
			var askPackage = {
				token: $scope.accountHolder.token,
				loginService: $scope.accountHolder.loginService,
				topic: $('#topic').val().trim(),
				question: $('#question').val().trim()
			}

			$http.defaults.headers.post['dataType'] = 'json'

			$http({
			  	method: 'POST',
			  	headers: {'Content-Type': 'application/json', 'Access-Control-Allow-Origin': '*'},
			  	url: cfg.BACKEND_ADDRESS + '/question/add',
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

		$scope.reject = 0;
		$scope.directRejectAnswer = function () {
			$scope.reject++;
			if($scope.reject >= 2)  {
				$('#answer-modal').modal('hide');
			}
		}

		$scope.apply = function () {
			var topics = $('#topics').val();

			if($scope.rawTopics == topics) {
				$('#update-modal').modal('hide');
				return;
			}

			var request = {
				token: accountService.get().token,
				loginService: accountService.get().loginService,
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
			  	url: cfg.BACKEND_ADDRESS + '/user/update',
			  	dataType: 'json',
			  	data: {request: request}
			  }).then(function(response) {
			  	if(response.data.response.message=='ok') $location.path("/home");
			  	}, function(response){console.log(response)});
			$('#update-modal').modal('hide');
		}

		$scope.workTopics = function () {
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

		var checkId = setInterval(checkQuestions, $scope.delay);
		var hiderId = setInterval($scope.messageContainerHider, 1000);
				

		function checkQuestions() {
			var extUserId = $scope.accountHolder.extUserId;
			var loginService = $scope.accountHolder.loginService;
			var token = $scope.accountHolder.token;

			var request = {
				topic: 'java',
				token: token,
				loginService: accountService.get().loginService
			}

			$http.defaults.headers.post['dataType'] = 'json'

			$http({
			  	method: 'POST',
			  	headers: {'Content-Type': 'application/json', 'Access-Control-Allow-Origin': '*'},
			  	url: cfg.BACKEND_ADDRESS + '/question/find',
			  	dataType: 'json',
			  	data: {request: request}
			  }).then(function(response) {
			  		console.log('find');
			  		console.log(response);
			  		if(response.data.response.message=='ok') {
			  			questionService.set({
			  				topic: response.data.response.question.topic,
			  				question: response.data.response.question.question,
			  				id : response.data.response.question.id

			  			});
			  			clearInterval(checkId);
						clearInterval(hiderId);
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
		$("[data-toggle=popover]").popover();
	}]);
