package com.example.shop.DAO;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.example.shop.model.ImgRelateTo;

public interface ImgRelateToDAO extends JpaRepository<ImgRelateTo, Integer> {

	@Query(value = "delete from img_relate_to where product_id = ?", nativeQuery = true)
	void delByProId(int pid);
	
	@Query(value = "select * from img_relate_to where product_id = ?", nativeQuery = true)
    List<ImgRelateTo> findByProductId(int product_id);


}
