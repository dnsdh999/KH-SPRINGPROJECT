<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html>
<head>
	<title>Home</title>
<script src="https://code.jquery.com/jquery-3.6.0.min.js"></script>
</head>
<body>
	<h1 align="center">Spring에서의 Ajax 사용</h1>
	
	<button onclick="location.href='test.do'">테스트</button>
	
	<ol>
		<li>
			서버쪽 컨트롤러로 값 보내기 <button id="test1">테스트</button>
			<script>
				$('#test1').on('click',function(){
					$.ajax({
						url: 'test1.do',
						data: {name:'강건강', age:20},
						success:function(data){
							console.log(data);
							if(data.trim()=='ok'){
								alert('전송성공');
							}else{
								alert('전송실패');
							}
						},
						error: function(data){
							console.log(data);
						}
					});
				});
			</script>
		</li>
		
		
		<li>
			컨트롤러에서 리턴하는 Json객체 받아서 출력하기
			<button id="test2">테스트</button>
			<div id="d2"></div>
			<script>
			$('#test2').on('click', function(){
				$.ajax({
					url:'test2.do',
					dataType:'json',
					success:function(data){
						console.log(data);
						<%--data = JSON.parse(data);--%>	//데이터타입지우고 할경우사용가능
						$('#d2').html('번호 : ' + data.no
										+ '<br>제목 : ' + data.title
										+ '<br>작성자 : ' + decodeURIComponent(data.writer)
										+ '<br>내용 : ' + decodeURIComponent(data.content.replace(/\+/g,' ')));
						//PrintWriter로 response utf-8로 변환시 디코드 부분을 빼도 아무문제없이 잘 실행이 된다.
					},
					error:function(data){
						console.log(data);
					}
				});
			});
			</script>
		
		
		</li>
		
		
		
		<li>
			컨트롤러에서 리턴하는 json배열을 받아서 출력하기 <button id="test3">테스트</button>
			<div id="d3"></div>
			
			<script>
				$('#test3').on('click',function(){
					$.ajax({
						url: 'test3.do',
						dataType:'json',
						success:function(data){
							console.log(data);
							
							var resultStr = "";
							
							for(var i in data){
								var user = data[i];
								resultStr += user.userId + ", " + user.userPwd + ", " + user.userName + ", " + user.age + ", " + user.email + ", " + user.phone + "<br>";
							}
						
							
							$('#d3').html(resultStr);
						},
						error: function(data){
							console.log(data);
						}
					});
				});
			</script>
		</li>
	</ol>
</body>
</html>
