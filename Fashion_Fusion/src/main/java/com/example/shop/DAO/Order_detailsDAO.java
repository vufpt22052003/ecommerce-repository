package com.example.shop.DAO;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.example.shop.model.Order_details;
import com.example.shop.model.Orders;

public interface Order_detailsDAO extends JpaRepository<Order_details, Integer> {

	// đơn hàng theo status
	@Query("select o from Order_details o where o.order_status = ?1 and o.product_id.user_id.id = ?2")
	List<Order_details> getOderStatus(String status, int uid);

	// vận chuyển lấy đơn hàng
	@Query("select o from Order_details o where o.order_status = ?1")
	List<Order_details> getOderStatusTransport(String status);

/// Lấy danh sách các đơn hàng chưa hoàn thành và đã hủy của một người dùng (userId)
	@Query("SELECT od FROM Order_details od WHERE od.order_id.user_id.id = ?1 AND (od.order_status != 'HoanThanh' OR od.order_status = 'HuyDon') ORDER BY od.id DESC")
	List<Order_details> Orders_AwaitingAndCancelled(int userId);

	// lấy danh sách where od có id =?
	@Query("SELECT od FROM Order_details od WHERE od.order_id.id = ?1 ORDER BY od.id DESC")
	List<Order_details> getOderDetailsByOrderId(int id);


	// Lấy danh sách các đơn hàng đã hoàn thành (hoàn thành và không bị hủy) của một
	// người dùng (userId)
	@Query("SELECT od FROM Order_details od WHERE od.order_id.user_id.id = ?1 AND od.order_status = 'HoanThanh' AND od.order_status = 'HuyDon' ")
	List<Order_details> Orders_confirmed(int userId);

	// Lấy danh sách các đơn hàng bị hủy của một người dùng (userId)
	@Query("SELECT od FROM Order_details od WHERE od.order_id.user_id.id = ?1 AND od.order_status = 'HuyDon'")
	List<Order_details> Orders_cancel(int userId);

	// Lấy tổng giá tiền của các đơn hàng đã hoàn thành của một người dùng (userId)
	@Query("SELECT SUM(od.price) FROM Order_details od WHERE od.order_id.user_id.id = ?1 AND od.order_status = 'HoanThanh'")
	Double getTotalPrice(int userId);

	// Lấy danh sách 10 sản phẩm có số lượng đơn hàng đã hoàn thành nhiều nhất, sắp
	// xếp theo tổng giá trị giảm dần
	@Query(value = "SELECT TOP 10 od.product_id, COUNT(*) AS total_orders, SUM(od.price) AS total_price, p.name "
			+ "FROM Order_details od " + "INNER JOIN Products p ON od.product_id = p.id "
			+ "WHERE p.user_id = ?1 AND od.order_status = 'HoanThanh' " + "GROUP BY od.product_id, p.name "
			+ "ORDER BY total_price DESC", nativeQuery = true)
	List<Object[]> findTop10ProductsByTotalOrders(int userId);

	// Lấy danh sách các sản phẩm có số lượng tổng cộng đã hoàn thành nhiều nhất,
	// sắp xếp theo tổng số lượng giảm dần
	@Query(value = "SELECT product_id, SUM(quantity) AS total_quantity " + "FROM Order_details "
			+ "WHERE order_status = 'HoanThanh' " + "GROUP BY product_id "
			+ "ORDER BY total_quantity DESC", nativeQuery = true)
	Page<Object[]> getHotTrendProducts(Pageable pageable);

	@Modifying
	@org.springframework.transaction.annotation.Transactional
	@Query(value = " UPDATE Order_details SET has_review = 1 where id= ?", nativeQuery = true)
	public void updateHasReviewStatus(int detailsId);

	@Modifying
	@org.springframework.transaction.annotation.Transactional
	@Query(value = "update  Order_details set \r\n" + "order_status = ? where id = ?", nativeQuery = true)
	public void UpdateStatus(String status, int id);

}
