/////////sale///////////
var app = angular.module('MyApp', []);

app.controller('SaleController', ['$http', '$scope', '$rootScope', function($http, $scope, $rootScope) {
	// lấy sale theo id
	$rootScope.getSaleById = function(id) {
		$rootScope.sale = {};
		$http({
			method: 'GET',
			url: '/api/getSaleById',
			params: { id: id }
		}).then(function(response) {
			$rootScope.sale = response.data; // gán dử liệu bên edit
			$rootScope.startDate = moment($rootScope.sale.start_date).format('YYYY-MM-DD HH:mm:ss');
			$rootScope.endDate = moment($rootScope.sale.end_date).format('YYYY-MM-DD HH:mm:ss');

		}, function(error) {
			console.log(error.data);
		});
	};



	$scope.selectType = 'onSale';
	$scope.dataSale = [];

	$scope.loadSale = function(type) {
		$http.get("/api/onSale?type=" + type)
			.then(function(response) {
				$scope.dataSale = response.data;
			})
			.catch(function(error) {
				// Xử lý lỗi, nếu có
				console.error('Error:', error);
			});
	};
	$scope.loadSale($scope.selectType);



	$scope.loadDaySale = function() {
		alert($scope.startDate + ', ' + $scope.endDate);

		// Kiểm tra nếu startDate và endDate không null hoặc undefined
		if ($scope.startDate && $scope.endDate) {
			// Sử dụng moment.js để định dạng ngày tháng
			var formattedStartDate = moment($scope.startDate, "YYYY-MM-DD HH:mm:ss.SSS").format("YYYY-MM-DDTHH:mm:ss.SSS");
			var formattedEndDate = moment($scope.endDate, "YYYY-MM-DD HH:mm:ss.SSS").format("YYYY-MM-DDTHH:mm:ss.SSS");

			$http.get("/api/onSale?startDate=" + formattedStartDate + "&endDate=" + formattedEndDate)
				.then(function(response) {
					$scope.dataSale = response.data;
				})
				.catch(function(error) {
					// Xử lý lỗi, nếu có
					console.error('Error:', error);
				});
		} else {
			// Xử lý khi ngày bắt đầu hoặc ngày kết thúc không được nhập
			console.log('Ngày bắt đầu và ngày kết thúc không được để trống');
		}
	};

	$scope.delSale = function(id) {
		// Gọi hàm SweetAlert2 với nút Yes và No
		Swal.fire({
			title: 'Thông báo',
			text: 'Bạn có muốn xóa Sale này không?',
			icon: 'info',
			showCancelButton: true,
			confirmButtonText: 'Yes',
			cancelButtonText: 'No'
		}).then((result) => {
			if (result.isConfirmed) {
				$http({
					method: 'GET',
					url: 'api/delSale', // Thêm tham số id vào URL
					params: { id: id }
				}).then(function(response) {
					$scope.loadSale($scope.selectType);
					$.toast({
						heading: 'Đơn Hàng Sale Xóa Thành Công',
						text: 'Đơn Hàng Sale Đã Được Xóa.',
						position: 'top-right',
						icon: 'success'
					})
				}, function(error) {
					// Xử lý lỗi (nếu có)
					console.log(error);
				});
			} else if (result.isDismissed === Swal.DismissReason.cancel) {
				console.log('No');
			}
		});
	}



}]);
                /*+++++++++++++++++++++++*/

