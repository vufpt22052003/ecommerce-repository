package com.example.shop.DAO;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.shop.model.Cart;

public interface CartDAO extends JpaRepository<Cart, Integer> {
	@Query(value = "select * from Cart where product_id= ?", nativeQuery = true)
	List<Cart> list(int pid);

	@Modifying
	@Query("UPDATE Cart c SET c.quantity = :quantity WHERE c.id = :id")
	void updateNumber(int quantity, int id);

	@Query(value = "SELECT * FROM Cart  WHERE id = ?", nativeQuery = true)
	List<Cart> findCartById(@Param("id") int id);

	@Query("select c from Cart c where c.user_id.id =?1")
	List<Cart> getCartByUserId(@Param("id") int id);
}
