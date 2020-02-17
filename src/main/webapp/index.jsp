<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>

<!DOCTYPE html>
<html>
<head>
<title>APP后台管理系统</title>
	 <link href="${pageContext.request.contextPath }/statics/css/bootstrap.min.css" rel="stylesheet">
    <!-- Font Awesome -->
    <link href="${pageContext.request.contextPath }/statics/css/font-awesome.min.css" rel="stylesheet">
    <!-- NProgress -->
    <link href="${pageContext.request.contextPath }/statics/css/nprogress.css" rel="stylesheet">
    <!-- Animate.css -->
    <link href="${pageContext.request.contextPath }/statics/css/animate.min.css" rel="stylesheet">

    <!-- Custom Theme Style -->
    <link href="${pageContext.request.contextPath }/statics/css/custom.min.css" rel="stylesheet">
	
</head>

<body class="login">
	<div class="login_wrapper">
		<div class="animate form login_form">
			<h1>APP 信息管理平台</h1>

			<div>
				<a class="btn btn-link" href="manager/login">后台管理系统入口</a>
			</div>
			<div>
				<a class="btn btn-link" href="dev/login">开发者平台入口</a>
			</div>
			<div class="clearfix"></div>
			<div>
				<p>企业级的CMS系统--APP信息管理系统</p>
			</div>
		</div>
	</div>
</body>
</html>
