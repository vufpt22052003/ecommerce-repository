package com.example.shop.controller;

import java.sql.Date;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.shop.DAO.UsersDAO;
import com.example.shop.Service.EmailService;
import com.example.shop.Service.LoginService;
import com.example.shop.Service.UserService;
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
	UsersDAO logindao;
	@Autowired
	LoginServiceImp loginServiceImp;
	@Autowired
	HttpServletResponse response;
	@Autowired
	HttpSession session;
	@Autowired
	EmailService emailService;
	@Autowired
	UserService userService;

	@GetMapping({ "/login" })
	public String Login() {
		String url = request.getRequestURL().toString();
		if (url.contains("login")) {

			return "views/login";
		}
		if (url.contains("logout")) {
			// session.removeAttribute("acc");
			session.invalidate(); // Xóa tất cả các thuộc tính trong session

			return "redirect:/index";
		}

		return "views/login";

	}

	@PostMapping("/login")
	public String login(HttpServletRequest request, @ModelAttribute("model") Users account) {
		UserDetails userDetails = userService.loadUserByUsername(account.getSdt());

		// Lưu thông tin người dùng vào session
		HttpSession session = request.getSession();

		session.setAttribute("acc", userDetails);

		return "redirect:/login"; // Chuyển hướng đến trang chính sau khi đăng nhập thành công
	}

//	@PostMapping("/login")
//	public String login(@ModelAttribute("model") Users account, Model model, HttpServletRequest request) {
////		String referer = request.getHeader("Referer");
////		System.out.println(referer);
//		Users acc = loginServiceImp.checkLogin(account.getPass(), account.getSdt());
//		if (acc != null) {
//			session.setAttribute("acc", acc);
//			/*
//			 * if (referer != null && !referer.isEmpty()) { return "redirect:" + referer; }
//			 * else { return "redirect:/index"; }
//			 */
//		} else {
//			List<Users> list = logindao.findAll();
//			for (Users item : list) {
//				if (item.getSdt() != account.getSdt()) {
//					model.addAttribute("error", "Số Điện Thoại Không Đúng");
//				}
//				if (item.getPass() != account.getPass()) {
//					model.addAttribute("error", "Mật Khẩu Không Đúng");
//				}
//			}
//			return "redirect:/login";
//		}
//
//		return "redirect:/index";
//	}

	// Gửi email
	@PostMapping("/sendEmail")
	public ResponseEntity<String> sendEmail(@RequestParam(value = "email", required = false) String email,
			@RequestParam("action") String action,
			@RequestParam(value = "forgotEmail", required = false) String forgotEmail) {

		Random random = new Random();
		// Tạo số ngẫu nhiên gồm 6 chữ số
		int randomNumber = random.nextInt(900000) + 100000;
		String randomNumberString = String.valueOf(randomNumber);
		String subject = "Nhận Mã Xác Minh Của Bạn";
		String body = "Chào bạn,\n\n";
		body += "Bạn đã đăng ký tài khoản với địa chỉ email này.\n";
		body += "Mã xác minh của bạn là: " + randomNumberString + "\n";
		body += "Vui lòng sử dụng mã này để hoàn tất quá trình đăng ký.\n\n";
		body += "Trân trọng,\n";
		body += "Đội ngũ quản trị";
		List<Users> list = logindao.findAll();

		if (action.equals("sing_up")) {
			for (Users item : list) {
				if (item.getEmail().equals(email)) {
					return ResponseEntity.ok("Email Đã Tồn Tại");
				}
			}
			emailService.sendEmail(email, subject, body);
		}
		if (action.equals("forgot")) {
			boolean emailFound = false;
			for (Users item : list) {
				if (item.getEmail().equals(forgotEmail)) {
					emailFound = true;
					break;
				}
			}
			if (!emailFound) {
				System.out.println(forgotEmail);
				return ResponseEntity.ok("Email Không Tồn Tại");
			}
			emailService.sendEmail(forgotEmail, subject, body);
		}

		return ResponseEntity.ok(randomNumberString);
	}

// dăng ký
	@PostMapping("/sign_up")
	public ResponseEntity<String> signUp(@RequestParam("newName") String newName,
			@RequestParam("newPass") String newPass, @RequestParam("newEmail") String newEmail,
			@RequestParam("newSdt") String newSdt) {
		List<Users> list = logindao.findAll();
		for (Users item : list) {
			if (item.getSdt() == newSdt) {
				return ResponseEntity.status(HttpStatus.OK).body("Số Điện Thoại Đã Tồn Tại");
			}
		}

		Users us = new Users();
		us.setUs(newName);

		us.setEmail(newEmail);
		us.setRole(false);
		us.setSdt(newSdt);
		LocalDateTime now = LocalDateTime.now();
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
		String formatDateTime = now.format(formatter);
		us.setCreate_day(formatDateTime);

		BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
		String hashedPassword = passwordEncoder.encode(newPass);
		us.setPass(hashedPassword);
		logindao.save(us);

		return ResponseEntity.ok("Đăng Ký Thành Công");
	}

	@PostMapping("/updatePass")
	@Transactional
	public ResponseEntity<Users> updatePass(@RequestParam("email") String email, @RequestParam("Passnew") String pass) {
		List<Users> listUs = logindao.findAll();
		Users updatedPass = null;
		for (Users item : listUs) {
			if (item.getEmail().equals(email)) {
				item.setPass(pass);
				updatedPass = logindao.save(item); // Lưu thay đổi vào cơ sở dữ liệu
				break; // Thoát khỏi vòng lặp sau khi tìm thấy email khớp
			}
		}
		if (updatedPass != null) {
			return new ResponseEntity<>(updatedPass, HttpStatus.OK);
		} else {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
	}

}
