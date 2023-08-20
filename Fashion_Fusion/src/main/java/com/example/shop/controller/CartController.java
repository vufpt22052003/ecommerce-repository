package com.example.shop.controller;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.example.shop.DAO.CartDAO;
import com.example.shop.DAO.Order_detailsDAO;
import com.example.shop.DAO.SaleDAO;
import com.example.shop.ServiceImp.AdresServiceImp;
import com.example.shop.ServiceImp.CartServiceImp;
import com.example.shop.ServiceImp.OrderDetailsServiceImp;
import com.example.shop.ServiceImp.ProductsServiceImp;
import com.example.shop.ServiceImp.UserVoucherServiceImp;
import com.example.shop.model.Address;
import com.example.shop.model.Cart;
import com.example.shop.model.Order_details;
import com.example.shop.model.Products;
import com.example.shop.model.Sale;
import com.example.shop.model.Users;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

@Controller
public class CartController {
	@Autowired
	CartServiceImp cartServiceImp;

	@Autowired
	ProductsServiceImp productsServiceImp;

	@Autowired
	SaleDAO saleDAO;
	@Autowired
	HttpSession session;
	@Autowired
	HttpServletRequest request;
	@Autowired
	Order_detailsDAO order_detailsDAO;
	@Autowired
	AdresServiceImp adressServiceImp;
	@Autowired
	UserVoucherServiceImp userVoucherServiceImp;
	public void buyNow(int pid, Model model, int quantity, String color, String size) {

		Optional<Products> pro = productsServiceImp.findById(pid);
		session.removeAttribute("selectedProducts");
		model.addAttribute("buyProduct", pro);
		model.addAttribute("quantity", quantity);
		model.addAttribute("color", color);
		model.addAttribute("size", size);
		if (pro.get().checkSale()) {
			model.addAttribute("price", pro.get().getPrice() * quantity);
		}

		model.addAttribute("total", pro.get().getPrice() * quantity);

	}

	public void addToCart(int quantity, int id, String color, String size) {
		Optional<Products> findById = productsServiceImp.findById(id);
		if (findById.isPresent()) {
			int Pid = findById.get().getId();
			cartServiceImp.addCart(Pid, quantity, color, size);
		}
	}

	@RequestMapping({ "/add-to-cart", "/add-to-cart/{Pid}" })
	public String addCart(@PathVariable(value = "Pid", required = false) Integer pid,
			@RequestParam(value = "quantity", required = false) Integer quantity,
			@RequestParam(value = "productId", required = false) Integer productId, HttpSession session, Model model,
			@RequestParam(value = "type", required = false) String type,
			@RequestParam(value = "color", required = false) String color,
			@RequestParam(value = "size", required = false) String size) {
		// Kiểm tra đăng nhập
		Users acc = (Users) session.getAttribute("acc");
		if (acc == null) {
			return "views/login";
		}

		int id = 0;
		if (pid != null) {
			id = pid;
		} else if (productId != null) {
			id = productId;	
		}

		// form them vào giỏ hàng
		if (type != null && type.equals("addCart")) {
			if (quantity != null || quantity > 	0 ) {
				addToCart(quantity, id, color, size);
			}
			return "redirect:/cart";
		}
		// form mua ngay
		if (type != null && type.equals("buyNow")) {
			buyNow(id, model, quantity, color, size);
			return "views/checkout";
		}

		// thêm vào giỏ hàng bằng link
		if (pid != null) {
			addToCart(1, id, color, size);
		}
		return "redirect:/cart";
	}

// chọn trong giỏ hàng các món mua
	@GetMapping("/checkout")
	public String checkout(@RequestParam(value = "check[]" , required = false) int[] Pid, Model model) {
		Users acc = (Users) session.getAttribute("acc");
		if (acc == null) {
			return "views/login";
		}
		ArrayList<Cart> selectedProducts = new ArrayList<>();
		int total = 0;

		for (int i = 0; i < Pid.length; i++) {
			int j = Pid[i];
			session.setAttribute("selectedPid", Pid);

			selectedProducts.addAll(cartServiceImp.findByIds(j));
			List<Cart> listcart = cartServiceImp.findByIds(j);
			for (Cart cart : listcart) {
				if (cart.getProduct_id().checkSale() == true) {
					cart.getProduct_id().setPrice(cart.getProduct_id().getPrice());
					total += (int) (cart.getProduct_id().getPrice() * cart.getQuantity());
				} else {
					total += (int) (cart.getProduct_id().getPrice() * cart.getQuantity());

				}
			}
		}

		
		session.setAttribute("selectedProducts", selectedProducts);
		model.addAttribute("total", total);
		
		List<com.example.shop.model.UserVoucher> listVch = userVoucherServiceImp.getVoucherByUs();
		model.addAttribute("listVch", listVch);
		
		// checkAdress(model);

		return "views/checkout";
	}

	// check Mua ngay
	@RequestMapping("/checkBuyNow")
	public String checkBuyNow(@RequestParam("productId") int pid, Model model,
			@RequestParam(value = "color", required = false) String color,
			@RequestParam(value = "size", required = false) String size,
			@RequestParam(value = "quantity", required = false) int quantity) {

		buyNow(pid, model, quantity, color, size);
		return "views/checkout";

	}

	@GetMapping("cart")
	public String cart() {
		Users acc = (Users) session.getAttribute("acc");
		if (acc == null) {
			return "views/login";
		}
		ArrayList<Cart> sale = (ArrayList<Cart>) cartServiceImp.getCartByUserId();
		session.setAttribute("listcart", sale);
		return "views/shopping-cart";
	}

	@GetMapping("check")
	public String check() {
		return "views/checkout";
	}

	@PostMapping("/updateNumber")
	public String updateNumber(@RequestParam("quantityValue") int number, @RequestParam("Cid") int cid) {
		System.out.println(cid);
		cartServiceImp.updateNumber(number, cid);
		return "views/checkout";
	}

//	@PostMapping("/updateDefault")
//	public String updateDefault(@RequestParam("id") int id) {
//		adressServiceImp.updateDefault(id);
//		return "views/checkout";
//	}

}