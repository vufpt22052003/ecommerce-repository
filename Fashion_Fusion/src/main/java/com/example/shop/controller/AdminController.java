package com.example.shop.controller;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.*;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import org.apache.catalina.User;
import org.apache.catalina.connector.Request;
import org.apache.catalina.connector.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.example.shop.APIManager.ProductAPI;
import com.example.shop.DAO.CategoryDAO;
import com.example.shop.DAO.ColorDAO;
import com.example.shop.DAO.ImgRelateToDAO;
import com.example.shop.DAO.ProductsDAO;
import com.example.shop.DAO.SaleDAO;
import com.example.shop.DAO.SizeDAO;
import com.example.shop.Service.OrderDetailsService;
import com.example.shop.Service.OrderService;
import com.example.shop.ServiceImp.AdminServiceImp;
import com.example.shop.ServiceImp.ProductsServiceImp;
import com.example.shop.model.Category;
import com.example.shop.model.Color;
import com.example.shop.model.ImgRelateTo;
import com.example.shop.model.Order_details;
import com.example.shop.model.ProductDTO;
import com.example.shop.model.Products;
import com.example.shop.model.Sale;
import com.example.shop.model.Size;
import com.example.shop.model.Users;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.val;

@Controller
//@MultipartConfig
public class AdminController {

	@Autowired
	AdminServiceImp adminServiceImp;
	@Autowired
	HttpSession session;
	@Autowired
	ProductsDAO productsDAO;
	@Autowired
	HttpServletRequest request;
	@Autowired
	ProductsServiceImp productsServiceImp;
	@Autowired
	OrderDetailsService orderDetailsService;
	@Autowired
	CategoryDAO categoryDAO;
	@Autowired
	SizeDAO sizeDAO;
	@Autowired
	ColorDAO colorDAO;

	@Autowired
	SaleDAO saleDAO;
	@Autowired
	OrderService orderService;

	@Autowired
	ImgRelateToDAO imgRelateToDAO;

	@GetMapping("admin")
	public String admin(Model m) {

		return "/Admin/index";
	}

	// đọc dử liệu cho việc thêm sp hoặc edit
	public void getForm(@ModelAttribute Products pro, Products product) {
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm");
		String currentDateTime = formatter.format(new Date());
		Date created_day;
		try {
			created_day = formatter.parse(currentDateTime);

			product.setName(pro.getName());
			product.setDescription(pro.getDescription());
			product.setPrice(pro.getPrice());
			System.out.println(pro.getIn_stock());
			product.setIn_stock(pro.getIn_stock());
			product.setOrigin(pro.getOrigin());
			product.setTarget_audience(pro.getTarget_audience());
			product.setCreated_day(created_day);
			Users acc = (Users) session.getAttribute("acc");

			product.setUser_id(acc);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@GetMapping("product")
	public String product(Model m) {
		return "/Admin/edit-product";
	}

	@PostMapping("addSale")
	public String addSale(@RequestParam("Pid") int pid) throws ParseException {

		Sale sale = new Sale();
		String price_sale = request.getParameter("sale_price");

		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm");
		String start_datetime_str = request.getParameter("start_datetime");
		Date start_datetime = formatter.parse(start_datetime_str);

		String end_datetime_str = request.getParameter("end_datetime");
		Date end_datetime = (Date) formatter.parse(end_datetime_str);

		String contend_sale = request.getParameter("contend_sale");
		// luu sale
		Products product = new Products();
		Optional<Products> pro = productsDAO.findById(pid);
		sale.setPrice_sale(Double.parseDouble(price_sale));
		sale.setContend_sale(contend_sale);

		sale.setProduct_id(pro.get());
		sale.setStart_date(start_datetime);
		sale.setEnd_date(end_datetime);

		saleDAO.save(sale);
		return "redirect:/saleManage";

	}

	@PostMapping("post")
	public String upload(@ModelAttribute Products pro, @RequestParam(value = "images[]") MultipartFile[] images,
			@RequestParam("image") MultipartFile image, @RequestParam(value = "sizes[]") String[] sizes,
			@RequestParam(value = "color[]", required = false) String[] color,
			@RequestParam(value = "Pid", required = false, defaultValue = "0") int Pid,
			@RequestParam(value = "start_datetime", required = false) String start_datetime_str,
			@RequestParam(value = "end_datetime", required = false) String end_datetime_str,
			@RequestParam(value = "fileNames", required = false) String[] files) {
		Products product = new Products();
		System.out.println(files + "---");
		getForm(pro, product);
		try {
			// Xử lý và lưu giá trị category_id
			int cid = Integer.parseInt(request.getParameter("category_id"));
			Optional<Category> optionalCategory = categoryDAO.findById(cid);
			if (optionalCategory.isPresent()) {
				Category category = optionalCategory.get();
				product.setCategory(category);
			} else {
				System.out.println(optionalCategory);
			}

			// Tiếp tục xử lý và lưu các giá trị khác của sản phẩm
			Users acc = (Users) session.getAttribute("acc");
			pro.setUser_id(acc);
			pro.set_promo(true);

			Path path = Paths.get("src/main/resources/static/images/");
			Path filePath = path.resolve(image.getOriginalFilename());

			Optional<Products> finByIdPro = productsDAO.findById(Pid);
			if (!finByIdPro.isPresent()) {
				// nếu id k tồn tại thì thêm
				try (InputStream inStream = image.getInputStream()) {
					Files.copy(inStream, filePath, StandardCopyOption.REPLACE_EXISTING);
					product.setImg(image.getOriginalFilename().toLowerCase());
				} catch (IOException e) {
					e.printStackTrace();
				}
				// Lưu sản phẩm
				adminServiceImp.addProduct(product);
				// Lưu size

				if (sizes != null) {
					for (String size : sizes) {
						Size sizeEntity = new Size();
						sizeEntity.setSize(size);
						sizeEntity.setProduct_id(product);
						sizeDAO.save(sizeEntity);
					}
				}
				// Lưu color (nếu có)
				if (color != null) {
					for (String clr : color) {
						Color colorEntity = new Color();
						colorEntity.setColor(clr);
						colorEntity.setProduct_id(product);
						colorDAO.save(colorEntity);
					}
				}
				// Đọc và lưu trữ các tệp tin ảnh
				for (MultipartFile file : images) {
					if (!file.isEmpty()) {
						System.out.println(file.getOriginalFilename() + "-----");
						String fileName = file.getOriginalFilename();
						Path filePaths = path.resolve(fileName);

						try (InputStream inputStream = file.getInputStream()) {
							Files.copy(inputStream, filePaths, StandardCopyOption.REPLACE_EXISTING);
							System.out.println(fileName);
							ImgRelateTo imgRelateTo = new ImgRelateTo();
							imgRelateTo.setImg_relate_to(fileName.toLowerCase());
							imgRelateTo.setProduct_id(product);
							imgRelateToDAO.save(imgRelateTo);
						} catch (IOException e) {
							e.printStackTrace();
						}
					}
				}
			} else {
				// nếu id tồn tại thì đoạn này dùng edit
				Products existingProduct = finByIdPro.get();
				// Xử lý và lưu giá trị category_id
				if (optionalCategory.isPresent()) {
					Category category = optionalCategory.get();
					existingProduct.setCategory(category);
				} else {
					System.out.println(optionalCategory);
				}
				// Thực hiện chỉnh sửa trên existingProduct
				getForm(product, existingProduct);

				// Kiểm tra xem người dùng đã chọn một tệp hình ảnh mới hay chưa
				if (!image.isEmpty()) {
					try (InputStream inStream = image.getInputStream()) {
						Files.copy(inStream, filePath, StandardCopyOption.REPLACE_EXISTING);
						existingProduct.setImg(image.getOriginalFilename().toLowerCase());
					} catch (IOException e) {
						e.printStackTrace();
					}
				}

				// Lưu lại sản phẩm đã chỉnh sửa vào cơ sở dữ liệu
				adminServiceImp.editProduct(existingProduct, Pid);

//				// size
//				List<Size> size = sizeDAO.listSize(existingProduct.getId());
//				// Xóa các bản ghi cũ
//				for (Size colorItem : size) {
//					sizeDAO.delete(colorItem);
//				}
//				if (size != null && !size.isEmpty()) {
//					if (sizes != null) {
//						for (String sizeItem : sizes) {
//							if (sizeItem != null && !sizeItem.isEmpty()) { // Kiểm tra size có giá trị hợp lệ không
//
//								Size sizeEntity = new Size();
//								sizeEntity.setSize(sizeItem);
//								sizeEntity.setProduct_id(existingProduct);
//								sizeDAO.save(sizeEntity);
//							}
//						}
//					}
//				}
				// edit size
				adminServiceImp.updateSzie(Pid, sizes);

				// edit color
				adminServiceImp.updateColor(Pid, color);
				// Lấy danh sách các bản ghi có product_id bằng existingProduct.getId()
				List<ImgRelateTo> imgs = imgRelateToDAO.findByProductId(existingProduct.getId());
				if (imgs != null && !imgs.isEmpty()) {

					// Xóa các bản ghi cũ
					for (ImgRelateTo itemImg : imgs) {
						imgRelateToDAO.delete(itemImg);
					}

					for (String file : files) {
						if (!file.isEmpty()) {
							// Path filePaths = path.resolve(file);
							ImgRelateTo imgRelateTo = new ImgRelateTo();

							imgRelateTo.setImg_relate_to(file.toLowerCase());
							imgRelateTo.setProduct_id(existingProduct);
							imgRelateToDAO.save(imgRelateTo);
						}
					}
				}

				for (MultipartFile file : images) {
					if (!file.isEmpty()) {
						System.out.println(file.getOriginalFilename() + "-----");
						String fileName = file.getOriginalFilename();
						Path filePaths = path.resolve(fileName);

						try (InputStream inputStream = file.getInputStream()) {
							Files.copy(inputStream, filePaths, StandardCopyOption.REPLACE_EXISTING);
							System.out.println(fileName);
							ImgRelateTo imgRelateTo = new ImgRelateTo();
							imgRelateTo.setImg_relate_to(fileName.toLowerCase());
							imgRelateTo.setProduct_id(existingProduct);
							imgRelateToDAO.save(imgRelateTo);
						} catch (IOException e) {
							e.printStackTrace();
						}
					}
				}

			}

			// Lưu sale (nếu ngày được nhập)
			if (start_datetime_str != null && !start_datetime_str.isEmpty() && end_datetime_str != null
					&& !end_datetime_str.isEmpty()) {
				SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm");
				Date start_datetime = formatter.parse(start_datetime_str);
				Date end_datetime = formatter.parse(end_datetime_str);
				String contend_sale = request.getParameter("contend_sale");
				String price_sale = request.getParameter("sale_price");

				Sale sale = new Sale();
				sale.setStart_date(start_datetime);
				sale.setEnd_date(end_datetime);
				sale.setContend_sale(contend_sale);
				sale.setPrice_sale(Double.parseDouble(price_sale));
				sale.setProduct_id(product);

				Calendar startDate = Calendar.getInstance();
				startDate.setTime(sale.getStart_date());
				Calendar endDate = Calendar.getInstance();
				endDate.setTime(sale.getEnd_date());

				saleDAO.save(sale);
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		return "redirect:/success";
	}

	@PostMapping("/editSale")
	public String editSale(@ModelAttribute Sale sale) {
		System.out.println("----");
		try {
			SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm");

			// Định dạng ngày tháng bắt đầu
			String startDatetimeStr = request.getParameter("startDate");
			Date startDatetime = formatter.parse(startDatetimeStr);
			sale.setStart_date(startDatetime);

			// Định dạng ngày tháng kết thúc
			String endDatetimeStr = request.getParameter("endDate");
			Date endDatetime = formatter.parse(endDatetimeStr);
			sale.setEnd_date(endDatetime);

			// productsDAO.findById(id);
			// sale.setProduct_id(sale.getProduct_id().getId());
			saleDAO.save(sale);

			ProductAPI api = new ProductAPI();
			api.getSaleById(sale.getId());

		} catch (Exception e) {
			e.printStackTrace();
			// Xử lý lỗi khi không thể chuyển đổi định dạng
		}
		return "redirect:/saleManage";
	}

//	// thêm cato
//	@GetMapping("/add_cato")
//	public String add_cato(@RequestParam("cato") String cato) {
//		Category category = new Category();
//		category.setName(cato);
//		Users acc = (Users) session.getAttribute("acc");
//
//		category.setUid_catogery(acc);
//		categoryDAO.save(category);
//		return "/Admin/edit-product";
//	}

	@GetMapping("saleManage")
	public String sale() {

		return "OrderAdmin/saleManage";
	}

	@GetMapping("/homeAdmin")
	public String OrderAdmin() {
		return "OrderAdmin/home";
	}

	@GetMapping("/Manager")
	public String table() {
		return "OrderAdmin/AdminOdManager";
	}

	@GetMapping("/addPro")
	public String form() {
		return "OrderAdmin/ProductCreator";
	}

	// sữa sản phẩm
	@GetMapping("/{id}")
	public String edit(@PathVariable("id") int id, Model model) {

		Optional<Products> product = productsServiceImp.findById(id);

		Optional<Category> catogery = productsServiceImp.getCategoryByPro(id);
		// đổ list category
		List<Category> listCato = productsServiceImp.getListCato();
		List<String> listImg = productsServiceImp.ListImgRrelateTo(id);

		// list màu sizeLi
		List<Color> listColor = productsServiceImp.getListColor(id);
		List<Size> listSize = productsServiceImp.getListSize(id);

		model.addAttribute("product", product.orElse(new Products())); // Lưu product vào model
		model.addAttribute("catogery", catogery.orElse(new Category())); // Lưu product vào model

		model.addAttribute("listImg", listImg); // Lưu listImg vào model

		model.addAttribute("listCato", listCato); // Lưu listImg vào model
		model.addAttribute("listColor", listColor); // Lưu listCato vào model
		model.addAttribute("listSize", listSize); // Lưu listCato vào model

		return "OrderAdmin/editProduct";
	}

	@GetMapping("success")
	public String sc() {
		return "OrderAdmin/Success";
	}

}
