<%@page import="common.Person"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>표현 언어(EL) - 객체 매개변수</title>
</head>
<body>
	<!-- 
	영역에 저장된 속성에 EL로 접근할때는 속성명이 중복되지 않는다면
	EL 내장객체 없이 속성명 만으로 출력할 수 있다.
	$ {personObj.name } 은 Person 객체의 getName()을 하는 것이다.
	즉 requestScope.personObj.name 형태로 기술하지 않아도 된다. 
	
	'속성명.멤버변수' 형태로 기술하면 클래스에 선언된 getter()를
	자동으로 호출하여 값을 출력한다. 즉 아래 문장은 getter()를
	호출하므로 Person 클래스에서 getName()을 주석처리하면 에러가
	발생한다. 또한 출력시 형변환이 자동으로 되므로 더욱 편리하다.
	-->
	<h2>영역을 통해 전달된 객체 읽기</h2>
	<ul>
		<li>Person 객체 => 이름 : ${ personObj.name },
							나이 : ${ personObj.age } </li>
		<li>String 객체 => ${ requestScope.stringObj }</li>
		<li>Integer 객체 => ${ integerObj }</li>
	</ul>
	
	<h3>JSP 내장객체를 통해 영역의 값 읽어오기</h3>
	<%
	// 영역에는 모든 인스턴스를 저장할 수 있으므로 Object로 저장된다.
	Object object = request.getAttribute("personObj");
	// 따라서 영역에서 꺼낸 후 원래의 타입으로 형변환해야 한다.
	Person person = (Person) object;
	// 출력시에는 멤버변수에 직접 접근할 수 없으므로 getter를 사용한다.
	out.println("이름 : " + person.getName());
	// 이런 모든 절차를 EL은 자동으로 처리해준다.
	%>
	
	<!-- 
	파라미터로 전달된 값을 읽을때 아래 3가지 방법을 사용할 수 있다.
	1) param.파라미터명
	2) param.['파라미터명']
	3) param.["파라미터명"]
	 -->
	<h2>매개변수로 전달된 값 읽기</h2>
	<ul>
		<!-- 파라미터로 전달되는 값은 항상 String 타입으로 반환된다.
		따라서 산술연산을 위해서는 반드시 숫자형식으로 변환 후 진행해야 한다.
		하지만 EL에선는 자동으로 숫자형식으로 변환되므로 아래와 같이
		즉시 연산할 수 있다. -->
		<li>${ param.firstNum + param['secondNum'] }</li>
		
		<!-- 파라미터를 문자형식으로 출력했으므로 10 + 20이 된다. -->
		<li>${ param.firstNum } + ${ param["secondNum"] }</li>
	</ul>
</body>
</html>