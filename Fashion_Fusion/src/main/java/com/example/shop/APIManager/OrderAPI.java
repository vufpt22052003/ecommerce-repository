package com.example.shop.APIManager;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.shop.DAO.Order_detailsDAO;
import com.example.shop.Service.OrderDetailsService;
import com.example.shop.Service.OrderService;
import com.example.shop.model.Order_details;

@RestController
public class OrderAPI {
	@Autowired
	OrderService orderService;

	@Autowired
	OrderDetailsService orderDetailsService;
	@Autowired
	Order_detailsDAO order_detailsDAO;

	// lấy danh sách để ad sử lý
	@GetMapping("api/getOrderDetails")
	public ResponseEntity<List<Order_details>> getOrderDetails(@RequestParam("type") String type) {
		List<Order_details> list;

		if (type.equals("all")) {
			// lấy tất cả
			list = orderService.listoder();

		} else if (type.equals("confirmed")) {
			// lấy danh sách đã chấp nhận
			list = orderService.getConfirmedOrders();
		} else if (type.equals("cancelled")) {
			// lấy danh sách đã hủy
			list = orderService.getCancelledOrders();
		} else {
			// Loại yêu cầu không hợp lệ
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}

		return new ResponseEntity<>(list, HttpStatus.OK);
	}

	// phía admin xử lý
	@PostMapping({ "/order_confim", "cancel_order" })
	public ResponseEntity<String> confim_order(@RequestParam("Oid") int oid, @RequestParam("Action") String action) {
		if (action.equals("confirm")) {
			orderDetailsService.confim_order(oid);
			
		} else if (action.equals("cancel")) {
			
			orderDetailsService.cancelOrder(oid);
		}
		return ResponseEntity.ok("xác nhận thành công");
	}

	// phía người dùng
	@GetMapping("/api/OrderStatus")
	public ResponseEntity<List<Order_details>> listoder(@RequestParam("type") String type) {
		List<Order_details> list = null;

		if (type.equals("pending")) {
			list = orderService.Orders_Awaiting();
		} else if (type.equals("confirm")) {
			list = orderService.Orders_confim();
		} else if (type.equals("cancel")) {
			list = orderService.Orders_cancel();
		} else {
			// Xử lý trường hợp không có điều kiện nào khớp
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
		return new ResponseEntity<>(list, HttpStatus.OK);
	}

//	@GetMapping("/api/O")
//	public ResponseEntity<List<Order_details>> list() {
//		List<Order_details> list = order_detailsDAO.ProductIdDesc();
//		return new ResponseEntity<>(list, HttpStatus.OK);
//	}
}
