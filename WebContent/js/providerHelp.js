$(document).ready(function(){
	/**
	 * 点击下一步按钮
	 */
	$("#nextBtn").click(function(){
		//拼凑skill
		var skill = "";
		if($('#checkbox1').is(':checked'))
			skill = skill+"1";
		else
			skill = skill+"0";
		if($('#checkbox2').is(':checked'))
			skill = skill+"1";
		else
			skill = skill+"0";
		if($('#checkbox3').is(':checked'))
			skill = skill+"1";
		else
			skill = skill+"0";
		if($('#checkbox4').is(':checked'))
			skill = skill+"1";
		else
			skill = skill+"0";
		if($('#checkbox5').is(':checked'))
			skill = skill+"1";
		else
			skill = skill+"0";
		if($('#checkbox6').is(':checked'))
			skill = skill+"1";
		else
			skill = skill+"0";
		if($('#checkbox7').is(':checked'))
			skill = skill+"1";
		else
			skill = skill+"0";
		if($('#checkbox8').is(':checked'))
			skill = skill+"1";
		else
			skill = skill+"0";
		if($('#checkbox9').is(':checked'))
			skill = skill+"1";
		else
			skill = skill+"0";
		if($('#checkbox10').is(':checked'))
			skill = skill+"1";
		else
			skill = skill+"0";
		if($('#checkbox11').is(':checked'))
			skill = skill+"1";
		else
			skill = skill+"0";
		if($('#checkbox12').is(':checked'))
			skill = skill+"1";
		else
			skill = skill+"0";
		
		//若全为0表示没有选
		if(skill=="000000000000")
			$("#reason").text("请选择你擅长的技术");
		
		//若已经选好，将skill保存至本地，判断是否登录
		else{
			//将skill、role存至本地
			localStorage.setItem("skill",skill);
			localStorage.setItem("role","1");
			
			//检查cookie是否存在
			//若Cookie不存在，则记下fromWhere然后跳转至注册页面
			if(localStorage.getItem("id")==null || localStorage.getItem("id")==''){
				localStorage.setItem("fromWhere","providerHelp");
				window.location.href="signin.html";
			}
			//若Cookie存在，则表示修改擅长的信息，提交后跳转至个人中心的我的信息
			else{
				//显示loading
			    $.mobile.loading('show', {  
			        text: '修改中...', //加载器中显示的文字  
			        textVisible: true, //是否显示文字  
			        theme: 'a',        //加载器主题样式a-e  
			        textonly: false,   //是否只显示文字  
			        html: ""           //要显示的html内容，如图片等  
			    }); 
			    
			  //发送请求
				$.get("post/postAction!updateSkillByProviderId?userEntity.id="+localStorage.getItem("id")+"&userEntity.skill="+localStorage.getItem("skill"),
					  
					  function(data,status){
					    var json = eval('(' + data + ')');
					    
					    //隐藏加载器  
					    $.mobile.loading('hide');
					    
					    //若返回no
					    if(json.result=="no"){
					    	$("#reason").text(json.reason);
					    }
					   
					    //若返回yes，提示用户“修改skill成功”
					    else{
					    	//跳转至个人中心
					    	window.location.href="adminHelper.html";
					    }
					  });
			}
			
		}
		
	});
	
	
	/**
	 * 加载设置成功后的个人信息
	 */
	$("#name").text(localStorage.getItem("name"));
	$("#phone").text(localStorage.getItem("phone"));
	$("#skill").text(localStorage.getItem("skill"));
	
});