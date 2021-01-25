package willms.blog.test;

import java.util.List;
import java.util.function.Supplier;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import willms.blog.model.RoleType;
import willms.blog.model.User;
import willms.blog.repository.UserRepository;
//REstController는 html이 아니고, data를return 해준다 .
@RestController
public class DummyControllerTest {
	@Autowired //의존성 주입
	private UserRepository userRepository;
	
	@DeleteMapping("/dummy/user/{id}")
	public String delete(@PathVariable int id) {
		try {
			userRepository.deleteById(id);
		} catch (EmptyResultDataAccessException e) {
			return "삭제 실패 no ID bitch";
		}
		return "삭제함";
	}
	
	//save함수는 id를 전달 안하면 insert
	//save함수는 id를 전달을 하면 update한다
	//save함수는 id전달고 데이타가 없으면 insert한다
	@Transactional //함수 종료시 자동 commit
	@PutMapping("/dummy/user/{id}")
	public User updateUser(@PathVariable int id,  @RequestBody User requestUser) {
		//json데이타 요청 -> Java object -> Message Converter의 jackson 라이브러리가 변환해서 받는다/
		System.out.println("id: " + id);
		System.out.println("pw: " + requestUser.getPassword());
		System.out.println("email: " + requestUser.getEmail());
		
		User user = userRepository.findById(id).orElseThrow(()->{
		return new IllegalArgumentException("유저 없당");
		});
		user.setPassword(requestUser.getPassword());
		user.setEmail(requestUser.getEmail());
		requestUser.setId(id);
		
		//userRepository.save(user);
		
		//더티 체킹 
		return user;
	}
	
	@GetMapping("/dummy/users")
	public List<User> list(){
		return userRepository.findAll();
	}
	
	//여기서는 한 페이지에 2건의 데이타
	@GetMapping("/dummy/user")
	public List<User> pageList(@PageableDefault(size = 2, sort="id", direction = Sort.Direction.DESC) Pageable pageable){
		Page<User> pagingUser = userRepository.findAll(pageable);

//		List<User> users = userRepository.findAll(pageable).getContent();
		List<User> users = pagingUser.getContent();
		return users;
	}
	
	@GetMapping("/dummy/user/{id}")
	public User detail(@PathVariable int id) {
		// user 4 를 찾는데, 유저가 없으면, null이 return되면 프로그램에 문제가
		//생길 수가 있어서, optional로 감싸서 User객체를 감싸고, 후에 User가 null인지 판단
		User user = userRepository.findById(id).orElseThrow(new Supplier<IllegalArgumentException>() {
			@Override
			public IllegalArgumentException get() {
				return new IllegalArgumentException("해당유저는 없다 id:" + id);
			}
		});
		return user; 
	}
	
	//람다식
	//User user = userRepository.findById(id).orElseThrow(()->{
	//return new IllegalArgumentException("유저 없당");
	//});
	
	
	//http://localhost:8002/blog/dummy/join
	//http의 body에 username, password, email 데이타
	@PostMapping("/dummy/join")
	public String join(User user) { 
		System.out.println(user.getUsername());
		System.out.println(user.getPassword());
		System.out.println(user.getEmail());
		
		user.setRole(RoleType.USER);
		userRepository.save(user);
		return "회원가입 완료";
	}
}
