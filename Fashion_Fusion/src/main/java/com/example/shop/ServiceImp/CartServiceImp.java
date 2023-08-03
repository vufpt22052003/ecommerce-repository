package com.example.shop.ServiceImp;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.apache.catalina.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.shop.DAO.CartDAO;
import com.example.shop.DAO.ProductsDAO;
import com.example.shop.Service.CartService;
import com.example.shop.model.Cart;
import com.example.shop.model.Products;
import com.example.shop.model.Users;

import jakarta.servlet.http.HttpSession;

@Service
public class CartServiceImp implements CartService {

	@Autowired
	CartDAO cartDAO;

	@Autowired
	ProductsDAO productsDAO;

	@Autowired
	HttpSession session;

	@Override
	public Cart addCart(int idProduct, Integer quantity, String color, String size) {
		Optional<Products> pro = Optional.ofNullable(productsDAO.findById(idProduct).orElse(null));
		List<Cart> list = cartDAO.findAll();
		for (Cart cart : list) {
			if (cart.getProduct_id().getId() == pro.get().getId()) {
				if (quantity == null) {
					cart.setQuantity(cart.getQuantity() + 1);
				} else {
					cart.setQuantity(cart.getQuantity() + quantity);
				}
				if (color != null && (cart.getColor() == null || !cart.getColor().equals(color))) {
				    cart.setColor(color);
				}
				if (size != null && (cart.getSize() == null || !cart.getSize().equals(size))) {
				    cart.setSize(size);
				}
				return cartDAO.save(cart);
			}
		}

		Cart cart = new Cart();
		cart.setProduct_id(pro.get());
		cart.setColor(color);
		cart.setSize(size);
		if (quantity == null || quantity == 0) {
			cart.setQuantity(1);
		} else {
			cart.setQuantity(quantity);

		}

		Users acc = (Users) session.getAttribute("acc");

		cart.setUser_id(acc);
		return cartDAO.save(cart);
	}

	@Override
	public List<Cart> findByIds(int ids) {
		return cartDAO.list(ids);
	}

	@Override
	public List<Cart> getCartByUserId() {
		Users acc = (Users) session.getAttribute("acc");
		int uid = acc.getId();
		return cartDAO.getCartByUserId(uid);
	}

	@Override
	@Transactional
	public void updateNumber(int quantity, int id) {

		Optional<Cart> existingCartItem = cartDAO.findById(id);
		System.out.println(id);
		if (existingCartItem.isPresent()) {
			cartDAO.updateNumber(quantity, id);
		} else {
			throw new RuntimeException("CartItem not found");
		}
	}

	@Override
	public void delCart(int id) {
		cartDAO.deleteById(id);
	}

	@Override
	public void delCarts(int[] ids) {
		for (int id : ids) {
			cartDAO.deleteById(id);
		}
	}

}
