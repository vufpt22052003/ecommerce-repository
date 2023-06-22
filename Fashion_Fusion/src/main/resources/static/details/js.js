/**
 * 
 */

				$('.tabs-title li').on('click', function (e) {
					$('.tabs-title li, .tab-content').removeClass('current');
					$(this).addClass('current');
					$('#' + $(this).data('tab') + '').addClass('current');

					var active = $(this);
					var left = active.position().left;
					var currScroll = $(this).parent('.tabs-title').scrollLeft();
					var contWidth = $(this).parent('.tabs-title').width() / 2;
					var activeOuterWidth = active.outerWidth() / 2;
					left = left + currScroll - contWidth + activeOuterWidth;

					$(this).parent('.tabs-title').animate({
						scrollLeft: left
					}, 'slow');
				})

				function scrollToxx() {
					if ($('.tab-review-c').length > 0) {
						$('html, body').animate({ scrollTop: $('.product-tab.e-tabs').offset().top }, 'slow');
						$('.tabs-title li, .tab-content').removeClass('current');
						$('#tab-review, .tab-review-c').addClass('current');
						return false;
					} else {
						alert('Cửa hàng chưa bật sử dụng tab reviews trong tùy chỉnh giao diện');
					}
				}

				setTimeout(function () {
					var ch = $('.product_getcontent').height(),
						smore = $('.show-more');
					if (ch > 550) {
						$('.ba-text-fpt').addClass('has-height');
						smore.removeClass('d-none');
					}
				}, 200);

				$('.btn--view-more').on('click', function (e) {
					e.preventDefault();
					var $this = $(this);
					$this.parents('.tab-content').find('.product_getcontent').toggleClass('expanded');
					$('html, body').animate({ scrollTop: $('.product_getcontent').offset().top - 110 }, 'slow');
					$(this).toggleClass('active');
					return false;
				});

				var galleryThumbs = new Swiper('.product-image-detail .gallery-thumbs', {
					spaceBetween: 10,
					slidesPerView: 5,
					freeMode: true,
					lazy: true,
					watchSlidesVisibility: true,
					watchSlidesProgress: true,
					hashNavigation: true,
					slideToClickedSlide: true,
					navigation: {
						nextEl: '.product-image-detail .swiper-button-next',
						prevEl: '.product-image-detail .swiper-button-prev',
					},
					breakpoints: {
						300: {
							slidesPerView: 4,
							spaceBetween: 10,
						},
						500: {
							slidesPerView: 4,
							spaceBetween: 10,
						},
						640: {
							slidesPerView: 5,
							spaceBetween: 10,
						},
						768: {
							slidesPerView: 6,
							spaceBetween: 10,
						},
						1024: {
							slidesPerView: 5,
							spaceBetween: 10,
						},
					}
				});
				var galleryTop = new Swiper('.product-image-detail .gallery-top', {
					spaceBetween: 10,
					lazy: true,
					freeMode: true,
					hashNavigation: true,
					thumbs: {
						swiper: galleryThumbs
					}
				});
				var galleryRelated = new Swiper('.swiper_related', {
					slidesPerView: 5,
					spaceBetween: 20,
					slidesPerGroup: 2,
					slideToClickedSlide: false,
					navigation: {
						nextEl: '.sliderelated .swiper-button-next',
						prevEl: '.sliderelated .swiper-button-prev',
					},
					breakpoints: {
						300: {
							slidesPerView: 2,
							slidesPerGroup: 2,
							spaceBetween: 10
						},
						640: {
							slidesPerView: 3,
							slidesPerGroup: 2,
							spaceBetween: 10
						},
						768: {
							slidesPerView: 4,
							slidesPerGroup: 2,
							spaceBetween: 10
						},
						1024: {
							slidesPerView: 4,
							slidesPerGroup: 2,
							spaceBetween: 10
						},
						1200: {
							slidesPerView: 5,
							slidesPerGroup: 2,
							spaceBetween: 20
						}
					}
				});

				var formProduct = $('.form-product');

				var ww = $(window).width();
				/*For recent product*/

				var variantsize = false;
				var alias = "ao-thun-be-gai-cotton-hinh-con-voi";

				var productOptionsSize = 1;
				/*end*/

				function validate(evt) {
					var theEvent = evt || window.event;
					var key = theEvent.keyCode || theEvent.which;
					key = String.fromCharCode(key);
					var regex = /[0-9]|\./;
					if (!regex.test(key)) {
						theEvent.returnValue = false;
						if (theEvent.preventDefault) theEvent.preventDefault();
					}
				}

				var selectCallback = function (variant, selector) {
					if (variant) {
						var form = jQuery('#' + selector.domIdPrefix).closest('form');
						for (var i = 0, length = variant.options.length; i < length; i++) {
							var radioButton = form.find('.swatch[data-option-index="' + i + '"] :radio[value="' + variant.options[i] + '"]');
							if (radioButton.length) {
								radioButton.get(0).checked = true;
							}
						};
					};
					var addToCart = jQuery('.form-product .btn-cart'),
						btnNow = jQuery('.form-product .btn-buy-now'),
						form = jQuery('.form-product .from-action-addcart'),
						productPrice = jQuery('.details-pro .special-price .product-price'),
						qty = jQuery('.inventory_quantity .a-stock'),
						comparePrice = jQuery('.details-pro .old-price .product-price-old'),
						comparePriceText = jQuery('.details-pro .old-price'),
						savePrice = jQuery('.details-pro .save-price .product-price-save'),
						savePriceText = jQuery('.details-pro .save-price'),
						qtyBtn = jQuery('.form-product .custom-btn-number'),
						BtnSold = jQuery('.form-product .btn-mua'),
						Unit = jQuery('.donvitinh'),
						product_sku = jQuery('.variant-sku');
					if (variant && variant.sku != "" && variant.sku != null) {
						product_sku.html("Mã sản phẩm: <strong>" + variant.sku + " </strong>");
					} else {
						product_sku.html('Mã sản phẩm: <strong>Cập nhật...</strong>');
					};

					if (variant && variant.weight != '0') {
						$('.dvt').show();
						var unitformart = variant.weight.unit;
						if (unitformart === 'g') {
							var unitformat = 'Gram';
						} else {
							var unitformat = 'Kg';
						}
						Unit.html(variant.weight + "" + unitformat + "");
					} else {
						$('.dvt').hide();
					}

					if (variant && variant.available) {
						if (variant.inventory_management == "bizweb") {
							if (variant.inventory_quantity != 0) {
								qty.html('<span class="a-stock">Còn hàng</span>');
							} else if (variant.inventory_quantity == '') {
								if (variant.inventory_policy == "continue") {
									qty.html('<span class="a-stock">Còn hàng</span>');
								} else {
									qty.html('<span class="a-stock a-stock-out">Hết hàng</span>');
								}
							}
						} else {
							qty.html('<span class="a-stock">Còn hàng</span>');
						};
						form.removeClass('d-none');
						btnNow.removeAttr('disabled').removeClass('d-none');
						addToCart.html('<span><svg class="icon"> <use xmlns:xlink="http://www.w3.org/1999/xlink" xlink:href="#addcarticon"></use> </svg></span> Cho vào giỏ').removeAttr('disabled');
						BtnSold.removeClass('btnsold');
						qtyBtn.removeClass('d-none');
						if (variant.price == 0) {
							productPrice.html('Liên hệ');
							comparePrice.hide();
							savePrice.hide();
							comparePriceText.hide();
							savePriceText.hide();
							form.addClass('d-none');
						} else {
							form.removeClass('d-none');
							productPrice.html(Bizweb.formatMoney(variant.price, "{{amount_no_decimals_with_comma_separator}}₫"));
							addToCart.html('<span><svg class="icon"> <use xmlns:xlink="http://www.w3.org/1999/xlink" xlink:href="#addcarticon"></use> </svg></span>Cho vào giỏ');
							if (variant.compare_at_price > variant.price) {
								comparePrice.html(Bizweb.formatMoney(variant.compare_at_price, "{{amount_no_decimals_with_comma_separator}}₫")).show();
								savePrice.html(Bizweb.formatMoney(variant.compare_at_price - variant.price, "{{amount_no_decimals_with_comma_separator}}₫") + ' <span>so với thị trường</span>').show();
								comparePriceText.show();
								savePriceText.show();
							} else {
								comparePrice.hide();
								savePrice.hide();
								comparePriceText.hide();
								savePriceText.hide();
							}
						};
					} else {
						btnNow.attr('disabled', 'disabled').addClass('d-none');
						qty.html('<span class="a-stock a-stock-out">Hết hàng</span>');
						addToCart.html('Hết hàng').attr('disabled', 'disabled');
						BtnSold.addClass('btnsold');
						qtyBtn.addClass('d-none');
						if (variant) {
							if (variant.price != 0) {
								form.removeClass('d-none');
								productPrice.html(Bizweb.formatMoney(variant.price, "{{amount_no_decimals_with_comma_separator}}₫"));
								if (variant.compare_at_price > variant.price) {
									comparePrice.html(Bizweb.formatMoney(variant.compare_at_price, "{{amount_no_decimals_with_comma_separator}}₫")).show();
									savePrice.html(Bizweb.formatMoney(variant.compare_at_price - variant.price, "{{amount_no_decimals_with_comma_separator}}₫") + ' <span>so với thị trường</span>').show();
									comparePriceText.show();
									savePriceText.show();
								} else {
									comparePrice.hide();
									savePrice.hide();
									comparePriceText.hide();
									savePriceText.hide();
								}
							} else {
								productPrice.html('Liên hệ');
								comparePrice.hide();
								savePrice.hide();
								comparePriceText.hide();
								savePriceText.hide();
								form.addClass('d-none');
							};
						} else {
							productPrice.html('Liên hệ');
							comparePrice.hide();
							savePrice.hide();
							comparePriceText.hide();
							savePriceText.hide();
							form.addClass('d-none');
						};
					}
					/*begin variant image*/
					if (variant && variant.image) {
						var originalImage = jQuery(".product-image-detail .gallery-thumbs img");
						var newImage = variant.image;
						var element = originalImage[0];
						Bizweb.Image.switchImage(newImage, element, function (newImageSizedSrc, newImage, element) {
							$('.product-image-detail .gallery-thumbs .swiper-slide').each(function () {
								var $this = $(this);
								var imgThis = $this.find('img').attr('data-image');
								if (newImageSizedSrc.split("?")[0] == imgThis.split("?")[0]) {
									var pst = $this.attr('data-hash');
									galleryTop.slideTo(pst, 1000, false);
								}
							});
						});
					}
					/*end of variant image*/
				};



				jQuery(function ($) {


					// Add label if only one product option and it isn't 'Title'. Could be 'Size'.


					// Hide selectors if we only have 1 variant and its title contains 'Default'.

					$('.selector-wrapper').hide();

					$('.selector-wrapper').css({
						'text-align': 'left',
						'margin-bottom': '15px'
					});
				});


				jQuery('.swatch :radio').change(function () {
					var optionIndex = jQuery(this).closest('.swatch').attr('data-option-index');
					var optionValue = jQuery(this).val();
					jQuery(this)
						.closest('form')
						.find('.single-option-selector')
						.eq(optionIndex)
						.val(optionValue)
						.trigger('change');
					$(this).closest('.swatch').find('.options-title .var').html(optionValue);
				});

				$(document).on('click', '.btn-buy-now', function () {
					var _variantID = $('#product-selectors').val();
					var _Qty = parseInt($('.input_number_product #qty').val());
					if (_variantID == null) {
						_variantID = $('#one_variant').val();
					}
					jQuery.ajax({
						type: "POST",
						url: "/cart/add.js",
						data: "quantity=" + _Qty + "&VariantId=" + _variantID,
						dataType: "json",
						success: function (e) {
							window.location = '/checkout';
						},
						error: function (e, t) {
							Bizweb.onError(e, t);
						}
					});
				});

				setTimeout(function () {
					$('.swatch .swatch-element').each(function () {
						$(this).closest('.swatch').find('.options-title .var').html($(this).closest('.swatch').find('input:checked').val());
					});
				}, 500);



				$(document).ready(function ($) {
					var getLimit = 20;
					var product = { "id": 26841733, "name": "Áo thun bé gái cotton hình con voi", "alias": "ao-thun-be-gai-cotton-hinh-con-voi", "vendor": "Việt Nam", "type": "Vải thun", "content": "<p>Áo cotton thấm mồ hôi tốt<br />\nChú ý: Màu sắc có thể hơi khác với thực tế do màn hình và độ sáng khác nhau<br />\nHình in sử dụng công nghệ in chuyển nhiệt thân thiện với môi trường, an toàn với da bé<br />\nHướng dẫn chọn size:<br />\nSố 1: 8 - 11 kg;<br />\nSố 2: 12 - 14 kg;<br />\nSố 3: 14 - 16kg;<br />\nSố 4: 16 - 18kg;<br />\nSố 5: 18 - 20kg;<br />\nBẢO QUẢN SẢN PHẨM<br />\nKhông giặt với nước nóng.<br />\nKhông dùng thuốc tẩy, hóa chất.<br />\nKhông giặt chung với quần áo sẫm màu.</p>", "summary": "<p>Chất liệu: cotton tici mềm mịn, thấm hút mồ hôi, thoáng mát cho bé</p>\n<p>Hình in sử dụng công nghệ in chuyển nhiệt không bong tróc, không bếp dính, không phai màu và an toàn cho da của bé.</p>", "template_layout": "product", "available": true, "tags": [], "price": 45000.0000, "price_min": 45000.0000, "price_max": 45000.0000, "price_varies": false, "compare_at_price": 80000.0000, "compare_at_price_min": 80000.0000, "compare_at_price_max": 80000.0000, "compare_at_price_varies": false, "variants": [{ "id": 68292987, "barcode": null, "sku": "kmx", "title": "Default Title", "options": ["Default Title"], "option1": "Default Title", "option2": null, "option3": null, "available": true, "taxable": false, "price": 45000.0000, "compare_at_price": 80000.0000, "inventory_management": "", "inventory_policy": "deny", "inventory_quantity": 1, "weight": 0, "requires_shipping": true, "image": { "src": "https://bizweb.dktcdn.net/100/459/160/products/8224f1f46b79a6bd203f60e9eac93f8d-jpeg.jpg?v=1657181802817" } }], "featured_image": { "src": "https://bizweb.dktcdn.net/100/459/160/products/8224f1f46b79a6bd203f60e9eac93f8d-jpeg.jpg?v=1657181802817" }, "images": [{ "src": "https://bizweb.dktcdn.net/100/459/160/products/8224f1f46b79a6bd203f60e9eac93f8d-jpeg.jpg?v=1657181802817" }, { "src": "https://bizweb.dktcdn.net/100/459/160/products/7a6c37e9f584287619dd4e99f0e74f43-jpeg.jpg?v=1657181802817" }, { "src": "https://bizweb.dktcdn.net/100/459/160/products/c8f857d74522d4e545d8e833c5db680d-jpeg.jpg?v=1657181802817" }], "options": ["Title"], "created_on": "2022-07-07T15:16:26", "modified_on": "2022-07-07T15:17:00", "published_on": "2022-07-07T15:03:00" };
					var alias_pro = 'ao-thun-be-gai-cotton-hinh-con-voi';
					var array_wish = [product];
					var list_viewed_pro_old = localStorage.getItem('last_viewed_products');
					var last_viewed_pro_new = "";
					if (list_viewed_pro_old == null || list_viewed_pro_old == '')
						last_viewed_pro_new = array_wish;
					else {
						var list_viewed_pro_old = JSON.parse(localStorage.last_viewed_products);
						list_viewed_pro_old.splice(20, 1);
						for (i = 0; i < list_viewed_pro_old.length; i++) {
							if (list_viewed_pro_old[i].alias == alias_pro) {
								list_viewed_pro_old.splice(i, 1);
								break;
							}
						}
						list_viewed_pro_old.unshift(array_wish[0]);
						last_viewed_pro_new = list_viewed_pro_old;
					}
					localStorage.setItem('last_viewed_products', JSON.stringify(last_viewed_pro_new));
					var last_viewd_pro_array = JSON.parse(localStorage.last_viewed_products);
					var recentview_promises = [];
					var size_pro_review = last_viewd_pro_array.length;
					if (size_pro_review >= 20) {
						size_pro_review = 20;
					} else {
						size_pro_review = last_viewd_pro_array.length;
					}
				});


		