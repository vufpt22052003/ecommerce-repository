package com.example.shop.model;

import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;

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

	
	
	@ManyToOne
	@JsonManagedReference
	@JoinColumn(name = "uid")
	@JsonIgnoreProperties({ "hibernateLazyInitializer", "handler" }) // Thêm annotation này
	private Users uid;

	@Temporal(TemporalType.TIMESTAMP)
	private Date create_date;

	private int rating;

	@ManyToOne
	@JsonManagedReference
	@JoinColumn(name = "product_id")
	@JsonIgnoreProperties({ "hibernateLazyInitializer", "handler" }) // Thêm annotation này
	private Products product_id;

	@OneToMany(mappedBy = "coment_id")
	@JsonIgnore
	private List<coment_photo> cmt_photo;

}
