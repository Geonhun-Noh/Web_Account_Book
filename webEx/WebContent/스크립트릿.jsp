<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
    <%!
   String name = "노건훈";
   public String getName(){ return name;}
   %>
   
   <% String age = request.getParameter("age"); 
   if(age==null){
   age="27";}%>
   
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>스트립트릿 연습</title>
</head>
<body>
<h1>안녕하세요 <%=name %>님!</h1>
<h1>나이는 <%=age %>살 입니다!</h1>

</body>
</html>