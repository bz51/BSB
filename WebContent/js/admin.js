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
				'<tr><td></td><td><a href="" class="doFeedbackBtn" data-role="button" data-theme="c" data-mini="true" onclick="clickHasDoBtn('+val.id+')">已处理</a></td></tr>'+
				'</table></li>';
				$("#list3").append(html);
			});
			
			$(".doFeedbackBtn").button();
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
	
	
	
	
	/**
	 * 点击合同按钮
	 * @param id
	 */
	function clickContractBtn(){
		//显示loading
		$.mobile.loading('show', {  
			text: '数据加载中...', //加载器中显示的文字  
			textVisible: true, //是否显示文字  
			theme: 'a',        //加载器主题样式a-e  
			textonly: false,   //是否只显示文字  
			html: ""           //要显示的html内容，如图片等  
		}); 
		
		//发送请求
		$.get("post/postAction!getNeedEntityList?state=3",
				
				function(data,status){
			var json = eval('(' + data + ')');
			
			//隐藏加载器  
			$.mobile.loading('hide');
			
			//若返回no
			if(json.result=="no"){
				alert(json.reason);
		    	window.location.href="#page4";
			}
			
			//若返回yes，显示查询到的结果
			else{
				window.location.href="#page4";
				
				$("#list4").text("");
				$.each(json.needList, function(index, val) {
					var html = '<li><a href="" id="getContractDetailBtn'+index+'"><table width="100%">'+
					'<tr><td width="70%"><span style="color:#009933;">'+(index+1)+'.'+val.title+'</span></td><td width="30%"><span style="font-size:13px;">'+timeStamp2String(val.time)+'</span></td></tr>'+
					'<tr><td><span style="font-size:13px;color:#999999;">'+val.needer_phone+'</span></td><td><span style="font-size:13px;color:#FF6600;">'+val.needer_name+'</span></td></tr>'+
					'<tr><td colspan="2"><span style="font-size:12px;color:red;">'+val.content+'</span></td><td></td></tr>'+
					'</table></a></li>';
					$("#list4").append(html);
					$("#getContractDetailBtn"+index).click(function(){
	    				clickContractDetailBtn(val);
	    			});
				});
				
				$("#list4").listview("refresh");
			}
			
		});
	}
	
	
	
	
	
	
	/**
	 * 点击查看订单详情
	 */
	var require_id = null;
	function clickContractDetailBtn(val){
		//将信息显示到界面上
		$("#title").text(val.title);
		$("#time").text(timeStamp2String(val.time));
		$("#needer_skill").text(skill2String_new(val.needer_skill));
		$("#money").text(val.money+"元");
		$("#content").text(val.content);
		$("#pic").attr('src',"http://erhuowang.cn/upload/"+val.pic);
		if(val.contract!=null)
			$("#contract").text(val.contract);
		require_id = val.id;
		
		//跳转
		window.location.href="#contractDetail";
	}
	
	
	
	
	
	/**
	 * 点击“发布合同”按钮
	 */
	function clickPostContractBtn(){
		//判断合同内容是否为空
		if($("#contract").val()==null || $("#contract").val()==''){
			alert("合同内容不能为空！");
			return;
		}
		
		//发布合同

		//显示loading
		$.mobile.loading('show', {  
			text: '数据加载中...', //加载器中显示的文字  
			textVisible: true, //是否显示文字  
			theme: 'a',        //加载器主题样式a-e  
			textonly: false,   //是否只显示文字  
			html: ""           //要显示的html内容，如图片等  
		}); 
		
		//发送请求
		$.get("post/postAction!postContract?contract="+$("#contract").val()+"&require_id="+require_id,
				
				function(data,status){
			var json = eval('(' + data + ')');
			
			//隐藏加载器  
			$.mobile.loading('hide');
			
			//若返回no
			if(json.result=="no"){
				alert(json.reason);
			}
			
			//若返回yes，显示查询到的结果
			else{
				alert("发布成功");
				//清空界面数据
				$("#title").text(null);
				$("#time").text(null);
				$("#needer_skill").text(null);
				$("#money").text(null);
				$("#content").text(null);
				$("#pic").attr('src',null);
				$("#contract").text(null);
				require_id = null;
				clickContractBtn();
			}
			
		});
	}
	
	
	/**
	 * 解析合同内容，并显示到界面上
	 */
	function AnalysisContract(contract){
		//清除列表中的内容
		$("#functionList").text("");
		
		var array = contract.split(""); //字符串转化为数组
		var firstIndex = 0;
		var html = "";
		var last = "";
		$.each(array, function(index, val) {
			//若遇到^，表示从firstIndex到当前index是模块名
			if(val=="^"){
				if(last=="p"){
					html = html + "</ol></li>";
					last = "";
				}
//				alert(contract.substring(firstIndex, index));
				html = html + "<li style='color:#666666;font-size:15px;'>"+contract.substring(firstIndex, index)+"</li>";
				firstIndex = index+1;
			}
			//若遇到~，表示从firstIndex到当前index是功能点
			else if(val=="~"){
				if(last==""){
					html = html + "<ol>";
				}
//				alert(contract.substring(firstIndex, index));
				html = html + "<li style='color:#666666;font-size:15px;'>"+contract.substring(firstIndex, index)+"</li>";
				firstIndex = index+1;
			}
			
			//若遇到*，表示从firstIndex到当前index是功能点的详细说明
			else if(val=="*"){
//				alert(contract.substring(firstIndex, index));
				html = html + "<p style='color:#666666;font-size:15px;'>"+contract.substring(firstIndex, index)+"</p>";
				last = "p";
				firstIndex = index+1;
			}
			
			//若遇到$表示终止，最后加上</ol></li>即可
			else if(val=="$"){
				html = html + "</ol></li>";
			}
		});
		
//		alert(html);
		//将生成的html添加到页面中
		$("#functionList").append(html);
//		$("#lalala").text(html);
	}