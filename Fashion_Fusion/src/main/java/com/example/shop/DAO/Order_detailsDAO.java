package com.example.shop.DAO;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.example.shop.model.Order_details;
import com.example.shop.model.Orders;

public interface Order_detailsDAO extends JpaRepository<Order_details, Integer> {
	// phía admin
	@Query("select o from Order_details o where o.product_id.user_id.id= ?1 and o.is_completed = false and o.cancelled = false")
	List<Order_details> listOder(int uid);

	@Query("select o from Order_details o where o.is_completed = true")
	List<Order_details> getConfirmedOrders();

	@Query("select o from Order_details o where o.cancelled = true")
	List<Order_details> getCancelledOrders();

//	phía người dùng
	@Query("SELECT od FROM Order_details od WHERE od.order_id.user_id.id = ?1 AND od.is_completed = false AND od.cancelled = false ORDER BY od.id DESC")
	List<Order_details> Orders_Awaiting(int userId);

	@Query("SELECT  od FROM Order_details od WHERE od.order_id.user_id.id = ?1 AND od.is_completed = true AND od.cancelled = false")
	List<Order_details> Orders_confim(int userId);

//	
	@Query("SELECT  od FROM Order_details od WHERE od.order_id.user_id.id = ?1 AND od.is_completed = false AND od.cancelled = true")
	List<Order_details> Orders_cancel(int userId);

//	@Query("SELECT od.product_id FROM Order_details od WHERE od.is_complete = true ORDER BY od.product_id DESC")
//	List<Order_details> ProductIdDesc();

	@Query("SELECT SUM(od.price) FROM Order_details od WHERE od.product_id.user_id.id = ?1 and od.is_completed = true ")
	Double getotalPrice(int userId);

	@Query(value = "SELECT TOP 10 od.product_id, COUNT(*) AS total_orders, SUM(od.price) AS total_price, p.name\r\n"
			+ "FROM Order_details od\r\n" + "INNER JOIN Products p ON od.product_id = p.id\r\n"
			+ "WHERE p.user_id = 1 AND od.is_completed = 1\r\n" + "GROUP BY od.product_id, p.name\r\n"
			+ "ORDER BY total_price DESC;\r\n" + "", nativeQuery = true)
	List<Object[]> findTop10ProductsByTotalOrders(int id);

	@Query(value = "SELECT product_id, SUM(quantity) AS total_quantity\r\n"
			+ "FROM Order_details\r\n"
			+ "WHERE is_completed = 1\r\n"
			+ "GROUP BY product_id\r\n"
			+ "ORDER BY total_quantity DESC;\r\n"
			+ "", nativeQuery = true)
	Page<Object[]> getHotTrendProducts(Pageable pageable);

}
