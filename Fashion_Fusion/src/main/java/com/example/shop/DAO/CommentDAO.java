package com.example.shop.DAO;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.example.shop.model.Comment;

public interface CommentDAO extends JpaRepository<Comment, Integer> {

//	@Query("select p From Comment where p.product_id=?1 ")
//	public Comment getCmt(int id);
}
