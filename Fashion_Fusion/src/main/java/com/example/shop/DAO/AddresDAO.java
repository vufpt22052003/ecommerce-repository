package com.example.shop.DAO;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.example.shop.model.Address;

public interface AddresDAO extends JpaRepository<Address, Integer> {

	@Query("select a from Address a where a.uid = ?1 and a.default_address = true")
	Address adres(int uid);

	@Query("select a from Address a where  a.uid = ?1  ORDER BY CASE WHEN a.default_address = true THEN 0 ELSE 1 END")
	List<Address> getAllByUser(int id);

//	@Modifying
//	@Query("update Address c set c.uid = ?1 ")
//	Address updateAdres(int uid);

	@Modifying
	@Query("UPDATE Address a SET a.default_address = CASE WHEN a.id = ?1 THEN true ELSE false END WHERE a.uid = ?2")
	void updateAdres(int id , int uid );

	

}
