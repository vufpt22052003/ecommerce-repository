package com.example.shop.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.shop.ServiceImp.CheckOutServiceImp;
import com.example.shop.model.Address;
import com.example.shop.model.Users;

import jakarta.servlet.http.HttpSession;

@Controller
public class CheckOutCtrl {

	@Autowired
	CheckOutServiceImp checkOutServiceImp;

	@Autowired
	HttpSession session;

	@GetMapping("/adres")
	public String shop2() {
		return "/views/adres";
	}

	@PostMapping("/add-adres")
	public String add_adres(@ModelAttribute("adres") Address adres, @RequestParam("ward") String ward,
			@RequestParam("province") String province, @RequestParam("district") String district) {
		String fullAddress = ward + ", " + district + ", " + province;
		adres.setAdress(fullAddress);

		Users acc = (Users) session.getAttribute("acc");
		int id = acc.getId();
		adres.setUid(id);

		System.out.println(fullAddress);
		checkOutServiceImp.updateAdres(adres);
		return "redirect:/adres";
	}

}
