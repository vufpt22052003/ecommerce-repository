var app = angular.module('myapp', []);

app.controller('Controller', ['$http', '$scope', function($http, $scope) {
	$scope.calculateTotal = function(source) {
		if (source === 'price_sale' && $scope.price_sale > $scope.price) {
			alert("Không được nhập giá giảm lớn hơn giá tiền");
		}

		$scope.total = parseFloat($scope.price) || 0;
		if ($scope.price_sale) {
			$scope.total -= parseFloat($scope.price_sale);
		}

	};
	// đọc api catogory lên
	$scope.data = [];
	$scope.loadCategory = function() {

		$http.get('/api/category')
			.then(function(response) {
				// Nếu yêu cầu thành công, lấy dữ liệu từ response và đổ vào mảng data
				$scope.data = response.data;
				console.log($scope.data);
			})
			.catch(function(error) {
				// Xử lý lỗi nếu có
				console.error('Error:', error);
			});
	};


	$scope.location = function() {

		$http.get('https://api.mysupership.vn/v1/partner/areas/province')
			.then(function(response) {
				// Nếu yêu cầu thành công, lấy dữ liệu từ response và đổ vào mảng data
				$scope.location = response.data;
				console.log($scope.data);
			})
	};
	$scope.location();
	$scope.loadCategory();

}]);
