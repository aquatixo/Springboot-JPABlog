package willms.blog.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import willms.blog.model.User;

//DAO
//자동으로 bean 등록
//@Repository 생략 가능
public interface UserRepository extends JpaRepository<User, Integer> {
	//naming query
	//SELCT * FROM user WHERE username = 1?;
	Optional<User> findByUsername(String username);
	
	
	// JPA naming 전략
	// SELECT * FROM user WHERE username = ?1 AND password = ?2;
//	User findByUsernameAndPassword(String username, String password);

//	@Query(value="SELECT * FROM user WHERE username = ?1 AND password = ?2", nativeQuery=true)
//	User login(String username, String password);
}