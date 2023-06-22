// xử lý đơn hàng
$(document).ready(function() {
	getListOrder();
	getConfimOrder();
	getCancelOrder()
});

function getListOrder() {
	$.ajax({
		url: "/api/getOrderDetails",
		type: "GET",
		data: {
			type: "all"
		},
		dataType: "json",
		success: function(response) {
			var listDetails = response;
			var table = $("#your-table-id");

			// Create a new tbody element
			var tbody = $("<tbody>").attr("id", "myTbody").css({
				"border": "1px solid rgba(0, 0, 0, .125)",
				"margin-bottom": "10px"
			});

			// Add data rows to the tbody
			for (var i = 0; i < listDetails.length; i++) {
				var dt = listDetails[i];
				var newRow = $("<tr>");
				newRow.append($("<td>").html('<b>' + dt.id + '</b > '));
				newRow.append($("<td>").text(dt.order_id.user_id.us));
				newRow.append($("<td>").text(dt.product_id.name));
				newRow.append($("<td>").text(dt.quantity));
				newRow.append($("<td>").text(dt.price * dt.quantity));
				newRow.append($("<td>").text(dt.created_at));
				newRow.append(
					$("<td>").html(`<button onclick="confim(${dt.id})">Xác Nhận</button><button onclick="cancel_order(${dt.id})">Hủy Đơn</button>`)
				);

				tbody.append(newRow);
			}

			// Replace the existing tbody with the new one
			table.find("tbody").replaceWith(tbody);
		},
		error: function(xhr, status, error) {
			console.log(error);
		}
	});
}

// xac nhan đơn hàng do admin làm
function confim(id) {
	$.ajax({
		url: "/order_confim",
		type: "POST",
		data: {
			Oid: id,
			Action: "confirm" // Thay đổi giá trị action tại đây
		},
		success: function(data) {
			getListOrder();
			getConfimOrder()
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


// Hủy nhận đơn hàng do admin làm
function cancel_order(id) {
	$.ajax({
		url: "/cancel_order",
		type: "POST",
		data: {
			Oid: id,
			Action: "cancel" // Thay đổi giá trị action tại đây
		},
		success: function(data) {
			getListOrder();
			getCancelOrder()
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


// Lấy danh sách đã thành công
function getConfimOrder() {
	$.ajax({
		url: "/api/getOrderDetails",
		type: "GET",
		data: {
			type: "confirmed"
		},
		dataType: "json",
		success: function(response) {
			var listDetails = response;
			var table = $("#table_confim");

			// Create a new tbody element
			var tbody = $("<tbody>").attr("id", "myTbody");



			// Add data rows to the tbody
			for (var i = 0; i < listDetails.length; i++) {
				var dt = listDetails[i];
				var newRow = $("<tr>");
				newRow.append($("<td>").html('<b>' + dt.id + '</b > '));
				newRow.append($("<td>").text(dt.order_id.user_id.us));
				newRow.append($("<td>").text(dt.product_id.name));
				newRow.append($("<td>").text(dt.quantity));
				newRow.append($("<td>").text(dt.price * dt.quantity));
				newRow.append($("<td>").text(dt.created_at));
				newRow.append($("<td>").text('Đã Xác Nhận'));

				tbody.append(newRow);
			}

			// Replace the existing tbody with the new one
			table.find("tbody").replaceWith(tbody);
		},
	})
}

// Lấy danh sách hủy thành công
function getCancelOrder() {
	$.ajax({
		url: "/api/getOrderDetails",
		type: "GET",
		data: {
			type: "cancelled"
		},
		dataType: "json",
		success: function(response) {
			var listDetails = response;
			var table = $("#table_cancel");

			// Create a new tbody element
			var tbody = $("<tbody>").attr("id", "myTbody");



			// Add data rows to the tbody
			for (var i = 0; i < listDetails.length; i++) {
				var dt = listDetails[i];
				var newRow = $("<tr>");
				newRow.append($("<td>").html('<b>' + dt.id + '</b > '));
				newRow.append($("<td>").text(dt.order_id.user_id.us));
				newRow.append($("<td>").text(dt.product_id.name));
				newRow.append($("<td>").text(dt.quantity));
				newRow.append($("<td>").text(dt.price * dt.quantity));
				newRow.append($("<td>").text(dt.created_at));
				newRow.append($("<td>").text('Đơn Đã Bị Hủy'));

				tbody.append(newRow);
			}

			// Replace the existing tbody with the new one
			table.find("tbody").replaceWith(tbody);
		},
	})
}