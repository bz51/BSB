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
	$.get("post/postAction!getOrderListByProviderId?provider_id="+localStorage.getItem("id"),
		  
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
		    	//若没有信息，则显示“还木有人向你求助哦～”
		    	if(json.needList.length<=0){
		    		$("#tips").show();
		    		$("#noDataList").show();
		    		$("#list").hide();
		    	}
		    	
		    	//遍历所有返回的结果
		    	$.each(json.needList, function(index, val) {
		    		//有数据的话，隐藏noDataList，显示list
		    		$("#tips").hide();
		    		$("#noDataList").hide();
		    		$("#list").show();
		    		
		    		//将title超过14个字符的部分用……表示
		    		var subTitle = val.title;
	    			if(val.title.length>14){
	    				subTitle = val.title.substr(0,14)+"……";
	    			}
	    			
	    			
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
		    			'<tr><td width="70%"><span style="color:#009933;">抢单进行中</span></td><td width="30%"><span style="font-size:13px;">'+timeStamp2String(val.time)+'</span></td></tr>'+
		    			'<tr><td><span style="font-size:13px;">'+subTitle+'</span></td><td><span style="font-size:13px;color:#FF6600;">'+val.money+'元</span></td></tr>'+
		    			'<tr><td><span style="font-size:12px;color:#999999;">'+skill2String(val.needer_skill)+'</span></td><td><a href="" data-role="button" data-inline="true" data-mini="true" data-theme="b" id="grabSingle" class="grabSingle">立即抢单</a></td></tr>'+
		    			'</table></a></li>';
		    			$("#list").append(html);
		    			$("#getDetail1Btn"+index).click(function(){
		    				clickDetail1Btn(val);
		    			});
		    		}
		    		
		    		//显示等待对方付款的信息
		    		if(val.state==6){
		    			var html = '<li><a href="" id="getDetail6Btn'+index+'"><table width="100%">'+
	    				'<tr><td width="70%"><span style="color:red;">等待对方付款</span></td><td width="30%"><span style="font-size:13px;">'+timeStamp2String(val.time)+'</span></td></tr>'+
	    				'<tr><td><span style="font-size:13px;">'+subTitle+'</span></td><td><span style="font-size:13px;color:#FF6600;">'+val.money+'元</span></td></tr>'+
	    				'<tr><td colspan="2"><span style="font-size:12px;color:#999999;">'+skill2String(val.needer_skill)+'</span></td><td></td></tr>'+
	    				'</table></a></li>';
	    			$("#list").append(html);
	    			$("#getDetail6Btn"+index).click(function(){
	    				clickDetail6Btn(val);
	    			});
	    			
	    		}
		    		
		    		//显示开发中的信息
		    		if(val.state==7){
		    			var html = '<li><a href="" id="getDetail7Btn'+index+'"><table width="100%">'+
		    			'<tr><td width="70%"><span style="color:red;">开发中</span></td><td width="30%"><span style="font-size:13px;">'+timeStamp2String(val.time)+'</span></td></tr>'+
		    			'<tr><td><span style="font-size:13px;">'+subTitle+'</span></td><td><span style="font-size:13px;color:#FF6600;">'+val.money+'元</span></td></tr>'+
		    			'<tr><td colspan="2"><span style="font-size:12px;color:#999999;">'+skill2String(val.needer_skill)+'</span></td><td></td></tr>'+
		    			'</table></a></li>';
		    			$("#list").append(html);
		    			$("#getDetail7Btn"+index).click(function(){
		    				clickDetail7Btn(val);
		    			});
		    			
		    		}
		    		
		    		//显示验收中的信息
		    		if(val.state==8){
		    			var html = '<li><a href="" id="getDetail8Btn'+index+'"><table width="100%">'+
		    			'<tr><td width="70%"><span style="color:red;">验收中</span></td><td width="30%"><span style="font-size:13px;">'+timeStamp2String(val.time)+'</span></td></tr>'+
		    			'<tr><td><span style="font-size:13px;">'+subTitle+'</span></td><td><span style="font-size:13px;color:#FF6600;">'+val.money+'元</span></td></tr>'+
		    			'<tr><td colspan="2"><span style="font-size:12px;color:#999999;">'+skill2String(val.needer_skill)+'</span></td><td></td></tr>'+
		    			'</table></a></li>';
		    			$("#list").append(html);
		    			$("#getDetail8Btn"+index).click(function(){
		    				clickDetail8Btn(val);
		    			});
		    			
		    		}
		    		
		    		//显示通过验收的信息
		    		if(val.state==9){
		    			var html = '<li><a href="" id="getDetail9Btn'+index+'"><table width="100%">'+
		    			'<tr><td width="70%"><span style="color:red;">交易成功</span></td><td width="30%"><span style="font-size:13px;">'+timeStamp2String(val.time)+'</span></td></tr>'+
		    			'<tr><td><span style="font-size:13px;">'+subTitle+'</span></td><td><span style="font-size:13px;color:#FF6600;">'+val.money+'元</span></td></tr>'+
		    			'<tr><td colspan="2"><span style="font-size:12px;color:#999999;">'+skill2String(val.needer_skill)+'</span></td><td></td></tr>'+
		    			'</table></a></li>';
		    			$("#list").append(html);
		    			$("#getDetail9Btn"+index).click(function(){
		    				clickDetail9Btn(val);
		    			});
		    			
		    		}
		    		
		    	});
		    	
		    	//刷新listview
    			$("#list").listview("refresh");
    			$(".grabSingle").button();
		    }
		    
	});
	
	/**
	 * 点击查看已抢单详情
	 */
	function clickDetail2Btn(val){
		//判断从哪个页面跳过来
		//若从抢单页面跳过来，则详细信息从本地读取
		if(localStorage.getItem("fromWhere")=="grabSingle"){
			$("#needer_name2").text(localStorage.getItem("needer_name"));
			$("#needer_phone2").text(localStorage.getItem("needer_phone"));
			$("#title2").text(localStorage.getItem("title"));
			$("#time2").text(localStorage.getItem("time"));
			$("#needer_skill2").text(localStorage.getItem("needer_skill"));
			$("#money2").text(localStorage.getItem("money")+"元");
			$("#content2").text(localStorage.getItem("content"));
			$("#contract2").text(localStorage.getItem("contract"));
		}
		
		//若从订单列表跳过来，则详细信息从列表页面上读取，并保存到本地
		else{
			$("#needer_name2").text(val.needer_name);
			$("#needer_phone2").text(val.needer_phone);
			$("#title2").text(val.title);
			$("#time2").text(timeStamp2String(val.time));
			$("#needer_skill2").text(skill2String(val.needer_skill));
			$("#money2").text(val.money+"元");
			$("#content2").text(val.content);
			$("#contract2").text(val.contract);
			
			//将页面上的数据保存到本地，供确认可以开始也读取
			//将所有信息保存至本地，等待抢单成功后使用
			localStorage.setItem("title",val.title);
			localStorage.setItem("time",timeStamp2String(val.time));
			localStorage.setItem("needer_skill",skill2String(val.needer_skill));
			localStorage.setItem("money",val.money);
			localStorage.setItem("content",val.content);
			localStorage.setItem("needer_phone",val.needer_phone);
			localStorage.setItem("needer_name",val.needer_name);
			localStorage.setItem("contract",val.contract);
			localStorage.setItem("require_id",val.id);
		}
		window.location.href="#pageDetail2";
	}
	
	/**
	 * 点击查看等待抢单详情
	 */
	function clickDetail1Btn(val){
		//将信息显示到界面上
		$("#title1").text(val.title);
		$("#time1").text(timeStamp2String(val.time));
		$("#needer_skill1").text(skill2String(val.needer_skill));
		$("#money1").text(val.money+"元");
		$("#content1").text(val.content);
		$("#contract1").text(val.contract);
		localStorage.setItem("require_id",val.id);
		
		//将所有信息保存至本地，等待抢单成功后使用
		localStorage.setItem("title",val.title);
		localStorage.setItem("time",timeStamp2String(val.time));
		localStorage.setItem("needer_skill",skill2String(val.needer_skill));
		localStorage.setItem("money",val.money);
		localStorage.setItem("content",val.content);
		localStorage.setItem("needer_phone",val.needer_phone);
		localStorage.setItem("needer_name",val.needer_name);
		localStorage.setItem("contract",val.contract);
		localStorage.setItem("require_id",val.id);
		
		//跳转
		window.location.href="#pageDetail1";
	}
	
	
	
	
	/**
	 * 点击查看等待对方付款详情
	 */
	function clickDetail6Btn(val){
		//判断从哪个页面跳过来
		//若从确认可以开始页面跳过来，则详细信息从本地读取
		if(localStorage.getItem("fromWhere")=="querenKaishi"){
			$("#needer_name6").text(localStorage.getItem("needer_name"));
			$("#needer_phone6").text(localStorage.getItem("needer_phone"));
			$("#title6").text(localStorage.getItem("title"));
			$("#time6").text(localStorage.getItem("time"));
			$("#needer_skill6").text(localStorage.getItem("needer_skill"));
			$("#money6").text(localStorage.getItem("money")+"元");
			$("#content6").text(localStorage.getItem("content"));
			$("#contract6").text(localStorage.getItem("contract"));
		}
		
		//若从订单列表跳过来，则详细信息从列表页面上读取
		else{
			$("#needer_name6").text(val.needer_name);
			$("#needer_phone6").text(val.needer_phone);
			$("#title6").text(val.title);
			$("#time6").text(timeStamp2String(val.time));
			$("#needer_skill6").text(skill2String(val.needer_skill));
			$("#money6").text(val.money+"元");
			$("#content6").text(val.content);
			$("#contract6").text(val.contract);
			localStorage.setItem("require_id",val.id);
		}
		window.location.href="#pageDetail6";
	}
	
	
	
	
	/**
	 * 点击查看开发中详情
	 */
	function clickDetail7Btn(val){
		//判断从哪个页面跳过来
		//若从确认可以开始页面跳过来，则详细信息从本地读取
		if(localStorage.getItem("fromWhere")=="finishDevelop"){
			$("#needer_name7").text(localStorage.getItem("needer_name"));
			$("#needer_phone7").text(localStorage.getItem("needer_phone"));
			$("#title7").text(localStorage.getItem("title"));
			$("#time7").text(localStorage.getItem("time"));
			$("#needer_skill7").text(localStorage.getItem("needer_skill"));
			$("#money7").text(localStorage.getItem("money")+"元");
			$("#content7").text(localStorage.getItem("content"));
			$("#contract7").text(localStorage.getItem("contract"));
		}
		
		//若从订单列表跳过来，则详细信息从列表页面上读取
		else{
			$("#needer_name7").text(val.needer_name);
			$("#needer_phone7").text(val.needer_phone);
			$("#title7").text(val.title);
			$("#time7").text(timeStamp2String(val.time));
			$("#needer_skill7").text(skill2String(val.needer_skill));
			$("#money7").text(val.money+"元");
			$("#content7").text(val.content);
			$("#contract7").text(val.contract);
			localStorage.setItem("require_id",val.id);
		}
		window.location.href="#pageDetail7";
	}
	
	
	
	
	/**
	 * 点击查看验收中详情
	 */
	function clickDetail8Btn(val){
		//判断从哪个页面跳过来
		//若从确认可以开始页面跳过来，则详细信息从本地读取
		if(localStorage.getItem("fromWhere")=="finishDevelop"){
			$("#needer_name8").text(localStorage.getItem("needer_name"));
			$("#needer_phone8").text(localStorage.getItem("needer_phone"));
			$("#title8").text(localStorage.getItem("title"));
			$("#time8").text(localStorage.getItem("time"));
			$("#needer_skill8").text(localStorage.getItem("needer_skill"));
			$("#money8").text(localStorage.getItem("money")+"元");
			$("#content8").text(localStorage.getItem("content"));
			$("#contract8").text(localStorage.getItem("contract"));
		}
		
		//若从订单列表跳过来，则详细信息从列表页面上读取
		else{
			$("#needer_name8").text(val.needer_name);
			$("#needer_phone8").text(val.needer_phone);
			$("#title8").text(val.title);
			$("#time8").text(timeStamp2String(val.time));
			$("#needer_skill8").text(skill2String(val.needer_skill));
			$("#money8").text(val.money+"元");
			$("#content8").text(val.content);
			$("#contract8").text(val.contract);
			localStorage.setItem("require_id",val.id);
		}
		window.location.href="#pageDetail8";
	}
	
	
	
	/**
	 * 点击查看交易成功详情
	 */
	function clickDetail9Btn(val){
		//将信息显示到界面上
		$("#needer_name9").text(val.needer_name);
		$("#needer_phone9").text(val.needer_phone);
		$("#title9").text(val.title);
		$("#time9").text(timeStamp2String(val.time));
		$("#needer_skill9").text(skill2String(val.needer_skill));
		$("#money9").text(val.money+"元");
		$("#content9").text(val.content);
		$("#contract9").text(val.contract);
		localStorage.setItem("require_id",val.id);
		
//		//将所有信息保存至本地，等待抢单成功后使用
//		localStorage.setItem("title",val.title);
//		localStorage.setItem("time",timeStamp2String(val.time));
//		localStorage.setItem("needer_skill",skill2String(val.needer_skill));
//		localStorage.setItem("money",val.money);
//		localStorage.setItem("content",val.content);
//		localStorage.setItem("needer_phone",val.needer_phone);
//		localStorage.setItem("needer_name",val.needer_name);
//		localStorage.setItem("contract",val.contract);
//		localStorage.setItem("require_id",val.id);
		
		//跳转
		window.location.href="#pageDetail9";
	}
	
	
	
	/**
	 * 点击“立即抢单”按钮
	 */
	$("#grabSingle").click(function(){
		alert("点击了grabSingle");
		//显示loading
	    $.mobile.loading('show', {  
	        text: '抢单中...', //加载器中显示的文字  
	        textVisible: true, //是否显示文字  
	        theme: 'a',        //加载器主题样式a-e  
	        textonly: false,   //是否只显示文字  
	        html: ""           //要显示的html内容，如图片等  
	    });  

	    //发送请求
	    $.get("post/postAction!grabSingle?require_id="+localStorage.getItem("require_id")+"&userEntity.name="+localStorage.getItem("name")+"&userEntity.phone="+localStorage.getItem("phone")+"&userEntity.skill="+localStorage.getItem("skill")+"&userEntity.id="+localStorage.getItem("id")+"&userEntity.role=1&userEntity.weixin_id="+localStorage.getItem("open_id"),
			  
			 function(data,status){
			   var json = eval('(' + data + ')');
			   
			   //隐藏加载器  
			   $.mobile.loading('hide');
			    
			   //若返回no
			   if(json.result=="no"){
				   //若已被抢单，则重新刷新订单列表
//				   if(json.reason=="已被抢单"){
				   	   alert(json.reason);
					   window.location.href="adminHelper.html";
//				   }
			    }
			   
			    //若返回yes，跳转到详情页面
			    else{
			    	//标识从抢单页面跳转至抢单成功详情
			    	localStorage.setItem("fromWhere","grabSingle");
//			    	clickDetail2Btn();
			    	//跳转至抢单成功前对话框，对话框点“知道了”再模拟点击clickDetail2Btn
			    	window.location.href="#grabSingleSuccessDialog";
			    }
			  });		
	});
	
	
	/**
	 * 点击“抢单成功页前对话框”中的“知道了”按钮
	 */
	$("#grabSingleSuccessDialogKnownBtn").click(function(){
		alert("点击了grabSingleSuccessDialogKnownBtn");
		//模拟点击clickDetail2Btn
		clickDetail2Btn();
	});
	
	/**
	 * 点击“返回”按钮
	 */
	$("#fanhui").click(function(){
		alert("fromWhere="+localStorage.getItem("fromWhere"));
		//判断是从哪个页面跳转过来
		//若是从抢单页面跳过来，那么返回的话需要返回到列表，并且刷新
		if(localStorage.getItem("fromWhere")=="grabSingle"){
			localStorage.setItem("fromWhere","");
			window.location.href="adminHelper.html";
		}
		
		//若从“确认可以开始”页面调过来，则直接返回列表页
		else if(localStorage.getItem("fromWhere")=="querenKaishi"){
			localStorage.setItem("fromWhere","");
			window.location.href="adminHelper.html";
		}
		
		//若从“确认可以开始”页面调过来，则直接返回列表页
		else if(localStorage.getItem("fromWhere")=="finishDevelop"){
			localStorage.setItem("fromWhere","");
			window.location.href="adminHelper.html";
		}
		
		//若是从列表页面跳过来，那么返回的话只需要简单的返回即可
		else{
			window.history.go(-1);
		}
	});
	
	
	
	/**
	 * 点击“确认可以开始对话框的确定”按钮
	 */
	$("#querenKaishiDialogBtn").click(function(){
		//显示loading
	    $.mobile.loading('show', {  
	        text: '确认中...', //加载器中显示的文字  
	        textVisible: true, //是否显示文字  
	        theme: 'a',        //加载器主题样式a-e  
	        textonly: false,   //是否只显示文字  
	        html: ""           //要显示的html内容，如图片等  
	    });  

	    //发送请求
	    alert("post/postAction!providerConfirm?require_id="+localStorage.getItem("require_id"));
	    $.get("post/postAction!providerConfirm?require_id="+localStorage.getItem("require_id"),
			  
			 function(data,status){
			   var json = eval('(' + data + ')');
			   
			   //隐藏加载器  
			   $.mobile.loading('hide');
			    
			   //若返回no
			   if(json.result=="no"){
				   //若求助者已经放弃此单，则返回用户中心(暂时还没实现……)
				   alert(json.reason);
				   window.location.href="adminHelper.html";
			    }
			   
			    //若返回yes，跳转到等待对方付款详情页
			    else{
			    	//跳转到确认可以开始对话框
			    	localStorage.setItem("fromWhere","querenKaishi");
//			    	window.location.href="#pageDetail6";
			    	clickDetail6Btn();
			    }
			  });		
	});
	
	
	
	/**
	 * 点击“放弃此单(订单确认前)”按钮
	 */
	$("#providerGiveupOrderPreBtn").click(function(){
		//显示loading
		$.mobile.loading('show', {  
			text: '放弃中...', //加载器中显示的文字  
			textVisible: true, //是否显示文字  
			theme: 'a',        //加载器主题样式a-e  
			textonly: false,   //是否只显示文字  
			html: ""           //要显示的html内容，如图片等  
		});  
		
		//发送请求
		alert("post/postAction!providerGiveupOrderPre?require_id="+localStorage.getItem("require_id")+"&provider_name="+localStorage.getItem("name"));
		$.get("post/postAction!providerGiveupOrderPre?require_id="+localStorage.getItem("require_id")+"&provider_name="+localStorage.getItem("name"),
				
				function(data,status){
			var json = eval('(' + data + ')');
			
			//隐藏加载器  
			$.mobile.loading('hide');
			
			//若返回no
			if(json.result=="no"){
				//若求助者已经放弃此单，则返回用户中心(暂时还没实现……)
				alert(json.reason);
				window.location.href="adminHelper.html";
			}
			
			//若返回yes，跳转到列表页
			else{
				alert("放弃成功！");
				window.location.href="adminHelper.html";
			}
		});		
	});
	
	
	
	/**
	 * 点击“验收须知中完成开发”按钮
	 */
	$("#finishDevelopBtn").click(function(){
		//显示loading
		$.mobile.loading('show', {  
			text: '处理中...', //加载器中显示的文字  
			textVisible: true, //是否显示文字  
			theme: 'a',        //加载器主题样式a-e  
			textonly: false,   //是否只显示文字  
			html: ""           //要显示的html内容，如图片等  
		});  
		
		//发送请求
		alert("post/postAction!finishDevelop?require_id="+localStorage.getItem("require_id"));
		$.get("post/postAction!finishDevelop?require_id="+localStorage.getItem("require_id"),
				
				function(data,status){
			var json = eval('(' + data + ')');
			
			//隐藏加载器  
			$.mobile.loading('hide');
			
			//若返回no
			if(json.result=="no"){
				//完成开发失败时(考虑是否有其它情况导致点击完成开发失败………………???)
				alert(json.reason);
				window.location.href="adminHelper.html";
			}
			
			//若返回yes，跳转到列表页
			else{
				//跳转到
		    	localStorage.setItem("fromWhere","finishDevelop");
		    	clickDetail8Btn();
			}
		});		
	});
	
	
	//开启定时线程，每隔10秒检查下当前大神的订单数，若增多：“您有新的订单”，若减少：“订单被别人抢了”
	/**
	setInterval('clock()',5000);//1000为1秒钟
	
	window.clock = function () {
    	
    	$.get("post/postAction!getOrderListByProviderId?provider_id="+localStorage.getItem("id"),
    			  
    		function(data,status){
    			var json = eval('(' + data + ')');
    			
    			//若返回no
    			if(json.result=="no"){
    				alert("后台定时线程获取数据失败");
    			}
    			   
    			//若返回yes，显示查询到的结果
    			else{
    				//若本地订单数为空，则将当前获取的订单数存至本地
    				if(localStorage.getItem("orderCount")==null || localStorage.getItem("orderCount")==''){
    					localStorage.setItem("orderCount",json.needList.length);
    				}
    				
    				//若本地orderCount(订单数)存在，则进行比较
    				else{
//    					alert("orderCount="+localStorage.getItem("orderCount")+",length="+json.needList.length);
    					//订单数增多，提示用户“您有新的订单”
    					if(json.needList.length>localStorage.getItem("orderCount")){
    						alert("您有新的订单");
    						localStorage.setItem("orderCount",json.needList.length);
    						window.location.href="adminHelper.html";
    					}
    					
    					//订单数减少，提示用户“手慢了，订单已被别人抢走”
    					else if(json.needList.length<localStorage.getItem("orderCount")){
    						alert("手慢了，订单已被别人抢走");
    						localStorage.setItem("orderCount",json.needList.length);
    						window.location.href="adminHelper.html";
    					}
    					
    					//若订单数量没变，那么啥也不干等待下一次循环
    				}
    			}
    	});
    };
    */
});