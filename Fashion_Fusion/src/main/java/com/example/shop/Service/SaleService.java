package com.example.shop.Service;

import java.sql.Date;
import java.util.Calendar;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.shop.DAO.SaleDAO;
import com.example.shop.model.Sale;

@Service
@Transactional
@Component
public class SaleService {

	@Autowired
	private SaleDAO saleRepository;

	@Scheduled(cron = "* * * * * ?") // mõi giây
	public void updateIsSaleInDatabase() {
		List<Sale> sales = saleRepository.findAll();
		for (Sale sale : sales) {
			Calendar startDate = Calendar.getInstance();
			startDate.setTime(sale.getStart_date());
			Calendar endDate = Calendar.getInstance();
			endDate.setTime(sale.getEnd_date());
			Calendar currentTime = Calendar.getInstance();

			if (currentTime.after(startDate) && currentTime.before(endDate)) {
				sale.setIs_sale(Boolean.TRUE);
				saleRepository.save(sale);
			} else {
				sale.setIs_sale(Boolean.FALSE);
				saleRepository.save(sale);
			}
		}
	}
	
	

}
