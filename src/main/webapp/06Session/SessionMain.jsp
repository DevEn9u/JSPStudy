<%@ page import="java.util.Date"%>
<%@ page import="java.text.SimpleDateFormat"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%
// 날짜의 서식 지정
SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");  

// 세션이 생성된 시간, 내장객체 session.getCreateionTime()
long creationTime = session.getCreationTime();  
String creationTimeStr = dateFormat.format(new Date(creationTime));

// 세션에 마지막으로 접근한 시간
long lastTime = session.getLastAccessedTime(); 
String lastTimeStr = dateFormat.format(new Date(lastTime));

/*
위 2개의 시간은 1970년 1월 1일 이후 흘러온 시간을 ms 단위로 return하는데
우리가 정한 서식을 적용한 형태로 변환한다.
*/

// session 내장객체를 통해 session 유지시간 설정 : 초단위(잘 사용 안함)
// session.setMaxInactiveInterval(60*20);
%>
<html>
<head><title>Session</title></head>
<body>
    <h2>Session 설정 확인</h2>
    <ul>
        <li>세션 유지 시간 : <%= session.getMaxInactiveInterval() %></li>
        <li>세션 아이디 : <%= session.getId() %></li>
        <li>최초 요청 시각 : <%= creationTimeStr %></li>
        <li>마지막 요청 시각 : <%= lastTimeStr %></li>
    </ul>
</body>
</html>
