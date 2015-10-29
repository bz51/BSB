
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
		if($('#checkbox13').is(':checked'))
			skill = skill+"1";
		else
			skill = skill+"0";
		if($('#checkbox14').is(':checked'))
			skill = skill+"1";
		else
			skill = skill+"0";
		if($('#checkbox15').is(':checked'))
			skill = skill+"1";
		else
			skill = skill+"0";
		
		//若全为0表示没有选
		if(skill=="000000000000000")
			$("#reason").text("请选择你的毕设所涉及的技术");
		
		//若已经选好，将skill保存至本地，然后跳转下一步
		else{
			localStorage.setItem("skill",skill);
			window.location.href="#page2";
		}
		
	});
	
	
	/**
	 * 点击第二个下一步按钮
	 */
	$("#next2Btn").click(function(){
		//判断title和content是否为空
		if($("#title").val()==null || $("#title").val()=='' || $("#content").val()==null || $("#content").val()=='')
			$("#reason2").text("上面的都要填哦～");
		
		//若都已填满，则进入下一步
		else{
			window.location.href="#page3";
		}
	});
	
	
	/**
	 * 点击完成按钮
	 */
	$("#submitBtn").click(function(){
		//判断money是否为空
		if($("#money").val()==null || $("#money").val()=='')
			$("#reason3").text("打赏金额还没填呢～");
		
		else if(!/^[0-9]*$/.test($("#money").val())){
//			alert($("#money").val());
			$("#reason3").text("请输入正确的金额");
		}
		
		//若都已填满，则提交表单
		else{
			//显示loading
		    $.mobile.loading('show', {  
		        text: '正在匹配大神...', //加载器中显示的文字  
		        textVisible: true, //是否显示文字  
		        theme: 'a',        //加载器主题样式a-e  
		        textonly: false,   //是否只显示文字  
		        html: ""           //要显示的html内容，如图片等  
		    });  

		    //判断用户是否登录了
		    //若登录了，则直接发布
		    if(localStorage.getItem("id")!=null && localStorage.getItem("id")!=''){
				
		    	//发送请求
		    	var url = "";
		    	//若从“提高赏金”页面调过来，则从本地读取各个参数
//		    	alert("fromWhere="+localStorage.getItem("fromWhere"));
		    	if(localStorage.getItem("fromWhere")=="increaseMoney"){
		    		url = "post/postAction!increaseMoney?needEntity.id="+localStorage.getItem("require_id")+"&needEntity.money="+$("#money").val();
		    		localStorage.setItem("fromWhere","");
		    	}
		    	//若从发布需求一步步到这个页面，则从界面上直接获取各个参数
		    	else
		    		url = "post/postAction!postNeed?needEntity.title="+$("#title").val()+"&needEntity.content="+$("#content").val()+"&needEntity.money="+$("#money").val()+"&needEntity.needer_id="+localStorage.getItem("id")+"&needEntity.needer_name="+localStorage.getItem("name")+"&needEntity.needer_phone="+localStorage.getItem("phone")+"&needEntity.needer_skill="+localStorage.getItem("skill")+"&needEntity.needer_weixin="+localStorage.getItem("open_id");
//		    	alert(url);
		    	$.get(url,
				  
				  function(data,status){
				    var json = eval('(' + data + ')');
				    
				    //隐藏加载器  
				    $.mobile.loading('hide');
				    
				    //若返回no
				    if(json.result=="no"){
				    	$("#reason3").text(json.reason);
				    }
				   
				    //若返回yes
				    else{
				    	//将匹配到的大神人数显示
//				    	alert(data);
//				    	localStorage.setItem("count",json.count);//将得到的大神人数存到本地，因为注册成功后会在注册页面发布需求，然后将count值保存到本地，最后跳转到发布成功页面显示count。若count不存到本地，当从本地获取count后显示在页面上就会显示null
				    	$("#count").text(json.count);
				    	//跳转至page4
				    	window.location.href="#page4";
				    }
				  });
		    }
		    
		    //若没有登录，则先将信息存下来，跳转至注册
		    else{
		    	//若从“提高赏金”页面调过来，则本地已经有各个参数，无需再存了
		    	if(localStorage.getItem("fromWhere")!="increaseMoney"){
		    		localStorage.setItem("title",$("#title").val());
		    		localStorage.setItem("content",$("#content").val());
		    		localStorage.setItem("money",$("#money").val());
		    		localStorage.setItem("needer_skill",localStorage.getItem("skill"));
		    	}
		    	localStorage.setItem("role","0");
		    	localStorage.setItem("fromWhere","getHelp");
		    	window.location.href="signin.html";
		    }
		}
	});
	
	
	/**
	 * 点击“返回”按钮
	 */
	$("#fanhui").click(function(){
		//判断是从哪个页面跳转过来
		//若是从本单详情页面跳过来，那么设置fromWhere为提高赏金页
		if(localStorage.getItem("fromWhere")=="increaseMoney"){
			localStorage.setItem("fromWhere","");
//			window.location.href="adminNeeder.html";
			window.history.go(-2);
		}
		
		//如果从前面一步步跳过来，那返回的话就简单返回即可
		else
			window.history.go(-1);
	});
	
	
	//注册完后发布，发布成功则跳转到本页，显示匹配到的大神个数
	$("#count").text(localStorage.getItem("count"));
	
	//注册完后发布，若发布失败，跳转回本页，并将money显示在框中
	if(localStorage.getItem("money")!=null && localStorage.getItem("money")!='')
		$("#money").text(localStorage.getItem("money"));
	
	
	//若是要提高赏金，则将第三页的title改为“提高赏金”；若是一步步过来，则无需修改title
	if(localStorage.getItem("fromWhere")=="increaseMoney"){
		$("#titlePage3").text("提高赏金");
	}
	
});