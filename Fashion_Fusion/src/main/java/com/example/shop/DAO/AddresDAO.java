package com.example.shop.DAO;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.example.shop.model.Address;

public interface AddresDAO extends JpaRepository<Address, Integer> {

	@Query("select a from Address a where a.uid = ?1")
	Address adres(int uid);

	@Modifying
	@Query("update Address c set c.uid = ?1 ")
	Address updateAdres(int uid);
}
