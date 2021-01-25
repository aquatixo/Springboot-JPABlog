package willms.blog.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import willms.blog.config.auth.PrincipalDetail;
import willms.blog.model.KakaoProfile;
import willms.blog.model.OAuthToken;
import willms.blog.model.User;
import willms.blog.service.UserService;

//인증이 안된 사용자들이 출입할 수 있는 경로를 /auth/**
//그냥 주소가 / 이면 index.jsp 허용
//static 이하는 있는 /js/**, /css/**, /image/**

@Controller
public class UserController {
	@Value("${cos.key}")
	private String cosKey;
	
	@Autowired
	private UserService userService;
	@Autowired
	private AuthenticationManager authenticationManager;
	
	@GetMapping("/auth/joinForm")
	public String joinForm() {
		return "user/joinForm";
	}
	@GetMapping("/auth/loginForm")
	public String loginForm() {
		return "user/loginForm";
	}
	
	@GetMapping("/auth/kakao/callback")
	public String kakaoCallback(String code) {//data를 리턴해주는 컨트롤러 함수
		
		//POST 방식으로 key = value 데이타를 요청 (카카오톡으로)
		//Retrofit2 (android)
		//OkHTTP
		//RestTemplate
		RestTemplate rt = new RestTemplate();
		//HttpHeader 오브잭트 생성
		HttpHeaders headers = new HttpHeaders();
		
		headers.add("Content-type", "application/x-www-form-urlencoded;charset=utf-8");
		
		//HttpBody 오브잭트 생성
		MultiValueMap<String, String> params = new LinkedMultiValueMap<>();		
		
		params.add("grant_type", "authorization_code");
		params.add("client_id", "0de71e031806337aa85671671937012d");
		params.add("redirect_uri", "http://localhost:8002/auth/kakao/callback");
		params.add("code", code);
		
		//HttpHeader 와 HttpBody를 하나의 오브잭트에 담기
		HttpEntity<MultiValueMap<String, String>> kakaoTokenRequest =
				new HttpEntity<>(params, headers);
		
		//Http요청하기 - Post방식으로 - 그리고 response 변수의 응덥 받음
		ResponseEntity<String> response = rt.exchange(
				"https://kauth.kakao.com/oauth/token",
				HttpMethod.POST,
				kakaoTokenRequest,
				String.class
		);
		
		//Gson, JsonSimple, Object Mapper //OAuthToken 만든후
		ObjectMapper objectMapper = new ObjectMapper();
		OAuthToken oauthToken = null;
		try {
		oauthToken = objectMapper.readValue(response.getBody(), OAuthToken.class);
		}catch (JsonMappingException e) {
			e.printStackTrace();
		}catch (JsonProcessingException e) {
			e.printStackTrace();
		}
		
		RestTemplate rt2 = new RestTemplate();
		HttpHeaders headers2 = new HttpHeaders();
		headers2.add("Authorization", "Bearer "+oauthToken.getAccess_token());
		headers2.add("Content-type", "application/x-www-form-urlencoded;charset=utf-8");
		
		HttpEntity<MultiValueMap<String, String>> kakaoProfileRequest =
				new HttpEntity<>(headers2);
		ResponseEntity<String> response2 = rt2.exchange(
				"https://kapi.kakao.com/v2/user/me",
				HttpMethod.POST,
				kakaoProfileRequest,
				String.class
		);
		
		//After Kakaoprofile
		ObjectMapper objectMapper2 = new ObjectMapper();
		KakaoProfile kakaoProfile= null;
		try {
		kakaoProfile = objectMapper2.readValue(response2.getBody(), KakaoProfile.class);
		}catch (JsonMappingException e) {
			e.printStackTrace();
		}catch (JsonProcessingException e) {
			e.printStackTrace();
		}
		//User오브잭트 
		System.out.println("카카오 아이디 번호" + kakaoProfile.getId());
		System.out.println("카카오 이메일" + kakaoProfile.getKakao_account().getEmail());
		
		System.out.println("블로그서버 유저네임: " + kakaoProfile.getKakao_account().getEmail() + "_" + kakaoProfile.getId());
		System.out.println("블로그서버 이메일: " + kakaoProfile.getKakao_account().getEmail());
		System.out.println("블로그서버 비밀번호: " + cosKey);
		
		User kakaoUser = User.builder()
				.username(kakaoProfile.getKakao_account().getEmail() + "_" + kakaoProfile.getId())
				.password(cosKey)
				.email(kakaoProfile.getKakao_account().getEmail())
				.oauth("kakao")
				.build();
		
		//가입자인지 비가입자인지
		User originUser = userService.회원찾기(kakaoProfile.getKakao_account().getEmail() + "_" + kakaoProfile.getId());
		if(originUser.getUsername() == null)
			userService.회원가입(kakaoUser);
		
		//로그인 처리
		Authentication authentication = 
				authenticationManager.authenticate
				(new UsernamePasswordAuthenticationToken(kakaoUser.getUsername(), cosKey));
		SecurityContextHolder.getContext().setAuthentication(authentication);
		
		return "redirect:/";//response2.getBody()
	}
	
	@GetMapping("/user/updateForm")
	public String updateForm(@AuthenticationPrincipal PrincipalDetail principal) {
		return "user/updateForm";
	}
}
