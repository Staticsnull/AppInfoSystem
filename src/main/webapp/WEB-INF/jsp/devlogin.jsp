<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>

<!DOCTYPE html>
<html>
<head>
<title>APP后台管理系统</title>
<link
	href="${pageContext.request.contextPath }/statics/css/bootstrap.min.css"
	rel="stylesheet">
<!-- Font Awesome -->
<link
	href="${pageContext.request.contextPath }/statics/css/font-awesome.min.css"
	rel="stylesheet">
<!-- NProgress -->
<link
	href="${pageContext.request.contextPath }/statics/css/nprogress.css"
	rel="stylesheet">
<!-- Animate.css -->
<link
	href="${pageContext.request.contextPath }/statics/css/animate.min.css"
	rel="stylesheet">

<!-- Custom Theme Style -->
<link
	href="${pageContext.request.contextPath }/statics/css/custom.min.css"
	rel="stylesheet">

</head>
<body class="login">
	<div>
		<div class="login_wrapper">
			<div class="animate form login_form">
				<section class="login_content">
					<form action="${pageContext.request.contextPath}/dev/dologin"
						method="post">
						<h1>APP 开发者平台</h1>

						<div>
							<input type="text" class="form-control" placeholder="请输入开发者用户名.."
								name="devCode" required="" />
						</div>
						<div>
							<input type="password" class="form-control"
								placeholder="请输入开发者用户密码.." name="devPassword" required="" />
						</div>
						<div>
							<span>${error }</span>
						</div>
						<div>
							<button type="submit" class="btn btn-success">登录</button>
							<button type="reset" class="btn btn-warning">重填</button>
						</div>
						<div class="clearfix"></div>

						<div class="separator">

							<div class="clearfix"></div>
							<br />

							<div>
								<p>©2017 All Rights Reserved</p>
							</div>
						</div>
					</form>
				</section>
			</div>
		</div>
	</div>
</body>

</html>
