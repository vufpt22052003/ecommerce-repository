package com.example.shop.Service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.example.shop.model.Order_details;
import com.example.shop.model.Products;

public interface OrderDetailsService {

	void add_orderDetails(Order_details od);

	void confim_order(int id);

	double getotalPrice();

	List<Object[]> getTop10ProductsByTotalOrders();

	Page<Object[]> getHotTrendProducts(Pageable pageable);

	void cancelOrder(int id, String cancel);

}
