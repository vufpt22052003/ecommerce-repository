package com.example.shop.APIManager;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.shop.ServiceImp.CartServiceImp;
import com.example.shop.model.Cart;

@RestController
public class CartAPI {
	@Autowired
	CartServiceImp cartServiceImp;

	// xóa theo danh sách chọn
	@RequestMapping("/deleteSelected")
	public ResponseEntity<String> deleteSelected(
			@RequestParam(value = "selectedIds[]", required = false) int[] selectedIds) {
		for (int i = 0; i < selectedIds.length; i++) {
			int id = selectedIds[i];
			System.out.println(id);
			cartServiceImp.delCart(id);
		}
		return ResponseEntity.ok("Sản phẩm đã được xóa thành công");
	}

	// xóa theo id
	@RequestMapping("/delCmt")
	public ResponseEntity<String> delCart(@RequestParam("id") int id) {
		cartServiceImp.delCart(id);
		return ResponseEntity.ok("Sản phẩm đã được xóa thành công");
	}

	// lấy ra danh sách cart
	@GetMapping("/cart/api")
	public ResponseEntity<List<Cart>> getAll() {
		List<Cart> cart = cartServiceImp.getCartByUserId();

		for (Cart c : cart) {
			if (c.getProduct_id().checkSale() == true) {
				c.getProduct_id().setPrice(c.getProduct_id().getPrice());
			}
		}

		return new ResponseEntity<>(cart, HttpStatus.OK);

	}

}
