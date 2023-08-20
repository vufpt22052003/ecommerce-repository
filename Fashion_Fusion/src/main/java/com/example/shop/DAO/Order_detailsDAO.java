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

/// Lấy danh sách các đơn hàng theo oder_status
	@Query("SELECT od FROM Order_details od WHERE od.order_id.user_id.id = ?1 AND  od.order_status =?2 ORDER BY od.id DESC")
	List<Order_details> getOrderStatusMyOder(int userId, String status);

	// lấy danh sách where od có id =?
	@Query("SELECT od FROM Order_details od WHERE od.order_id.id = ?1 and od.order_status ='BanGiaoVanChuyen' ORDER BY od.id DESC")
	List<Order_details> getOderDetailsByOrderId(int id);

	// Lấy tổng giá tiền của các đơn hàng đã hoàn thành của một người dùng (userId)
	@Query("SELECT SUM(od.price) FROM Order_details od WHERE od.order_id.user_id.id = ?1 AND od.order_status = 'HoanThanh'")
	Double getTotalPrice(int userId);

	// Lấy danh sách 10 sản phẩm có số lượng đơn hàng đã hoàn thành nhiều nhất, sắp
	@Query(value = "SELECT TOP 10 od.product_id, COUNT(*) AS total_orders, SUM(od.price) AS total_price, p.name "
			+ "FROM Order_details od " + "INNER JOIN Products p ON od.product_id = p.id "
			+ "WHERE p.user_id = ?1 AND od.order_status = 'HoanThanh' " + "GROUP BY od.product_id, p.name "
			+ "ORDER BY total_price DESC", nativeQuery = true)
	List<Object[]> findTop10ProductsByTotalOrders(int userId);

	// Lấy danh sách các sản phẩm có số lượng tổng cộng đã hoàn thành nhiều nhất,
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

	@Query("SELECT DISTINCT od FROM Order_details od JOIN FETCH od.product_id p JOIN FETCH p.user_id u")
	List<Order_details> getOd();

	@Query(value = "SELECT a.id, a.avt, a.us, a.sdt, a.email, COUNT(*) AS count_rows \r\n" + "FROM Account a\r\n"
			+ "JOIN Products p ON a.id = p.user_id \r\n" + "JOIN Order_details od ON p.id = od.product_id  \r\n"
			+ "WHERE od.order_status = 'BanGiaoVanChuyen'\r\n" + "GROUP BY a.id, a.avt, a.us, a.sdt, a.email\r\n"
			+ "ORDER BY COUNT(*) DESC;", nativeQuery = true)
	List<Object[]> getClient();

	// update giá tiền chia hoa hòngo sau khi giao hàng thành công
	@Modifying
	@Query(value = "UPDATE Order_details SET  price = ? , share_roses =? WHERE id = ? ", nativeQuery = true)
	void ShareRose(Double price, Double share_roses, int id);

	// tính xem bao nhiêu đơn dã bán đc bên supper admin
	@Query(value = "SELECT Count(*) FROM Order_details WHERE order_status = 'HoanThanh'", nativeQuery = true)
	int countCompletedOrders();

	// tinh tiền đc bao nhiêu đơn giao bên vận chuyển
	@Query(value = "SELECT Orders.transport\r\n" + "FROM Orders\r\n"
			+ "INNER JOIN Order_details ON Orders.id=Order_details.order_id\r\n"
			+ "where Order_details.order_status = 'HoanThanh'", nativeQuery = true)
	List<Object[]> countDeliveries();

	@Query("select o from Order_details o where o.id = ?1")
	List<Order_details> getListByOder(int id);

}
