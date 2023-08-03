package com.example.shop.APIManager;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.shop.ServiceImp.CommentServiceImp;
import com.example.shop.model.Comment;

import jakarta.servlet.http.HttpSession;

@RestController
public class CommentAPI {
	@Autowired
	CommentServiceImp commentServiceImp;

	@Autowired
	HttpSession session;
	
	@RequestMapping("/api/getComment")

	public ResponseEntity<List<Comment>> getCmt(@RequestParam("ProductId") int id, @RequestParam("type") String type) {
	
		
		ArrayList<Comment> listCmt = (ArrayList<Comment>) commentServiceImp.getCmt(id);
		return new ResponseEntity<>(listCmt, HttpStatus.OK);
	}

//	@RequestMapping("/api/getCmtByUserId")
//	public ResponseEntity<List<Comment>> getCmtByUserId() {
//		return new ResponseEntity<>(listCmtByUs, HttpStatus.OK);
//	}

	@RequestMapping("/api/deleteCmt")
	public ResponseEntity<List<Comment>> getCmtByUserId(@RequestParam("id") int id) {
		commentServiceImp.deleteCmt(id);
		return new ResponseEntity<>(HttpStatus.OK);
	}

//	@RequestMapping("/api/findOrderByRatingDesc")
//	public ResponseEntity<List<Comment>> getCmt() {
//		ArrayList<Comment> listCmt = (ArrayList<Comment>) commentServiceImp.findOrderByRatingDesc();
//		return new ResponseEntity<>(listCmt, HttpStatus.OK);
//	}

	@RequestMapping("/api/getCmtByUserId")
	public ResponseEntity<List<Comment>> getCmt(@RequestParam("type") String type) {
		System.out.println(type + "tipe");
		ArrayList<Comment> listCmt = null;
		if (type.equals("all")) {
			listCmt = (ArrayList<Comment>) commentServiceImp.getCmtByUserId();
		}
		if (type.equals("rating")) {
			listCmt = (ArrayList<Comment>) commentServiceImp.findOrderByRatingDesc();
		}
		return new ResponseEntity<>(listCmt, HttpStatus.OK);
	}
}
