package com.example.shop.Service;

import java.sql.Date;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Calendar;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.example.shop.DAO.ProductsDAO;
import com.example.shop.DAO.SaleDAO;
import com.example.shop.model.Products;
import com.example.shop.model.Sale;

@Service
public class PromoService {
	@Autowired
	private ProductsDAO productsDAO;

	@Autowired
	private SaleDAO saleDAO;

	@Scheduled(cron = "0 0 * * * *") // Chạy mỗi giờ
	public void updatePromoStatus() {
		System.out.println("p[á'd");
		List<Sale> sales = saleDAO.findAll();
		LocalDateTime currentDateTime = LocalDateTime.now();

		for (Sale s : sales) {
			Date startDate = (Date) s.getStart_date();
			Date endDate = (Date) s.getEnd_date();

			Calendar startDateCalendar = Calendar.getInstance();
			startDateCalendar.setTime(startDate);
			LocalDateTime startDateTime = LocalDateTime.ofInstant(startDateCalendar.toInstant(),
					ZoneId.systemDefault());

			Calendar endDateCalendar = Calendar.getInstance();
			endDateCalendar.setTime(endDate);
			LocalDateTime endDateTime = LocalDateTime.ofInstant(endDateCalendar.toInstant(), ZoneId.systemDefault());

			Calendar currentTime = Calendar.getInstance();
			LocalDateTime currentLocalDateTime = LocalDateTime.ofInstant(currentTime.toInstant(),
					ZoneId.systemDefault());

			Products product = new Products();
			if (currentLocalDateTime.isAfter(startDateTime)
					&& (currentLocalDateTime.isEqual(endDateTime) || currentLocalDateTime.isBefore(endDateTime))) {
				product.set_promo(true);
			} else {
				product.set_promo(false);
			}
			productsDAO.save(product);
		}

	}
}
