package com.example.shop.DAO;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.example.shop.model.Comment;

public interface CommentDAO extends JpaRepository<Comment, Integer> {

	@Query("select p From Comment p where p.product_id.id=?1 ")
	public List<Comment> getCmtByIdPro(int id);

	@Query(value = "SELECT Comment.*\r\n" + "FROM Comment\r\n" + "JOIN Products ON Comment.product_id = Products.id\r\n"
			+ "WHERE Products.user_id = ?;\r\n" + "", nativeQuery = true)
	public List<Comment> getCmtByUserId(int id);

	@Query(value = "SELECT Comment.* from Comment\r\n" + "JOIN Products ON Comment.product_id = Products.id\r\n"
			+ "where Products.user_id = ?  ORDER BY rating DESC ;", nativeQuery = true)
	public List<Comment> findOrderByRatingDesc(int uid);

	

}
