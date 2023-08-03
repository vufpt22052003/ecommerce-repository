package com.example.shop.ServiceImp;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.shop.DAO.UsersDAO;
import com.example.shop.Service.LoginService;
import com.example.shop.model.Users;
import com.fasterxml.jackson.annotation.JacksonInject.Value;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Service
public class LoginServiceImp implements LoginService {

	@Autowired
	UsersDAO logindao;

	@Autowired
	HttpServletRequest request;

	@Autowired
	HttpServletResponse response;

	@Override
	public Users checkLogin(String pass, String sdt) {
		Users acc = logindao.checkLogin(pass, sdt);
//		if (acc != null) {	
//			String phone = String.valueOf(false);
//			Cookie usernameCookie = new Cookie("sdt", phone);
//			Cookie passCookie = new Cookie("pass", pass);
//
//			usernameCookie.setMaxAge(3600); // Thời gian tồn tại cookie là 1 giờ
//			passCookie.setMaxAge(3600);
//			usernameCookie.setPath("/");
//			passCookie.setPath("/");
//			response.addCookie(usernameCookie);
//			response.addCookie(passCookie);
//		}

		return acc;
	}

	
}
