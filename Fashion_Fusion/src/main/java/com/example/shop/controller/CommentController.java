package com.example.shop.controller;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.example.shop.DAO.ComentPhotoDAO;
import com.example.shop.Service.CommentService;
import com.example.shop.ServiceImp.CommentServiceImp;
import com.example.shop.model.Comment;
import com.example.shop.model.Users;
import com.example.shop.model.coment_photo;

import jakarta.servlet.http.HttpSession;

@Controller
public class CommentController {

	@Autowired
	CommentServiceImp commentServiceImp;
	@Autowired
	HttpSession session;
	@Autowired
	ComentPhotoDAO comentPhotoDAO;

	@RequestMapping("/api/postComment")
	public ResponseEntity<Void> postCmt(@RequestParam("ProductId") int id, @RequestParam("comment") String content,
			@RequestParam(name = "images[]", required = false) MultipartFile[] images,
			@RequestParam("rating") int rating, @RequestParam("detailsId") int detailsId) throws IOException {
		// Kiểm tra đăng nhập
//		Users acc = (Users) session.getAttribute("acc");
////		if (acc == null) {
////			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
////		}

		System.out.println("Gửi đánh giá: " + content + " cho sản phẩm có ID: " + id + " và detailsId" + detailsId);

		System.out.println(rating + "rating");
		Comment commentId = commentServiceImp.PostCmt(id, content, rating , detailsId); // Lưu và nhận comment_id
		if (images != null && images.length > 0) {
			for (MultipartFile itemImg : images) {
				String fileName = itemImg.getOriginalFilename();
				Path path = Paths.get("src/main/resources/static/EvaluateImg/");
				Path filePaths = path.resolve(fileName);
				try (InputStream inputStream = itemImg.getInputStream()) {
					Files.copy(inputStream, filePaths, StandardCopyOption.REPLACE_EXISTING);
					// khởi tạo đối tượng trong mỗi lần lặp tránh để bên ngoài vì để bên ngoài sẽ
					// chỉ lưu đc ảnh cuối cùng
					coment_photo comentPhoto = new coment_photo();
					comentPhoto.setImg(fileName.toLowerCase());
					comentPhoto.setComent_id(commentId);
					comentPhotoDAO.save(comentPhoto);
				}
			}

		}

		return ResponseEntity.ok().build();
	}

}
