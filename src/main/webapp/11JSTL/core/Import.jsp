<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>JSTL - import</title>
</head>
<body>
<!-- 
import 태그
: 외부 문서를 현재문서에 포함시킨다. include 지시어처럼 외부문서를 먼저
포함시킨 후 컴파일하는 형식이 아니라, 컴파일이 완료된 결과물을 포함시킨다.
따라서 서로 다른 페이지이므로 page 영역은 공유되지 않고, request 영역만
공유된다.
URL 속성에 절대경로를 사용하는 경우 컨텍스트루트 경로명(우리로 치면 프로젝트명인 JSPStudy)은
포함되지 않는다.
=> 프로젝트를 직접 배포할때 컨텍스트루트 경로명이 있으면 안되기 때문.
-->
	<!-- request 영역에 변수를 저장 -->
	<c:set var="requestVar" value="MustHave" scope="request" />
	<!-- 
	import 태그에서 var 속성을
		미사용시 : include와 동일하게 현재 위치에 즉시 외부문서를
			포함시킨다.
		사용시 : var에 지정한 변수를 EL로 출력하는 곳에 포함시킨다.
			선언과 출력을 별도로 할 수 있으므로 코드의 가독성이 높아진다.
	 -->
	<c:import url="/11JSTL/inc/OtherPage.jsp" var="contents">
		<c:param name="user_param1" value="JSP" />
		<c:param name="user_param2" value="기본서" />
	</c:import>
	<!-- 
		import 태그의 하위태그로 param을 사용할 수 있다. 쿼리스트링을 통해
		전달하듯 지정된 페이지로 파라미터를 전달한다.
	 -->
	
	<h4>다른 문서 삽입하기</h4>
	${ contents }
	
	<h4>외부 자원 삽입하기</h4>
	<iframe src="../inc/GoldPage.jsp" style="width: 100%; height: 600px;"></iframe>
</body>
</html>