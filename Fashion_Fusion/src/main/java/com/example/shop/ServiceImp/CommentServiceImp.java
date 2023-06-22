package com.example.shop.ServiceImp;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.shop.DAO.CommentDAO;
import com.example.shop.DAO.ProductsDAO;
import com.example.shop.Service.CommentService;
import com.example.shop.model.Comment;
import com.example.shop.model.Products;

@Service
public class CommentServiceImp implements CommentService {

	@Autowired
	CommentDAO commentDAO;
	@Autowired
	ProductsDAO productsDAO;

//	@Override
//	public void getCmt(int productId, String content) {
//		commentDAO.get
//	}

	@Override
	public void PostCmt(int productId, String content) {
		Optional<Products> pro = productsDAO.findById(productId);
		Comment cmt = new Comment();
		cmt.setProduct_id(pro.get());
		cmt.setContents(content);
		cmt.setCreate_date("111s");
		cmt.setUid(1);
		cmt.setUpdated_at("ádl");

		commentDAO.save(cmt);
	}
}
