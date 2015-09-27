document.write("<script language=javascript src='js/common.js'></script>");

$(document).ready(function(){
	//显示loading
    $.mobile.loading('show', {  
        text: '数据加载中...', //加载器中显示的文字  
        textVisible: true, //是否显示文字  
        theme: 'a',        //加载器主题样式a-e  
        textonly: false,   //是否只显示文字  
        html: ""           //要显示的html内容，如图片等  
    }); 
    
	//发送请求
	$.get("post/postAction!getOrderListByNeederId?needer_id="+localStorage.getItem("id"),
		  
		  function(data,status){
		    var json = eval('(' + data + ')');
		    
		    //隐藏加载器  
		    $.mobile.loading('hide');
		    
		    //若返回no
		    if(json.result=="no"){
		    	alert("网络错误，请重试");
		    }
		   
		    //若返回yes，显示查询到的结果
		    else{
		    	//遍历所有返回的结果
		    	$.each(json.needList, function(index, val) {
		    		
		    		//将title超过14个字符的部分用……表示
		    		var subTitle = val.title;
	    			if(val.title.length>14)
	    				subTitle = val.title.substr(0,14)+"……";
		    		//显示已抢单的信息
		    		if(val.state==1){
		    			var html = '<li><a href="" id="getDetail2Btn'+index+'"><table width="100%">'+
		    				'<tr><td width="70%"><span style="color:red;">抢单成功</span></td><td width="30%"><span style="font-size:13px;">'+timeStamp2String(val.time)+'</span></td></tr>'+
		    				'<tr><td><span style="font-size:13px;">'+subTitle+'</span></td><td><span style="font-size:13px;color:#FF6600;">'+val.money+'元</span></td></tr>'+
		    				'<tr><td colspan="2"><span style="font-size:12px;color:#999999;">'+skill2String(val.needer_skill)+'</span></td><td></td></tr>'+
		    				'</table></a></li>';
		    			$("#list").append(html);
		    			$("#getDetail2Btn"+index).click(function(){
		    				clickDetail2Btn(val);
		    			});
		    		}
		    		
		    		//显示等待抢单的信息
		    		if(val.state==0){
		    			var html = '<li><a href="" id="getDetail1Btn'+index+'"><table width="100%">'+
		    			'<tr><td width="70%"><span style="color:#009933;">等待抢单</span></td><td width="30%"><span style="font-size:13px;">'+timeStamp2String(val.time)+'</span></td></tr>'+
		    			'<tr><td><span style="font-size:13px;">'+subTitle+'</span></td><td><span style="font-size:13px;color:#FF6600;">'+val.money+'元</span></td></tr>'+
		    			'<tr><td colspan="2"><span style="font-size:12px;color:#999999;">'+skill2String(val.needer_skill)+'</span></td><td><input type="hidden" id="require_id"/></td></tr>'+
		    			'</table></a></li>';
		    			$("#list").append(html);
		    			$("#getDetail1Btn"+index).click(function(){
		    				clickDetail1Btn(val);
		    			});
		    		}
		    		
		    		//显示已失效的信息
		    		if(val.state==2){
		    			var html = '<li><table width="100%">'+
		    			'<tr><td width="70%"><span style="color:#666666;">已失效</span></td><td width="30%"><span style="font-size:13px;color:#666666;">'+timeStamp2String(val.time)+'</span></td></tr>'+
		    			'<tr><td><span style="font-size:13px;color:#666666;">'+subTitle+'</span></td><td><span style="font-size:13px;color:#666666;">'+val.money+'元</span></td></tr>'+
		    			'<tr><td colspan="2"><span style="font-size:12px;color:#666666;">'+skill2String(val.needer_skill)+'</span></td><td></td></tr>'+
		    			'</table></li>';
		    			$("#list").append(html);
		    		}
		    	});
		    	
		    	//刷新listview
    			$("#list").listview("refresh");
		    }
		    
	});
	
	/**
	 * 点击查看已抢单详情
	 */
	function clickDetail2Btn(val){
		$("#provider_name2").text(val.provider_name);
		$("#provider_phone2").text(val.provider_phone);
		$("#provider_skill2").text(skill2String(val.provider_skill));
		$("#title2").text(val.title);
		$("#time2").text(timeStamp2String(val.time));
		$("#needer_skill2").text(skill2String(val.needer_skill));
		$("#money2").text(val.money+"元");
		$("#content2").text(val.content);
		window.location.href="#pageDetail2";
	}
	
	/**
	 * 点击查看等待抢单详情
	 */
	function clickDetail1Btn(val){
		$("#title1").text(val.title);
		$("#time1").text(timeStamp2String(val.time));
		//在详情页加载之前，就把needer_skill的代码值存下来；这样当点击“提高赏金”时，从页面获取的needer_skill不是中文
		localStorage.setItem("needer_skill",val.needer_skill);
		$("#needer_skill1").text(skill2String(val.needer_skill));
		$("#money1").text(val.money+"元");
		$("#content1").text(val.content);
		localStorage.setItem("require_id",val.id);
		window.location.href="#pageDetail1";
	}

	
	/**
	 * 点击“放弃此单”按钮
	 */
	$("#giveUpBtn").click(function(){
		//显示loading
	    $.mobile.loading('show', {  
	        text: '处理中...', //加载器中显示的文字  
	        textVisible: true, //是否显示文字  
	        theme: 'a',        //加载器主题样式a-e  
	        textonly: false,   //是否只显示文字  
	        html: ""           //要显示的html内容，如图片等  
	    });  

	    //发送请求
	    $.get("post/postAction!neederGiveUp?require_id="+localStorage.getItem("require_id"),
			  
			 function(data,status){
			   var json = eval('(' + data + ')');
			    
			   //隐藏加载器  
			   $.mobile.loading('hide');
			    
			   //若返回no
			   if(json.result=="no"){
				   //若已有大神抢单，则获取该单详情，并跳转到抢单成功页面
				   if(json.reason=="已有大神抢单!"){
					   //显示loading
					   $.mobile.loading('show', {  
						   text: '刷新中...', //加载器中显示的文字  
						   textVisible: true, //是否显示文字  
						   theme: 'a',        //加载器主题样式a-e  
						   textonly: false,   //是否只显示文字  
						   html: ""           //要显示的html内容，如图片等  
					   });  

					   //发送请求，获取本单详情
					   $.get("post/postAction!getNeedDetail?require_id="+localStorage.getItem("require_id"),
						  
						 function(data,status){
						   var json = eval('(' + data + ')');
						   //隐藏加载器  
						   $.mobile.loading('hide');
						    
						   //若返回no
						   if(json.result=="no"){
							   alert("刷新出错，请重试");
						    }
						   
						    //若返回yes
						    else{
						    	alert("已被大神抢单！立即查看");
						    	clickDetail2Btn(json.needEntity);
						    }
						  });
				   }
			    }
			   
			    //若返回yes，跳转到订单列表
			    else{
			    	//将匹配到的大神人数显示
			    	window.location.href="adminNeeder.html";
			    }
			  });
	});
	
	
	/**
	 * 点击“提高赏金”按钮
	 */
	$("#increaseBtn").click(function(){
		//判断本单是否被抢
		//显示loading
		   $.mobile.loading('show', {  
			   text: '刷新中...', //加载器中显示的文字  
			   textVisible: true, //是否显示文字  
			   theme: 'a',        //加载器主题样式a-e  
			   textonly: false,   //是否只显示文字  
			   html: ""           //要显示的html内容，如图片等  
		   });  

		   //发送请求，获取本单详情
		   $.get("post/postAction!getNeedDetail?require_id="+localStorage.getItem("require_id"),
			  
			 function(data,status){
			   var json = eval('(' + data + ')');
			   //隐藏加载器  
			   $.mobile.loading('hide');
			    
			   //若返回no
			   if(json.result=="no"){
				   alert("网络出错，请重试");
			    }
			   
			    //若返回yes
			    else{
			    	//若已被抢单，则进入成功抢单详情页
			    	if(json.needEntity.state==1){
				    	alert("已被大神抢单！立即查看");
			    		clickDetail2Btn(json.needEntity);
			    	}
			    	
			    	//若未被抢单，则可以提高赏金
			    	else{
			    		//将本条需求的详细信息记录到本地
			    		localStorage.setItem("title",$("#title1").text());
			    		localStorage.setItem("time",$("#time1").text());
			    		//详情页加载之前needer_skill的代码形式就已经存入本地了，这里就不用再存了
//			    		localStorage.setItem("needer_skill",$("#needer_skill1").text());
			    		//若要提高赏金，那旧赏金就不需要了
//			    		localStorage.setItem("money",$("#money1").text());
			    		localStorage.setItem("content",$("#content1").text());
			    		//记录fromWhere
			    		localStorage.setItem("fromWhere","increaseMoney");
			    		//跳转至填写赏金页面
			    		window.location.href="getHelp.html#page3";
			    	}
			    }
			  });
		
		
	});
	
});