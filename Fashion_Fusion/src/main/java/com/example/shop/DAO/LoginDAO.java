package com.example.shop.DAO;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.example.shop.model.Users;

public interface LoginDAO extends JpaRepository<Users, Integer> {

	@Query("SELECT u FROM Users u WHERE u.us = ?1 AND u.pass = ?2")
	Users checkLogin(String us, String pass);

}
