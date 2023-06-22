package com.example.shop.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.example.shop.DAO.LoginDAO;
import com.example.shop.Service.LoginService;
import com.example.shop.ServiceImp.LoginServiceImp;
import com.example.shop.model.Users;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@Controller
public class LoginController {

	@Autowired
	HttpServletRequest request;

	@Autowired
	LoginDAO logindao;
	@Autowired
	LoginServiceImp loginServiceImp;
	@Autowired
	HttpServletResponse response;
	@Autowired
	HttpSession session;

	@GetMapping({ "/login", "/logout" })
	public String Login() {
		String url = request.getRequestURL().toString();

		if (url.contains("logout")) {
			session.removeAttribute("acc");
			return "redirect:/index";
		}
		return "views/login";

	}

	@PostMapping("/sign_in")
	public String login(@ModelAttribute("model") Users account, Model model, HttpServletRequest request) {
//		String referer = request.getHeader("Referer");
//		System.out.println(referer);
		Users acc = loginServiceImp.checkLogin(account.getUs(), account.getPass());
		if (acc != null) {
			session.setAttribute("acc", acc);
			/*
			 * if (referer != null && !referer.isEmpty()) { return "redirect:" + referer; }
			 * else { return "redirect:/index"; }
			 */
		}

		return "redirect:/index";
	}


}
