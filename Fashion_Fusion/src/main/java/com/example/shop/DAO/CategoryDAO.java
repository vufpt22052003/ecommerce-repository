package com.example.shop.DAO;

import java.util.ArrayList;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.shop.model.Category;
import com.example.shop.model.Products;

public interface CategoryDAO extends JpaRepository<Category, Integer> {

//	@Query("select c from Category c where c.uid_catogery.id = ?1")
//	List<Category> listCategory(int uid);

//	@Query(value = "SELECT c.* FROM Category c INNER JOIN Products p ON c.id = p.category_id WHERE p.name LIKE %:key%", nativeQuery = true)
//	List<Category> findCategoriesByName(String key);

	@Query(value = "SELECT  name FROM Category c ", nativeQuery = true)
	List<Category> getListCato();

//	@Query(value = "SELECT Category.name FROM Category INNER JOIN Products ON Products.category_id = Category.id WHERE Products.id =?", nativeQuery = true)
//	Category getCategoryByIdPro(@Param("id")  int id);

	@Query("SELECT c FROM Category c JOIN c.products p WHERE p.id = ?1")
	Category getCategoryByIdPro(int id);

	// lấy đối tượng theo id catogery
	@Query("SELECT p from Category p where p.id = ?1")
	Category findByCategoryId(int id);
}