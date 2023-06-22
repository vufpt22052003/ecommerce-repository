package com.example.shop.controller;

import java.util.List;

import org.apache.catalina.connector.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.shop.Service.OrderDetailsService;
import com.example.shop.Service.OrderService;
import com.example.shop.ServiceImp.OrderServiceImp;
import com.example.shop.model.Order_details;

@Controller
public class Order_Management {
	@Autowired
	OrderService orderService;

	@Autowired
	OrderDetailsService orderDetailsService;

	

	@GetMapping("/MyOrders")
	public String p(Model m) {
//		List<Order_details> list = orderService.Orders_Awaiting();
//		m.addAttribute("list_Pending", list);
		return "views/MyOrders";
	}
	
	@GetMapping("/ManagerProduct")
	public String add(Model m) {
//		List<Order_details> list = orderService.Orders_Awaiting();
//		m.addAttribute("list_Pending", list);
		return "OrderAdmin/ProdutManage";
	}
	
	@GetMapping("/icon")
	public String i() {

		return "OrderAdmin/fontawesome";
	}
	
	
}
