$(document).ready(function() {
	getProduct()
})

function getProduct() {
	$.ajax({
		url: '/page',
		success: function(response) {
		},
		error: function(xhr, status, error) {
			//$("#container_products").html(response);
		}
	});
}


function myPage(page) {
	$.ajax({
		url: "/PriceFilter",
		type: "GET",
		data: {
			price: page,
		},
		success: function(data) {
			getProduct()
		},
	});
}