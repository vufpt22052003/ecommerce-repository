package com.example.shop.model;

import java.sql.Date;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
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

public class Users  {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;

	@Column(name = "us")
	private String us;

	@Column(name = "pass")
	private String  pass;

	@Column(name = "email")
	private String email;

	@Column(name = "Role")
	private Boolean Role;

	@Column(name = "gender")
	private Boolean gender;

	@Column(name = "birthday")
	private String birthday;

	@Column(name = "avt")
	private String avt;

	@Column(name = "sdt")
	private String sdt;

	@Column(name = "link_instagram")
	private String linkInstagram;

	@Column(name = "link_fb")
	private String linkFb;

	@OneToMany(mappedBy = "user_id")
	@JsonIgnore
	List<Products> product;

	private String create_day;
//
//	@OneToMany(mappedBy = "uid_catogery")
//	@JsonIgnore
//	List<Category> uid_catogery;

	@OneToMany(mappedBy = "user_id")
	@JsonIgnore
	List<Cart> cart;

	@OneToMany(mappedBy = "uid")
	@JsonIgnore
	List<UserVoucher> userVouchers;


	
	@OneToMany(mappedBy = "uid")
	@JsonBackReference
	@JsonIgnoreProperties({ "hibernateLazyInitializer", "handler" }) // Thêm annotation này
	private List<Comment> comments;

	@OneToMany(mappedBy = "user_id")
	@JsonBackReference // @JsonBackReference được sử dụng để ngăn chặn việc lặp vô hạn khi chuyển đổi
						// giữa JSON và đối tượng Java trong quan hệ hai chiều.
	private List<Orders> order;

	@OneToMany(mappedBy = "uid_send")
	@JsonBackReference
	@JsonIgnoreProperties({ "hibernateLazyInitializer", "handler" })
	List<Message> uid_send;
	
	@OneToMany(mappedBy = "send_uid")
	@JsonBackReference
	@JsonIgnoreProperties({ "hibernateLazyInitializer", "handler" })
	List<Message> send_uid;
	

}
