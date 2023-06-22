package com.example.shop.ServiceImp;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.shop.DAO.CategoryDAO;
import com.example.shop.DAO.ColorDAO;
import com.example.shop.DAO.ProductsDAO;
import com.example.shop.Service.AdminService;
import com.example.shop.model.Category;
import com.example.shop.model.Color;
import com.example.shop.model.Products;

import jakarta.transaction.Transactional;

@Service
public class AdminServiceImp implements AdminService {

	@Autowired
	CategoryDAO categoryDAO;
	@Autowired
	ProductsDAO productsDAO;
	@Autowired
	ColorDAO colorDAO;

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

}
