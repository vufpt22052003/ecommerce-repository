$(document).ready(function() {
	getProduct()
})

function getProduct() {
	$.ajax({
		url: '/page',
		success: function(response) {
			//$('html').html(response)
		},
		error: function(xhr, status, error) {
			//$("#container_products").html(response);
		}
	});
}


function myPage(page) {
	var url = "/PriceFilter/0".replace(/\/0$/, "/" + page);

	$.ajax({
		url: "/PriceFilter",
		type: "GET",
		data: {
			price: page,
		},
		success: function(response) {
			// Tạo một phần tử div ảo để chứa dữ liệu phản hồi
			var tempDiv = document.createElement('div');
			tempDiv.innerHTML = response;

			// Lấy nội dung của phần tử có class "col-lg-9 col-md-7" từ dữ liệu phản hồi
			var contentHtml = $(tempDiv).find('.col-lg-9.col-md-7').html();

			// Đặt nội dung của thẻ div có id "sss" bằng nội dung lấy được
			$('#col-lg-9').html(contentHtml);
			var url = "/PriceFilter/0?price=" + page;

			// Chuyển hướng trang đến URL mới
			window.location.href = url;
		},
		error: function(xhr, status, error) {
			// Xử lý lỗi nếu cần thiết
			console.log(error);
		}
	});
}

function validateNumberInput(input) {
	var value = input.value;

	// Xóa các ký tự không hợp lệ khỏi input (chỉ giữ lại số và dấu chấm)
	var cleanedValue = value.replace(/[^\d.]/g, '');

	// Chuyển giá trị đã làm sạch thành số
	var numericValue = parseFloat(cleanedValue);

	// Kiểm tra xem giá trị có phải là số hợp lệ hay không
	if (!isNaN(numericValue)) {
		// Định dạng giá trị thành chuỗi có dấu phẩy ngăn cách hàng nghìn
		var formattedValue = numericValue.toLocaleString('en');
		// Đặt giá trị mới vào ô input
		input.value = formattedValue;
	} else {
		// Nếu không phải số hợp lệ, xóa toàn bộ giá trị trong ô input
		input.value = '';
	}
}

function handleBackspace(event, input) {
	// Kiểm tra xem nút được nhấn có phải là nút "xóa back" (mã phím 8) hay không
	if (event.keyCode === 8) {
		// Xóa các ký tự không hợp lệ khỏi giá trị nhập vào, chỉ giữ lại số và dấu chấm
		var cleanedValue = input.value.replace(/[^\d.]/g, '');
		// Đặt giá trị mới sau khi xóa các ký tự không hợp lệ vào ô input
		input.value = cleanedValue;
	}
}

// chọn khoảng giá
function PriceRange(price) {
	$.ajax({
		url: "/PriceLimit",
		type: "GET",
		data: {
			price: price,

		},
		success: function(response) {
			// Tạo một phần tử div ảo để chứa dữ liệu phản hồi
			var tempDiv = document.createElement('div');
			tempDiv.innerHTML = response;

			// Lấy nội dung của phần tử có class "col-lg-9 col-md-7" từ dữ liệu phản hồi
			var contentHtml = $(tempDiv).find('.col-lg-9.col-md-7').html();

			// Đặt nội dung của thẻ div có id "sss" bằng nội dung lấy được
			$('#col-lg-9').html(contentHtml);
			var url = "/PriceLimit/0?price=" + price;

			// Chuyển hướng trang đến URL mới
			window.location.href = url;
		},
		error: function(xhr, status, error) {
			// Xử lý lỗi nếu cần thiết
			console.log(error);
		}
	});
}