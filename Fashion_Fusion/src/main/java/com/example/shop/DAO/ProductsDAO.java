package com.example.shop.DAO;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.example.shop.model.Order_details;
import com.example.shop.model.Products;

public interface ProductsDAO extends JpaRepository<Products, Integer> {

	@Query(value = "select *  from Products ORDER BY price ASC", nativeQuery = true)
	Page<Products> PriceASC(Pageable pageable);

	@Query(value = "SELECT * FROM Products WHERE price <= ?;", nativeQuery = true)
	Page<Products> ByPriceASC(Pageable pageable, double price);

	@Query(value = "select * from Products where price >= ?", nativeQuery = true)
	Page<Products> ByPriceDESC(Pageable pageable, double price);

	@Query(value = "select *  from Products ORDER BY price DESC", nativeQuery = true)
	Page<Products> PriceDESC(Pageable pageable);

	@Query("select p from Products p where p.price BETWEEN ?1 AND ?2 ")
	Page<Products> PriceRrange(Pageable pageable, double min, double max);

	@Query(value = "SELECT img_relate_to.img_relate_to " + "FROM img_relate_to "
			+ "JOIN Products ON img_relate_to.product_id = Products.id "
			+ "WHERE img_relate_to.product_id = ?;", nativeQuery = true)
	List<String> ListImgRrelateTo(int id);

	// lấy theo liên quan
	@Query("SELECT p FROM Products p WHERE p.name LIKE %:name% ORDER BY p.id DESC LIMIT 6")
	List<Products> findByName(String name);


	// lấy danh sách sp theo uses
	@Query("SELECT p FROM Products p WHERE p.user_id.id = ?1")
	Page<Products> finAllByUser(Pageable pageable, int uid);

	// lấy danh sách sp theo uses
	@Query(value = "SELECT * FROM ( " + "    SELECT *, ROW_NUMBER() OVER (ORDER BY (SELECT NULL)) AS RowNum "
			+ "    FROM Products " + "    WHERE user_id = ?1 " + ") AS SubQuery " + "WHERE RowNum > 1 "
			+ "ORDER BY RowNum " + "OFFSET ?2 ROWS " + "FETCH NEXT ?3 ROWS ONLY", nativeQuery = true)
	List<Products> getProductByUser(int userId, int offset, int limit);

	// lấy danh sách khi tim kiếm
	@Query("SELECT p FROM Products p WHERE p.name LIKE %:keyword%")
	Page<Products> searchByName(Pageable pageable, String keyword);

	// lấy danh sách theo id catogery
	@Query("SELECT p from Products p where p.category.id = ?1")
	Page<Products> getProByCatoId(Pageable pageable, int id);

	@Query(value = "SELECT * FROM Products ORDER BY id DESC LIMIT 8", nativeQuery = true)
	List<Products> findTop8Products();

	// lấy danh sách với điều kiện danhf cho ai ( nam nữ trẻ em )
	@Query(value = " select * from Products where target_audience = ?", nativeQuery = true)
	Page<Products> findByTargetAudience(Pageable pageable, String target_aud);

	// lấy sp mới
	@Query(value = "SELECT * FROM Products\r\n"
			+ "WHERE created_day >= DATEADD(DAY, -7, CONVERT(DATETIME, CONVERT(DATE, GETDATE()), 101));\r\n"
			+ "", nativeQuery = true)
	Page<Products> getProNew(Pageable pageable);

}
