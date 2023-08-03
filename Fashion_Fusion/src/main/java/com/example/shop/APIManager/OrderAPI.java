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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.shop.DAO.Order_detailsDAO;
import com.example.shop.Service.OrderDetailsService;
import com.example.shop.Service.OrderService;
import com.example.shop.ServiceImp.CommentServiceImp;
import com.example.shop.model.Comment;
import com.example.shop.model.OrderStatusDTO;
import com.example.shop.model.Order_details;

@RestController
public class OrderAPI {
		@Autowired
		OrderService orderService;
	
		@Autowired
		OrderDetailsService orderDetailsService;
		@Autowired
		Order_detailsDAO order_detailsDAO;
	@Autowired
	CommentServiceImp commentServiceImp;

	// lấy danh sách để ad sử lý
	@GetMapping("api/getOrderDetails")
	public ResponseEntity<List<Order_details>> getOrderDetails(@RequestParam("type") String type) {
		List<Order_details> list;
		System.out.println(type);
		list = orderDetailsService.getOderStatus(type);
		for (Order_details order_details : list) {
			System.out.println(order_details.getId());
		}
		/*
		 * if (type.equals("all")) { // lấy tất cả list = orderService.listoder(); }
		 * else if (type.equals("confirmed")) { // lấy danh sách đã chấp nhận list =
		 * orderService.getConfirmedOrders();
		 * 
		 * } else if (type.equals("cancelled")) { // lấy danh sách đã hủy list =
		 * orderService.getCancelledOrders(); } else if (type.equals("prepare")) { //
		 * lấy danh đang chuẩn bị hàng list = orderService.getOderPrepare(); } else if
		 * (type.equals("prepare")) { // lấy danh đang chuẩn bị hàng } else { // Loại
		 * yêu cầu không hợp lệ return new ResponseEntity<>(HttpStatus.BAD_REQUEST); }
		 */

		return new ResponseEntity<>(list, HttpStatus.OK);
	}

	// phía admin xử lý
	@PostMapping({ "order_confim", "cancel_order" })
	public ResponseEntity<String> confim_order(@RequestParam("Oid") int oid, @RequestParam("Action") String action,
			@RequestParam(value = "cancelBy", required = false) String cancel) {

		if (action.equals("confirm")) {
			orderDetailsService.confim_order(oid);
		}
		if (action.equals("cancel")) {
			orderDetailsService.cancelOrder(oid, cancel);
		}
		return ResponseEntity.ok("xác nhận thành công");
	}

	// phía người dùng
	@GetMapping("/api/OrderStatus")
	public ResponseEntity<List<Order_details>> listoder(@RequestParam("type") String type) {
		// OrderStatusDTO orderStatusDTO = new OrderStatusDTO();
		List<Order_details> orderList = null;
		// List<Comment> commentList = null;

		if (type.equals("pending")) {
			orderList = orderService.Orders_Awaiting();
			// commentList = commentServiceImp.getCmtByUserId();
		} else if (type.equals("confirm")) {
			orderList = orderService.Orders_confim();
		} else if (type.equals("cancel")) {
			orderList = orderService.Orders_cancel();
		} else {
			// Xử lý trường hợp không có điều kiện nào khớp
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}

//		orderStatusDTO.setOrderList(orderList);
//		orderStatusDTO.setCommentList(commentList);

		return ResponseEntity.ok(orderList);
	}

	@GetMapping("/api/getTop10Order")
	public ResponseEntity<List<Object[]>> getTop10Order() {
		List<Object[]> top10 = orderDetailsService.getTop10ProductsByTotalOrders();
		return new ResponseEntity<>(top10, HttpStatus.OK);
	}

	@GetMapping("/api/getTopUserByOder")
	public ResponseEntity<List<Object[]>> getTopUserByOder() {
		List<Object[]> top = orderService.getTopUserByOder();
		return new ResponseEntity<>(top, HttpStatus.OK);
	}

	@RequestMapping("/api/update_status")
	public ResponseEntity<Void> update_status(@RequestParam("selectedStatus") String selectedStatus,
			@RequestParam("orderId") int orderId) {
		System.out.println(selectedStatus);
		orderDetailsService.update_status(selectedStatus, orderId);
		return ResponseEntity.ok().build();
	}
//	@GetMapping("/api/O")
//	public ResponseEntity<List<Order_details>> list() {
//		List<Order_details> list = order_detailsDAO.ProductIdDesc();
//		return new ResponseEntity<>(list, HttpStatus.OK);
//	}
}
