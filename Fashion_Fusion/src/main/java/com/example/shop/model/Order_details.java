package com.example.shop.model;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.*;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "Order_details")
public class Order_details {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;

	@ManyToOne
	@JoinColumn(name = "product_id")
	private Products product_id;

	private int quantity;

	@ManyToOne
    //@JsonBackReference
	@JoinColumn(name = "order_id")
	private Orders order_id;
	
	private double price;


	private Date created_at;

	private String color ;
	private String size ;
	
	private Date updated_at;
	
	private String order_status;

	
	private String cancelled_by ;
	
	private boolean has_review;
	
	
}
