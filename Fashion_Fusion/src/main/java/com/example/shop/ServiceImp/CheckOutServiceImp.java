package com.example.shop.ServiceImp;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.shop.DAO.AddresDAO;
import com.example.shop.Service.CheckOutService;
import com.example.shop.model.Address;
import com.example.shop.model.Users;

import jakarta.servlet.http.HttpSession;
import jakarta.transaction.Transactional;

@Service
public class CheckOutServiceImp implements CheckOutService {

	@Autowired
	AddresDAO addresDAO;

	@Autowired
	HttpSession session;

	@Override
	public void adres(Address adr) {
		addresDAO.save(adr);
	}

	@Override
	public Address getAdres(int uid) {
		return addresDAO.adres(uid);
	}

	@Override
	@Transactional
	public void updateAdres(Address adr) {
		Users acc = (Users) session.getAttribute("acc");
		if (acc != null) {
			int uid = acc.getId();
			addresDAO.updateAdres(1);
		} else {
			addresDAO.save(adr);
		}

	}

}
