let index = {
	init:function(){
		$("#btn-save").on("click",()=>{
			this.save();
		});
		$("#btn-delete").on("click",()=>{
			this.deleteById();
		});
		$("#btn-update").on("click",()=>{
			this.update();
		});
		$("#btn-reply-save").on("click",()=>{
			this.replySave();
		});
	},
	
	save:function(){
		let data = {
			title : $("#title").val(),
			content: $("#content").val(),
		};

		$.ajax({
			type: "POST",
			url: "/api/board",
			data: JSON.stringify(data), //JSON 문자열이 된다
			contentType:"application/json; charset=utf-8", //body 데이타가 어떤 타입인지(MIME)
			dataType: "json" //요청을 서버로해서 응답이 왔을때 기본적으로 모든것이 문자열(생긴게 json이면) =>javascript오브젝트
		}).done(function(resp){
			alert("글쓰기 완료");
			console.log(resp);
			location.href = "/";
		}).fail(function(error){
			alert(JSON.stringify(error));
		});
	},
	
	deleteById:function(){
		var id = $("#id").text();
		
		$.ajax({
			type: "DELETE",
			url: "/api/board/"+id,
			dataType: "json" //요청을 서버로해서 응답이 왔을때 기본적으로 모든것이 문자열(생긴게 json이면) =>javascript오브젝트
		}).done(function(resp){
			alert("삭제 완료");
			location.href = "/";
		}).fail(function(error){
			alert(JSON.stringify(error));
		});
	},
	
	update:function(){
		let id = $("#id").val();
		let data = {
			title : $("#title").val(),
			content: $("#content").val(),
		};

		$.ajax({
			type: "PUT",
			url: "/api/board/" +id,
			data: JSON.stringify(data), //JSON 문자열이 된다
			contentType:"application/json; charset=utf-8", //body 데이타가 어떤 타입인지(MIME)
			dataType: "json" //요청을 서버로해서 응답이 왔을때 기본적으로 모든것이 문자열(생긴게 json이면) =>javascript오브젝트
		}).done(function(resp){
			alert("글수정 완료");
			console.log(resp);
			location.href = "/";
		}).fail(function(error){
			alert(JSON.stringify(error));
		});
	},
	
	replySave:function(){
		let data = {
			userId: $("#userId").val(),
			boardId: $("#boardId").val(),
			content: $("#reply-content").val(),
		};
		
		$.ajax({
			type: "POST",
			url: `/api/board/${data.boardId}/reply`,
			data: JSON.stringify(data), 
			contentType:"application/json; charset=utf-8", 
			dataType: "json"
		}).done(function(resp){
			alert("글쓰기 완료");
			console.log(resp);
			location.href = `/board/${data.boardId}`;
		}).fail(function(error){
			alert(JSON.stringify(error));
		});
	},
	
	replyDelete:function(boardId, replyId){
		$.ajax({
			type: "DELETE",
			url: `/api/board/${boardId}/reply/${replyId}`,
			dataType: "json" 
		}).done(function(resp){
			alert("댓글 삭제 완료");
			console.log(resp);
			location.href = `/board/${boardId}`;
		}).fail(function(error){
			alert(JSON.stringify(error));
		});
	},
}

index.init();