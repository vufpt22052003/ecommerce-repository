package com.example.shop.model;

import java.util.ArrayList;
import java.util.Calendar;

import jakarta.persistence.Table;

import org.springframework.beans.factory.annotation.Autowired;

import com.example.shop.DAO.SaleDAO;
import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "Cart")
public class Cart {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;

	@ManyToOne
	@JoinColumn(name = "product_id")
	private Products product_id;

	private String color;
	private String size;
	private int quantity;

	@ManyToOne
	@JoinColumn(name = "user_id")
	@JsonIgnore
	private Users user_id;

}
