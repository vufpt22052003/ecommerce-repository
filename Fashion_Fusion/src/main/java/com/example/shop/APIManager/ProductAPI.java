package com.example.shop.APIManager;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.shop.DAO.CategoryDAO;
import com.example.shop.DAO.SaleDAO;
import com.example.shop.Service.OrderDetailsService;
import com.example.shop.Service.OrderService;
import com.example.shop.ServiceImp.AdminServiceImp;
import com.example.shop.ServiceImp.ProductsServiceImp;
import com.example.shop.model.Category;
import com.example.shop.model.ProductDTO;
import com.example.shop.model.Products;
import com.example.shop.model.Sale;
import com.example.shop.model.Users;

import jakarta.servlet.http.HttpSession;

@RestController
public class ProductAPI {
	@Autowired
	ProductsServiceImp productsServiceImp;
	@Autowired
	SaleDAO saleDAO;
	@Autowired
	OrderService orderService;
	@Autowired
	AdminServiceImp adminServiceImp;
	@Autowired
	OrderDetailsService orderDetailsService;
	@Autowired
	CategoryDAO categoryDAO;
	@Autowired
	HttpSession session;

	// lấy ra sản phẩm nào đang sale
	@GetMapping("/api/onSale")
	public ResponseEntity<List<Sale>> onsale(@RequestParam(value = "startDate", required = false) Date start,
			@RequestParam(value = "endDate", required = false) Date end,
			@RequestParam(value = "type", defaultValue = "all") String type) throws ParseException {

		List<Sale> list = null;
 		// lấy ngày hiện tại
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date currentDate = new Date();
		String formattedDate = formatter.format(currentDate);
		Date date = formatter.parse(formattedDate);

		if (type.equals("onSale")) {
			list = productsServiceImp.findSalesByIsSale();
		}

		if (type.equals("comingSoon")) {
			list = saleDAO.findSalesAfterStartDate(date);
		}
		if (type.equals("noSale")) {
			list = saleDAO.findSalesBeforeEndDate(date);
		}
		return new ResponseEntity<>(list, HttpStatus.OK);
	}

	// xoa sale
	@RequestMapping("/api/delSale")
	public ResponseEntity<Void> delSale(@RequestParam("id") int sid) {
		System.out.println(sid + "sid");
		saleDAO.deleteById(sid);
		return new ResponseEntity<>(HttpStatus.OK);

	}

	// api sản phẩm
	@GetMapping("api/listProByUs/{i}")
	public ResponseEntity<Page<Products>> finAllByUser(@PathVariable("i") Optional<Integer> i) {
		Pageable pageable = PageRequest.of(i.orElse(0), 5);

		Page<Products> list = productsServiceImp.finAllByUser(pageable, 1);
		// Xử lý dữ liệu và gán giá trị cho thuộc tính checkSale
		for (Products product : list) {
			boolean hasSale = product.checkSale() == false;
			product.setCheckIsSale(hasSale);
		}
		return new ResponseEntity<>(list, HttpStatus.OK);
	}

	// lấy ra dannh sách product bao gôm ảnh và catogery theo id product
	@GetMapping("/api/showPro")
	public ResponseEntity<ProductDTO> showPro(@RequestParam("id") int id) {
		Optional<Products> productOptional = productsServiceImp.findById(id);
		List<Category> listCato = productsServiceImp.getListCato();
		List<String> imgRelateTos = productsServiceImp.ListImgRrelateTo(id);

		if (productOptional.isPresent() && listCato != null && imgRelateTos != null) {
			ProductDTO proDTO = new ProductDTO(productOptional.get(), listCato, imgRelateTos);
			return ResponseEntity.ok(proDTO);
		} else {
			return ResponseEntity.notFound().build();
		}
	}

	// timf id sale
	@GetMapping("/api/getSaleById")
	public ResponseEntity<Optional<Sale>> getSaleById(@RequestParam("id") int Sid) {
		System.out.println("---");
		Optional<Sale> sale = saleDAO.findById(Sid);
		return ResponseEntity.ok(sale);
	}

	// lấy danh sách sản phẩm
	@GetMapping({ "api/page" })
	public ResponseEntity<Page<Products>> getProduct(@PathVariable("id") Optional<Integer> id) {
		// chỉ định số lương bảng ghi
		Pageable pageable = PageRequest.of(id.orElse(0), 12);
		Page<Products> page = productsServiceImp.getAllProduct(pageable);
		return ResponseEntity.ok(page);
	}

	// xoa sp
	@RequestMapping("/deleteProduct")
	public ResponseEntity<Products> delProById(@RequestParam("id") int id) {
		productsServiceImp.delProById(id);
		return ResponseEntity.ok().build();
	}

	@GetMapping("/api/totalPrice")
	public ResponseEntity<Double> getotalPrice() {
		double total = orderDetailsService.getotalPrice();
		return new ResponseEntity<>(total, HttpStatus.OK);
	}

	
	// viet api cho catogery
	@GetMapping("/api/category")
	public ResponseEntity<List<Category>> getCategory() {

		List<Category> listCato = adminServiceImp.listCategory();
		return new ResponseEntity<>(listCato, HttpStatus.OK);
	}

}
