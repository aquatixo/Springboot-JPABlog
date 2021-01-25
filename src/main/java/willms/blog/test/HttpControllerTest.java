package willms.blog.test;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

//@Controller => HTML 파일 응답
@RestController  //Data응답
public class HttpControllerTest {
	//http://localhost:8002/http/get 
	//select
	@GetMapping("/http/blog/get")
	public String getTest(Member m) {
		return "get " + m.getId() + "," + m.getUsername() + ", " + m.getPassword() + ", " +m.getEmail();
	}//localhost:8002/http/get?id=1&username=ssat&password=1234&email=앙기
	// SPRING이 알아서 각 변수에 값을 넣는다 헐헐
	@PostMapping("/http/blog/post")
	public String postTest(@RequestBody Member m) { //MessageConverter가 알아서 각 요소를 Member에 잘 넣음 헐헐
		return "post  " + m.getId() + ", " + m.getUsername() + ", " + m.getPassword() + ", " +m.getEmail();
	}
	@PutMapping("/http/blog/put")
	public String putTest(@RequestBody Member m) {
		return "put  " + m.getId() + ", " + m.getUsername() + ", " + m.getPassword() + ", " +m.getEmail();
	}
	@DeleteMapping("/http/blog/delete")
	public String deleteTest() {
		return "delete ";
	}
	
	}