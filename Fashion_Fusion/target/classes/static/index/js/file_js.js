
			var swiper = new Swiper('.swiper-container', {
				slidesPerView : 'auto', // Số lượng slide hiển thị trên mỗi view
				spaceBetween : 20, // Khoảng cách giữa các slide
				navigation : {
					nextEl : '.swiper-button-next', // Selector của nút điều hướng slide tiếp theo
					prevEl : '.swiper-button-prev', // Selector của nút điều hướng slide trước đó
				},
			});
			
			window.addEventListener('load', function() {
				  var adContainer = document.getElementById('adContainer');
				  var modalOverlay = document.getElementById('modalOverlay');
				  var closeModalButton = document.getElementById('closeModalButton');

				  // Kiểm tra giá trị đã lưu trong sessionStorage
				  var randomCount = sessionStorage.getItem('randomCount');
				  if (randomCount === null) {
				    // Lần đầu tiên mở tab, set randomCount = 0
				    randomCount = 0;
				  } else {
				    // Đã có giá trị randomCount được lưu, chuyển đổi thành số nguyên
				    randomCount = parseInt(randomCount);
				  }

				  // Xử lý sự kiện click nút đóng
				  closeModalButton.addEventListener('click', function() {
				    modalOverlay.style.display = 'none'; // Ẩn modal khi nút được nhấp
				  });

				  // Thay đổi ảnh ngẫu nhiên
				  function changeRandomImage() {
				    if (randomCount < 4) {
				      var bannerImage = document.getElementById("bannerImage");
 
				      // Tạo số ngẫu nhiên từ 0 đến độ dài của mảng ảnh
				      var randomIndex = Math.floor(Math.random() * images.length);

				      // Thay đổi src của hình ảnh
				      bannerImage.src = images[randomIndex];

				      randomCount++; // Tăng biến đếm sau mỗi lần random
				      document.getElementById("closeModalButton").innerHTML = 'X'
				    }

				    // Lưu giá trị của biến randomCount vào sessionStorage
				    sessionStorage.setItem('randomCount', randomCount.toString());

				    // Kiểm tra nếu đã đạt đủ 3 lần ngẫu nhiên, ẩn modal và ẩn nút "X"
				    if (randomCount >= 3) {
				      modalOverlay.style.display = 'none';
				      closeModalButton.style.display = 'none';
				    }
				  }

				  // Gọi hàm changeRandomImage để thay đổi ảnh ngẫu nhiên khi trang được tải
				  changeRandomImage();

				  setTimeout(function() {
				    adContainer.style.display = 'none';
				  }, 50000); // Thời gian hiển thị quảng cáo (tính bằng mili giây)
				});

				// Mảng chứa các đường dẫn ảnh
				var images = ["banner/b7.jpg", "banner/b2.jpg", "banner/b3.jpg",
				  "banner/b4.jpg", "banner/b5.jpg", "banner/b6.png"];

