package com.example.shop.Service;

import java.util.Date;
import java.util.List;

import com.example.shop.model.Comment;

public interface CommentService {


	List<Comment> getCmt(int productId);


	List<Comment> getCmtByUserId();





	void deleteCmt(int id);


	List<Comment> findOrderByRatingDesc();


	Comment PostCmt(int productId, String content, int rating, int detailsId);
      
}
