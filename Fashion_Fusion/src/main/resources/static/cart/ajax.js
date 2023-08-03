$(document).ready(function() {
	getCart();
});
function getCart() {
	$.ajax({
		url: "/cart/api",
		type: "GET",
		dataType: "json",
		success: function(response) {
			response.reverse();
			var cartList = response;
			var table = $("#table_cart");

			// Tạo tbody và gán vào table
			var tbody = $("<tbody>").attr("id", "myTbody");

			table.append(tbody);

			// Thêm các dòng thẻ <tr> chứa các thẻ <th>
			var headerRow = $("<tr>");
			headerRow.append($("<th></th>"));
			var cellProduct = $("<th>").html('Product')
			headerRow.append(cellProduct);
			headerRow.append($("<th>Số Lượng</th>"));
			headerRow.append($("<th>Total</th>"));
			tbody.append(headerRow);
			tbody.empty(); // Xóa dữ liệu cũ trong tbody

			// Thêm dữ liệu vào bảng
			for (var i = 0; i < cartList.length; i++) {
				var cartItem = cartList[i];

				// Tạo một dòng mới trong tbody
				var newRow = $("<tr>");

				// Thêm các ô dữ liệu vào dòng
				var cell1 = $("<td>").html(`
<input type="checkbox" class="checkPrice" name="check[]" id="myCheckbox" onclick="checkPrice()" value="${cartItem.product_id.id}" data-cart-id="${cartItem.id}">
					<input id="checkPrices" hidden value="${cartItem.product_id.price * cartItem.quantity}">
				`);
				var cell2 = $("<td>").html(`
					<td class="product__cart__item">
						<div class="product__cart__item__pic">
							<img src="images/${cartItem.product_id.img}" class="contain" alt="" style="height:155px;width:120px;object-fit: contain;margin-left: 20px;">
						</div>
						<div class="product__cart__item__text">
							<h6>${cartItem.product_id.name}</h6>
							<h5>$${cartItem.product_id.price.toFixed(2)}</h5>
							<span > ${cartItem.color != null ? 'Màu : ' + cartItem.color : ''}</span>
							<br />
							<span> ${cartItem.size != null ? 'Kích Cở ' + cartItem.size : ''}<span>
							<input type="hidden" value="${cartItem.size}" name="size"/>
							<input type="hidden" value="${cartItem.color}" name="color"/>
						</div>
					</td>
				`);

				var cell3 = $("<td>").html(`
					<td class="quantity__item">
						<div class="quantity">
							<div class="pro-qty-2">
								<input type="number" value="${cartItem.quantity}" data-cart-id="${cartItem.id}" onclick="getQuantityValue(this)">
							</div>
						</div>
					</td>
				`);

				var cell4 = $("<td>").text(`$ ${(cartItem.product_id.price * cartItem.quantity).toFixed(2)}`);
				var cell6 = $("<td>").text(`$ ${(cartItem.product_id.price * cartItem.quantity).toFixed(2)}`);

				var cell5 = $("<td>").html(`
					<td class="cart__close">
						<i onclick="del(${cartItem.id})" class="fa fa-close"></i>
					</td>
				`);


				newRow.append(cell1, cell2, cell3, cell4, cell5);

				// Thêm dòng vào tbody
				tbody.append(newRow);
			}
			table.find("tbody").replaceWith(tbody);
		},
		error: function(xhr, status, error) {
			console.log(error);
		}
	});
}

// check giá tiềnvar totalCount = 0; // Biến toàn cục

function checkPrice() {
	var checkboxes = document.querySelectorAll('input[name="check[]"]:checked');
	var totalPrice = 0;
	var count = 0;
	var exits = false;
	for (var i = 0; i < checkboxes.length; i++) {
		var checkbox = checkboxes[i];
		var price = parseFloat(checkbox.nextElementSibling.value);
		totalPrice += price;
		count++;
		exits = true;
	}
	totalCount = count; // Gán giá trị count vào biến toàn cục totalCount

	if (exits) {
		document.getElementById("totalPrice").innerHTML = "Tổng Thanh Toán (" + count + ") Sản Phẩm ₫" + totalPrice.toFixed(2);
		document.getElementById("delCart").innerHTML = "Xóa (" + count + ") Sản Phẩm ";
		document.getElementById("btnDelCart").style.display = "block";
	} else {
		document.getElementById("btnDelCart").style.display = "none";
		uncheckAll()
	}
}


// reset bỏ chọn các ô
function update_cart() {
	var checkboxes = document.querySelectorAll(".checkPrice");
	var checkAll = document.querySelector(".textAll");
	var checkedCheckbox = document.getElementById("checked");

	for (var i = 0; i < checkboxes.length; i++) {
		checkboxes[i].checked = false;
	}

	if (checkAll) {
		checkAll.checked = false;
	}

	if (checkedCheckbox) {
		checkedCheckbox.checked = false;
	}

	checkPrice();
}


// chuyển qua trang mua hàng
function Purchase(event) {
	var checkboxes = document.querySelectorAll(".checkPrice");
	var uncheckedCount = 0;

	for (var i = 0; i < checkboxes.length; i++) {
		if (!checkboxes[i].checked) {
			uncheckedCount++;
		}
	}

	if (uncheckedCount === checkboxes.length) {
		alert("Vui Lòng Chọn Sản Phẩm Muốn Mua");
		event.preventDefault(); // Ngăn chặn sự kiện mặc định của form
	}
}


//chọn tất cả
function checkAll() {
	var checkboxes = document.querySelectorAll(".checkPrice");
	for (var i = 0; i < checkboxes.length; i++) {
		checkboxes[i].checked = true;
		checkPrice()
		count = checkboxes.length;
	}
	totalCount = count; // Gán giá trị count vào biến toàn cục totalCount

	document.getElementById("textAll").innerHTML = "Chọn Tất Cả" + " (" + count + ") Sản Phẩm";
}

function uncheckAll() {
	var checkboxes = document.querySelectorAll(".checkPrice");

	for (var i = 0; i < checkboxes.length; i++) {
		checkboxes[i].checked = false;
	}
	document.getElementById("totalPrice").innerHTML = "Tổng thanh toán (0 Sản phẩm):    ₫0"

	document.getElementById("textAll").innerHTML = "Chọn Tất Cả"
}


// ấn vào checkbox bên button chọn tất cả để chọn các ô checkbox
function check() {
	var checkbox = document.getElementById("checked");
	if (checkbox.checked) {
		checkAll()
		checkPrice()
	} else {
		uncheckAll()
		checkPrice()
	}
}


// đếm số lượng sản phẩm mua
function getQuantityValue(number) {
	//var Cid = document.getElementById("Cid").value;
	var quantityValue = number.value;
	var cartId = number.getAttribute("data-cart-id");
	console.log(cartId);

	$.ajax({
		url: "/updateNumber",
		type: "POST",
		data: {
			Cid: cartId,
			quantityValue: quantityValue,
		},
		success: function(data) {

		},
	});
}

//xóa theo id cart
function del(id) {
	$.ajax({
		url: "/delCmt",
		type: "POST",
		data: {
			id: id,
		},
		success: function(data) {
			getCart();
		},
	});
}


function deleteSelected() {
	Swal.fire({
		title: 'Xóa Sản Phẩm Trong Giỏ Hàng ?',
		text: "Xóa " + totalCount + " Sản Phẩm Có Trong Giỏ Hàng", // Biến toàn cục
		icon: 'warning',
		showCancelButton: true,
		confirmButtonColor: '#3085d6',
		cancelButtonColor: '#d33',
		confirmButtonText: 'Yes, delete it!'
	}).then((result) => {
		if (result.isConfirmed) {
			Swal.fire(
				'Deleted!',
				'Your file has been deleted.',
				'success'
			).then(() => {
				var checkboxes = document.querySelectorAll('input.checkPrice[name="check[]"]:checked');
				var selectedIds = [];

				var tbodys = document.getElementById("myTbody");

				for (var i = 0; i < checkboxes.length; i++) {
					var checkbox = checkboxes[i];
					var id = checkbox.value;
					var cartId = checkbox.getAttribute('data-cart-id'); // Lấy giá trị ID cart từ thuộc tính data
					selectedIds.push(cartId);
				}

				$.ajax({
					url: "/deleteSelected",
					type: "POST",

					data: {
						selectedIds: selectedIds,
					},
					success: function(response) {
						getCart();
					},
					error: function(xhr, status, error) {
						console.log(error);
					}
				});
			});
		}
	});
}


