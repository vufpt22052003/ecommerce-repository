package com.example.shop.DAO;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.example.shop.model.Users;

public interface LoginDAO extends JpaRepository<Users, Integer> {

	@Query("SELECT u FROM Users u WHERE u.pass = ?1 AND u.sdt = ?2")
	Users checkLogin(String pass, int sdt );

	@Modifying
	@Query(value="UPDATE Account SET pass = ?  WHERE email = ? " , nativeQuery = true)
	Void updatePass(String pass , String email);
}
