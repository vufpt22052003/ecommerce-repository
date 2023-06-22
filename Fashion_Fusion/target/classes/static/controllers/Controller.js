var app = angular.module('MyApp', []);


// phía sản phẩm
app.controller('PorductController', ['$http', '$scope', '$rootScope', function($http, $scope, $rootScope) {
	$scope.currentPage = 0; // Trang hiện tại
	$scope.pageSize = 5; // Số sản phẩm hiển thị trên mỗi trang
	$scope.totalItems = 0; // Tổng số sản phẩm

	// Hàm thay đổi trang
	$scope.changePage = function(page) {
		$scope.currentPage = page;
		$scope.loadData();
	};


	$rootScope.data = []
	$scope.loadData = function() {
		$http.get('api/listProByUs/' + $scope.currentPage)
			.then(function(response) {
				$rootScope.data = response.data.content; // Dữ liệu sản phẩm
				$scope.totalItems = response.data.totalElements; // Cập nhật tổng số sản phẩm
			})
			.catch(function(error) {
				console.error('Error:', error);
			});
	};



	$scope.loadData();

	$scope.getTotalPages = function() {
		var totalPages = Math.ceil($scope.totalItems / $scope.pageSize);
		var pageArray = [];

		for (var i = 1; i <= totalPages; i++) {
			pageArray.push(i);
		}

		return pageArray;
	};



	$scope.getRange = function(totalPages) {
		return new Array(totalPages);
	};



	$scope.deleteImage = function(index) {
		$scope.dataProDTO.imgRelateTos.splice(index, 1);
	}


	$scope.ShowPro = function(id) {
		$scope.dataProDTO = {}
		$http({
			method: 'GET',
			url: '/api/showPro',
			params: { id: id }
		}).then(function(response) {
			$scope.dataProDTO = response.data; // gán dử liệu bên edit
		}, function(error) {
			console.log(error.data);
		});
	}
	$scope.ShowPro();

	$rootScope.getSaleById = function(id) {

		$rootScope.sale = {};
		$http({
			method: 'GET',
			url: '/api/getSaleById',
			params: { id: id }
		}).then(function(response) {
			$rootScope.sale = response.data;
			$rootScope.startDate = moment($rootScope.sale.start_date).format('YYYY-MM-DD HH:mm:ss');
			$rootScope.endDate = moment($rootScope.sale.end_date).format('YYYY-MM-DD HH:mm:ss');
			$scope.showPrice = true;
			$scope.isUpdateClicked = false; // ẩn chữ thay đôti
			$scope.addPriceSale = false; // ẩn thêm giá tiền sale
			$rootScope.id = id
		}, function(error) {
			console.log(error.data);
		});
	};

	$scope.update = function(id) {
		$rootScope.sale = {}; // Xóa dữ liệu trong $rootScope.sale
		delete $rootScope.startDate; // Xóa giá trị trong $rootScope.startDate
		delete $rootScope.endDate; // Xóa giá trị trong $rootScope.endDate   
		// angular.element('.change-button').addClass('d-none');
		$scope.isUpdateClicked = true;
		$scope.showPrice = false;
		$scope.addPriceSale = true;
		$scope.id = id

	}

	$scope.deleteProduct = function(id) {
		$http({
			method: 'GET',
			url: '/deleteProduct',
			params: { id: id }
		}).then(function(response) {
			// Tìm vị trí của sản phẩm trong danh sách hiện tại
			var index = $scope.data.findIndex(function(product) {
				return product.id === id;
			});

			if (index !== -1) {
				// Xóa sản phẩm khỏi danh sách
				$scope.data.splice(index, 1);
			}
			$.toast({
				heading: 'Xóa đơn hàng thành công',
				text: 'Đơn hàng đã được xóa.',
				position: 'top-right',
				icon: 'success'
			})
		}, function(error) {
			$.toast({
				heading: 'Warning',
				text: 'Đã Có Lỗi Xảy Ra',
				icon: 'warning'
			})
		});
	};




	/////////end///////////

}]);

app.controller('statisticsCtrl', ['$http', '$scope', '$rootScope', function($http, $scope, $rootScope) {
	// tính tổng tiền 
	$scope.total = 0
	$scope.loadTotal = function() {
		$http.get('/api/totalPrice')
			.then(function(response) {
				$scope.total = response.data;
			})
			.catch(function(error) {
				console.error('Error:', error);
			});
	};
	$scope.loadTotal();

	// lấy top các sản phẩm bán chạy
	$scope.top10Order = []
	$scope.loadTop10 = function() {
		$http.get('/api/getTop10Order')
			.then(function(response) {
				$scope.top10Order = response.data;
			})
			.catch(function(error) {
				console.error('Error:', error);
			});
	};
	$scope.loadTop10();

	// lấy danh sách các người mua nhiều nhất
	$scope.loadUserByOder = []
	$scope.loadTopUser = function() {
		$http.get("/api/getTopUserByOder")
			.then(function(response) {
				$scope.loadUserByOder = response.data;
			})
	}
	$scope.loadTopUser()

	$scope.selectedOption = "Hiện 10 Sản Phẩm"; // Tùy chọn mặc định
	$scope.productLimit = 10; // Số lượng sản phẩm hiển thị mặc định

	$scope.changeProductLimit = function() {
		if ($scope.selectedOption === "Hiện 10 Sản Phẩm") {
			$scope.productLimit = 10;
		} else if ($scope.selectedOption === "Hiện 20 Sản Phẩm") {
			$scope.productLimit = 2;
		} else if ($scope.selectedOption === "Hiện 30 Sản Phẩm") {
			$scope.productLimit = 30;
		} else {
			// Hiện tất cả các sản phẩm
			$scope.productLimit = $scope.products.length;
		}
	};


	/*+++++++++++++++++++++++*/

}]);

/*+++++++++++++++++++++++*/
// order
app.controller('OrderController', ['$http', '$scope', '$rootScope', function($http, $scope, $rootScope) {

	// phía ng dùng
	$scope.information = 'pending';
	$scope.data = [];

	$scope.loadData = function(type) {
		$http.get('/api/OrderStatus?type=' + type)
			.then(function(response) {
				$scope.data = response.data;

			})
			.catch(function(error) {
				console.error('Error:', error);
			});
	};


	$scope.loadData($scope.information);


	$scope.openModal = function(item) {
		$scope.selectedProduct = item;
	};

	/////////end///////////


	//////////////// phía admin
	$scope.details = [];
	$scope.selectType = "all";


	$scope.countDay = 0
	$scope.loadDetails = function(type) {
		$http.get('/api/getOrderDetails?type=' + type)
			.then(function(response) {
				$scope.details = response.data;
				var currentDate = moment().format('YYYY-MM-DD HH:mm:ss');
				for (var i = 0; i < response.data.length; i++) {
					var createdDate = moment(response.data[i].created_at);
					if (createdDate.isSame(currentDate, 'day')) {
						$scope.countDay++
					}
				}

			})
			.catch(function(error) {
				console.error('Error:', error);
			});
	};

	$scope.loadDetails($scope.selectType); // Gọi hàm loadDetails với type ban đầu



	/////////////////////////////////////

	// xác nhận
	$scope.confirm = function(id) {
		$.ajax({
			url: "/order_confim",
			type: "POST",
			data: {
				Oid: id,
				Action: "confirm" // Thay đổi giá trị action tại đây
			},
			success: function(data) {
				console.log(data)

				$scope.loadData($scope.selectedOption); // Gọi lại hàm loadData với selectedOption hiện tại
				$scope.loadDetails($scope.selectType);

				$.toast({
					heading: 'Xác nhận thành công',
					text: 'Đơn hàng đã được xác nhận.',
					position: 'top-right',
					icon: 'success'
				})
			},
			error: function(xhr, status, error) {
				$.toast({
					heading: 'Error',
					text: 'Đã Có Lỗi Xãy Ra, Không Thể Xác Nhận Đơn Hàng.',
					position: 'top-right',
					icon: 'error'
				})


			}
		});
	}

	// hủy đơn 
	$scope.cancel = function(id) {

		$.ajax({
			url: "/cancel_order",
			type: "POST",
			data: {
				Oid: id,
				Action: "cancel" // Thay đổi giá trị action tại đây
			},
			success: function(data) {
				$scope.loadData($scope.selectedOption); // Gọi lại hàm loadData với selectedOption hiện tại
				$scope.loadDetails($scope.selectType);
				$scope.loadData($scope.information);
				$.toast({
					heading: 'Hủy Đơn Hàng thành công',
					text: 'Đơn hàng đã được hủy.',
					position: 'top-right',
					icon: 'success'
				})
			},
			error: function(xhr, status, error) {
				// Xử lý lỗi nếu có
				console.log(error);
			}
		});

	}


	$scope.s = function() {
		$.toast({
			heading: 'Hủy Đơn Hàng thành công',
			text: 'Đơn hàng đã được hủy.',
			position: 'top-right',
			icon: 'success'
		})
	}

}]);
