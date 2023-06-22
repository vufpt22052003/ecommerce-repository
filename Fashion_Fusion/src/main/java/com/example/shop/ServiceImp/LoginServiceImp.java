package com.example.shop.ServiceImp;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.shop.DAO.LoginDAO;
import com.example.shop.Service.LoginService;
import com.example.shop.model.Users;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Service
public class LoginServiceImp implements LoginService {

	@Autowired
	LoginDAO logindao;

	@Autowired
	HttpServletRequest request;

	@Autowired
	HttpServletResponse response;

	@Override
	public Users checkLogin(String name, String pass) {
		Users acc = logindao.checkLogin(name, pass);
		if (acc != null) {
			Cookie usernameCookie = new Cookie("us", name);
			Cookie passCookie = new Cookie("pass", pass);

			usernameCookie.setMaxAge(3600); // Thời gian tồn tại cookie là 1 giờ
			passCookie.setMaxAge(3600);
			usernameCookie.setPath("/");
			passCookie.setPath("/");
			response.addCookie(usernameCookie);
			response.addCookie(passCookie);
		}
		return acc;
	}

}
