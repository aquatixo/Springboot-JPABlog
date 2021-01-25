package willms.blog.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import willms.blog.config.auth.PrincipalDetailService;
@Configuration //Bean 등록
@EnableWebSecurity // 시큐리티 필터 추가 => Spring security가 활성화 되어있는데.
//																				어떤 설정을 해당 파일에서 한다.
@EnableGlobalMethodSecurity(prePostEnabled = true) //특정 주소로 접근하면 권한 및 인증을 미리 체크한다
//위 세개는 하나의 세트라고 본다
public class SecurityConfig extends WebSecurityConfigurerAdapter{
	
	@Autowired
	private PrincipalDetailService principalDetailService;
	
	@Bean //IOC
	public BCryptPasswordEncoder encodePWD() {
		return new BCryptPasswordEncoder();
	}
	
	@Bean
	@Override
	public AuthenticationManager authenticationManagerBean() throws Exception {
		return super.authenticationManagerBean();
	}

	//시큐리티가 대신 로그인 해주는데, password를 가로채기 하는데,
	//해당 password가 뭘로 해쉬가 되어 회원가입이 되었는지 알아야
	//같은 해쉬로 암호화해서 DB의 해쉬랑 비교
	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth.userDetailsService(principalDetailService).passwordEncoder(encodePWD());
	}
	
	@Override
	protected void configure(HttpSecurity http) throws Exception{
		http
				.csrf().disable() //CSRF 토큰 비활성화 (테스트시 거는게 좋다)
				.authorizeRequests()
				.antMatchers("/", "/auth/**", "/js/**",  "/css/**", "/image/**", "dummy/**")
				.permitAll() //auth는 허용
				.anyRequest() //나머지 login 인증
				.authenticated()
			.and()
				.formLogin()
				.loginPage("/auth/loginForm") //인증 필요한 페이지 가면 로그인 페이지로 옮겨준다
				.loginProcessingUrl("/auth/loginProc") // 스프링 시큐리티가 로그인 가로챈다, 대신 로그인 해준다
				.defaultSuccessUrl("/")
				;
	}
}
