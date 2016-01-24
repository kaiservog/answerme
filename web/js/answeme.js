angular.module('home', [])
    .controller('homeController', ['$scope', function($scope) {
        $scope.msgs = [
			{
				type: 'question',
				topic: 'Java',
				text: 'How to create classpath for maven?',
				time: '15 mins ago',
				avatar: 'http://placehold.it/50/55C1E7/fff'
			},
			{
				type: 'answer',
				topic: 'Java',
				text: 'try MVN_HOME on control panel > add envirement variable',
				time: '10 mins ago',
				avatar: 'http://placehold.it/50/FA6F57/fff'
			}
		];
    }]);