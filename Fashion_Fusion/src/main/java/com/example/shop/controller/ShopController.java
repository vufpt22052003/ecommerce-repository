package com.example.shop.controller;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.shop.DAO.LoginDAO;
import com.example.shop.DAO.SaleDAO;
import com.example.shop.Service.OrderDetailsService;
import com.example.shop.ServiceImp.OrderDetailsServiceImp;
import com.example.shop.ServiceImp.ProductsServiceImp;
import com.example.shop.model.Category;
import com.example.shop.model.ImgRelateTo;
import com.example.shop.model.Order_details;
import com.example.shop.model.ProductDTO;
import com.example.shop.model.Products;
import com.example.shop.model.Sale;
import com.example.shop.model.Users;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

@Controller
public class ShopController {
	@Autowired
	HttpSession session;

	@Autowired
	ProductsServiceImp productsServiceImp;
	@Autowired
	OrderDetailsServiceImp orderDetailsServiceImp;
	@Autowired
	HttpServletRequest request;

	@Autowired
	SaleDAO saleDAO;

	@Autowired
	LoginDAO loginDAO;

	@GetMapping({ "page", "/page/{i}" })
	public String shop(@PathVariable("i") Optional<Integer> i, Model m) {
		Pageable pageable = PageRequest.of(0, 999);
		Page<Products> listTarget = productsServiceImp.getAllProduct(pageable);
		;

		List<Products> productList = new ArrayList<>(listTarget.getContent());
		Collections.shuffle(productList);

		Page<Products> shuffledPage = new PageImpl<>(productList, pageable, productList.size());

		session.setAttribute("listPro", shuffledPage);
		m.addAttribute("url", "page");
		return "/views/shop";
	}

	@RequestMapping(value = { "/searchProduct", "/searchProduct/{i}" })
	public String searchByName(@PathVariable("i") Optional<Integer> i, Model model,
			@RequestParam("nameProduct") String name) {
		Pageable pageable = PageRequest.of(i.orElse(0), 8);
		Page<Products> page = productsServiceImp.searchByName(pageable, name);
		System.out.println(i);
		// Truyền giá trị đường dẫn vào model
		session.setAttribute("listPro", page);
		for (Products products : page) {
			System.out.println(products.getId());
		}
		model.addAttribute("query", "nameProduct=" + name);

		model.addAttribute("url", "searchProduct");

		return "/views/shop";
	}

	@GetMapping("/ProductSale")
	public String ProductSale(Model model) {
		List<Sale> listSale = productsServiceImp.findSalesByIsSale();
		Page<Products> listPro = productsServiceImp.getAllProduct(PageRequest.of(0, 8));

		List<Products> saleProductsList = new ArrayList<>();
		for (Products product : listPro.getContent()) {
			for (Sale sale : listSale) {
				if (product.getId() == sale.getProduct_id().getId()) {
					saleProductsList.add(product);
					break;
				}
			}
		}

		Pageable pageable = PageRequest.of(0, 8);
		Page<Products> saleProducts = new PageImpl<>(saleProductsList, pageable, saleProductsList.size());

		model.addAttribute("title", "Sản Phẩm Giảm Giá");
		session.setAttribute("listPro", saleProducts);

		return "/views/shop";
	}

	// lọc giá
	@GetMapping("/PriceFilter")
	public String PriceFilter(@RequestParam("price") String price) {
		Pageable pageable = PageRequest.of(0, 9);
		System.out.println(price);
		if (price.equals("ASC")) {
			Page<Products> pageASC = productsServiceImp.PriceASC(pageable);
			session.setAttribute("listPro", pageASC);
		}
		if (price.equals("DESC")) {
			Page<Products> pageDESC = productsServiceImp.PriceDESC(pageable);
			session.setAttribute("listPro", pageDESC);

		}
		return "/views/shop";

	}

	@GetMapping("index")
	public String index(Model m) {
		// lấy sp sale
		List<Sale> listSale = productsServiceImp.findSalesByIsSale();
		List<Integer> list = new ArrayList<>();

		for (Sale sale : listSale) {
			int salePercent = productsServiceImp.sale_percent(sale.getProduct_id().getId());
			list.add(salePercent);
		}

		m.addAttribute("sale_percent", list);

		for (Integer integer : list) {
			System.out.println(integer);
		}

		m.addAttribute("listSale", listSale);

		// lấy sp hot
		List<Products> productList = productsServiceImp.findAll();
		Page<Products> listPro = new PageImpl<>(productList);

		List<Products> hotTrendList = new ArrayList<>();

		Pageable pageable = PageRequest.of(0, 8);
		Page<Object[]> page = orderDetailsServiceImp.getHotTrendProducts(pageable);

		for (Object[] obj : page) {
			for (Products pro : listPro) {
				if (obj[0].equals(pro.getId())) {
					hotTrendList.add(pro);
				}
			}
		}

		m.addAttribute("listPro", hotTrendList);
		return "/views/index";
	}

	@RequestMapping({ "/price_range", "/price_range/{i}" })
	public String price_range(@RequestParam(name = "min", required = true) double min, @RequestParam("max") double max,
			Model m, @PathVariable("i") Optional<Integer> i) {
		Pageable pageable = PageRequest.of(i.orElse(0), 8);

//		// Mã hóa các tham số min và max trong đường dẫn
//		String encodedMin = URLEncoder.encode(String.valueOf(min), StandardCharsets.UTF_8);
//		String encodedMax = URLEncoder.encode(String.valueOf(max), StandardCharsets.UTF_8);

		Page<Products> page = productsServiceImp.PriceRrange(pageable, min, max);
		session.setAttribute("listPro", page);
		m.addAttribute("url", "price_range");
		m.addAttribute("query", "min=" + min + "&max=" + max);
		return "/views/shop";
	}

	@GetMapping("shop2")
	public String shop2(Model m) {
		Pageable pageable = PageRequest.of(0, 9);
		Page<Products> pageASC = productsServiceImp.PriceASC(pageable);
		m.addAttribute("listPro2", pageASC);
		return "/views/shop2";
	}

	@GetMapping("shopUser/{uid}")
	public String getProductByUser(@PathVariable("uid") int uid, Model m) {
		Pageable pageable = PageRequest.of(0, 999);
		Page<Products> pageASC = productsServiceImp.finAllByUser(pageable, uid);
		m.addAttribute("listProUser", pageASC);

		Optional<Users> user = loginDAO.findById(uid);
		m.addAttribute("user", user);
		m.addAttribute("totalPro", pageASC.getTotalElements());
		return "/views/shopUser";
	}

	@GetMapping("audiences/{target}")
	public String getProductByUser(@PathVariable("target") String target, Model m) {
		Pageable pageable = PageRequest.of(0, 999);
		Page<Products> listTarget = productsServiceImp.findByTargetAudience(pageable, target);

		List<Products> productList = new ArrayList<>(listTarget.getContent());
		Collections.shuffle(productList);

		Page<Products> shuffledPage = new PageImpl<>(productList, pageable, productList.size());

		session.setAttribute("listPro", shuffledPage);

		m.addAttribute("query", "target=" + target);
		m.addAttribute("url", "audiences");

		if (target.equals("nữ")) {
			m.addAttribute("title", "Sản Phẩm Dành Cho Nữ");
		}
		if (target.equals("nam")) {
			m.addAttribute("title", "Sản Phẩm Dành Cho Nam");
		}
		if (target.equals("trẻ_em")) {
			m.addAttribute("title", "Sản Phẩm Dành Cho Trẻ Em");
		}

		return "/views/shop";
	}

	@GetMapping("getCatogery/{id}")
	public String getProByCatoId(@PathVariable("id") int id, Model m) {
		if (id > 0) {
			Pageable pageable = PageRequest.of(0, 999);
			Page<Products> page = productsServiceImp.getProByCatoId(pageable, id);
			List<Products> productList = new ArrayList<>(page.getContent());
			Collections.shuffle(productList);
			page = new PageImpl<>(productList, pageable, productList.size());

			session.setAttribute("listPro", page);
		}
		return "/views/shop";
	}

	@GetMapping("/getHotTrend")
	public String getHotTrend(Model m) {
		Pageable pageable = PageRequest.of(0, 999);
		Page<Object[]> hotTrendProductIds = (Page<Object[]>) orderDetailsServiceImp.getHotTrendProducts(pageable);

		List<Products> productList = productsServiceImp.findAll();
		Page<Products> listPro = new PageImpl<>(productList);

		List<Products> hotTrendList = new ArrayList<>();

		for (Object[] productId : hotTrendProductIds) {
			for (Products products : productList) {
				if (productId[0].equals(products.getId())) {
					hotTrendList.add(products);
				}
			}
		}

		Page<Products> PageHotTrend = new PageImpl<>(hotTrendList, pageable, hotTrendList.size());
		m.addAttribute("title", "Sản Phẩm Bán Chạy");

		session.setAttribute("listPro", PageHotTrend);
		return "/views/shop";
	}

	@GetMapping("orderSucess")
	public String orderSucess() {
		return "views/Success";
	}
}
