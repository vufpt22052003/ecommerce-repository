package com.example.shop.Service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.example.shop.model.Order_details;
import com.example.shop.model.Orders;

public interface OrderService {

	void add_order(Orders od);

	

	List<Object[]> getTopUserByOder();

	List<Orders> getOderStatus(String status);

	List<Order_details> getOrderStatusMyOder(String status);



	void updateShareRole(double price, double share_roses, int id);



	List<Order_details> getListByOder(int id);

}
