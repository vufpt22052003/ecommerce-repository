package com.example.shop.controller;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.apache.catalina.connector.Request;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.example.shop.DAO.CommentDAO;
import com.example.shop.DAO.SaleDAO;
import com.example.shop.ServiceImp.CommentServiceImp;
import com.example.shop.ServiceImp.ProductsServiceImp;
import com.example.shop.ServiceImp.UserVoucherServiceImp;
import com.example.shop.model.Color;
import com.example.shop.model.Comment;
import com.example.shop.model.ImgRelateTo;
import com.example.shop.model.Products;
import com.example.shop.model.Sale;
import com.example.shop.model.Size;

import jakarta.servlet.http.HttpSession;

@Controller
public class ShopDetailsCtrl {

	@Autowired
	ProductsServiceImp productsServiceImp;
	@Autowired
	HttpSession session;
	@Autowired
	SaleDAO saleDAO;
	@Autowired
	CommentServiceImp commentServiceImp;

	@Autowired
	UserVoucherServiceImp userVoucherServiceImp;

	@GetMapping("shop_info/{id}")
	public String ShopInfo(@PathVariable("id") int id, Model m) {
		// lấy đối tượng tìm được
		Optional<Products> pro = productsServiceImp.findById(id);
		ArrayList<Sale> listSale = (ArrayList<Sale>) saleDAO.findAll();
		m.addAttribute("price", pro.get().getPrice());

		if (pro.get().getIn_stock() > 0) {
			m.addAttribute("product", pro);
			m.addAttribute("productId", pro.get().getId());
		} else {
			m.addAttribute("product", pro);
			ArrayList<Products> listByName = (ArrayList<Products>) productsServiceImp.findByName(pro.get().getName());
			m.addAttribute("listByName", listByName);
			m.addAttribute("price", pro.get().getPrice());
			System.out.println((pro.get().getPrice() + "]]]"));
			List<Products> filteredList = new ArrayList<>();

			for (Products products : listByName) {
				if (products.getId() != id) { // loại bỏ sp đang xem để k trùng
					filteredList.add(products);
				}
			}
			if (filteredList == null || filteredList.isEmpty()) {
				Pageable pageable = PageRequest.of(0, 10);
				ArrayList<Products> findTop8Products = (ArrayList<Products>) productsServiceImp.findTop8Products();
				Collections.shuffle(findTop8Products); // random

				m.addAttribute("listByName", findTop8Products);
			}
			return "views/SoldOut";
		}
		// System.out.println(pro.get().getSale(0));
		// lấy ra sản phẩm có liên quan
		String key = pro.get().getName();
		List<Products> list = productsServiceImp.findByName(key);
		m.addAttribute("listByName", list);

		int sale_percent = productsServiceImp.sale_percent(id);
		m.addAttribute("sale_percent", sale_percent);

		// lấy list size
		List<Size> listSize = productsServiceImp.getListSize(id);
		m.addAttribute("listSize", listSize);

		// lấy list listColor
		List<Color> listColor = productsServiceImp.getListColor(id);
		m.addAttribute("listColor", listColor);

		// lấy list ảnh có liên quan
		List<String> ListImgRrelateTo = productsServiceImp.ListImgRrelateTo(id);
		m.addAttribute("ListImg", ListImgRrelateTo);
		// list cmt
		ArrayList<Comment> listCmt = (ArrayList<Comment>) commentServiceImp.getCmt(id);
		m.addAttribute("listCmt", listCmt);

		// tính số sao 
		int[] listCountRating = new int[5]; // Mảng để lưu số lượng đánh giá từ rating 1 đến 5

		for (Comment comment : listCmt) {
		    int rating = comment.getRating();
		    if (rating >= 1 && rating <= 5) {
		    	listCountRating[rating - 1]++; // Tăng số lượng đánh giá cho rating tương ứng
		    }
		}
		m.addAttribute("listCountRating",listCountRating);

		int sum = 0;
		List<Integer> listRating = new ArrayList<>(); // Khởi tạo danh sách đánh giá
		for (Comment itemCmt : listCmt) {
			int rating = itemCmt.getRating();
			listRating.add(rating);
			sum += rating;
		}

		double averageRating = (double) sum / listRating.size();

		String formattedRating = String.format("%.1f", averageRating);

		m.addAttribute("countRating", formattedRating);
		m.addAttribute("listRating", listRating);

		// list ảnh cmt
		List<Object[]> ListImgCmt = productsServiceImp.ListImgCmt(id);
		m.addAttribute("ListImgCmt", ListImgCmt);
		for (Object[] objects : ListImgCmt) {
			System.out.println(objects[1]);
		}

		return "views/shopDetails";
	}

	@GetMapping("/detai")
	public String shop_details() {
		return "/Admin/add-product";
	}
}
