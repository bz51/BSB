$(document).ready(function(){
	
	//判断是否登录
	//登录则进入个人中心
	if(localStorage.getItem("id")!=null && localStorage.getItem("id")!=''){
//		alert("id="+localStorage.getItem("id")+",role="+localStorage.getItem("role"));
		if(localStorage.getItem("role")=="0")
			window.location.href="adminNeeder.html";
		else
			window.location.href="adminHelper.html";
	}

	//未登录，就进入首页
	else{
		//大神在线人数
		var count = new Array(365,479,273,398,401,321,379,405,455,412);
		var i = Math.floor(Math.random()*10);
		$("#count").text(count[i]);
	}

});