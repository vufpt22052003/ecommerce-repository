package com.example.shop.ServiceImp;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpRequest;
import org.springframework.stereotype.Service;

import com.example.shop.DAO.CategoryDAO;
import com.example.shop.DAO.ColorDAO;
import com.example.shop.DAO.ProductsDAO;
import com.example.shop.DAO.SizeDAO;
import com.example.shop.Service.AdminService;
import com.example.shop.model.Category;
import com.example.shop.model.Color;
import com.example.shop.model.Products;
import com.example.shop.model.Sale;
import com.example.shop.model.Size;

import jakarta.transaction.Transactional;

@Service
public class AdminServiceImp implements AdminService {

	@Autowired
	CategoryDAO categoryDAO;
	@Autowired
	ProductsDAO productsDAO;
	@Autowired
	ColorDAO colorDAO;
	@Autowired
	SizeDAO sizeDAO;

	@Override
	public List<Category> listCategory() {
		return categoryDAO.findAll();
	}

	// thêm sản phẩm
	@Override
	public Products addProduct(Products pro) {
		return productsDAO.save(pro);
	}

	// theem catogery
	@Override
	public Category addCategory(Category c) {
		return categoryDAO.save(c);
	}

	@Override
	public void editProduct(Products product, int id) {
		Optional<Products> pro = productsDAO.findById(id);
		if (pro.isPresent()) {
			productsDAO.save(product);
		}
	}

	@Override
	@Transactional
	public void editColor(String col, int id) {

		List<Color> colors = colorDAO.listColor(id);
		for (Color color : colors) {
			if (color.getProduct_id().getId() == id) {
				color.setColor(col);
				System.out.println(color.getId() + "[[[");
				colorDAO.updateColor(col, color.getId());
			} else {
				System.out.println("Màu sắc không hợp lệ.");
			}
		}
	}

	public void updateColor(int pid, String[] color) {
		Optional<Products> existingProduct = productsDAO.findById(pid);

		// Lưu color (nếu có)
		List<Color> colos = colorDAO.listColor(pid);
		if (colos != null && !colos.isEmpty()) {

			// Xóa các bản ghi cũ
			for (Color colorItem : colos) {
				colorDAO.delete(colorItem);
			}
			if (color != null && color.length > 0) {
				for (String clr : color) {
					if (clr != null && !clr.isEmpty()) { // Kiểm tra color có giá trị hợp lệ không
						Color colorEntity = new Color();
						System.out.println(clr + "vlasd");
						colorEntity.setColor(clr);
						colorEntity.setProduct_id(existingProduct.get());
						colorDAO.save(colorEntity);
					}
				}
			}
		}
	}

	public void updateSzie(int pid, String sizes[]) {
		Optional<Products> existingProduct = productsDAO.findById(pid);
		List<Size> size = sizeDAO.listSize(pid);
		// Xóa các bản ghi cũ
		for (Size colorItem : size) {
			sizeDAO.delete(colorItem);
		}
		if (size != null && !size.isEmpty()) {
			if (sizes != null) {
				for (String sizeItem : sizes) {
					if (sizeItem != null && !sizeItem.isEmpty()) { // Kiểm tra size có giá trị hợp lệ không
						Size sizeEntity = new Size();
						sizeEntity.setSize(sizeItem);
						sizeEntity.setProduct_id(existingProduct.get());
						sizeDAO.save(sizeEntity);
					}
				}
			}
		}
	}

//	@Autowired
//	HttpRequest request;
//
//	public void addDay() {// Lưu sale (nếu ngày được nhập)
//		if (start_datetime_str != null && !start_datetime_str.isEmpty() && end_datetime_str != null
//				&& !end_datetime_str.isEmpty()) {
//			SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm");
//			Date start_datetime = formatter.parse(start_datetime_str);
//			Date end_datetime = formatter.parse(end_datetime_str);
//			String contend_sale = request.getParameter("contend_sale");
//			String price_sale = request.getParameter("sale_price");
//
//			Sale sale = new Sale();
//			sale.setStart_date(start_datetime);
//			sale.setEnd_date(end_datetime);
//			sale.setContend_sale(contend_sale);
//			sale.setPrice_sale(Double.parseDouble(price_sale));
//			sale.setProduct_id(product);
//
//			Calendar startDate = Calendar.getInstance();
//			startDate.setTime(sale.getStart_date());
//			Calendar endDate = Calendar.getInstance();
//			endDate.setTime(sale.getEnd_date());
//
//			saleDAO.save(sale);
//		}
//	}
}
