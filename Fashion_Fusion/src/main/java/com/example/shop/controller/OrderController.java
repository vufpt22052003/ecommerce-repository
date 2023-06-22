package com.example.shop.controller;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.apache.catalina.connector.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttribute;
import org.springframework.web.context.annotation.SessionScope;

import com.example.shop.Service.OrderDetailsService;
import com.example.shop.Service.OrderService;
import com.example.shop.model.Cart;
import com.example.shop.model.Order_details;
import com.example.shop.model.Orders;
import com.example.shop.model.Users;

import jakarta.servlet.http.HttpSession;

@Controller
public class OrderController {

	@Autowired
	OrderService orderService;

	@Autowired
	HttpSession session;

	@Autowired
	OrderDetailsService orderDetailsService;

	@PostMapping("/add_order")
	public String add_order(@ModelAttribute("order") Orders order,
			@ModelAttribute("checkout") Order_details model_details,
			@SessionAttribute("selectedProducts") ArrayList<Cart> selectedProducts, HttpSession session) {

		Users acc = (Users) session.getAttribute("acc");
		int uid = acc.getId();

		Orders ord = new Orders();
		ord.setUser_id(acc);
		ord.setTotal_price(order.getTotal_price());

		// Lấy ngày và giờ hiện tại
		Calendar calendar = Calendar.getInstance();
		Date currentDate = calendar.getTime();

		// Gán giá trị ngày hiện tại vào thuộc tính created_at
		//ord.setCreated_at(currentDate);

		orderService.add_order(ord);

		for (Cart cart : selectedProducts) {
			Order_details od_details = new Order_details();
			od_details.setOrder_id(ord);
			od_details.setPrice(cart.getProduct_id().getPrice());
			od_details.setQuantity(cart.getQuantity());
			od_details.setProduct_id(cart.getProduct_id());
			od_details.setCreated_at(currentDate);

			orderDetailsService.add_orderDetails(od_details);

		}

		session.removeAttribute("selectedProducts");

		return "redirect:/orderSucess";
	}
	
	

}
