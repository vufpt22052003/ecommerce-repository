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
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.shop.DAO.OrderDAO;
import com.example.shop.DAO.Order_detailsDAO;
import com.example.shop.Service.EmailService;
import com.example.shop.Service.OrderDetailsService;
import com.example.shop.Service.OrderService;
import com.example.shop.Service.ProductsService;
import com.example.shop.Service.RoseService;
import com.example.shop.ServiceImp.CommentServiceImp;
import com.example.shop.model.Comment;
import com.example.shop.model.OrderStatusDTO;
import com.example.shop.model.Order_details;
import com.example.shop.model.Orders;
import com.example.shop.model.Products;

import jakarta.persistence.criteria.Order;

@RestController
public class OrderAPI {
	@Autowired
	OrderService orderService;
	@Autowired
	OrderDAO orderDAO;

	@Autowired
	OrderDetailsService orderDetailsService;
	@Autowired
	Order_detailsDAO order_detailsDAO;
	@Autowired
	CommentServiceImp commentServiceImp;

	@Autowired
	RoseService roseService;
	@Autowired
	ProductsService productsService;
	@Autowired
	EmailService emailService;

	// lấy danh sách để ad sử lý
	@GetMapping("api/getOrderDetails")
	public ResponseEntity<List<Order_details>> getOrderDetails(@RequestParam("type") String type) {
		List<Order_details> list;
		System.out.println(type);
		list = orderDetailsService.getOderStatus(type);

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
		System.out.println(type);
		List<Order_details> orderList;
		// List<Comment> commentList = null;

		if (type != null) {
			orderList = orderService.getOrderStatusMyOder(type);

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
			@RequestParam(value = "orderDetails_id[]", required = false) Integer[] orderDetailsIds,
			@RequestParam(value = "orderId", required = false) Integer orderId) {

		if (orderId != null) {
			orderDetailsService.update_status(selectedStatus, orderId);
		}

		if (orderDetailsIds != null) {

			for (Integer oderDetaisId : orderDetailsIds) {
				if (selectedStatus.equals("HoanThanh")) {

					List<Order_details> orderDetailsList = order_detailsDAO.getListByOder(oderDetaisId);
					Optional<Order_details> od = order_detailsDAO.findById(oderDetaisId);

					String subject = "Thông Tin Đơn Hàng";
					String baseBody = "Chào bạn,\n\nĐơn Hàng " + od.get().getProduct_id().getName() + " Có Mã " + " "
							+ oderDetaisId + " Đang Trên Đường Giao Đến Bạn\n\nTrân trọng,\nĐội ngũ quản trị \n \n";
					baseBody += "Xin Vui Lòng Chú ý Điện Thoại";
					for (Order_details item : orderDetailsList) {
						String userEmail = item.getOrder_id().getUser_id().getEmail();
						String body = String.format(baseBody, oderDetaisId);
						emailService.sendEmail(userEmail, subject, body);
					}
				}

				orderDetailsService.update_status(selectedStatus, oderDetaisId);
				Optional<Order_details> od = order_detailsDAO.findById(oderDetaisId);
				Optional<Products> pro = productsService.findById(od.get().getProduct_id().getId());
				roseService.save(od.get().getPrice(), pro.get());

				int commissionAmount = (int) ((od.get().getPrice() * 10) / 100); // phần trăm được chia
				double priceRose = od.get().getPrice() - commissionAmount; // Số tiền được chia
				orderService.updateShareRole(priceRose, commissionAmount, oderDetaisId);
			}
		}

		return ResponseEntity.ok().build();
	}
//	@GetMapping("/api/O")
//	public ResponseEntity<List<Order_details>> list() {
//		List<Order_details> list = order_detailsDAO.ProductIdDesc();
//		return new ResponseEntity<>(list, HttpStatus.OK);
//	}
}
