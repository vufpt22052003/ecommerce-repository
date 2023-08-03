package com.example.shop.DAO;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.example.shop.model.Order_details;
import com.example.shop.model.Orders;

public interface OrderDAO extends JpaRepository<Orders, Integer> {
//	@Query("SELECT o FROM Orders o JOIN o.order_details od WHERE o.user_id.id = ?1 AND od.is_completed = false AND od.cancelled = false")
//	List<Orders> Orders_Awaiting(int userId);
	@Query(value = "SELECT u.id, u.us AS ten_nguoi_mua, u.avt AS avatar, COUNT(*) AS total_orders, \r\n"
			+ "p.user_id AS nguoi_ban, SUM(od.quantity) AS total_quantity\r\n" + "FROM Orders o\r\n"
			+ "INNER JOIN Order_details od ON o.id = od.order_id\r\n"
			+ "INNER JOIN Products p ON od.product_id = p.id\r\n" + "INNER JOIN Account u ON o.user_id = u.id\r\n"
			+ "WHERE od.order_status = 'HoanThanh' AND p.user_id = 1\r\n" + "GROUP BY u.id, u.us, p.user_id, u.avt\r\n"
			+ "ORDER BY total_quantity DESC\r\n", nativeQuery = true)
	List<Object[]> getTopAccountsByOrder(int id);

//	@Query(value = "SELECT * FROM Orders  \r\n"
//			+ "INNER JOIN Order_details ON Orders.id = Order_details.order_id \r\n"
//			+ "where Order_details.order_status = ?1", nativeQuery = true)
//	List<Orders> getOderStatus(String status);

	@Query("SELECT o FROM Orders o INNER JOIN order_details od ON o.id = od.order_id WHERE od.order_status =?1 ")
	List<Orders> getOderStatus(String status);
}
