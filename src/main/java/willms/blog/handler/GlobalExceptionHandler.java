package willms.blog.handler;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;

import willms.blog.dto.ResponseDto;

@ControllerAdvice //모든 exception이 이루온다?
@RestController
public class GlobalExceptionHandler {
	//(value=Exception.class) <- 모든 exception
	@ExceptionHandler(value=Exception.class) //IllegalArgument만
	public ResponseDto<String>  handleArgumentException(Exception e) {
		return new ResponseDto<String>(HttpStatus.INTERNAL_SERVER_ERROR .value(), e.getMessage());
		//return "<h1>"+e.getMessage()+"</h1>";
	}		
}