package com.example.shop.Service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.example.shop.model.Order_details;
import com.example.shop.model.Orders;

public interface OrderService {

	void add_order(Orders od);



	List<Order_details> Orders_Awaiting( );

	List<Order_details> Orders_cancel( );

	List<Order_details> Orders_confim( );

	List<Object[]> getTopUserByOder();



	List<Orders> getOderStatus(String status);



}
