$(document).ready(function(){
	
	//判断是否登录
	//登录则进入个人中心
	if(localStorage.getItem("id")!=null && localStorage.getItem("id")!=''){
		if(localStorage.getItem("role")=="0")
			window.location.href="adminNeeder.html";
		else
			window.location.href="adminHelper.html";
	}
	
});