package com.example.shop.DAO;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.shop.model.Sale;

@Repository
public interface SaleDAO extends JpaRepository<Sale, Integer> {
	@Query("select s from Sale s where s.product_id.id = ?1 ")
	Sale finByIdSale(int id);

//	@Query("SELECT s FROM Sale s WHERE s.start_date < CURRENT_TIMESTAMP AND s.end_date > CURRENT_TIMESTAMP")
//	List<Sale> findActiveSales();

//    List<Sale> findByStartDateTimeBeforeAndEndDateTimeAfter(LocalDateTime startDateTime, LocalDateTime endDateTime);

	// lấy sp đang sale
	@Query("Select s from Sale s where s.is_sale = true")
	List<Sale> findSalesByIsSale();

	// ngày hiện tại nhỏ hơn ngày bắt đầu
	@Query("SELECT s FROM Sale s WHERE s.start_date > ?1")
	List<Sale> findSalesAfterStartDate(Date start);

	// ngày hiện tại lơn hơn ngày ngày kết thúc
	@Query("SELECT s FROM Sale s WHERE s.end_date < ?1")
	List<Sale> findSalesBeforeEndDate(Date end);
	
//	@Query("SELECT s FROM Sale s WHERE s.start_date > ?1 and s.end_date < ?1")
//	List<Sale> findActiveSales(Date start , Date end);


}
