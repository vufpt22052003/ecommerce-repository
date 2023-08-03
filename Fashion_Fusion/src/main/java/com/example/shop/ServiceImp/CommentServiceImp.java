package com.example.shop.ServiceImp;

import java.util.Date;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Optional;

import org.apache.catalina.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.shop.DAO.CommentDAO;
import com.example.shop.DAO.Order_detailsDAO;
import com.example.shop.DAO.ProductsDAO;
import com.example.shop.Service.CommentService;
import com.example.shop.model.Comment;
import com.example.shop.model.Products;
import com.example.shop.model.Users;

import jakarta.servlet.http.HttpSession;

@Service
public class CommentServiceImp implements CommentService {

	@Autowired
	CommentDAO commentDAO;
	@Autowired
	ProductsDAO productsDAO;

	@Autowired
	HttpSession session;
	@Autowired
	Order_detailsDAO order_detailsDAO;

	@Override
	public List<Comment> getCmt(int productId) {
		return commentDAO.getCmtByIdPro(productId);
	}

	@Override
	public Comment PostCmt(int productId, String content, int rating, int detailsId) {
		Optional<Products> pro = productsDAO.findById(productId);
		Comment cmt = new Comment();
		cmt.setProduct_id(pro.get());
		cmt.setContents(content);
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
		String currentDateTime = formatter.format(new Date());
		Date created_day;
		try {
			created_day = formatter.parse(currentDateTime);
			cmt.setCreate_date(created_day);
		} catch (Exception e) {
			e.printStackTrace();
		}
		cmt.setRating(rating);

		Users acc = (Users) session.getAttribute("acc");

		cmt.setUid(acc);

		order_detailsDAO.updateHasReviewStatus(detailsId);

		return commentDAO.save(cmt);
	}

	@Override
	public List<Comment> getCmtByUserId() {
		Users acc = (Users) session.getAttribute("acc");
		int uid = acc.getId();
		return commentDAO.getCmtByUserId(uid);
	}

	@Override
	public void deleteCmt(int id) {
		commentDAO.deleteById(id);
	}

	@Override
	public List<Comment> findOrderByRatingDesc() {
		Users acc = (Users) session.getAttribute("acc");
		int uid = acc.getId();
	
		return commentDAO.findOrderByRatingDesc(uid);
	}

}
