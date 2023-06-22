function myPage(price) {
	$.ajax({
		type: 'GET',
		url: '/PriceFilter',
		data: { price },
		success: function(response) {
			$('html').html(response)
		},
		error: function(xhr, status, error) {
			console.log(error);
			console.log(status);
		}
	});
}