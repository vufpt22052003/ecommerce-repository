package com.example.shop.model;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity
@Table(name = "Account")
public class Users {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;

	@Column(name = "us")
	private String us;

	@Column(name = "pass")
	private String pass;

	@Column(name = "email")
	private String email;

	@Column(name = "is_admin")
	private Boolean is_admin;

	@Column(name = "gender")
	private Boolean gender;

	@Column(name = "birthday")
	private String birthday;

	@Column(name = "avt")
	private String avt;

	@Column(name = "sdt")
	private int sdt;
	
	@Column(name = "link_instagram")
	private String linkInstagram;
	
	@Column(name = "link_fb")
	private String linkFb;

	@OneToMany(mappedBy = "user_id")
	@JsonIgnore
	List<Products> product;
//
//	@OneToMany(mappedBy = "uid_catogery")
//	@JsonIgnore
//	List<Category> uid_catogery;

	@OneToMany(mappedBy = "user_id")
	@JsonIgnore
	List<Cart> cart;

	@OneToMany(mappedBy = "user_id")
	@JsonBackReference // @JsonBackReference được sử dụng để ngăn chặn việc lặp vô hạn khi chuyển đổi
						// giữa JSON và đối tượng Java trong quan hệ hai chiều.
	private List<Orders> order;

}
