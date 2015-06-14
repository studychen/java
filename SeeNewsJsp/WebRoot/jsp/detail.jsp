<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ page language="java" import="com.chenxb.news.*" %>
<%@ page language="java" import="com.chenxb.bean.*" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%
String path = request.getContextPath();
//String num = request.getQueryString(); //获得新闻详情的num
String num = request.getParameter("num");
String original = "http://see.xidian.edu.cn/html/news/" + num + ".html";
NewsDetailItem detail = NewsDetail.loadDetail(num, true);
request.setAttribute("detail", detail);
request.setAttribute("original", original);

String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
  	<meta name="viewport" content="width=device-width, initial-scale=1.0, maximum-scale=2.0, minimum-scale=1.0, user-scalable=yes" />
  	<%--显示为可以拨号的连接 --%>
    <meta name="format-detection" content="telephone=yes">
    <base href="<%=basePath%>">
    
    <title>${detail.title }</title>
	<meta http-equiv="pragma" content="no-cache">
	<meta http-equiv="cache-control" content="no-cache">
	<meta http-equiv="expires" content="0">    
	<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
	<meta http-equiv="description" content="This is my page"> 
	<link rel="stylesheet" type="text/css" href=./css/detail.css>
  </head>
  
  <body> 
    <div id="article_title">
    	<h1>${detail.title }
    	</h1>
   	</div>
    
    <div id="article_detail">
    	<span>${detail.date }    </span>
    	<span>   浏览次数:${detail.readCount }</span>
   	</div>
    
    <div id="article_content">
    	 ${detail.body }
   	</div>
   	
   	<div id="original_post">
   		<p>SeeNews已优化<a href="${original}">原网页</a>方便移动设备查看</p>
 	</div>
  
  </body>
</html>
