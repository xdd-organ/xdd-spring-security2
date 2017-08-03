<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>首页</title>
	<script type="text/javascript" src="http://apps.bdimg.com/libs/jquery/1.9.0/jquery.min.js"></script>
</head>
<body>
<a href="/logout">退出</a>
<button id="btn">注销</button>
<script type="text/javascript">
	$(function () {
        $("#btn").click(function () {
            $.post("/logout",function(data,status){
                console.log("退出成功！");
            });
        });
    });
</script>
</body>
</html>
