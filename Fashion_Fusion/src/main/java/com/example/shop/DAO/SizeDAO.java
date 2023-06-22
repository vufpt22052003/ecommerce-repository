package com.example.shop.DAO;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.example.shop.model.Size;

public interface SizeDAO extends JpaRepository<Size, Integer> {

	@Query("select s from Size s where s.product_id.id=?1 ")
	List<Size> listSize(int id);
}
