$(document).ready(function(){
	
	//判断是否登录
	
	/**
	//登录则进入个人中心
	if(localStorage.getItem("id")!=null && localStorage.getItem("id")!=''){
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
	*/
	
	//情况1:本地无id、无openid：本地生成open_token，服务器生成open_token＋open_id键值对，state＝1，返回首页，发现有open_token，继而判断id是否存在，若存在则跳转到个人中心，若不存在则停留在首页
	if((localStorage.getItem("open_id")==null || localStorage.getItem("open_id")=='') 
		&& (localStorage.getItem("id")==null || localStorage.getItem("id")=='')){
//		alert("情况1");
		
		//本地无open_token:表示服务器还没获取open_id，需要进入授权流程
		//若本地open_token存在，但服务器重启了，服务器上open_token＋open_id键值对没有了，这时候怎么办？？？？？？？？？？？？？？？？？？
		if(localStorage.getItem("open_token")==null || localStorage.getItem("open_token")==''){
			//生成open_token，并存储本地
			var open_token = RndNum(10);
//			alert("open_token="+open_token);
			localStorage.setItem("open_token",open_token);
			//进行微信授权(state携带参数open_token)
			window.location.href="https://open.weixin.qq.com/connect/oauth2/authorize?appid=wx1a4c2e86c17d1fc4&redirect_uri=http://www.erhuowang.cn/wechat/wechatAction!auth_return_index&response_type=code&scope=snsapi_base&state="+open_token+"#wechat_redirect";
		}
		
		//本地有open_token:表示刚才服务器已经获取open_id，现在停留首页即可
//		else
//			alert("本地已存在open_token，直接停留首页即可!");
	}
	
	//情况2:(针对老用户)本地有id，无openid：本地生成open_token，服务器生成open_token＋open_id键值对，state＝2，返回登录页面
	else if((localStorage.getItem("open_id")==null || localStorage.getItem("open_id")=='' || localStorage.getItem("open_id")=='null') 
			&& (localStorage.getItem("id")!=null && localStorage.getItem("id")!='')){
//		alert("情况2");
		
		//本地无open_token:表示服务器还没获取open_id，需要进入授权流程
		if(localStorage.getItem("open_token")==null || localStorage.getItem("open_token")==''){
			//生成open_token，并存储本地
			var open_token = RndNum(10);
//			alert("open_token="+open_token);
			localStorage.setItem("open_token",open_token);
			//进行微信授权(state携带参数open_token)
			window.location.href="https://open.weixin.qq.com/connect/oauth2/authorize?appid=wx1a4c2e86c17d1fc4&redirect_uri=http://www.erhuowang.cn/wechat/wechatAction!auth_return_login&response_type=code&scope=snsapi_base&state="+open_token+"#wechat_redirect";
		}
		
		//本地有open_token:表示刚才服务器已经获取open_id，跳转至登录页面
		else
//			alert("本地已存在open_token，直接停留首页即可!");
			window.location.href="login.html";
	}
	
	//情况3:本地有id、有openid,直接跳转至相应的个人中心即可
	else if(localStorage.getItem("open_id")!=null && localStorage.getItem("open_id")!=''
		&& localStorage.getItem("id")!=null && localStorage.getItem("id")!=''){
//		alert("情况3");
		
		//根据不同的角色跳转至相应的个人中心
		if(localStorage.getItem("role")=="0")
			window.location.href="adminNeeder.html";
		else
			window.location.href="adminHelper.html";
	}
	
	//情况4://有open_id没有id，这是不可能的！
	
	/**
	 * 产生指定长度的随机数
	 * @param n
	 * @returns
	 */
	function RndNum(n){
		  var rnd="";
		  for(var i=0;i<n;i++)
		     rnd+=Math.floor(Math.random()*10);
		  return rnd;
		}

});