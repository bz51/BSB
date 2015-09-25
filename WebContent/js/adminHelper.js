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
		    	//遍历所有返回的结果
		    	$.each(json.needList, function(index, val) {
		    		
		    		//显示已抢单的信息
		    		if(val.state==1){
		    			var html = '<li><a href="" id="getDetail2Btn'+index+'"><table width="100%">'+
		    				'<tr><td width="70%"><span style="color:red;">抢单成功</span></td><td width="30%"><span style="font-size:13px;">'+timeStamp2String(val.time)+'</span></td></tr>'+
		    				'<tr><td><span style="font-size:13px;">'+val.title+'</span></td><td><span style="font-size:13px;color:#FF6600;">'+val.money+'元</span></td></tr>'+
		    				'<tr><td colspan="2"><span style="font-size:13px;color:#999999;">'+skill2String(val.needer_skill)+'</span></td><td></td></tr>'+
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
		    			'<tr><td><span style="font-size:13px;">'+val.title+'</span></td><td><span style="font-size:13px;color:#FF6600;">'+val.money+'元</span></td></tr>'+
		    			'<tr><td><span style="font-size:13px;color:#999999;">'+skill2String(val.needer_skill)+'</span></td><td><a href="" data-role="button" data-inline="true" data-mini="true" data-theme="b" id="grabSingle">立即抢单</a></td></tr>'+
		    			'</table></a></li>';
		    			$("#list").append(html);
		    			$("#getDetail1Btn"+index).click(function(){
		    				clickDetail1Btn(val);
		    			});
		    		}
		    		
		    	});
		    	
		    	//刷新listview
    			$("#list").listview("refresh");
    			$("#grabSingle").button();
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
		}
		
		//若从订单列表跳过来，则详细信息从列表页面上读取
		else{
			$("#needer_name2").text(val.needer_name);
			$("#needer_phone2").text(val.needer_phone);
			$("#title2").text(val.title);
			$("#time2").text(timeStamp2String(val.time));
			$("#needer_skill2").text(skill2String(val.needer_skill));
			$("#money2").text(val.money+"元");
			$("#content2").text(val.content);
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
		localStorage.setItem("require_id",val.id);
		
		//将所有信息保存至本地，等待抢单成功后使用
		localStorage.setItem("title",val.title);
		localStorage.setItem("time",timeStamp2String(val.time));
		localStorage.setItem("needer_skill",skill2String(val.needer_skill));
		localStorage.setItem("money",val.money);
		localStorage.setItem("content",val.content);
		localStorage.setItem("needer_phone",val.needer_phone);
		localStorage.setItem("needer_name",val.needer_name);
		
		//跳转
		window.location.href="#pageDetail1";
	}
	
	/**
	 * 点击“立即抢单”按钮
	 */
	$("#grabSingle").click(function(){
		//显示loading
	    $.mobile.loading('show', {  
	        text: '抢单中...', //加载器中显示的文字  
	        textVisible: true, //是否显示文字  
	        theme: 'a',        //加载器主题样式a-e  
	        textonly: false,   //是否只显示文字  
	        html: ""           //要显示的html内容，如图片等  
	    });  

	    //发送请求
	    $.get("post/postAction!grabSingle?require_id="+localStorage.getItem("require_id")+"&userEntity.name="+localStorage.getItem("name")+"&userEntity.phone="+localStorage.getItem("phone")+"&userEntity.skill="+localStorage.getItem("skill")+"&userEntity.id="+localStorage.getItem("id")+"&userEntity.role=1",
			  
			 function(data,status){
			   var json = eval('(' + data + ')');
			    
			   //隐藏加载器  
			   $.mobile.loading('hide');
			    
			   //若返回no
			   if(json.result=="no"){
				   //若已被抢单，则重新刷新订单列表
				   if(json.reason=="已被抢单"){
					   window.location.href="adminHelper.html";
				   }
				   alert(json.reason);
			    }
			   
			    //若返回yes，跳转到详情页面
			    else{
			    	//标识从抢单页面跳转至抢单成功详情
			    	localStorage.setItem("fromWhere","grabSingle");
			    	clickDetail2Btn()
			    }
			  });		
	});

});