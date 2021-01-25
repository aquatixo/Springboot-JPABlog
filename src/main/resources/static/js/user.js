let index = {
	init: function() {
		$("#btn-save").on("click", () => {
			this.save();
		});
		$("#btn-update").on("click", () => {
			this.update();
		});
		//		$("#btn-login").on("click",()=>{
		//			this.login();
		//		}); 
	},

	save: function() {
		let data = {
			username: $("#username").val(),
			password: $("#password").val(),
			email: $("#email").val()
		};
		//console.log(data)

		//ajax 호츌시 default가 비동기 호출
		$.ajax({
			//회원가입 수행 요청
			type: "POST",
			url: "/auth/joinProc",
			data: JSON.stringify(data), //JSON 문자열이 된다
			contentType: "application/json; charset=utf-8", //body 데이타가 어떤 타입인지(MIME)
			dataType: "json" //요청을 서버로해서 응답이 왔을때 기본적으로 모든것이 문자열(생긴게 json이면) =>javascript오브젝트
		}).done(function(resp) {
			if (resp.status === 500) {
				alert("회원가입 실패");
			} else {
				//성공
				alert("회원가입 완료");
				//alert(resp);//UserApiController return 값이 resp
				//console.log(resp);
				location.href = "/";
			}

		}).fail(function(error) {
			//실패
			alert(JSON.stringify(error));
		});//ajax 통신을 이용해서 3개의 데이타를 json으로 변경해서 insert 요청		
	},

	update: function() {
		let data = {
			id: $("#id").val(),
			username: $("#username").val(),
			password: $("#password").val(),
			email: $("#email").val()
		};

		$.ajax({
			type: "PUT",
			url: "/user",
			data: JSON.stringify(data),
			contentType: "application/json; charset=utf-8",
			dataType: "json"
		}).done(function(resp) {
			alert("회원수정 완료");
			location.href = "/";
		}).fail(function(error) {
			alert(JSON.stringify(error));
		});
	},

	//	login:function(){
	//		let data = {
	//			username : $("#username").val(),
	//			password: $("#password").val(),
	//		};
	//		//console.log(data)
	//		
	//		$.ajax({
	//			type: "POST",
	//			url: "/api/user/login",
	//			data: JSON.stringify(data), 
	//			contentType:"application/json; charset=utf-8",
	//			dataType: "json" 
	//		}).done(function(resp){
	//			//성공
	//			alert("로그인 완료");
	//			console.log(resp);
	//			location.href = "/";
	//		}).fail(function(error){
	//			alert(JSON.stringify(error));
	//		});
	//	}
}

index.init();