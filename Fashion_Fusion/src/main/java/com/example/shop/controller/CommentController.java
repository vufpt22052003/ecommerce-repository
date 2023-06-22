package com.example.shop.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.shop.Service.CommentService;
import com.example.shop.ServiceImp.CommentServiceImp;
import com.example.shop.model.Users;

import jakarta.servlet.http.HttpSession;

@Controller
public class CommentController {

	@Autowired
	CommentServiceImp commentServiceImp;
	@Autowired
	HttpSession session;

	@RequestMapping("/postComment")
	public String postCmt(@RequestParam("ProductId") int id, @RequestParam("comment") String contend) {
		// Kiểm tra đăng nhập
		Users acc = (Users) session.getAttribute("acc");
		if (acc == null) {
			return "views/login";
		}
		System.out.println("ápdk'");
		commentServiceImp.PostCmt(id, contend);
		System.out.println(contend + "name=\"myformCmt\"");
		return "views/index";
	}
}
