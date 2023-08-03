package com.example.shop.Service;

import java.util.List;
import java.util.Optional;

import com.example.shop.model.Cart;

public interface CartService {

	//Cart addCart(int idProduct, int Uid);

	List<Cart> findByIds(int pid);

	
	void updateNumber(int id, int quantity);

	void delCart(int id);

	void delCarts(int[] ids);

	List<Cart> getCartByUserId();





	Cart addCart(int idProduct, Integer quantity, String color, String size);


//	Cart updateNumber(int id);


}
