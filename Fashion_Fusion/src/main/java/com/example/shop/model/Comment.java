package com.example.shop.model;

import jakarta.persistence.*;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "Comment")
public class Comment {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;

	private String contents;

	private int uid;

	private String create_date;

	private String updated_at;

	@ManyToOne
	@JoinColumn(name = "product_id")
	private Products product_id;

}

