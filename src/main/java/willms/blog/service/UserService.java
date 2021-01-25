package willms.blog.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import willms.blog.model.RoleType;
import willms.blog.model.User;
import willms.blog.repository.UserRepository;

@Service //spring이 component scan 통해서 bean에 등록,IOC함
public class UserService {
	@Autowired
	private UserRepository userRepository;
	@Autowired
	private BCryptPasswordEncoder encode;
	@Transactional
	public void 회원가입(User user) {
		String rawPassword = user.getPassword(); //1234
		String encPassword = encode.encode(rawPassword); //HASHED
		user.setPassword(encPassword);
		user.setRole(RoleType.USER);
		
		userRepository.save(user);
	}
	
	@Transactional
	public void 회원수정(User user) {
		//수정시에는 영속성 컨텍스트 User 오브잭트를 영속화시키고, 영속화된 User 오브잭트를 수정
		//select를 해서 User 오브잭트를 DB로 부터 가져오는 이유는 영속화하기 위해서
		//영속화된 오브잭트를 변경하면 자동으로 DB에 update문을 날려준다
		User persistance = userRepository.findById(user.getId()).orElseThrow(()->{
			return new IllegalArgumentException("회원 찾기 실패");
		});
		//oauth체크 //oauth이 없어야 수정 가능
		if(persistance.getOauth() == null || persistance.getOauth().equals("")) {
		String rawPassword = user.getPassword();
		String encPassword = encode.encode(rawPassword);
		persistance.setPassword(encPassword);
		persistance.setEmail(user.getEmail());
		}
		
		//회원수정 함수 종료시 = 서비스 종료 = 트랜잭션 = commit이 자동으로 됩니다.
		//영속화된 persistance 객체의 변화가 감지되면 더티체킹이 되어 update 문을 날려준다
	}
	
	@Transactional(readOnly = true)
	public User 회원찾기(String username) {
		User user = userRepository.findByUsername(username).orElseGet(()->{
			return new User();
		});
		return user;
	}
	
//	@Transactional(readOnly = true)// Select할 때 트랜젝션 시작, 서비스 종료시에 트랜잭션 종료(정합성)
//	public User 로그인(User user) {
//		return userRepository.findByUsernameAndPassword(user.getUsername(), user.getPassword());
//	}
}