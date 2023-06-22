package com.example.shop.Service;

import java.util.List;

import com.example.shop.model.Category;
import com.example.shop.model.Color;
import com.example.shop.model.Products;
import com.example.shop.model.Size;

public interface AdminService {


	Products addProduct(Products pro);

	Category addCategory(Category c);


	void editColor(String col, int pid);

	void editProduct(Products product, int id);

	List<Category> listCategory();

}
