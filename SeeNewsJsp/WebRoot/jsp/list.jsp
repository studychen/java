<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ page language="java" import="com.chenxb.news.*" %>
<%@ page language="java" import="com.chenxb.bean.*" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%
String path = request.getContextPath();
//String num = request.getQueryString(); //获得新闻详情的num
String num = request.getParameter("index");
String original = "http://see.xidian.edu.cn/index.php/index/more";
if(!num.equals("0"))
	original = "http://see.xidian.edu.cn/html/category/"+num+".html";
NewsList newsList = NewsList.loadNewsListItem(num, true);;
List<NewsListItem> listItems = newsList.getList();
request.setAttribute("listItems", listItems);

String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
  	<meta name="viewport" content="width=device-width, initial-scale=1.0, maximum-scale=2.0, minimum-scale=1.0, user-scalable=no" />
    <base href="<%=basePath%>">
    <title>My JSP 'index.jsp' starting page</title>
	<meta http-equiv="pragma" content="no-cache">
	<meta http-equiv="cache-control" content="no-cache">
	<meta http-equiv="expires" content="0">    
	<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
	<meta http-equiv="description" content="This is my page">
	<!--
	<link rel="stylesheet" type="text/css" href="styles.css">
	-->
  </head>
 
  <body> 
    <div id="original_post">
   		<p>SeeNews已优化<a href="<%= original %>">原网页</a>方便移动设备查看</p>
 	</div>
    <div id = "list_area">
    	<c:forEach items="${ listItems }" var="listItem">
   		<li>
    		<a class="list_item" href="${listItem.url }" target="_blank">
    		<span class="left_date">${listItem.date }</span>
    		${listItem.title }</a>
    		<span class="news_date">${listItem.click}</span>
   		</li>
    
    </c:forEach>
    </div>
  
  </body>
</html>
