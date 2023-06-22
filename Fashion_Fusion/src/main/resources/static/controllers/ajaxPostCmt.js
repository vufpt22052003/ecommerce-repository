
var app = angular.module('MyApp', []);
app.controller('CommentController', ['$scope', '$http', function($scope, $http) {
	$scope.postComment = function() {
		var comments = $scope.comment;


		    console.log($scope.productId); // In ra giá trị productId


		// Đọc giá trị productId từ attribute và gán vào $scope
		

		$http({
			method: 'POST',
			url: '/postComment',
			data: {
				//ProductId: Vid,
				comment: comments
			}
		}).then(function(response) {
			// Xử lý phản hồi thành công (nếu cần)
		}, function(error) {
			// Xử lý lỗi (nếu có)
			console.log(error);
		});
	};
}]);


