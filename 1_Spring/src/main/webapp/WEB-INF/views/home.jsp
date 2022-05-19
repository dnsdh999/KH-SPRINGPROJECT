<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page session="false" %>
<html>
<head>
	<title>Home</title>
	<style>
		#tb{margin:auto; width:700px;}
	</style>
</head>
<body>
	<c:import url="common/menubar.jsp"></c:import>
	
	<h1 align="center">게시글 top5목록</h1>
	<table id="tb" border="1">
		<thead>
			<tr style="background: yellowgreen;">
				<th>번호</th>
				<th>제목</th>
				<th>작성자</th>
				<th>날짜</th>
				<th>조회수</th>
				<th>첨부파일</th>
			</tr>
		</thead>
		<tbody></tbody>
	</table>
	

	
	<script>
		function topList(){
			$.ajax({
				url:'topList.bo',
				success:function(data){
					console.log(data);
					$tableBody = $('#tb tBody');
					$tableBody.html('');
					
					var $tr;
					var $boardId;
					var $boardTitle;
					var $nickName;
					var $boardCreateDate;
					var $boardCount;
					var $fileYN;
					
					if(data.length>0){
						for(var i in data){
							$tr = $('<tr>');
							$boardId = $('<td width="100">').text(data[i].boardId);
							$boardTitle = $('<td>').text(data[i].boardTitle);
							$nickName = $('<td width="100">').text(data[i].nickName);
							$boardCreateDate = $('<td width="100">').text(data[i].boardCreateDate);
							$boardCount = $('<td>').text(data[i].boardCount);
							if(data[i].originalFileName == null){
								$fileYN = $('<td align="center" width="100">').text("");
							}else{
								$fileYN = $('<td align="center" width=100">').text("◎");
							}
							
							$tr.append($boardId);
							$tr.append($boardTitle);
							$tr.append($nickName);
							$tr.append($boardCreateDate);
							$tr.append($boardCount);
							$tr.append($fileYN);
							
							$tableBody.append($tr);
;
						}
						
						$('#tb td').mouseenter(function(){
							$(this).parent().css({'color':'yellowgreen','font-weight':'bold','cursor':'pointer'});
						}).mouseout(function(){
							$(this).parent().css({'color':'black','font-weight':'normal'});
						}).click(function(){
							var bId = $(this).parent().children().eq(0).text();
							location.href="bdetail.bo?bId=" + bId + '&page=1';
						});
						
					}else{
						$tr = $('<tr>');
						$content = $('<td colspan="6">').text("등록된 게시글이 없습니다.");
						
						$tr.append($content);
						$tableBody.append($tr);
					}
				},
				error:function(data){
					console.log(data);
				}
			});
		}
		
		$(function(){
			
			topList();
			
			
			
			setInterval(function(){
				topList();
			}, 5000);
		});
	</script>
	
</body>
</html>
