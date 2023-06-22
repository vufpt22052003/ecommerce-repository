package com.example.shop.ServiceImp;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.example.shop.DAO.CategoryDAO;
import com.example.shop.DAO.ColorDAO;
import com.example.shop.DAO.ProductsDAO;
import com.example.shop.DAO.SaleDAO;
import com.example.shop.DAO.SizeDAO;
import com.example.shop.Service.ProductsService;
import com.example.shop.model.Category;
import com.example.shop.model.Color;
import com.example.shop.model.Order_details;
import com.example.shop.model.Products;
import com.example.shop.model.Sale;
import com.example.shop.model.Size;

import jakarta.servlet.http.HttpSession;

@Service
public class ProductsServiceImp implements ProductsService {

	@Autowired
	ProductsDAO productsDAO;

	@Autowired
	CategoryDAO categoryDAO;

	@Autowired
	SizeDAO sizeDAO;

	@Autowired
	SaleDAO saleDAO;

	@Autowired
	ColorDAO colorDAO;
	@Autowired
	HttpSession session;

	@Override
	public Page<Products> getAllProduct(Pageable pageable) {
		return productsDAO.findAll(pageable);
	}

	@Override
	public Page<Products> PriceASC(Pageable pageable) {
		return productsDAO.PriceASC(pageable);
	}

	@Override
	public Page<Products> PriceDESC(Pageable pageable) {
		return productsDAO.PriceDESC(pageable);
	}

	@Override
	public Page<Products> PriceRrange(Pageable pageable, double min, double max) {
		return productsDAO.PriceRrange(pageable, min, max);
	}

	
	@Override
	public Optional<Products> findById(int id) {
		return productsDAO.findById(id);
	}

	@Override
	public List<Size> getListSize(int id) {
		return sizeDAO.listSize(id);
	}

	@Override
	public List<Color> getListColor(int id) {
		return colorDAO.listColor(id);
	}

	@Override
	public List<String> ListImgRrelateTo(int id) {
		return productsDAO.ListImgRrelateTo(id);
	}

	@Override
	public Page<Sale> getListSale(Pageable pageable) {
		return saleDAO.findAll(pageable);
	}

	@Override
	public Optional<Sale> finByOptionalSale(int id) {
		Sale sale = saleDAO.finByIdSale(id);
		return Optional.ofNullable(sale);
	}

	@Override
	public List<Products> findByName(String name) {
		return productsDAO.findByName(name);
	}

	@Override
	public int sale_percent(int id) {
		Optional<Sale> finByIdSale = Optional.ofNullable(saleDAO.finByIdSale(id));
		if (finByIdSale.isPresent()) {
			double price_product = finByIdSale.get().getProduct_id().getPrice();
			double price_sale = finByIdSale.get().getPrice_sale();

			if (price_sale < price_product) {
				double discountPercentage = ((price_product - price_sale) / price_product) * 100;
				int discountPercentageInt = (int) Math.round(discountPercentage);

				return discountPercentageInt;
			}
		}
		// Không tìm thấy Sale
		return 0;
	}

	// lấy danh sách sp theo uses
	@Override
	public Page<Products> finAllByUser(Pageable pageable , int uid) {
		return productsDAO.finAllByUser(pageable, uid);

	}

	// lây category theo id product
	@Override
	public Optional<Category> getCategoryByPro(int id) {
		return Optional.ofNullable(categoryDAO.getCategoryByIdPro(id));
	}

	@Override
	public List<Sale> findSalesByIsSale() {
		return saleDAO.findSalesByIsSale();
	}

	// laasy khi tìm danh scash
	@Override
	public Page<Products> searchByName(Pageable pageable, String name) {
		return productsDAO.searchByName(pageable, name);
	}

	@Override
	public List<Products> findTop8Products(Pageable pageable) {
		return productsDAO.findTop8Products(pageable);
	}

	@Override
	public void delProById(int id) {
	    Optional<Products> proOptional = productsDAO.findById(id);
	    if (proOptional.isPresent()) {
	        productsDAO.deleteById(proOptional.get().getId());
	    }
	}
	
	@Override
	public Page<Products> findByTargetAudience(Pageable pageable , String target_aud) {
		return  productsDAO.findByTargetAudience(pageable , target_aud);
	}
	@Override
	public List<Category> getListCato() {
		return categoryDAO.findAll();
	}
	
	@Override
	public Page<Products> getProByCatoId(Pageable pageable , int id ) {
		return productsDAO.getProByCatoId(pageable , id);
	}


	@Override
	public List<Products> findAll() {
		return productsDAO.findAll();
	}
	

}
