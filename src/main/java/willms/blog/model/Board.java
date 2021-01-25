package willms.blog.model;

import java.sql.Timestamp;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;

import org.hibernate.annotations.CreationTimestamp;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@Builder
@NoArgsConstructor
public class Board {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY) // auto_increment
	private int id;

	@Column(nullable = false, length = 100)
	private String title;
	@Lob // 대용량 데이타
	private String content; // 섬머노트 라이브러리 <html>태그가 섞여서 디자인 -> 그래서 LOB

	//@ColumnDefault("0")
	private int count; // 조회수

	@ManyToOne(fetch = FetchType.EAGER) // Many는 BOARD, One는 User // FK 만든다
	@JoinColumn(name = "userId")
	private User user; // DB는 오브젝트 저장할 수 없지만, 그래서 FK 쓰지만, 자바는 오브젝트를 저장하기에,

	// 이board는 아마 reply의 board?인듯?
	@OneToMany(mappedBy = "board", fetch = FetchType.EAGER, cascade = CascadeType.REMOVE) // mappedBy는 연관관계의 주인이 아니다(FK가 아니다), DB에 컬럼 만들지마세요
	// 하나의 board는 많은 댓글
	@JsonIgnoreProperties({"board"})//무한 참조 방지
	//@JsonIgnoreProperties({"board", "user"})//무한 참조 방지
	@OrderBy("id desc")
	private List<Reply> replys;

	@CreationTimestamp
	private Timestamp createDate;
}