package com.example.shop.controller;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Optional;

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
import org.springframework.web.bind.annotation.SessionAttribute;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.context.annotation.SessionScope;

import com.example.shop.Service.OrderDetailsService;
import com.example.shop.Service.OrderService;
import com.example.shop.ServiceImp.ProductsServiceImp;
import com.example.shop.model.Cart;
import com.example.shop.model.Order_details;
import com.example.shop.model.Orders;
import com.example.shop.model.Products;
import com.example.shop.model.Users;

import org.springframework.web.bind.support.SessionStatus;
import jakarta.servlet.http.HttpSession;

@Controller
public class OrderController {

	@Autowired
	OrderService orderService;

	@Autowired
	HttpSession session;
	@Autowired
	ProductsServiceImp productsServiceImp;
	@Autowired
	OrderDetailsService orderDetailsService;

	@RequestMapping({ "/add_order" })
	public String processOrder(@ModelAttribute("order") Orders order,
			@ModelAttribute("checkout") Order_details model_details,
			@RequestParam(value = "productId" , required = false) Integer pid,
			@RequestParam("submitButton") String submitButton, SessionStatus sessionStatus) {

		Users acc = (Users) session.getAttribute("acc");
		int uid = acc.getId();
		// Lấy ngày và giờ hiện tại
		Calendar calendar = Calendar.getInstance();
		Date currentDate = calendar.getTime();

		Orders ord = new Orders();
		if (submitButton.equals("BuyInCart")) {

			ord.setUser_id(acc);
			ord.setTotal_price(order.getTotal_price());

			orderService.add_order(ord);
			ArrayList<Cart> selectedProducts = (ArrayList<Cart>) session.getAttribute("selectedProducts");
			for (Cart cart : selectedProducts) {
				Order_details od_details = new Order_details();
				od_details.setOrder_id(ord);
				od_details.setPrice(cart.getProduct_id().getPrice());
				od_details.setQuantity(cart.getQuantity());
				od_details.setProduct_id(cart.getProduct_id());
				od_details.setCreated_at(currentDate);

				orderDetailsService.add_orderDetails(od_details);
			}

		}
		
		if (submitButton.equals("BuyNow")) {

			Optional<Products> pro = productsServiceImp.findById(pid);
			System.out.println(pid + "pridik");

			// Orders ord = new Orders();
			ord.setUser_id(acc);
			ord.setTotal_price(pro.get().getPrice());
			orderService.add_order(ord);

			Order_details od_details = new Order_details();
			od_details.setOrder_id(ord);
			od_details.setPrice(pro.get().getPrice());
			od_details.setQuantity(1);
			od_details.setProduct_id(pro.get());
			od_details.setCreated_at(currentDate);
			

			orderDetailsService.add_orderDetails(od_details);
		}

		return "redirect:/orderSucess";
	}

}
