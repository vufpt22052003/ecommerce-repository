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

import com.example.shop.DAO.SaleDAO;
import com.example.shop.ServiceImp.ProductsServiceImp;
import com.example.shop.model.Color;
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
			List<Products> filteredList = new ArrayList<>();

			for (Products products : listByName) {
				if (products.getId() != id) {
					filteredList.add(products);

				}
			}
			if (filteredList == null || filteredList.isEmpty()) {
				Pageable pageable = PageRequest.of(0, 10);
				ArrayList<Products> findTop8Products = (ArrayList<Products>) productsServiceImp
						.findTop8Products(pageable);
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
		for (String string : ListImgRrelateTo) {
			System.out.println(string);
		}
		m.addAttribute("ListImg", ListImgRrelateTo);

		return "views/shop-details2";
	}

	@GetMapping("/detai")
	public String shop_details() {
		return "/Admin/add-product";
	}
}
