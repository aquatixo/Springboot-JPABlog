package willms.blog.config.auth;

import java.util.ArrayList;
import java.util.Collection;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import lombok.Getter;
import willms.blog.model.User;
//스프링 시큐리티가 로그인 요청을 가로채서, 로그인을 진행하고 완료하면, UserDetails타의 오브젝트를
//스프링 시큐리티의 고유한 세션저장소에 저장을 한다.
@Getter
public class PrincipalDetail implements UserDetails{
	private User user; //콤포지션, User를 들고있는것을 composition
	
	public PrincipalDetail(User user) {
		this.user = user;
	}
	
	@Override
	public String getPassword() {
		return user.getPassword();
	}

	@Override
	public String getUsername() {
		return user.getUsername();
	}

	//계정이 만료인지 (true 만료안댐)
	@Override
	public boolean isAccountNonExpired() {
		return true;
	}
	//계정이 잠겨있는지 (true 잠김아님)
	@Override
	public boolean isAccountNonLocked() {
		return true;
	}
	//비밀번호 만료인지 (true 만료아님)
	@Override
	public boolean isCredentialsNonExpired() {
		return true;
	}
	//계정이 활성화(사용가능인지) (true 활성화)
	@Override
	public boolean isEnabled() {
		return true;
	}
	//계정이 가지고 있는 권한 목록
	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		
		Collection<GrantedAuthority> collectors = new ArrayList<>();
		collectors.add(()->{
			return "ROLE_"+user.getRole();
		});
		
		return collectors;
	}
}