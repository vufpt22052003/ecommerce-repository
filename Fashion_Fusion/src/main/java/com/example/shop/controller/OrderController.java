package com.example.shop.controller;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.TimeZone;

import org.apache.catalina.connector.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.SessionAttribute;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.context.annotation.SessionScope;
import org.springframework.web.servlet.ModelAndView;

import com.example.shop.DAO.AddresDAO;
import com.example.shop.DAO.VoucherDAO;
import com.example.shop.Service.OrderDetailsService;
import com.example.shop.Service.OrderService;
import com.example.shop.ServiceImp.ProductsServiceImp;
import com.example.shop.ServiceImp.UserVoucherServiceImp;
import com.example.shop.ServiceImp.VoucherServiceImp;
import com.example.shop.model.Address;
import com.example.shop.model.Cart;
import com.example.shop.model.Order_details;
import com.example.shop.model.Orders;
import com.example.shop.model.Products;
import com.example.shop.model.Users;
import com.example.shop.model.Voucher;

import org.springframework.web.bind.support.SessionStatus;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@RestController
public class OrderController {

	@Autowired
	OrderService orderService;

	@Autowired
	HttpSession session;
	@Autowired
	ProductsServiceImp productsServiceImp;
	@Autowired
	OrderDetailsService orderDetailsService;
	@Autowired
	AddresDAO addresDAO;
	@Autowired
	UserVoucherServiceImp userVoucherServiceImp;

	@Autowired
	VoucherDAO voucherDAO;
	@Autowired
	VoucherServiceImp voucherServiceImp;
	@Autowired
	PaymentController paymentController;
	@Autowired
	HttpServletRequest req;
	@Autowired
	HttpServletResponse response;

	@RequestMapping({ "/add_order" })
	public ModelAndView processOrder(@ModelAttribute("order") Orders order,
			@RequestParam(value = "priceTotal", required = false) String price,
			@ModelAttribute("checkout") Order_details model_details,
			@RequestParam(value = "idVouch", required = false) Integer idVouch,
			@RequestParam(value = "productId", required = false) Integer pid,
			@RequestParam("submitButton") String submitButton, SessionStatus sessionStatus,
			@RequestParam("id_adres") int id_adres, @RequestParam(value = "color", required = false) String color,
			@RequestParam(value = "size", required = false) String size,
			@RequestParam(value = "Note", required = false) String note, @RequestParam("delivery") String delivery)
			throws IOException {
		Users acc = (Users) session.getAttribute("acc");
		int uid = acc.getId();

		// Lấy ngày và giờ hiện tại
		Calendar calendar = Calendar.getInstance();
		Date currentDate = calendar.getTime();

		Orders ord = new Orders();
		Optional<Address> geAdresById = addresDAO.findById(id_adres);

		double pricecOder = Double.parseDouble(price);
		ord.setTransport(delivery);
		ord.setTotal_price(pricecOder);

		ord.setAdres_id(geAdresById.get());
		ord.setNotes(note);
		//
		if (idVouch != null) {
			// Optional<Voucher> vc = voucherDAO.findById(idVouch);
			voucherServiceImp.decreaseVoucherQuantity(idVouch);
			userVoucherServiceImp.updateStatusByIdVouch(idVouch);
		}
//=========
		// mua từ trong giỏ hàng
		if (submitButton.equals("BuyInCart")) {
			ord.setUser_id(acc);

			orderService.add_order(ord);

			ArrayList<Cart> selectedProducts = (ArrayList<Cart>) session.getAttribute("selectedProducts");
			for (Cart cart : selectedProducts) {
				Order_details od_details = new Order_details();
				Optional<Products> pro = Optional.of(cart.getProduct_id());
				// getOrderedProductDetails đc gọi từ public Order_details
				// getOrderedProductDetails()

				Order_details od_detailsCart = getOrderedProductDetails(ord, pro, currentDate, color, size);
				orderDetailsService.add_orderDetails(od_detailsCart);
			}
		}

		// mua từ details
		boolean check = true;
		if (submitButton.equals("BuyNow")) {
			Optional<Products> pro = productsServiceImp.findById(pid);

			ord.setUser_id(acc);
			ord.setTotal_price(pro.get().getPrice());
			ord.setPayment_status(false);

			orderService.add_order(ord);
			Order_details od_details = getOrderedProductDetails(ord, pro, currentDate, color, size);

			orderDetailsService.add_orderDetails(od_details);

		}

		// ấn thanh toán bằng vnpay
		if (submitButton.equals("PayNow"))

		{

			long pricecOders = (long) pricecOder;
			long amount = (long) pricecOder;

			boolean paymentSuccess = paymentController.vnpayPost(amount);

			// néu nhập đúng thẻ thì lưu các thông tin vào session để đem xuống xác thực otp
			// và lưu
			if (paymentSuccess) {
				session.setAttribute("price", price);
				session.setAttribute("idVouch", idVouch);
				session.setAttribute("pid", pid);
				session.setAttribute("submitButton", submitButton);
				session.setAttribute("id_adres", id_adres);
				session.setAttribute("color", color);
				session.setAttribute("size", size);
				session.setAttribute("currentDate", currentDate);

				return new ModelAndView("views/Success");
			} else {

			}
		}

		return new ModelAndView("redirect:/orderSucess");
	}

	public Order_details getOrderedProductDetails(Orders ord, Optional<Products> pro, Date currentDate, String color,
			String size) {
		Order_details od_details = new Order_details();

		od_details.setOrder_id(ord);
		// Kiểm tra xem Optional có giá trị hay không trước khi sử dụng get()
		if (pro.isPresent()) {
			od_details.setPrice(pro.get().getPrice());
			od_details.setProduct_id(pro.get());
		}
		od_details.setOrder_status("DangCho");
		od_details.setQuantity(1);
		od_details.setCreated_at(currentDate);
		od_details.setColor(color);
		od_details.setSize(size);

		return od_details;
	}

	public boolean checkOder(Products pid) {
		boolean check = false;
		Users acc = (Users) session.getAttribute("acc");
		if (acc.getId() != pid.getUser_id().getId()) {
			System.out.println("trùng");
			return true;
		}
		return false;

	}

	@GetMapping("/payment/done")
	public ModelAndView vnpayDone(HttpServletRequest request, HttpSession session) {
		StringBuilder str = new StringBuilder();
		request.getParameterMap().forEach((key, value) -> {
			str.append(key).append(": ").append(value[0]).append("<br>");
		});

		// đọc sesion được lấy từ if(submitButton.equals("PayNow")) {}
		Integer idVouch = (Integer) session.getAttribute("idVouch");

		int productId = 0;
		Integer pidInCart = (Integer) session.getAttribute("pid");
		ArrayList<Cart> selectedProducts = (ArrayList<Cart>) session.getAttribute("selectedProducts");

		// thanh toán danh sách đc chọn trong cart bằng thẻ
		if (selectedProducts != null && selectedProducts.size() > 0) {
			for (Cart cart : selectedProducts) {
				productId = cart.getProduct_id().getId();
				break; // Lưu ý: Dừng lại sau khi lấy productId từ Cart đầu tiên trong selectedProducts
			}
		} else if (pidInCart != null) { // nêus trong cart k có gì tức là người dùng chọn mua ngay
			productId = pidInCart;
		}

		// đọc sesion được lấy từ if(submitButton.equals("PayNow")) {}

		String color = (String) session.getAttribute("color");
		String size = (String) session.getAttribute("size");
		Date currentDate = (Date) session.getAttribute("currentDate");

		// tìm id của địa chỉ và product
		Orders ord = new Orders();

		Integer id_adres = (Integer) session.getAttribute("id_adres");
		if (id_adres != null) {
			// Nếu giá trị khác null, thực hiện các thao tác cần thiết
			int intValue = id_adres.intValue(); // Gọi intValue() khi đối tượng không null
			// Tiếp tục sử dụng giá trị intValue trong các thao tác khác
			Optional<Address> geAdresById = addresDAO.findById(intValue);
			ord.setAdres_id(geAdresById.get());
		} else {
			// Xử lý tình huống khi giá trị là null
			// Ví dụ: Gán giá trị mặc định hoặc ném một Exception thông báo lỗi
		}

		Optional<Products> pro = productsServiceImp.findById(productId);
		Users acc = (Users) session.getAttribute("acc");
		ord.setUser_id(acc);

		String price = (String) session.getAttribute("price");
		if (price != null && !price.trim().isEmpty()) {
			double pricecOder = Double.parseDouble(price);
			ord.setTotal_price(pricecOder);
		}
		// thiết lập cho oder

//		ModelAndView modelAndView = new ModelAndView("views/Success");
//		modelAndView.addObject("orderDetails", str.toString());

		String[] orderDetailsArray = str.toString().split("<br>");

		String vnp_Amount = "";
		String vnp_BankCode = "";
		String vnp_OrderInfo = "";
		String vnp_ResponseCode = request.getParameter("vnp_ResponseCode");

		// nếu nhập otp đúng thì lưu đơn hàng odder và oder details xuống
		if ("00".equals(vnp_ResponseCode)) {
			System.out.println("otp thanh cong");

			ord.setPayment_status(true); // câpj nhật dã thanh toán

			// Lưu đơn hàng và chi tiết đơn hàng vào cơ sở dữ liệu
			orderService.add_order(ord);
			ArrayList<Cart> selectedProductsCart = (ArrayList<Cart>) session.getAttribute("selectedProducts");

			// nếu mua trong từ giỏ hàng ra thì lấy trong vòng lặp for lưu
			// selectedProductsCart đc gởi từ cart qua
			if (selectedProductsCart != null && selectedProductsCart.size() > 0) {
				for (Cart cart : selectedProductsCart) {
					Optional<Products> product = Optional.of(cart.getProduct_id());
					Order_details od_detailsCart = getOrderedProductDetails(ord, product, currentDate, cart.getColor(),
							cart.getSize());
					orderDetailsService.add_orderDetails(od_detailsCart);
				}
			} else {
				// nếu mua bằng buy now thì lưu 1 cái
				Order_details od_details = getOrderedProductDetails(ord, pro, currentDate, color, size);
				orderDetailsService.add_orderDetails(od_details);
			}

			// Xóa các thuộc tính đã lưu trong session sau khi đã xử lý xong
			session.removeAttribute("price");
			session.removeAttribute("idVouch");
			session.removeAttribute("pid");
			session.removeAttribute("submitButton");
			session.removeAttribute("id_adres");
			session.removeAttribute("color");
			session.removeAttribute("size");
			session.removeAttribute("currentDate");
			session.removeAttribute("selectedProducts");
			session.removeAttribute("selectedProducts");
		}

//		for (String detail : orderDetailsArray) {
//			String[] keyValue = detail.split(": ");
//			if (keyValue.length == 2) {
//				String key = keyValue[0].trim();
//				String value = keyValue[1].trim();
//				if (key.equals("vnp_Amount")) {
//					vnp_Amount = value;
//				} else if (key.equals("vnp_BankCode")) {
//					vnp_BankCode = value;
//				} else if (key.equals("vnp_OrderInfo")) {
//					vnp_OrderInfo = value;
//				}
//			}
//
//			modelAndView.addObject("vnp_Amount", vnp_Amount);
//			modelAndView.addObject("vnp_BankCode", vnp_BankCode);
//			modelAndView.addObject("vnp_OrderInfo", vnp_OrderInfo);
//		}

		return new ModelAndView("redirect:/orderSucess");

	}

}