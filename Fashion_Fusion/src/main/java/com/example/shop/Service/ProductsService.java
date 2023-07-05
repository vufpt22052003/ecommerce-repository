package com.example.shop.Service;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import com.example.shop.model.Category;
import com.example.shop.model.Color;
import com.example.shop.model.ImgRelateTo;
import com.example.shop.model.Products;
import com.example.shop.model.Sale;
import com.example.shop.model.Size;

public interface ProductsService {

	Page<Products> getAllProduct(Pageable pageable);

	Page<Products> PriceASC(Pageable pageable);

	Page<Products> PriceDESC(Pageable pageable);

	Page<Products> PriceRrange(Pageable pageable, double min, double max);

	// List<Category> getListCato();

	Optional<Products> findById(int id);

	List<Size> getListSize(int id);

	List<Color> getListColor(int id);

	List<String> ListImgRrelateTo(int id);

	Page<Sale> getListSale(Pageable pageable);

	Optional<Sale> finByOptionalSale(int id);

	int sale_percent(int id);

	List<Products> findByName(String name);


	List<Sale> findSalesByIsSale();

	Page<Products> searchByName(Pageable pageable, String name);


	List<Products> findTop8Products(Pageable pageable);

	void delProById(int id);

	Page<Products> finAllByUser(Pageable pageable, int uid);


	List<Category> getListCato();

	Optional<Category> getCategoryByPro(int id);


	Page<Products> getProByCatoId(Pageable pageable, int id);


	Page<Products> findByTargetAudience(Pageable pageable, String target_aud);

	List<Products> findAll();


	Page<Products> ByPriceASC(Pageable pageable, double price);

	Page<Products> ByPriceDESC(Pageable pageable, double price);




}
