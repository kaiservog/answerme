angular
	.module('answermeApp')
	.controller('signupController', ['$scope', '$http', '$location', 'accountService', 'cfg',
		function($scope, $http, $location, accountService, cfg) {
	$scope.apply = function () {
		var topics = $('#topics').val();

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