$(document).ready(function(){
	/**
	 * 点击登录按钮
	 */
	$("#loginButton").click(function(){
		//判断是否为空
		if($("#phone").val()==null || $("#password").val()==null || $("#phone").val()=='' || $("#password").val()==''){
			$("#reason").text("手机号、密码还没填好呢～");
		}
		
		//发送请求
		else{
			//显示loading
		    $.mobile.loading('show', {  
		        text: '登录中...', //加载器中显示的文字  
		        textVisible: true, //是否显示文字  
		        theme: 'a',        //加载器主题样式a-e  
		        textonly: false,   //是否只显示文字  
		        html: ""           //要显示的html内容，如图片等  
		    });  
		    
		    //发送请求
			$.post("user/userAction!signin",
				  {
					phone:$("#phone").val(),
					password:$("#password").val(),
					open_token:localStorage.getItem("open_token")//??????注册是一定要提交open_token的，若提交时open_token没了怎么办？?????????
				  },
				  
				  function(data,status){
				    var json = eval('(' + data + ')');
//				    alert(data);
				    //隐藏加载器  
				    $.mobile.loading('hide');
				    
				    //若返回no
				    if(json.result=="no"){
				    	$("#reason").text(json.reason);
				    	//本地的open_token存在，而服务器的open_token不存在，则重新跳转至授权页
				    	if(json.reason=='open_token is missing'){
				    		alert("授权过期啦，是否重新授权呀？");
							//进行微信授权(state携带参数open_token)
							window.location.href="https://open.weixin.qq.com/connect/oauth2/authorize?appid=wx1a4c2e86c17d1fc4&redirect_uri=http://www.erhuowang.cn/wechat/wechatAction!auth_return_login&response_type=code&scope=snsapi_base&state="+localStorage.getItem("open_token")+"#wechat_redirect";
				    	}
				    }
				   
				    //若返回yes，将用户信息保存到cookie
				    else{
				    	//将用户信息保存到本地
				    	localStorage.setItem("id","");
				    	localStorage.setItem("name","");
				    	localStorage.setItem("phone","");
				    	localStorage.setItem("skill","");
				    	localStorage.setItem("open_id","");//将服务器传回的open_id存入本地
				    	localStorage.setItem("id",json.id);
					    localStorage.setItem("name",json.name);
					    localStorage.setItem("phone",json.phone);
					    localStorage.setItem("skill",json.skill);
					    localStorage.setItem("open_id",json.open_id);//将服务器传回的open_id存入本地
					    
					  //判断往哪儿跳转
					    var fromWhere = localStorage.getItem("fromWhere");
//					    alert("fromWhere:"+fromWhere);
					    //从provider跳来,判断用户的role是否是provider
					    if(fromWhere=="providerHelp"){
					    	//若是，则进入个人中心
					    	if(json.role==localStorage.getItem("role")){
					    		localStorage.setItem("role","");
							    localStorage.setItem("role",json.role);
					    		window.location.href="adminHelper.html";
					    	}
					    	//若不是，则提示用户“你的身份是大神，无法修改身份”
					    	else{
					    		localStorage.setItem("role","");
							    localStorage.setItem("role",json.role);
					    		$("#title").text("你是求助者，还不能帮助别人哦～");
					    		$("#confirmBtn").attr("href","adminNeeder.html");
					    		window.location.href="#dialog";
					    	}
					    }
					    
					    //从发布需求跳来
					    else if(fromWhere=="getHelp"){
					    	//若身份吻合，则发布需求
					    	if(json.role==localStorage.getItem("role")){

						    	//显示loading
							    $.mobile.loading('show', {  
							        text: '正在发布...', //加载器中显示的文字  
							        textVisible: true, //是否显示文字  
							        theme: 'a',        //加载器主题样式a-e  
							        textonly: false,   //是否只显示文字  
							        html: ""           //要显示的html内容，如图片等  
							    });  
							    
						    	//发送请求
//							    alert("post/postAction!postNeed?needEntity.title="+localStorage.getItem("title")+"&needEntity.content="+localStorage.getItem("content")+"&needEntity.money="+localStorage.getItem("money")+"&needEntity.needer_id="+localStorage.getItem("id")+"&needEntity.needer_name="+localStorage.getItem("name")+"&needEntity.needer_phone="+localStorage.getItem("phone")+"&needEntity.needer_skill="+localStorage.getItem("needer_skill"));
						    	$.get("post/postAction!postNeed?needEntity.title="+localStorage.getItem("title")+"&needEntity.content="+localStorage.getItem("content")+"&needEntity.money="+localStorage.getItem("money")+"&needEntity.needer_id="+localStorage.getItem("id")+"&needEntity.needer_name="+localStorage.getItem("name")+"&needEntity.needer_phone="+localStorage.getItem("phone")+"&needEntity.needer_skill="+localStorage.getItem("needer_skill"),
								  
								  function(data,status){
								    var json = eval('(' + data + ')');
								    
								    //隐藏加载器  
								    $.mobile.loading('hide');
								    
								    //若返回no,则表明发布失败，跳转至发布最后一页
								    if(json.result=="no"){
								    	window.location.href="getHelp.html#page3";
								    	alert(json.reason);
								    }
								   
								    //若返回yes，需求发布成功
								    else{
								    	//将匹配到的大神人数保存至本地
								    	localStorage.setItem("count",json.count);
								    	//跳转至getHelp.html#page4
								    	window.location.href="getHelp.html#page4";
								    }
								  });
					    	}
					    	//若身份不吻合，则跳转至大神个人中心
					    	else{
					    		localStorage.setItem("role","");
							    localStorage.setItem("role",json.role);
					    		$("#title").text("你的身份是大神，无法发布求助信息哦～");
					    		$("#confirmBtn").attr("href","adminHelper.html");
					    		window.location.href="#dialog";
					    	}
					    }
					    
					    //从首页跳来，直接进入个人中心
					    else{
					    	//先将json中的role存到本地
				    		localStorage.setItem("role","");
					    	localStorage.setItem("role",json.role);
					    	
					    	//然后跳转至指定个人中心
					    	if(json.role==0)
				    			window.location.href="adminNeeder.html";
				    		else
				    			window.location.href="adminHelper.html";
					    } 
				    }
				  });
		}
	});
	
	
	
	
	
});