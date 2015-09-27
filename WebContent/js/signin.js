$(document).ready(function(){
	/**
	 * 点击下一步按钮
	 */
	$("#next").click(function(){
		//判断是否为空
		if($("#name").val()=='' || $("#name").val()==null || $("#password").val()=='' || $("#password").val()==null || $("#repassword").val()=='' || $("#repassword").val()==null){
			$("#reason").text("称呼、密码还没填完呢~");
		}
		
		//若都填完了，判断密码和确认密码是否一致
		else if($("#password").val()!=$("#repassword").val()){
			$("#reason").text("两次密码不一致");
		}
		
		//填的都没问题了，则进入下一步
		else{
			window.location.href="#page2";
		}
	});
	
	
	/**
	 * 点击获取验证码按钮
	 */
	$("#getButton").click(function(){
		//判断电话是否为空
		if($("#phone").val()==null || $("#phone").val()=="")
			$("#reason2").text("手机号不能为空哦～");
		
		//验证手机号是否合法
		else if(!/^1\d{10}$/.test($("#phone").val())){
			$("#reason2").text("请输入正确的手机号～");
		}
		
		//若填了手机号，则获取验证码
		else{

			//显示loading
		    $.mobile.loading('show', {  
		        text: '获取中...', //加载器中显示的文字  
		        textVisible: true, //是否显示文字  
		        theme: 'a',        //加载器主题样式a-e  
		        textonly: false,   //是否只显示文字  
		        html: ""           //要显示的html内容，如图片等  
		    });  
		    
		    //发送请求
			$.post("user/userAction!getAuthCode",
				  {
					phone:$("#phone").val(),
				  },
				  
				  function(data,status){
				    var json = eval('(' + data + ')');
				    
				    //隐藏加载器  
				    $.mobile.loading('hide');
				    
				    //若返回no
				    if(json.result=="no"){
				    	$("#reason2").text(json.reason);
				    }
				   
				    //若返回yes，提示用户“验证码发送成功”
				    else{
				    	//跳转至个人中心
				    	$("#reason2").text("验证码发送成功啦！");
				    }
				  });
			
		}
	});
	
	
	/**
	 * 点击完成注册按钮
	 */
	$("#submitBtn").click(function(){
		//判断验证码和手机号是否为空
		if($("#phone").val()==null || $("#phone").val()=='' || $("#code")==null || $("#code")=='')
			$("#reason2").text("手机号和验证码不能为空哦～");
		
		//若验证码和手机号不为空，则提交所有信息，开始注册
		else{
			//显示loading
		    $.mobile.loading('show', {  
		        text: '注册中...', //加载器中显示的文字  
		        textVisible: true, //是否显示文字  
		        theme: 'a',        //加载器主题样式a-e  
		        textonly: false,   //是否只显示文字  
		        html: ""           //要显示的html内容，如图片等  
		    });  
		    
		    //模拟----------------------------------
		    //localStorage.setItem("role","1");
		    //localStorage.setItem("skill","1111111");
		    //----------------------------------
		    
		    //发送请求
			$.post("user/userAction!login",
				  {
					name:$("#name").val(),
					password:$("#password").val(),
					phone:$("#phone").val(),
					authcode:$("#code").val(),
					role:localStorage.getItem("role"),
					skill:localStorage.getItem("skill")
				  },
				  
				  function(data,status){
				    var json = eval('(' + data + ')');
				    
				    //隐藏加载器  
				    $.mobile.loading('hide');
				    
				    //若返回no
				    if(json.result=="no"){
				    	$("#reason2").text(json.reason);
				    }
				   
				    //若返回yes，提示用户“验证码发送成功”
				    else{
				    	
				    	//将用户信息保存到本地
				    	localStorage.setItem("id",json.id);
					    localStorage.setItem("name",json.name);
					    localStorage.setItem("phone",json.phone);
				    	localStorage.setItem("role",json.role);
					    localStorage.setItem("skill",json.skill);
				    	
					    //判断往哪儿跳转
					    var fromWhere = localStorage.getItem("fromWhere");
//					    alert("fromwhere＝"+fromWhere);
					    
				    	//跳转至provider个人中心
					    if(fromWhere=="providerHelp"){
					    	//跳转至发布成功页面providerHelp.html#page2
					    	window.location.href="provideHelp.html#page2";
					    }
					    
				    	//发布需求
					    else if(fromWhere=="getHelp"){
					    	
					    	//显示loading
						    $.mobile.loading('show', {  
						        text: '注册成功！正在发布...', //加载器中显示的文字  
						        textVisible: true, //是否显示文字  
						        theme: 'a',        //加载器主题样式a-e  
						        textonly: false,   //是否只显示文字  
						        html: ""           //要显示的html内容，如图片等  
						    });  
						    
					    	//发送请求
					    	$.get("post/postAction!postNeed?needEntity.title="+localStorage.getItem("title")+"&needEntity.content="+localStorage.getItem("content")+"&needEntity.money="+localStorage.getItem("money")+"&needEntity.needer_id="+localStorage.getItem("id")+"&needEntity.needer_name="+localStorage.getItem("name")+"&needEntity.needer_phone="+localStorage.getItem("phone")+"&needEntity.needer_skill="+localStorage.getItem("skill"),
							  
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
							    	localStorage.setItem("count",json.count)
							    	//跳转至getHelp.html#page4
							    	window.location.href="getHelp.html#page4";
							    }
							  });
					    }
				    	
				    }
				  });
		}
	});
	
});