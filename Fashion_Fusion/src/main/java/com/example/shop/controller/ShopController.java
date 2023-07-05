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
import jakarta.validation.constraints.Max;

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
		Pageable pageable = PageRequest.of(i.orElse(0), 12);
		Page<Products> getAll = productsServiceImp.getAllProduct(pageable);

		// getAll.stream().map(p -> p.getId()).forEach(p2 -> System.out.println(p2));

//		List<Products> productList = new ArrayList<>(getAll.getContent());
//		Collections.shuffle(productList);

		/*
		 * Page<Products> shuffledPage = new PageImpl<>(productList, pageable,
		 * productList.size());
		 */
		session.setAttribute("listPro", getAll);
		m.addAttribute("url", "page");

		return "/views/shop";
	}

	@RequestMapping(value = { "/searchProduct", "/searchProduct/{i}" })
	public String searchByName(@PathVariable("i") Optional<Integer> i, Model model,
			@RequestParam("nameProduct") String name) {
		Pageable pageable = PageRequest.of(i.orElse(0), 1);
		Page<Products> page = productsServiceImp.searchByName(pageable, name);
		session.setAttribute("listPro", page);
		model.addAttribute("query", "nameProduct=" + name);

		model.addAttribute("url", "searchProduct");

		return "/views/shop";
	}

	@GetMapping("/ProductSale")
	public String ProductSale(Model model) {
		List<Sale> listSale = productsServiceImp.findSalesByIsSale();
		Page<Products> listPro = productsServiceImp.getAllProduct(PageRequest.of(0, Integer.MAX_VALUE));

		List<Products> saleProductsList = new ArrayList<>();
		for (Products product : listPro.getContent()) {
			for (Sale sale : listSale) {
				if (product.getId() == sale.getProduct_id().getId()) {
					System.out.println(product.getId());
					saleProductsList.add(product);
					break;
				}
			}
		}

		Pageable pageable = PageRequest.of(0, 12);
		Page<Products> saleProducts = new PageImpl<>(saleProductsList, pageable, saleProductsList.size());

		model.addAttribute("title", "Sản Phẩm Giảm Giá");
		session.setAttribute("listPro", saleProducts);

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

		Pageable pageable = PageRequest.of(0, 12);
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

	@GetMapping("shop2")
	public String shop2(Model m) {
		Pageable pageable = PageRequest.of(0, 9);
		Page<Products> pageASC = productsServiceImp.PriceASC(pageable);
		m.addAttribute("listPro2", pageASC);
		return "/views/shop2";
	}

	@GetMapping("/shopUser/{uid}")
	public ResponseEntity<List<Products>> getProductByUser(@PathVariable("uid") int uid, Model m) {
		Pageable pageable = PageRequest.of(1, 8);
		Page<Products> pageASC = productsServiceImp.finAllByUser(pageable, uid);
		m.addAttribute("listProUser", pageASC);

		Optional<Users> user = loginDAO.findById(uid);
		m.addAttribute("user", user);
		m.addAttribute("totalPro", pageASC.getTotalElements());

		List<Products> productList = pageASC.getContent();
		return ResponseEntity.ok(productList);
	}

	@GetMapping("/audiences/{index}/{target}")

	public String getProductByUser(@PathVariable("target") String target, Model m,
			@PathVariable(value = "index", required = false) Optional<Integer> i) {
		session.removeAttribute("listPro");
		System.out.println(target + "jasd");
		Pageable pageable = PageRequest.of(i.orElse(0), 8);
		Page<Products> listTarget = productsServiceImp.findByTargetAudience(pageable, target);

		List<Products> productList = new ArrayList<>(listTarget.getContent());
		Collections.shuffle(productList);

		Page<Products> shuffledPage = new PageImpl<>(productList, pageable, productList.size());

		session.setAttribute("listPro", listTarget);

		m.addAttribute("queryPath", target);
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

	@GetMapping({"/getCatogery/{i}/{id}" })
	public String getProByCatoId(@PathVariable(value="i" , required = false) Optional<Integer> i, @PathVariable("id") int id, Model m) {
		if (id > 0) {
			System.out.println(id + "i");
			Pageable pageable = PageRequest.of(i.orElse(0), 12);
			Page<Products> page = productsServiceImp.getProByCatoId(pageable, id);
//			List<Products> productList = new ArrayList<>(page.getContent());
//			Collections.shuffle(productList);
//			page = new PageImpl<>(productList, pageable, productList.size());

			m.addAttribute("queryPath", id);
			m.addAttribute("url", "getCatogery");
			session.setAttribute("listPro", page);
		}
		return "/views/shop";
	}

	@GetMapping({ "/getHotTrend", "/getHotTrend/{i}" })
	public String getHotTrend(Model m, @PathVariable("i") Optional<Integer> i) {
		int pageNumber = i.orElse(0); // Lấy số trang từ Optional, mặc định là 0 nếu không có giá trị
		int pageSize = 10; // Kích thước trang mong muốn, bạn có thể thay đổi theo ý muốn

		Pageable pageable = PageRequest.of(pageNumber, pageSize);
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
		System.out.println(PageHotTrend.getTotalElements());
		session.setAttribute("listPro", PageHotTrend);
		return "/views/shop";
	}

	@GetMapping("orderSucess")
	public String orderSucess() {
		return "views/Success";
	}

	// Khoảng Giá
	@RequestMapping({ "/PriceRange", "/PriceRange/{i}" })
	public String price_range(@RequestParam(name = "min", required = true) String min, @RequestParam("max") String max,
			Model m, @PathVariable("i") Optional<Integer> i) {
		Pageable pageable = PageRequest.of(i.orElse(0), 12);

		if (min.contains(",")) {
			min = min.replaceAll(",", "");
		}
		if (max.contains(",")) {
			max = max.replaceAll(",", "");
		}
		Double priceMin = Double.parseDouble(min);
		Double priceMax = Double.parseDouble(max);

//		// Mã hóa các tham số min và max trong đường dẫn
//		String encodedMin = URLEncoder.encode(String.valueOf(min), StandardCharsets.UTF_8);
//		String encodedMax = URLEncoder.encode(String.valueOf(max), StandardCharsets.UTF_8);

		m.addAttribute("min", priceMin);
		m.addAttribute("max", priceMax);
		Page<Products> page = productsServiceImp.PriceRrange(pageable, priceMin, priceMax);
		session.setAttribute("listPro", page);
		m.addAttribute("url", "price_range");
		m.addAttribute("query", "min=" + priceMin + "&max=" + priceMax);
		return "/views/shop";
	}

	// lọc giá
	@GetMapping({ "/PriceFilter", "/PriceFilter/{i}" })
	public String PriceFilter(@RequestParam(name = "price", required = false) String price, Model m,
			@PathVariable("i") Optional<Integer> i) {
		Pageable pageable = PageRequest.of(i.orElse(0), 12);

		if (price != null && price.equals("ASC")) {
			Page<Products> pageASC = productsServiceImp.PriceASC(pageable);
			session.setAttribute("listPro", pageASC);
			pageASC.stream().map(p -> p.getPrice()).forEach(item -> System.out.println(item));

		} else if (price != null && price.equals("DESC")) {
			Page<Products> pageDESC = productsServiceImp.PriceDESC(pageable);
			session.setAttribute("listPro", pageDESC);

		}

		m.addAttribute("filterSelect", price);
		m.addAttribute("url", "PriceFilter");
		m.addAttribute("query", "price=" + price);
		return "/views/shop";

	}

	// gioi hạn giá
	@GetMapping({ "/PriceLimit", "/PriceLimit/{i}" })
	public String PriceRange(@RequestParam(name = "price", required = false) String price,
			@PathVariable("i") Optional<Integer> i, Model m) {

		String cleanPrice = price.replace(",", "").replaceAll("\\s", "").toLowerCase();
		Page<Products> pagePro = null;

		int priceMin = 0;
		int priceMax = 0;
		if (cleanPrice.contains("trên")) {
			Pageable pageable = PageRequest.of(i.orElse(0), 12);
			String[] priceRangeMin = cleanPrice.split("trên");
			// chuổi phía trc là chữ 'trên' chuổi 2 là 500000
			priceMax = Integer.parseInt(priceRangeMin[1]);
			System.out.println(i + "sd");
			pagePro = productsServiceImp.ByPriceDESC(pageable, priceMax);
			// do tham số đọc lần đầu bằng @requesparam là 1 chuổi nên trả về lại 1 chuổi
			String priceQuery = String.valueOf(priceMax);
			m.addAttribute("query", "price=Trên " + priceQuery);
		}
		if (cleanPrice.contains("dưới")) {
			Pageable pageable = PageRequest.of(i.orElse(0), 12);
			String[] priceRangeMin = cleanPrice.split("dưới");
			priceMin = Integer.parseInt(priceRangeMin[1]);
			pagePro = productsServiceImp.ByPriceASC(pageable, priceMin);
			String priceQuery = String.valueOf(priceMin);
			m.addAttribute("query", "price=Dưới " + priceQuery);
		}

		else {
			Pageable pageable = PageRequest.of(i.orElse(0), 12);

			String[] priceRange = cleanPrice.split("-");
			if (priceRange.length == 2) { // độ dài đủ 2 mảng
				priceMin = Integer.parseInt(priceRange[0]);
				priceMax = Integer.parseInt(priceRange[1]);
				pagePro = productsServiceImp.PriceRrange(pageable, priceMin, priceMax);
				m.addAttribute("query", "priceMin=" + priceMin + "&priceMax=" + priceMax);
			}

			// pagePro.stream().map(Products::getPrice).forEach(item ->
			// System.out.println(item));
		}
		session.setAttribute("listPro", pagePro);
		m.addAttribute("url", "PriceLimit");

		return "/views/shop";
	}
}
