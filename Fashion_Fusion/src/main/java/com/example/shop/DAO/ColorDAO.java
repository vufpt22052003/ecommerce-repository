package com.example.shop.DAO;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.example.shop.model.Color;
import com.example.shop.model.Products;

public interface ColorDAO extends JpaRepository<Color, Integer> {
	@Query("select c from Color c where c.product_id.id = ?1 ")
	List<Color> listColor(int id);

	@Modifying
	@Query(value = "UPDATE Color SET color=? WHERE id=?", nativeQuery = true)
	Color updateColor(String color, int id);
}
