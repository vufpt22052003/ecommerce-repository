package com.example.shop.ServiceImp;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.example.shop.DAO.Order_detailsDAO;
import com.example.shop.Service.OrderDetailsService;
import com.example.shop.Service.OrderService;
import com.example.shop.model.Order_details;
import com.example.shop.model.Orders;
import com.example.shop.model.Products;

@Service
public class OrderDetailsServiceImp implements OrderDetailsService {

	@Autowired
	Order_detailsDAO order_detailsDAO;

	@Autowired
	ProductsServiceImp productsServiceImp;

	@Override
	public void add_orderDetails(Order_details od) {
		order_detailsDAO.save(od);
	}

	@Override
	public void confim_order(int id) {
		Optional<Order_details> odOptional = order_detailsDAO.findById(id);
		Optional<Products> pro = productsServiceImp.findById(odOptional.get().getProduct_id().getId());
		if (odOptional.isPresent()) {
			Order_details od = odOptional.get();
			if (pro.get().getIn_stock() >= odOptional.get().getQuantity()) {
				pro.get().setIn_stock(pro.get().getIn_stock() - odOptional.get().getQuantity());
				od.set_completed(true); // Cập nhật trường is_complete thành true
				Calendar calendar = Calendar.getInstance();
				Date currentDate = calendar.getTime();
				od.setUpdated_at(currentDate);
			} else {
				System.out.println(" k dủ so luong");
			}
			order_detailsDAO.save(od);
		}
	}

	@Override
	public void cancelOrder(int id) {
		Optional<Order_details> odOptional = order_detailsDAO.findById(id);

		if (odOptional.isPresent()) {
			Order_details od = odOptional.get();
			od.setCancelled(true);
			od.setCancelled_by("admin");
			order_detailsDAO.save(od);
		}
	}

	@Override
	public double getotalPrice() {
		return order_detailsDAO.getotalPrice(1);
	}

	@Override
	public List<Object[]> getTop10ProductsByTotalOrders() {
		int id = 1;
		return order_detailsDAO.findTop10ProductsByTotalOrders(id);

	}
	@Override
	public Page<Object[]> getHotTrendProducts(Pageable pageable) {
		return order_detailsDAO.getHotTrendProducts(pageable);
	}
}
