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
		    				'<tr><td width="70%"><span style="color:#009933;">抢单成功</span></td><td width="30%"><span style="font-size:13px;">'+timeStamp2String(val.time)+'</span></td></tr>'+
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
		    			'<tr><td width="70%"><span style="color:red;">抢单进行中</span></td><td width="30%"><span style="font-size:13px;">'+timeStamp2String(val.time)+'</span></td></tr>'+
		    			'<tr><td><span style="font-size:13px;">'+subTitle+'</span></td><td><span style="font-size:13px;color:#FF6600;">'+val.money+'元</span></td></tr>'+
		    			'<tr><td><span style="font-size:12px;color:#999999;">'+skill2String(val.needer_skill)+'</span></td><td><a href="" data-role="button" data-inline="true" data-mini="true" data-theme="b" id="grabSingle" class="grabSingle">立即抢单</a></td></tr>'+
		    			'</table></a></li>';
		    			$("#list").append(html);
		    			$("#getDetail1Btn"+index).click(function(){
		    				clickDetail1Btn(val);
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
//				   if(json.reason=="已被抢单"){
				   	   alert(json.reason);
					   window.location.href="adminHelper.html";
//				   }
			    }
			   
			    //若返回yes，跳转到详情页面
			    else{
			    	//标识从抢单页面跳转至抢单成功详情
			    	localStorage.setItem("fromWhere","grabSingle");
			    	clickDetail2Btn();
			    }
			  });		
	});
	
	
	/**
	 * 点击“返回”按钮
	 */
	$("#fanhui").click(function(){
		//判断是从哪个页面跳转过来
		//若是从抢单页面跳过来，那么返回的话需要返回到列表，并且刷新
		if(localStorage.getItem("fromWhere")=="grabSingle"){
			localStorage.setItem("fromWhere","");
			window.location.href="adminHelper.html";
		}
		
		//若是从列表页面跳过来，那么返回的话只需要简单的返回即可
		else{
			window.history.go(-1);
		}
	});
	
	
	//开启定时线程，每隔10秒检查下当前大神的订单数，若增多：“您有新的订单”，若减少：“订单被别人抢了”
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
});