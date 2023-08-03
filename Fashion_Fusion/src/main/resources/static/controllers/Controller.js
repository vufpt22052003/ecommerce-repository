var app = angular.module('MyApp', []);


// phía sản phẩm
app.controller('PorductController', ['$http', '$scope', '$rootScope', function($http, $scope, $rootScope) {
	$scope.currentPage = 0; // Trang hiện tại
	$scope.pageSize = 7; // Số sản phẩm hiển thị trên mỗi trang
	$scope.totalItems = 0; // Tổng số sản phẩm

	// Hàm thay đổi trang
	$scope.goToNextPage = function(page) {
		$scope.currentPage = $scope.currentPage + 1;
		$scope.loadData();
	};


	$scope.goToFirstPage = function() {
		$scope.currentPage = 0;
		// Gọi hàm để tải dữ liệu cho trang đầu tiên tại đây
		$scope.loadData();
	};



	// Hàm thay đổi trang
	$scope.changePage = function(page) {
		$scope.currentPage = page;
		$scope.loadData();
	};


	$rootScope.data = []
	$scope.day = false
	$scope.loadData = function() {

		$http.get('/api/listProByUs/' + $scope.currentPage)
			.then(function(response) {
				$rootScope.data = response.data.content; // Dữ liệu sản phẩm
				$scope.totalItems = response.data.totalElements; // Cập nhật tổng số sản phẩm
				for (var i = 0; i < $rootScope.data.length; i++) {
					var item = $rootScope.data[i].checkIsSale;
					if (item = false) {
						$scope.day = true
					}
				}
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
			console.log(response.data)
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

					// Hiển thị thông báo thành công
					$.toast({
						heading: 'Xóa đơn hàng thành công',
						text: 'Đơn hàng đã được xóa.',
						position: 'top-right',
						icon: 'success'
					});
				}, function(error) {
					// Xử lý lỗi khi xóa không thành công
					$.toast({
						heading: 'Warning',
						text: 'Đã có lỗi xảy ra khi xóa sản phẩm.',
						icon: 'warning'
					});
				});
			}
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

	// xuất exel
	$scope.ExcelExporter = function() {
		// Create a clean copy of the top10Order array
		var cleanData = angular.copy($scope.top10Order);

		// Remove the $$hashKey property from the cleanData array
		cleanData.forEach(function(item) {
			delete item.$$hashKey;
		});

		// Create a new workbook
		var workbook = XLSX.utils.book_new();

		// Create a new worksheet from the clean data
		var worksheet = XLSX.utils.json_to_sheet(cleanData);

		// Define the column headers
		var header = ["Mã đơn hàng", "Đã Bán", "Giá Tiền", "Sản phẩm", "Ngày ra hóa đơn"];

		// Set the column headers in the worksheet
		var headerRowIndex = 0;
		var headerColumnIndex = 0;
		for (var i = 0; i < header.length; i++) {
			var cellAddress = XLSX.utils.encode_cell({ r: headerRowIndex, c: headerColumnIndex });
			worksheet[cellAddress] = { v: header[i] };
			headerColumnIndex++;
		}

		// Add the invoice date at the end of each row
		var invoiceDateColumnIndex = header.indexOf("Ngày ra hóa đơn");
		var currentDate = new Date();
		var invoiceDate = currentDate.toISOString().slice(0, 10);
		var dataRowIndex = 1; // Start from the first data row (excluding header)
		cleanData.forEach(function(item) {
			var cellAddress = XLSX.utils.encode_cell({ r: dataRowIndex, c: invoiceDateColumnIndex });
			worksheet[cellAddress] = { v: invoiceDate };
			dataRowIndex++;
		});

		// Auto-adjust column width for better visibility
		var columns = Object.keys(worksheet);
		var maxLengths = {};
		columns.forEach(function(column) {
			var columnLength = column.length;
			if (maxLengths[columnLength] === undefined || columnLength > maxLengths[columnLength]) {
				maxLengths[columnLength] = columnLength;
			}
		});
		Object.keys(maxLengths).forEach(function(length) {
			var width = { wch: maxLengths[length] + 2 };
			var colRef = XLSX.utils.decode_range(worksheet['!ref']).e.c;
			for (var i = 0; i <= colRef; i++) {
				var column = XLSX.utils.encode_col(i);
				worksheet[column + '1'] = Object.assign(worksheet[column + '1'], width);
			}
		});

		// Append the worksheet to the workbook
		XLSX.utils.book_append_sheet(workbook, worksheet, "Top 10 Order");

		// Export the workbook to an Excel file
		var today = new Date();
		var fileName = "Top Sản Phẩm Bán Chạy_" + today.toISOString().slice(0, 10) + ".xlsx";
		XLSX.writeFile(workbook, fileName);
	};



	// lấy danh sách các người mua nhiều nhất
	$scope.loadUserByOder = []
	$scope.loadTopUser = function() {
		$http.get("/api/getTopUserByOder")
			.then(function(response) {
				console.log(response.data)

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


	$scope.postCmt = function() {
		var comment = $scope.comment;
		var rating = $scope.rating;

		var pid = $scope.selectedProduct.id; // Đọc giá trị selectedProduct.id
		console.log(pid);

		var detailsId = $scope.selectedProduct.detailsId;
		var images = $('#imageInput')[0].files;

		var formData = new FormData();

		for (var i = 0; i < images.length; i++) {
			var file = images[i];

			// Lưu trữ file ảnh
			formData.append('images[]', file, file.name);
		}
		formData.append('comment', comment);
		formData.append('ProductId', pid);
		formData.append('detailsId', detailsId);
		formData.append('rating', rating);


		$.ajax({
			url: "/api/postComment",
			method: "POST",
			data: formData,
			contentType: false,
			processData: false,
			success: function(response) {
				console.log("Success:", response);
				$('#reviewModal').modal('hide'); // Đóng modal
				$scope.loadData($scope.information);

				// Hiển thị thông báo xác nhận thành công
				Swal.fire({
					position: 'center',
					icon: 'success',
					title: 'Cảm Ơn Bạn Đã Đánh Giá',
					showConfirmButton: false,
					timer: 1500
				});
				document.getElementById("myForm").reset();


			},
			error: function(xhr, status, error) {
				var errorMessage = xhr.responseJSON.message;
				if (xhr.status === 400) {
					errorMessage = "Đã có lỗi xảy ra, không thể xác nhận đơn hàng.";
				}

				// Hiển thị thông báo lỗi
				Swal.fire({
					icon: 'error',
					title: 'Đã Có Lỗi Xảy Ra',
					text: 'Đánh Giá Thất Bại',

				})
			}
		});
	};

	$scope.testPlugin = function() {
		$.toast({
			heading: 'Xác nhận thành công',
			text: 'Đơn hàng đã được xác nhận.',
			position: 'top-right',
			icon: 'success'
		});
	}

	$scope.information = 'pending';
	// phía ng dùng
	$scope.loadData = function(type) {

		$scope.information = type;
		if (type === 'cancel') {
			$scope.classify = false;
		} else {
			$scope.classify = true;
		}
		$http.get('/api/OrderStatus?type=' + type)
			.then(function(response) {
				response.data.reverse(); // Đảo ngược mảng response.data
				$scope.data = response.data;

				$scope.selectedProduct = response.data
			})
			.catch(function(error) {
				console.error('Error:', error);
			});
	};


	$scope.loadData($scope.information);

	$scope.openModal = function(item, itemAll) {

		if (itemAll === 'itemAll') {
			$scope.selectedProduct = item;
		}

		if (itemAll === 'itemCofirmed') {
			$scope.selectedProduct = item;
		}

		if (itemAll === 'cancel') {
			$scope.selectedProduct = item;
		}
	};


	/////////end///////////


	//////////////// phía admin
	$scope.details = [];
	$scope.selectType = "DangCho";
	$scope.countDay = 0;

	$scope.loadDetails = function(type) {
		$http.get('/api/getOrderDetails?type=' + type)
			.then(function(response) {
				$scope.details = response.data;
				$scope.details.reverse(); // Đảo ngược thứ tự của mảng details để lấy đơn hàng mới
				console.log(response.data);
				var currentDate = moment().format('YYYY-MM-DD HH:mm:ss');
				for (var i = 0; i < response.data.length; i++) {
					var createdDate = moment(response.data[i].created_at);
					if (createdDate.isSame(currentDate, 'day')) {
						$scope.countDay++;
						console.log($scope.details)
					}
				}

				// Nhiều điều kiện trong toán tử 3 ngôi
				$scope.status =
					($scope.selectType == 'DangCho') ? "Đang Chờ" :
						($scope.selectType == 'ChuanBi') ? "Chuẩn Bị Hàng" :
							($scope.selectType == 'BanGiaoVanChuyen') ? "Đã Bàn Giao Cho Vận Chuyển" :
								($scope.selectType == 'HuyDon') ? "Đơn Đã Bị Hủy" :
									($scope.selectType == 'HoanThanh') ? "Đơn Hàng Thành Công" : "";

			})
			.catch(function(error) {
				console.error('Error:', error);
			});
	};

	$scope.loadDetails($scope.selectType); // Gọi hàm loadDetails với type ban đầu


	/////////////////////////////////////

	$scope.confirm = function(id) {
		// Hiển thị hộp thoại xác nhận
		Swal.fire({
			title: 'Xác nhận',
			text: 'Bạn có muốn xác nhận đơn hàng này đã hoàn thành?',
			icon: 'info',
			showCancelButton: true,
			confirmButtonText: 'Yes',
			cancelButtonText: 'No'
		}).then((result) => {
			if (result.isConfirmed) {
				// Thực hiện ajax call khi người dùng chọn "Yes"
				$.ajax({
					url: "/order_confim",
					type: "POST",
					data: {
						Oid: id,
						Action: "confirm" // Thay đổi giá trị action tại đây nếu cần
					},
					success: function(data) {
						console.log(data)

						$scope.loadData($scope.selectedOption); // Gọi lại hàm loadData với selectedOption hiện tại
						$scope.loadDetails($scope.selectType);

						// Hiển thị thông báo xác nhận thành công
						$.toast({
							heading: 'Xác nhận thành công',
							text: 'Đơn hàng đã được xác nhận.',
							position: 'top-right',
							icon: 'success'
						});
					},
					error: function(xhr, status, error) {
						var errorMessage = xhr.responseJSON.message
						if (xhr.status === 400) {
							errorMessage = "Đã có lỗi xảy ra, không thể xác nhận đơn hàng.";
						}

						// Hiển thị thông báo lỗi
						$.toast({
							heading: 'ERROR',
							text: errorMessage,
							position: 'top-right',
							icon: 'error'
						});
					}
				});
			}
		});
	};

	$scope.update_status = function(orderId) {
		// Lấy giá trị đã chọn từ biến selectedStatus
		var selectedStatus = $scope.selectedStatus;
		// Thực hiện các xử lý cập nhật thông tin đơn hàng tại đây, sử dụng selectedStatus và orderId

		// Ví dụ:
		console.log("Mã đơn hàng:", orderId);
		console.log("Trạng thái đã chọn:", selectedStatus);

		$.ajax({
			url: "/api/update_status",
			type: "POST",
			data: {
				orderId: orderId,
				selectedStatus: selectedStatus,
			},
			success: function(data) {
				$scope.loadDetails($scope.selectType);
				$scope.loadData($scope.information);

			},
			error: function(xhr, status, error) {
				console.log(error);
			}
		});
	};



	$scope.cancel = function(id, cancel) {
		// Hiển thị hộp thoại xác nhận
		Swal.fire({
			title: 'Hủy đơn hàng',
			text: 'Bạn có muốn hủy đơn hàng này?',
			icon: 'info',
			showCancelButton: true,
			confirmButtonText: 'Yes',
			cancelButtonText: 'No'
		}).then((result) => {
			if (result.isConfirmed) {
				// Thực hiện ajax call khi người dùng chọn "Yes"
				$.ajax({
					url: "/cancel_order",
					type: "POST",
					data: {
						Oid: id,
						Action: "cancel",
						cancelBy: cancel
						// Thay đổi giá trị action tại đây nếu cần
					},
					success: function(data) {
						$scope.loadDetails($scope.selectType);
						$scope.loadData($scope.information);

						// Hiển thị thông báo hủy đơn hàng thành công
						$.toast({
							heading: 'Hủy Đơn Hàng thành công',
							text: 'Đơn hàng đã được hủy.',
							position: 'top-right',
							icon: 'success'
						});
					},
					error: function(xhr, status, error) {
						// Xử lý lỗi nếu có
						console.log(error);
					}
				});
			}
		});
	};
	$scope.finById = function(id, name, img, detailsId) {
		$scope.selectedProduct = {
			id: id,
			name: name,
			img: img,
			detailsId: detailsId
		};


	};

}]);
