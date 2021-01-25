package willms.blog.test;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController 
public class BlogControllerTest {
	//http://localhost:8002/test/hello
	@GetMapping("test/hello")
	public String hello() {
		return "<h1>hello Spring boot</h1>";
	}
}