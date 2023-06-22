package com.example.shop.model;

import java.io.Serializable;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Autowired;

import com.example.shop.DAO.SaleDAO;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.*;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "Products")

public class Products {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;

	private String name;

	private String description;

	private String origin;

	private double price;

	private int in_stock; // số lượng

	private String img;

	private boolean is_promo;

	private boolean is_active;

	private String location;

	private String target_audience;

	@Transient
	public int sale_percent; // biến lưu % giảm giá

	@Transient
	private double originalPrice; // Biến để lưu giá gốc

	@Temporal(TemporalType.TIMESTAMP)
	private Date created_day;
	
//	public boolean checkProNew() {
//	    if (this.getCreated_day() == null) {
//	        return false; // hoặc giá trị mặc định khác tuỳ thuộc vào yêu cầu của bạn
//	    }
//
//	    Date currentDate = new Date();
//	    Calendar calendar = Calendar.getInstance();
//	    calendar.setTime(currentDate);
//	    calendar.add(Calendar.DAY_OF_MONTH, -7);
//	    Date startDateMinus7Days = calendar.getTime();
//	    
//	    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
//	    String formattedStartDate = dateFormat.format(startDateMinus7Days);
//	    System.out.println(formattedStartDate);
//
//	    return this.getCreated_day().before(startDateMinus7Days);
//	}
	@Transient
	private boolean checkIsSale; // biến kiểm tra id đó có sale k (bên controller)

	// lấy ngày nhỏ hơn ngày bắt đầu
	public int getDaySale() {
		if (this.getSale().size() > 0) {
			LocalDateTime startDate = LocalDateTime.ofInstant(this.getSale().get(0).getStart_date().toInstant(),
					ZoneId.systemDefault());
			LocalDateTime currentDateTime = LocalDateTime.now();
			if (currentDateTime.isBefore(startDate)) {
				return this.getSale().get(0).getId();
			}
		}

		return 0;
	}

	// lấy id nào đang sale
	public int getIdSale() {
		if (this.getSale().size() > 0 && this.getSale().get(0).getIs_sale() == true) {
			// System.out.println(this.getSale().get(0).getId() + "ed");
			return this.getSale().get(0).getId();
		}
		return 0;
	}

	// thiết lâpj giá khi đang sale
	public boolean checkSale() {
		boolean foundSale = false;
		double originalPrice = this.price; // Biến cục bộ để lưu giá gốc

		for (Sale sale : sale) {
			// Kiểm tra điều kiện khuyến mãi
			Calendar startDate = Calendar.getInstance();
			startDate.setTime(sale.getStart_date());
			Calendar endDate = Calendar.getInstance();
			endDate.setTime(sale.getEnd_date());
			Calendar currentTime = Calendar.getInstance();

			if (currentTime.after(startDate) && currentTime.before(endDate) ) { // co khuyen mai
				this.setPrice(sale.getPrice_sale()); // Áp dụng giá khuyến mãi

				// tính phần trăm giảm
				double price_product = originalPrice;
				double price_sale = sale.getPrice_sale();

				if (price_sale < price_product) {
					double discountPercentage = ((price_product - price_sale) / price_product) * 100;
					int discountPercentageInt = (int) Math.round(discountPercentage);
					sale_percent = discountPercentageInt;
				}

				foundSale = true;
				System.out.println(originalPrice+"price");

				break;
			}
		}

		
		if (!foundSale) {
			this.setPrice(originalPrice); // Áp dụng giá gốc nếu không có khuyến mãi
		}

		return foundSale;
	}

	@ManyToOne
	@JoinColumn(name = "category_id")
	@JsonIgnore
	private Category category;

	@ManyToOne
	@JoinColumn(name = "user_id")
	@JsonIgnore
	private Users user_id;

	@OneToMany(mappedBy = "product_id")
	@JsonIgnore
	List<Comment> comment;

	@OneToMany(mappedBy = "product_id")
	@JsonIgnore
	List<Order_details> details;

	@OneToMany(mappedBy = "product_id")
	@JsonIgnore
	List<Size> size;

	@OneToMany(mappedBy = "product_id")
	@JsonBackReference
	List<Color> colors;

	@OneToMany(mappedBy = "product_id")
	@JsonIgnore
	List<Sale> sale;

	@OneToMany(mappedBy = "product_id")
	@JsonIgnore
	List<Cart> cart;

	@OneToMany(mappedBy = "product_id")
	List<ImgRelateTo> imgRelateTos;

}
