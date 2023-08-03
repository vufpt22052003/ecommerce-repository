package com.example.shop.model;

import java.util.List;

import jakarta.persistence.*;

import com.fasterxml.jackson.annotation.JsonIgnore;

//import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "Address")
public class Address {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;

	// @NotBlank(message = "Vui Lòng Nhập Họ Tên")
	private String name;

	private int phone;
	private String adress;
	private int uid;
	private String Tool_address;
	private boolean default_address;
//	@OneToOne(mappedBy = "Address_id")
//	@JsonIgnore
//	private Checkout Address_id;

	@OneToMany(mappedBy = "adres_id")
	 @JsonIgnore
	List<Orders> order;
}
