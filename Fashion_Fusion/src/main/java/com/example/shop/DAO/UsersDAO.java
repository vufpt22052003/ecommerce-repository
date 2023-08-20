package com.example.shop.DAO;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.example.shop.model.AccountTranSport;
import com.example.shop.model.Users;

public interface UsersDAO extends JpaRepository<Users, Integer> {

	@Query("SELECT u FROM Users u WHERE u.pass = ?1 AND u.sdt = ?2")
	Users checkLogin(String encoder, String sdt);

	@Query("SELECT u FROM Users u WHERE  u.sdt = ?1")
	Users checkSdt(String sdt);

	@Modifying
	@Query(value = "UPDATE Account SET pass = ?  WHERE email = ? ", nativeQuery = true)
	Void updatePass(String pass, String email);

	@Modifying
	@Query(value = "UPDATE Account SET Role = 1 where id =? ", nativeQuery = true)
	void updateROLE(int uid);


}
