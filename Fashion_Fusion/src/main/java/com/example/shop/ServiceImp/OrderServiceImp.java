package com.example.shop.ServiceImp;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.example.shop.DAO.OrderDAO;
import com.example.shop.DAO.Order_detailsDAO;
import com.example.shop.Service.OrderService;
import com.example.shop.model.Order_details;
import com.example.shop.model.Orders;
import com.example.shop.model.Users;

import jakarta.servlet.http.HttpSession;
import jakarta.transaction.Transactional;

@Service
public class OrderServiceImp implements OrderService {

	@Autowired
	OrderDAO orderDAO;

	@Autowired
	Order_detailsDAO order_detailsDAO;

	@Autowired
	HttpSession session;

	@Override
	public void add_order(Orders od) {
		orderDAO.save(od);
	}

//	// lấy đơn hàng đang cbi
//	@Override
//	public List<Order_details> getOderPrepare() {
//		Users acc = (Users) session.getAttribute("acc");
//		int uid = acc.getId();
//		return (List<Order_details>) order_detailsDAO.getOderPrepare(uid);
//	}

	// người dùng chờ đợi

	@Override
	public List<Order_details> getOrderStatusMyOder(String status) {
		Users acc = (Users) session.getAttribute("acc");
		int uid = acc.getId();
		return (List<Order_details>) order_detailsDAO.getOrderStatusMyOder(uid, status);
	}
	

	@Override
	public List<Order_details> getListByOder(int id) {
		Users acc = (Users) session.getAttribute("acc");
		int uid = acc.getId();
		return (List<Order_details>) order_detailsDAO.getListByOder(id);
	}

	// người dùng coi đơn dã tahnh cong

	@Override
	public List<Object[]> getTopUserByOder() {
		Users acc = (Users) session.getAttribute("acc");
		int uid = acc.getId();
		return orderDAO.getTopAccountsByOrder(21);
	}

	@Override
	public List<Orders> getOderStatus(String status) {
		return (List<Orders>) orderDAO.getOderStatus(status);
	}

	@Override
	@Transactional
	public void updateShareRole(double price, double share_roses, int id) {
		order_detailsDAO.ShareRose(price, share_roses, id);
	}

}
