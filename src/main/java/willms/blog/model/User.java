package willms.blog.model;

import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import org.hibernate.annotations.CreationTimestamp;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
@Entity //-> user 클래스가  MySql에 테이블이 생성된다
//ORM --> Java(+다른언어) 객체로, 테이블을 만들어 준다
@Data
@AllArgsConstructor
@Builder
@NoArgsConstructor
//@DynamicInsert // insert시에 null 인 필드를 제외시켜준다
public class User {
	@Id //Primary Key
	@GeneratedValue(strategy = GenerationType.IDENTITY) //프로젝트에서 연결된 DB전략을 쓴다, oracle ->SEQ, mySql autoincrement
	private int id; //seq / auto_increment
	
	@Column(nullable = false, length = 100, unique = true)
	private String username;
	@Column(nullable = false, length = 100) //큰 이유 123456 => 해쉬 할거다
	private String password;
	@Column(nullable=false, length = 50)
	private String email;
	
	//@ColumnDefault("user")
	//DB는 RoleType이 없기에 
	@Enumerated(EnumType.STRING)
	private RoleType role;//Enum쓰는게 좋다 //admin,user 
	
	private String oauth; // 카카오 로그인 홧인
	
	@CreationTimestamp // 시간이 자동 입력
	private Timestamp createDate;
}