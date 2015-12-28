document.write("<script language=javascript src='js/common.js'></script>");

$(document).ready(function(){

	//判断本地是否有密码
	if(localStorage.getItem("admin_pass")!=null || localStorage.getItem("admin_pass")!=""){
		//显示loading
	    $.mobile.loading('show', {  
	        text: '数据加载中...', //加载器中显示的文字  
	        textVisible: true, //是否显示文字  
	        theme: 'a',        //加载器主题样式a-e  
	        textonly: false,   //是否只显示文字  
	        html: ""           //要显示的html内容，如图片等  
	    }); 
	    
	    
		//发送请求
		$.get("admin/adminAction!getAllUser?password="+localStorage.getItem("admin_pass"),
			  
			  function(data,status){
			    var json = eval('(' + data + ')');
			    
			    //隐藏加载器  
			    $.mobile.loading('hide');
			    
			    //若返回no
			    if(json.result=="no"){
			    	alert(json.reason);
			    	window.location.href="#page1";
			    }
			   
			    //若返回yes，显示查询到的结果
			    else{
			    	window.location.href="#page2";
			    	
			    	$.each(json.userList, function(index, val) {
			    		var role = "";
			    		if(val.role==0)
			    			role = "求助者";
			    		else
			    			role = "大神";
			    		var html = '<li><table width="100%">'+
			    		'<tr><td width="70%"><span style="color:#009933;">'+(index+1)+'.'+val.name+'</span></td><td width="30%"><span style="font-size:13px;">'+timeStamp2String(val.time)+'</span></td></tr>'+
			    		'<tr><td><span style="font-size:13px;">'+skill2String(val.skill)+'</span></td><td><span style="font-size:13px;color:#FF6600;">'+role+'</span></td></tr>'+
			    		'<tr><td colspan="2"><span style="font-size:12px;color:#999999;">'+val.phone+'</span></td><td></td></tr>'+
			    		'</table></li>';
			    	$("#list").append(html);
			    });
			    	
			    	$("#list").listview("refresh");
			    }
			    
		});
	}
	


	
	
	
	/**
	 * 点击“登录”按钮
	 */
	$("#loginButton").click(function(){
		//显示loading
	    $.mobile.loading('show', {  
	        text: '数据加载中...', //加载器中显示的文字  
	        textVisible: true, //是否显示文字  
	        theme: 'a',        //加载器主题样式a-e  
	        textonly: false,   //是否只显示文字  
	        html: ""           //要显示的html内容，如图片等  
	    }); 
	    
		//发送请求
		$.get("admin/adminAction!getAllUser?password="+$("#password").val(),
			  
			  function(data,status){
			    var json = eval('(' + data + ')');
			    
			    //隐藏加载器  
			    $.mobile.loading('hide');
			    
			    //若返回no
			    if(json.result=="no"){
			    	alert(json.reason);
			    	window.location.href="#page1";
			    }
			   
			    //若返回yes，显示查询到的结果
			    else{
			    	localStorage.setItem("admin_pass",$("#password").val());
			    	
			    	window.location.href="#page2";
			    	
			    	$.each(json.userList, function(index, val) {
			    		var role = "";
			    		if(val.role==0)
			    			role = "求助者";
			    		else
			    			role = "大神";
			    		var html = '<li><table width="100%">'+
			    		'<tr><td width="70%"><span style="color:#009933;">'+(index+1)+'.'+val.name+'</span></td><td width="30%"><span style="font-size:13px;">'+timeStamp2String(val.time)+'</span></td></tr>'+
			    		'<tr><td><span style="font-size:13px;">'+skill2String(val.skill)+'</span></td><td><span style="font-size:13px;color:#FF6600;">'+role+'</span></td></tr>'+
			    		'<tr><td colspan="2"><span style="font-size:12px;color:#999999;">'+val.phone+'</span></td><td></td></tr>'+
			    		'</table></li>';
			    	$("#list").append(html);
			    });
			    	
			    	$("#list").listview("refresh");
			    }
			    
		});
	});
	
	
});



/**
 * 点击“注册会员”按钮
 */
function clickUserBtn(){

	//显示loading
	$.mobile.loading('show', {  
		text: '数据加载中...', //加载器中显示的文字  
		textVisible: true, //是否显示文字  
		theme: 'a',        //加载器主题样式a-e  
		textonly: false,   //是否只显示文字  
		html: ""           //要显示的html内容，如图片等  
	}); 
	
	//发送请求
	$.get("admin/adminAction!getAllUser?password="+localStorage.getItem("admin_pass"),
			
			function(data,status){
		var json = eval('(' + data + ')');
		
		//隐藏加载器  
		$.mobile.loading('hide');
		
		//若返回no
		if(json.result=="no"){
			alert(json.reason);
	    	window.location.href="#page1";
		}
		
		//若返回yes，显示查询到的结果
		else{
			window.location.href="#page2";
			
			$("#list").text("");
			$.each(json.userList, function(index, val) {
				var role = "";
				if(val.role==0)
					role = "求助者";
				else
					role = "大神";
				var html = '<li><table width="100%">'+
				'<tr><td width="70%"><span style="color:#009933;">'+(index+1)+'.'+val.name+'</span></td><td width="30%"><span style="font-size:13px;">'+timeStamp2String(val.time)+'</span></td></tr>'+
				'<tr><td><span style="font-size:13px;">'+skill2String(val.skill)+'</span></td><td><span style="font-size:13px;color:#FF6600;">'+role+'</span></td></tr>'+
				'<tr><td colspan="2"><span style="font-size:12px;color:#999999;">'+val.phone+'</span></td><td></td></tr>'+
				'</table></li>';
				$("#list").append(html);
			});
			
			$("#list").listview("refresh");
		}
		
	});

}



/**
 * 点击“用户反馈”按钮
 */
function clickFeedbackBtn(){
	//显示loading
	$.mobile.loading('show', {  
		text: '数据加载中...', //加载器中显示的文字  
		textVisible: true, //是否显示文字  
		theme: 'a',        //加载器主题样式a-e  
		textonly: false,   //是否只显示文字  
		html: ""           //要显示的html内容，如图片等  
	}); 
	
	//发送请求
	$.get("admin/adminAction!getFeedback?password="+localStorage.getItem("admin_pass"),
			
			function(data,status){
		var json = eval('(' + data + ')');
		
		//隐藏加载器  
		$.mobile.loading('hide');
		
		//若返回no
		if(json.result=="no"){
			alert(json.reason);
	    	window.location.href="#page1";
		}
		
		//若返回yes，显示查询到的结果
		else{
			window.location.href="#page3";
			
			$("#list3").text("");
			$.each(json.feedbackList, function(index, val) {
				var role = "";
				if(val.role==0)
					role = "求助者";
				else
					role = "大神";
				var html = '<li><table width="100%">'+
				'<tr><td width="70%"><span style="color:#009933;">'+(index+1)+'.'+val.name+'</span></td><td width="30%"><span style="font-size:13px;">'+timeStamp2String(val.time)+'</span></td></tr>'+
				'<tr><td><span style="font-size:13px;color:#999999;">'+val.phone+'</span></td><td><span style="font-size:13px;color:#FF6600;">'+role+'</span></td></tr>'+
				'<tr><td colspan="2"><span style="font-size:12px;color:red;">'+val.content+'</span></td><td></td></tr>'+
				'<tr><td><a href="" data-role="button" data-theme="b" onclick="clickHasDoBtn('+val.id+')">已处理</a></td><td></td></tr>'+
				'</table></li>';
				$("#list3").append(html);
			});
			
			$("#list3").listview("refresh");
		}
		
	});
}


/**
 * 点击反馈信息“已处理”按钮
 * @param id
 */
function clickHasDoBtn(id){
	//显示loading
	$.mobile.loading('show', {  
		text: '数据加载中...', //加载器中显示的文字  
		textVisible: true, //是否显示文字  
		theme: 'a',        //加载器主题样式a-e  
		textonly: false,   //是否只显示文字  
		html: ""           //要显示的html内容，如图片等  
	}); 
	
	//发送请求
	$.get("admin/adminAction!doFeedback?password="+localStorage.getItem("admin_pass")+"&feedback_id="+id,
			
			function(data,status){
		var json = eval('(' + data + ')');
		
		//隐藏加载器  
		$.mobile.loading('hide');
		
		//若返回no
		if(json.result=="no"){
			alert(json.reason);
			if(json.reason=="口令错误")
				window.location.href="#page1";
		}
		
		//若返回yes，显示查询到的结果
		else{
			alert("操作成功");
			clickFeedbackBtn();
		}
		
	});

	
}