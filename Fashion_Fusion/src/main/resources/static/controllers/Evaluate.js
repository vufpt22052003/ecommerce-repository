/*$(document).ready(function() {
	getCmt();
});

function getCmt() {
	var pid = $('input[name="pid"]').val();
	$.getJSON("/getComment", { ProductId: pid })
		.done(function(response) {
			console.log("Success:", response);

			// Ví dụ: Thêm dữ liệu vào giao diện
			var commentContainer = $(".comment-container");
			commentContainer.empty(); // Xóa dữ liệu cũ (nếu cần)

			response.forEach(function(comment) {
				var commentBox = $('<div class="comment-box row"></div>');
				var avtCol = $('<div class="col-2 mghji"></div>');
				var detailCol = $('<div class="col-10 detaila"></div>');

				// Avatar image
				var avatarImg;
				if (comment.uid.avt) {
					avatarImg = $('<img src="' + '/images/' + comment.uid.avt + '" alt="avatar" class="avatar-img">');
				} else {
					avatarImg = $('<img src="https://i.pinimg.com/originals/c6/e5/65/c6e56503cfdd87da299f72dc416023d4.jpg" alt="" class="avatar-img">');
				}
				avtCol.append(avatarImg);

				// Comment name
				var nameElement = $('<h6 class="comment-name">' + comment.uid.us + '</h6>');
				detailCol.append(nameElement);

				// Comment date
				var dateElement = $('<small class="comment-date">' + comment.create_date + '</small>');
				detailCol.append(dateElement);

				// Comment content
				var commentElement = $('<p class="comment-content">' + comment.contents + '</p>');
				detailCol.append(commentElement);

				// Add columns to comment box
				commentBox.append(avtCol);
				commentBox.append(detailCol);

				// Add comment box to comment container
				commentContainer.append(commentBox);
			});


		})
		.fail(function(xhr, status, error) {
			console.log("Errors:", error);
		});
}
*/


var app = angular.module('AppEvaluate', []);


// lấy đánh giá bên admin
app.controller('EvaluateController', ['$http', '$scope', '$rootScope', function($http, $scope, $rootScope) {
	$scope.data = []
	$scope.selectType = "all";
	$scope.listCmtByUs = function(type) {
		$http.get("/api/getCmtByUserId?type=" + type)
			.then(function(response) {
				//response.data.reverse(); // Đảo ngược mảng response.data	
				$scope.data = response.data
				//$scope.selectType = type
				console.log(response.data);
			})
			.catch(function(error) {
				// Xử lý lỗi nếu có
				console.log(error);
			});
	}
	$scope.listCmtByUs($scope.selectType);

	

	$scope.delCmt = function(id) {
		Swal.fire({
			title: 'Thông báo',
			text: 'Bạn có muốn xóa bình luận này không?',
			icon: 'info',
			showCancelButton: true,
			confirmButtonText: 'Yes',
			cancelButtonText: 'No'
		}).then((result) => {
			if (result.isConfirmed) {
				$http({
					method: 'GET',
					url: '/api/deleteCmt',
					params: { id: id }
				}).then(function(response) {
					$scope.listCmtByUs($scope.selectType);

					// Hiển thị thông báo thành công
					$.toast({
						heading: 'Xóa bình luận thành công',
						text: 'Bình luận đã được xóa.',
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
}]);
