<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
<style type="text/css">
html, body {width: 100%;height: 70%;}
html {overflow-y: scroll;}
body, div, dl, dt, dd, ul ol, li, h1, h2, h3, h4, h5, form, fieldset, p,button {margin: 0;padding: 0;}
body, h1, h2, h3, h4, input, button {font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif;font-size: 13px;color: rgb(6, 49, 46)}
body {background-color: gainsboro;}
li {list-style: none;}
#container {width: 760px;margin: 0 auto}
header {margin-top: 20px;padding: 20px;border: 1px solid #000000;background-color: teal;}
hgroup {overflow: hidden;padding-bottom: 20px;}
hgroup h1 {float: left;font-size: 18px;color: #fff;text-align: center;}
hgroup h2 {float: right;font-weight: normal;color: #fff;opacity: 0.8;font-family: 'Franklin Gothic Medium', 'Arial Narrow', Arial, sans-serif}
nav {clear: both;overflow: hidden;}
nav li {float: left;padding-right: 5px;font-family: Verdana, Geneva, Tahoma, sans-serif;}
nav li span {display: inline-block;padding: 3px 10px;border-radius: 5px;background-color: darkgreen;}
section {float: left;width: 518px;margin-top: 15px;margin-bottom: 20px;padding: 20px;border: 1px solid #330000;line-height: 20px;background-color: rgb(36, 124, 124);}
article {margin-bottom: 10px;}
article h1 {font-size: 16px;color: rgb(181, 223, 162);}
p {color: rgb(146, 180, 130);}
aside {float: right;width: 163px;padding: 10px;margin-top: 15px;margin-bottom: 15px;border: 1px solid #330000;line-height: 20px;background: rgb(36, 124, 124);}
aside .ad {height: 100px;margin-bottom: 20px;background-color: #ebebeb;text-align: center;font-size: 15px;color: #000000}
footer {clear: both;padding: 20px;border: 1px solid #330000;background: rgb(36, 124, 124);text-align: center;}
</style>
</head>
<body>
<div id="container">
<%@ include file="top.jsp" %>
<section>
            <article>
                <h1> 첫 번째 도서 제목</h1>
                <p> 첫 번째 도서의 내용</p>
            </article>
            <article>
                <h1> 두 번째 도서 제목</h1>
                <p> 두 번째 도서의 내용</p>
            </article>
            <article>
                <h1> 세 번째 도서 제목</h1>
                <p> 세 번째 도서의 내용</p>
            </article>
        </section>
            <aside>
                <p class="ad">문화 강화 광고</p>
                <ul>
                    <li><b>html강좌</b></li>
                    <li><b>컴퓨터강좌</b></li>
                    <li><b>독서강좌</b></li>
                </ul>
            </aside>
           <%@ include file="footer.jsp" %>
            </div>

</body>
</html>