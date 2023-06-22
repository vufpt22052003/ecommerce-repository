var app = angular.module('ProductApp', []);


app.controller('ProController', ['$http', '$scope', function($http, $scope) {

	$scope.getProducts = function() {
		$http.get('api/page')
			.then(function(response) {
				$scope.data = response.data;
				console.log($scope.data)
			})
			.catch(function(error) {
				console.error(error);
			});
	};
	$scope.getProducts();
	$scope.goToPage = function(page) {
		$scope.currentPage = page;
		$scope.getProducts();
	};
	app.controller('YourController', function($scope) {
  $scope.data = {}; // Đối tượng chứa dữ liệu trang hiện tại và tổng số trang
  
  // Hàm getRange để tạo một mảng các số từ 1 đến tổng số trang
  $scope.getRange = function(totalPages) {
    return Array.from({ length: totalPages }, (_, index) => index + 1);
  };
});

}]);