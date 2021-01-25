package willms.blog.config.auth;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import willms.blog.model.User;
import willms.blog.repository.UserRepository;
@Service
public class PrincipalDetailService implements UserDetailsService{
	@Autowired
	private UserRepository userRep;
	
	@Override //스프링이 로그인 요청 가로챌때, username password 변수 2개를 가로채는데.
	//password부분 처리는 자동,
	//username이 DB에 있는지만 확인해주자
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		User principal = userRep.findByUsername(username)
				.orElseThrow(()->{
					return new UsernameNotFoundException("해당 사용자 없다: " + username);
				});
		return new PrincipalDetail(principal); //시큐리티 의 세션에 유저정보가 저장
	}
}