package com.example.shop.controller;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.shop.ServiceImp.AdresServiceImp;
import com.example.shop.model.Address;
import com.example.shop.model.Users;

import jakarta.servlet.http.HttpSession;

@Controller
public class AdresController {

	@Autowired
	AdresServiceImp adresServiceImp;

	@Autowired
	HttpSession session;

	@GetMapping("/adres")
	public String shop2() {
		return "/views/adres";
	}

	@PostMapping("/add-adres")
	public String add_adres(@ModelAttribute("adres") Address adres, @RequestParam("ward") String ward,
			@RequestParam("province") String province, @RequestParam("district") String district,

			@RequestParam(value = "idAdres", required = false) Integer idAdres,
			@RequestParam(value = "old_adres", required = false) String old_adres) {
		ArrayList<Address> getAll = (ArrayList<Address>) adresServiceImp.findAll();

		String fullAddress = ward + ", " + district + ", " + province;
		adres.setAdress(fullAddress);

		Users acc = (Users) session.getAttribute("acc");
		int id = acc.getId();
		adres.setUid(id);

		boolean isAddressExists = false;

		if (idAdres != null) {
			for (Address address : getAll) {
				if (address.getId() == idAdres) {
					isAddressExists = true;
					break;
				}
			}
		}

		if (isAddressExists) {
			Address newAddress = new Address();
			newAddress.setName(adres.getName());
			if (ward == null || district == null || province == null || ward.isEmpty() || district.isEmpty()
					|| province.isEmpty()) {
				newAddress.setAdress(old_adres);
			} else {
				newAddress.setAdress(fullAddress);
			}
			newAddress.setUid(id);
			newAddress.setId(idAdres);
			newAddress.setPhone(adres.getPhone());
			newAddress.setTool_address(adres.getTool_address());
			// Thực hiện cập nhật thông tin địa chỉ đã tồn tại
			adresServiceImp.adres(newAddress);
		} else {
			// Thực hiện thêm địa chỉ mới
			adresServiceImp.adres(adres);
		}

		return "redirect:/adres";
	}

	
}
