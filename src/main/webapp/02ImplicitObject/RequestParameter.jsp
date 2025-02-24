<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>내장객체 - request</title>
</head>
<body>
<%
/*
Tomcat 9.x 버젼에서는 post방식으로 한글은 전송하는 경우 깨짐현상이 발생되므로
아래와 같이 인코딩 처리를 해야한다.
*/
request.setCharacterEncoding("UTF-8");
/*
getParameter()
	: <input> 태그의 text, radio 타입처럼 하나의 값이 전송되는 경우에
	사용한다. 입력값이 문자, 숫자에 상관 없이 String 타입으로 저장된다
getParameterValues()
	: checkbox 혹은 <select> 태그의 multiple 속성을 부여하여 2개 이상의
	값이 전송되는 경우에 사용한다. 받은 값은 String 타입의 배열로 저장된다.
*/
// 하나의 값은 getParameter()로 받을 수 있다.
String id = request.getParameter("id");
String sex = request.getParameter("sex");
// 2개 이상의 값은 문자열 배열로 받아서 사용한다.
String[] favo = request.getParameterValues("favo");
String favoStr = "";
if (favo != null) {
	// 배열의 크기만큼 반복하여 항목을 얻어온다.
	for (int i = 0; i < favo.length; i++) {
		favoStr += favo[i] + " ";
	}
	// 체크박스는 선택(체크)한 항목만 서버로 전송된다.
}
/*
textarea 태그는 2주 이상 입력가능하므로 엔터를 추가하는 경우
\r\n 으로 지정한다. 따라서 웹브라우저에 출력할 대는 <br> 태그로
변경해야 한다.
*/
String intro = request.getParameter("intro").replace("\r\n", "<br/>"); 
%>
<ul>
	<li>아이디 : <%= id %></li>
	<li>성별 : <%= sex %></li>
	<li>관심사항 : <%= favo %></li>
	<li>자기소개 : <%= intro %></li>
</ul>
</body>
</html>