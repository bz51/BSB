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
    
    //若从提高赏金页面返回到本单详情，则要显示原本的本单详情
//    if(localStorage.getItem("fromWhere")=="increaseMoney"){
//    	$("#title1").text(localStorage.getItem("title"));
//		$("#time1").text(localStorage.getItem("time"));
//		//在详情页加载之前，就把needer_skill的代码值存下来；这样当点击“提高赏金”时，从页面获取的needer_skill不是中文
//		$("#needer_skill1").text(skill2String(localStorage.getItem("needer_skill")));
//		$("#money1").text(localStorage.getItem("money"));
//		$("#content1").text(localStorage.getItem("content"));
//		localStorage.setItem("fromWhere","");
//		window.location.href="#pageDetail1";
//    }
    
    
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
//		    	alert("size="+json.needList.size);
		    	$.each(json.needList, function(index, val) {
		    		
//		    		alert("当前state＝"+val.state);
		    		
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

		    		//显示拟定合同的信息
		    		if(val.state==3){
		    			var html = '<li><a href="" id="getDetail3Btn'+index+'"><table width="100%">'+
		    			'<tr><td width="70%"><span style="color:#009933;">交易协议定制中</span></td><td width="30%"><span style="font-size:13px;">'+timeStamp2String(val.time)+'</span></td></tr>'+
		    			'<tr><td><span style="font-size:13px;">'+subTitle+'</span></td><td><span style="font-size:13px;color:#FF6600;">'+val.money+'元</span></td></tr>'+
		    			'<tr><td colspan="2"><span style="font-size:12px;color:#999999;">'+skill2String(val.needer_skill)+'</span></td><td><input type="hidden" id="require_id"/></td></tr>'+
		    			'</table></a></li>';
		    			$("#list").append(html);
		    			$("#getDetail3Btn"+index).click(function(){
		    				clickDetail3Btn(val);
		    			});
		    		}
		    		
		    		//显示合同拟定成功的信息
		    		if(val.state==4){
		    			var html = '<li><a href="" id="getDetail4Btn'+index+'"><table width="100%">'+
		    			'<tr><td width="70%"><span style="color:red;">请阅读交易协议</span></td><td width="30%"><span style="font-size:13px;">'+timeStamp2String(val.time)+'</span></td></tr>'+
		    			'<tr><td><span style="font-size:13px;">'+subTitle+'</span></td><td><span style="font-size:13px;color:#FF6600;">'+val.money+'元</span></td></tr>'+
		    			'<tr><td colspan="2"><span style="font-size:12px;color:#999999;">'+skill2String(val.needer_skill)+'</span></td><td><input type="hidden" id="require_id"/></td></tr>'+
		    			'</table></a></li>';
		    			$("#list").append(html);
		    			$("#getDetail4Btn"+index).click(function(){
		    				clickDetail4Btn(val);
		    			});
		    		}
		    		
		    		//显示大神已确认可以开始服务的信息
		    		if(val.state==6){
		    			var html = '<li><a href="" id="getDetail6Btn'+index+'"><table width="100%">'+
		    			'<tr><td width="70%"><span style="color:red;">大神准备就绪</span></td><td width="30%"><span style="font-size:13px;">'+timeStamp2String(val.time)+'</span></td></tr>'+
		    			'<tr><td><span style="font-size:13px;">'+subTitle+'</span></td><td><span style="font-size:13px;color:#FF6600;">'+val.money+'元</span></td></tr>'+
		    			'<tr><td colspan="2"><span style="font-size:12px;color:#999999;">'+skill2String(val.needer_skill)+'</span></td><td><input type="hidden" id="require_id"/></td></tr>'+
		    			'</table></a></li>';
		    			$("#list").append(html);
		    			$("#getDetail6Btn"+index).click(function(){
		    				clickDetail6Btn(val);
		    			});
		    		}
		    		
		    		//显示大神开发中的信息
		    		if(val.state==7){
		    			var html = '<li><a href="" id="getDetail7Btn'+index+'"><table width="100%">'+
		    			'<tr><td width="70%"><span style="color:red;">大神开发中</span></td><td width="30%"><span style="font-size:13px;">'+timeStamp2String(val.time)+'</span></td></tr>'+
		    			'<tr><td><span style="font-size:13px;">'+subTitle+'</span></td><td><span style="font-size:13px;color:#FF6600;">'+val.money+'元</span></td></tr>'+
		    			'<tr><td colspan="2"><span style="font-size:12px;color:#999999;">'+skill2String(val.needer_skill)+'</span></td><td><input type="hidden" id="require_id"/></td></tr>'+
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
		    			'<tr><td colspan="2"><span style="font-size:12px;color:#999999;">'+skill2String(val.needer_skill)+'</span></td><td><input type="hidden" id="require_id"/></td></tr>'+
		    			'</table></a></li>';
		    			$("#list").append(html);
		    			$("#getDetail8Btn"+index).click(function(){
		    				clickDetail8Btn(val);
		    			});
		    		}
		    		
		    		//显示交易成功的信息
		    		if(val.state==9){
		    			var html = '<li><a href="" id="getDetail9Btn'+index+'"><table width="100%">'+
		    			'<tr><td width="70%"><span style="color:red;">交易成功</span></td><td width="30%"><span style="font-size:13px;">'+timeStamp2String(val.time)+'</span></td></tr>'+
		    			'<tr><td><span style="font-size:13px;">'+subTitle+'</span></td><td><span style="font-size:13px;color:#FF6600;">'+val.money+'元</span></td></tr>'+
		    			'<tr><td colspan="2"><span style="font-size:12px;color:#999999;">'+skill2String(val.needer_skill)+'</span></td><td><input type="hidden" id="require_id"/></td></tr>'+
		    			'</table></a></li>';
		    			$("#list").append(html);
		    			$("#getDetail9Btn"+index).click(function(){
		    				clickDetail9Btn(val);
		    			});
		    		}
		    		
		    		//显示仲裁中的信息
		    		if(val.state==10){
		    			var html = '<li><a href="" id="getDetail10Btn'+index+'"><table width="100%">'+
		    			'<tr><td width="70%"><span style="color:red;">仲裁中</span></td><td width="30%"><span style="font-size:13px;">'+timeStamp2String(val.time)+'</span></td></tr>'+
		    			'<tr><td><span style="font-size:13px;">'+subTitle+'</span></td><td><span style="font-size:13px;color:#FF6600;">'+val.money+'元</span></td></tr>'+
		    			'<tr><td colspan="2"><span style="font-size:12px;color:#999999;">'+skill2String(val.needer_skill)+'</span></td><td><input type="hidden" id="require_id"/></td></tr>'+
		    			'</table></a></li>';
		    			$("#list").append(html);
		    			$("#getDetail10Btn"+index).click(function(){
		    				clickDetail10Btn(val);
		    			});
		    		}
		    		
		    		//显示仲裁完成的信息
		    		if(val.state==12){
		    			var html = '<li><a href="" id="getDetail12Btn'+index+'"><table width="100%">'+
		    			'<tr><td width="70%"><span style="color:red;">仲裁结束</span></td><td width="30%"><span style="font-size:13px;">'+timeStamp2String(val.time)+'</span></td></tr>'+
		    			'<tr><td><span style="font-size:13px;">'+subTitle+'</span></td><td><span style="font-size:13px;color:#FF6600;">'+val.money+'元</span></td></tr>'+
		    			'<tr><td colspan="2"><span style="font-size:12px;color:#999999;">'+skill2String(val.needer_skill)+'</span></td><td><input type="hidden" id="require_id"/></td></tr>'+
		    			'</table></a></li>';
		    			$("#list").append(html);
		    			$("#getDetail12Btn"+index).click(function(){
		    				clickDetail12Btn(val);
		    			});
		    		}
		    	});
		    	
		    	//刷新listview
    			$("#list").listview("refresh");
		    }
	
	});
	
	
	
	
	
	
	/**
	 * 点击“返回”按钮
	 */
//	$("#fanhui").click(function(){
//		//alert("fromWhere="+localStorage.getItem("fromWhere"));
//		//判断是从哪个页面跳转过来
//		//若是从抢单页面跳过来，那么返回的话需要返回到列表，并且刷新
//		if(localStorage.getItem("fromWhere")=="neederPay"){
//			localStorage.setItem("fromWhere","");
//			window.location.href="adminNeeder.html";
//		}
//		
//		//若是从列表页面跳过来，那么返回的话只需要简单的返回即可
//		else{
//			window.history.go(-1);
//		}
//	});
	
    
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
		
		//将合同内容显示到合同页中去
		AnalysisContract(val.contract);
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
		
		//将合同内容显示到合同页中去
		AnalysisContract(val.contract);
		window.location.href="#pageDetail1";
	}
	
	/**
	 * 点击查看拟定合同中详情
	 */
	function clickDetail3Btn(val){
		$("#title3").text(val.title);
		$("#time3").text(timeStamp2String(val.time));
		//在详情页加载之前，就把needer_skill的代码值存下来；这样当点击“提高赏金”时，从页面获取的needer_skill不是中文
		localStorage.setItem("needer_skill",val.needer_skill);
		$("#needer_skill3").text(skill2String(val.needer_skill));
		$("#money3").text(val.money+"元");
		$("#content3").text(val.content);
		localStorage.setItem("require_id",val.id);
		alert("管理员正在为您定制交易协议，预计30分钟后与您联系");
		window.location.href="#pageDetail3";
	}
	
	/**
	 * 点击查看确认合同中详情
	 */
	function clickDetail4Btn(val){
		$("#title4").text(val.title);
		$("#time4").text(timeStamp2String(val.time));
		//在详情页加载之前，就把needer_skill的代码值存下来；这样当点击“提高赏金”时，从页面获取的needer_skill不是中文
		localStorage.setItem("needer_skill",val.needer_skill);
		$("#needer_skill4").text(skill2String(val.needer_skill));
		$("#money4").text(val.money+"元");
		$("#content4").text(val.content);
//		$("#contract4").text(val.contract);
		localStorage.setItem("require_id",val.id);
		//解析合同内容，并显示到界面上
		AnalysisContract(val.contract);
		window.location.href="#pageDetail4";
	}
	
	/**
	 * 解析合同内容，并显示到界面上
	 */
	function AnalysisContract(contract){
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
		$("#functionList2").append(html);
//		$("#lalala").text(html);
	}
	
	/**
	 * 点击查看大神准备就绪详情
	 */
	function clickDetail6Btn(val){
		$("#provider_name6").text(val.provider_name);
		$("#provider_phone6").text(val.provider_phone);
		$("#provider_skill6").text(skill2String(val.provider_skill));
		$("#title6").text(val.title);
		$("#time6").text(timeStamp2String(val.time));
		$("#needer_skill6").text(skill2String(val.needer_skill));
		$("#money6").text(val.money+"元");
		$("#content6").text(val.content);
		$("#contract6").text(val.contract);
		//将所有信息保存至本地，供查看大神开发中使用
		localStorage.setItem("title",val.title);
		localStorage.setItem("time",timeStamp2String(val.time));
		localStorage.setItem("needer_skill",skill2String(val.needer_skill));
		localStorage.setItem("provider_skill",skill2String(val.provider_skill));
		localStorage.setItem("money",val.money);
		localStorage.setItem("content",val.content);
		localStorage.setItem("provider_phone",val.provider_phone);
		localStorage.setItem("provider_name",val.provider_name);
		localStorage.setItem("contract",val.contract);
		localStorage.setItem("require_id",val.id);

		//将合同内容显示到合同页中去
		AnalysisContract(val.contract);
		window.location.href="#pageDetail6";
	}
	
	/**
	 * 点击查看大神开发中详情
	 */
	function clickDetail7Btn(val){
		//判断从哪个页面跳过来
		//若从付款页面跳过来，则详细信息从本地读取
		if(localStorage.getItem("fromWhere")=="neederPay"){
			$("#provider_name7").text(localStorage.getItem("provider_name"));
			$("#provider_phone7").text(localStorage.getItem("provider_phone"));
			$("#provider_skill7").text(localStorage.getItem("provider_skill"));
			$("#needer_name7").text(localStorage.getItem("needer_name"));
			$("#needer_phone7").text(localStorage.getItem("needer_phone"));
			$("#title7").text(localStorage.getItem("title"));
			$("#time7").text(localStorage.getItem("time"));
			$("#needer_skill7").text(localStorage.getItem("needer_skill"));
			$("#money7").text(localStorage.getItem("money")+"元");
			$("#content7").text(localStorage.getItem("content"));
			$("#contract7").text(localStorage.getItem("contract"));

			//将合同内容显示到合同页中去
			AnalysisContract(localStorage.getItem("contract"));
		}
		
		//若从订单列表跳过来，则详细信息从列表页面上读取，并保存到本地
		else{
			$("#provider_name7").text(val.provider_name);
			$("#provider_phone7").text(val.provider_phone);
			$("#provider_skill7").text(skill2String(val.provider_skill));
			$("#title7").text(val.title);
			$("#time7").text(timeStamp2String(val.time));
			$("#needer_skill7").text(skill2String(val.needer_skill));
			$("#money7").text(val.money+"元");
			$("#content7").text(val.content);
			$("#contract7").text(val.contract);
			localStorage.setItem("require_id",val.id);

			//将合同内容显示到合同页中去
			AnalysisContract(val.contract);
		}
		window.location.href="#pageDetail7";
	}
	
	
	/**
	 * 点击查看验收中详情
	 */
	function clickDetail8Btn(val){
		$("#provider_name8").text(val.provider_name);
		$("#provider_phone8").text(val.provider_phone);
		$("#provider_skill8").text(skill2String(val.provider_skill));
		$("#title8").text(val.title);
		$("#time8").text(timeStamp2String(val.time));
		$("#needer_skill8").text(skill2String(val.needer_skill));
		$("#money8").text(val.money+"元");
		$("#content8").text(val.content);
		$("#contract8").text(val.contract);
		//将所有信息保存至本地，供查看大神开发中使用
		localStorage.setItem("title",val.title);
		localStorage.setItem("time",timeStamp2String(val.time));
		localStorage.setItem("needer_skill",skill2String(val.needer_skill));
		localStorage.setItem("provider_skill",skill2String(val.provider_skill));
		localStorage.setItem("money",val.money);
		localStorage.setItem("content",val.content);
		localStorage.setItem("provider_phone",val.provider_phone);
		localStorage.setItem("provider_name",val.provider_name);
		localStorage.setItem("contract",val.contract);
		localStorage.setItem("require_id",val.id);

		//将合同内容显示到合同页中去
		AnalysisContract(val.contract);
		alert("大神将使用QQ远程协助为您部署程序！");
		window.location.href="#pageDetail8";
	}
	
	
	/**
	 * 点击查看交易成功详情
	 */
	function clickDetail9Btn(val){
		//判断从哪个页面跳过来
		//若从付款页面跳过来，则详细信息从本地读取
		if(localStorage.getItem("fromWhere")=="confirmOrder"){
			$("#provider_name9").text(localStorage.getItem("provider_name"));
			$("#provider_phone9").text(localStorage.getItem("provider_phone"));
			$("#provider_skill9").text(localStorage.getItem("provider_skill"));
			$("#needer_name9").text(localStorage.getItem("needer_name"));
			$("#needer_phone9").text(localStorage.getItem("needer_phone"));
			$("#title9").text(localStorage.getItem("title"));
			$("#time9").text(localStorage.getItem("time"));
			$("#needer_skill9").text(localStorage.getItem("needer_skill"));
			$("#money9").text(localStorage.getItem("money")+"元");
			$("#content9").text(localStorage.getItem("content"));
			$("#contract9").text(localStorage.getItem("contract"));

			//将合同内容显示到合同页中去
			AnalysisContract(localStorage.getItem("contract"));
		}
		
		//若从订单列表跳过来，则详细信息从列表页面上读取，并保存到本地
		else{
			$("#provider_name9").text(val.provider_name);
			$("#provider_phone9").text(val.provider_phone);
			$("#provider_skill9").text(skill2String(val.provider_skill));
			$("#title9").text(val.title);
			$("#time9").text(timeStamp2String(val.time));
			$("#needer_skill9").text(skill2String(val.needer_skill));
			$("#money9").text(val.money+"元");
			$("#content9").text(val.content);
			$("#contract9").text(val.contract);
			localStorage.setItem("require_id",val.id);

			//将合同内容显示到合同页中去
			AnalysisContract(val.contract);
		}
		window.location.href="#pageDetail9";
	}
	
	
	/**
	 * 点击查看仲裁中的详情
	 */
	function clickDetail10Btn(val){
		//判断从哪个页面跳过来
		//若从申请仲裁跳过来，则详细信息从本地读取
		//alert("fromWhere="+localStorage.getItem("fromWhere"));
		if(localStorage.getItem("fromWhere")=="zhongcai"){
			localStorage.setItem("fromWhere","");
			$("#provider_name10").text(localStorage.getItem("provider_name"));
			$("#provider_phone10").text(localStorage.getItem("provider_phone"));
			$("#provider_skill10").text(localStorage.getItem("provider_skill"));
			$("#needer_name10").text(localStorage.getItem("needer_name"));
			$("#needer_phone10").text(localStorage.getItem("needer_phone"));
			$("#title10").text(localStorage.getItem("title"));
			$("#time10").text(localStorage.getItem("time"));
			$("#needer_skill10").text(localStorage.getItem("needer_skill"));
			$("#money10").text(localStorage.getItem("money")+"元");
			$("#content10").text(localStorage.getItem("content"));
			$("#contract10").text(localStorage.getItem("contract"));
			//alert("从本地读取＝"+localStorage.getItem("zhongcai"));
			$("#zhongcaiReason10").text(localStorage.getItem("zhongcai"));

			//将合同内容显示到合同页中去
			AnalysisContract(localStorage.getItem("contract"));
		}
		
		//若从订单列表跳过来，则详细信息从列表页面上读取，并保存到本地
		else{
			$("#provider_name10").text(val.provider_name);
			$("#provider_phone10").text(val.provider_phone);
			$("#provider_skill10").text(skill2String(val.provider_skill));
			$("#title10").text(val.title);
			$("#time10").text(timeStamp2String(val.time));
			$("#needer_skill10").text(skill2String(val.needer_skill));
			$("#money10").text(val.money+"元");
			$("#content10").text(val.content);
			$("#contract10").text(val.contract);
			//alert("从服务端读取＝"+val.zhongcai);
			//alert(val);
			$("#zhongcaiReason10").text(val.zhongcai);
			localStorage.setItem("require_id",val.id);

			//将合同内容显示到合同页中去
			AnalysisContract(val.contract);
		}
		window.location.href="#pageDetail10";
	}
	
	
	
	
	
	
	/**
	 * 点击查看仲裁结束详情
	 */
	function clickDetail12Btn(val){
		$("#provider_name12").text(val.provider_name);
		$("#provider_phone12").text(val.provider_phone);
		$("#provider_skill12").text(skill2String(val.provider_skill));
		$("#title12").text(val.title);
		$("#time12").text(timeStamp2String(val.time));
		$("#needer_skill12").text(skill2String(val.needer_skill));
		$("#money12").text(val.money+"元");
		$("#content12").text(val.content);
		$("#contract12").text(val.contract);
		$("#zhongcaiResult12").text(val.zhongcai_result);

		//将合同内容显示到合同页中去
		AnalysisContract(val.contract);
		window.location.href="#pageDetail12";
	}
	
	
	
	
	/**
	 * 点击“确认合同”按钮
	 */
	$("#confirmContractBtn").click(function(){
		//显示loading
		$.mobile.loading('show', {  
			text: '处理中...', //加载器中显示的文字  
			textVisible: true, //是否显示文字  
			theme: 'a',        //加载器主题样式a-e  
			textonly: false,   //是否只显示文字  
			html: ""           //要显示的html内容，如图片等  
		});  
		
		//发送请求
		$.get("post/postAction!confirmContract?require_id="+localStorage.getItem("require_id")+"&skill="+localStorage.getItem("needer_skill"),
				
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
				   alert(json.reason);
				   window.location.href="adminNeeder.html";
			    
			   }
//			   
//			   //若返回no
//			   if(json.result=="no"){
//				   //若已有大神抢单，则获取该单详情，并跳转到抢单成功页面
//				   if(json.reason=="已有大神抢单!"){
//					   //显示loading
//					   $.mobile.loading('show', {  
//						   text: '刷新中...', //加载器中显示的文字  
//						   textVisible: true, //是否显示文字  
//						   theme: 'a',        //加载器主题样式a-e  
//						   textonly: false,   //是否只显示文字  
//						   html: ""           //要显示的html内容，如图片等  
//					   });  
//					   
//					   //发送请求，获取本单详情
//					   $.get("post/postAction!getNeedDetail?require_id="+localStorage.getItem("require_id"),
//							   
//							   function(data,status){
//						   var json = eval('(' + data + ')');
//						   //隐藏加载器  
//						   $.mobile.loading('hide');
//						   
//						   //若返回no
//						   if(json.result=="no"){
//							   alert("刷新出错，请重试");
//						   }
//						   
//						   //若返回yes
//						   else{
//							   alert("已被大神抢单！立即查看");
//							   clickDetail2Btn(json.needEntity);
//						   }
//					   });
//				   }
//			   }
			   
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
//			    		clickDetail2Btn(json.needEntity);
				    	window.location.href="adminNeeder.html";
			    	}
			    	
			    	//若未被抢单，则可以提高赏金
			    	else{
			    		//将本条需求的详细信息记录到本地
			    		localStorage.setItem("title",$("#title1").text())
			    		localStorage.setItem("time",$("#time1").text());
			    		//详情页加载之前needer_skill的代码形式就已经存入本地了，这里就不用再存了
//			    		localStorage.setItem("needer_skill",$("#needer_skill1").text());
			    		//若要提高赏金，那旧赏金就不需要了(但现在为了返回时用，所以将money存储起来)
			    		localStorage.setItem("money",$("#money1").text());
			    		localStorage.setItem("content",$("#content1").text());
			    		//记录fromWhere
			    		localStorage.setItem("fromWhere","increaseMoney");
			    		//跳转至填写赏金页面
			    		window.location.href="getHelp.html#page3";
			    	}
			    }
			  });
		
		
	});
	
	
	/**
	 * 点击“重找大神”按钮
	 */
	$("#chongZhaoDaShenBtn").click(function(){
		//判断本单是否被抢
		//显示loading
		$.mobile.loading('show', {  
			text: '加载中...', //加载器中显示的文字  
			textVisible: true, //是否显示文字  
			theme: 'a',        //加载器主题样式a-e  
			textonly: false,   //是否只显示文字  
			html: ""           //要显示的html内容，如图片等  
		});  
		
		//发送请求，获取本单详情
		//alert("post/postAction!chongZhaoProvider?require_id="+localStorage.getItem("require_id")+"&needer_name="+localStorage.getItem("name"));
		$.get("post/postAction!chongZhaoProvider?require_id="+localStorage.getItem("require_id")+"&needer_name="+localStorage.getItem("name"),
				
				function(data,status){
			var json = eval('(' + data + ')');
			//隐藏加载器  
			$.mobile.loading('hide');
			
			//若返回no
			if(json.result=="no"){
				alert(json.reason);
			}
			
			//若返回yes
			else{
				//标识从抢单页面跳转至抢单成功详情
//				localStorage.setItem("fromWhere","chongZhaoProvider");
//				//跳转等待抢单页面
//				clickDetail1Btn(json.needEntity);
				alert("已为您重新匹配大神，请等待大神抢单");
				window.location.href="adminNeeder.html";
			}
		});
		
		
	});
	
	
	/**
	 * 点击“付款（新的）”按钮
	 */
	var pay_title = "";
	var pay_money = "";
	var pay_require_id = "";
	$("#neederPayBtn").click(function(){
		//显示loading
		$.mobile.loading('show', {  
			text: '加载中...', //加载器中显示的文字  
			textVisible: true, //是否显示文字  
			theme: 'a',        //加载器主题样式a-e  
			textonly: false,   //是否只显示文字  
			html: ""           //要显示的html内容，如图片等  
		});  
		
		//获取prepay_id
		$.get("wechat/wechatAction!getPrepayId?require_id="+localStorage.getItem("require_id"),
			function(data,status){
			  	var json = eval('(' + data + ')');
			  		
			  	//隐藏加载器  
				$.mobile.loading('hide');
				
				//获取prepay_ID失败
				if(json.result=="no"){
					alert(json.reason);
					window.location.href="adminNeeder.html";
				}
				//获取prepay_ID成功
				else{
					//将数据显示到订单详情页上，并跳转至确认订单页
					$("#payMoney").text("¥"+json.needEntity.money);
					$("#payTitle").text(json.needEntity.title);
					$("#payRequireId").text("NO.01"+json.needEntity.id);
					//将prepay_id存入本地
					localStorage.setItem("prepay_id",json.prepay_id);
					//跳转页面
					window.location.href="#pageDetail_pay";
				}
				
		});
		
		
	});
	
	
	/**
	 * 点击“付款（旧的）”按钮
	 */
	$("#neederPayBtn2").click(function(){
		//判断本单是否被抢
		//显示loading
		$.mobile.loading('show', {  
			text: '加载中...', //加载器中显示的文字  
			textVisible: true, //是否显示文字  
			theme: 'a',        //加载器主题样式a-e  
			textonly: false,   //是否只显示文字  
			html: ""           //要显示的html内容，如图片等  
		});  
		
		//发送请求，获取本单详情
		$.get("post/postAction!neederPay?require_id="+localStorage.getItem("require_id"),
				
				function(data,status){
			var json = eval('(' + data + ')');
			//隐藏加载器  
			$.mobile.loading('hide');
			
			//若返回no
			if(json.result=="no"){
				alert(json.reason);
			}
			
			//若返回yes
			else{
				//标识从抢单页面跳转至抢单成功详情
		    	localStorage.setItem("fromWhere","neederPay");
		    	//跳转大神开发中页面
		    	clickDetail7Btn();
			}
		});
		
		
	});
	
	
	/**
	 * 点击“确认支付”按钮
	 */
	$("#confirmPayBtn").click(function(){
				
		//获取prepay_id
//		//alert("prepay_id="+localStorage.getItem("prepay_id"));
		//生成sign
		var appId = "wx1a4c2e86c17d1fc4";
		var nonceStr = randomString(16);
		var prepay_id = localStorage.getItem("prepay_id");
		var signType = "MD5";
		var timeStamp = Math.round(new Date().getTime()/1000);
		
		var stringA = "appId="+appId+"&nonceStr="+nonceStr+"&package=prepay_id="+prepay_id+"&signType="+signType+"&timeStamp="+timeStamp;
		var stringSignTemp = stringA+"&key=chaibozhouzhouxiaobin19930620123";
		//alert("stringSignTemp="+stringSignTemp);
		var paySign = md5(stringSignTemp);
		//alert("paySign="+paySign);
		paySign = paySign.toUpperCase();
		//alert("upcase="+paySign);
		
		//调用支付接口
		function onBridgeReady(){
			//alert("onBridgeReady");
			WeixinJSBridge.invoke(
				'getBrandWCPayRequest', {
					"appId" : appId,     //公众号名称，由商户传入     
					"timeStamp": timeStamp,         //时间戳，自1970年以来的秒数     
					"nonceStr" : nonceStr, //随机串     
					"package" : "prepay_id="+prepay_id,     
					"signType" : signType,         //微信签名方式:     
					"paySign" : paySign //微信签名 
				},
			  	function(res){
			  		//alert("err_msg="+res.err_msg);
			  		if(res.err_msg == "get_brand_wcpay_request:ok" ) {
			  			//alert("支付成功");
			  			//标识从抢单页面跳转至抢单成功详情
				    	localStorage.setItem("fromWhere","neederPay");
				    	//跳转大神开发中页面
				    	clickDetail7Btn();
			  		}     // 使用以上方式判断前端返回,微信团队郑重提示:res.err_msg将在用户支付成功后返回    ok，但并不保证它绝对可靠。 
			  	}
			 ); 
		}
			  	
		if (typeof WeixinJSBridge == "undefined"){
			if( document.addEventListener ){
			  	//alert("1");
			  	document.addEventListener('WeixinJSBridgeReady', onBridgeReady, false);
			}else if (document.attachEvent){
			  	//alert("2");
			  	document.attachEvent('WeixinJSBridgeReady', onBridgeReady); 
			  	document.attachEvent('onWeixinJSBridgeReady', onBridgeReady);
			}
		}else{
			  //alert("3");
			  onBridgeReady();
		}
		
	});
	
	
	/**
	 * 点击“通过验收”按钮
	 */
	$("#confirmOrderBtn").click(function(){
		//获取密码，并判断是否为空
		if($("#password").val()==null || $("#password").val()==''){
			$("#reason").text("密码还没填好呢～");
		}
		
		else{
			//判断本单是否被抢
			//显示loading
			$.mobile.loading('show', {  
				text: '加载中...', //加载器中显示的文字  
				textVisible: true, //是否显示文字  
				theme: 'a',        //加载器主题样式a-e  
				textonly: false,   //是否只显示文字  
				html: ""           //要显示的html内容，如图片等  
			});  
			
			//发送请求，获取本单详情
			//alert("post/postAction!confirmOrder?needer_id="+localStorage.getItem("id")+"&password="+$("#password").val()+"&require_id="+localStorage.getItem("require_id"));
			$.get("post/postAction!confirmOrder?needer_id="+localStorage.getItem("id")+"&password="+$("#password").val()+"&require_id="+localStorage.getItem("require_id"),
					
					function(data,status){
				var json = eval('(' + data + ')');
				//隐藏加载器  
				$.mobile.loading('hide');
				
				//若返回no
				if(json.result=="no"){
//					alert(json.reason);
					$("#reason").text(json.reason);
					//若错误原因是“当前页面已失效!”，则跳转至个人中心
					if(json.reason=="当前页面已失效!"){
						alert("当前页面已失效!即将跳转至个人中心");
						window.location.href="adminNeeder.html";
					}
				}
				
				//若返回yes
				else{
					//标识验收页跳转至交易成功页
					localStorage.setItem("fromWhere","confirmOrder");
					//跳转至交易成功页
					clickDetail9Btn();
				}
			});
		}
	});

	
	
	
	/**
	 * 点击“提交仲裁”按钮
	 */
	$("#postZhongcaiBtn").click(function(){
		//alert();
		//判断内容是否为空
		if($("#zhongcaiContent").val()==null || $("#zhongcaiContent").val()==''){
			$("#zhongcai_reason").text("请填写申请原因！");
		}
		else{
			//调用申请仲裁接口
			//显示loading
			$.mobile.loading('show', {  
				text: '提交中...', //加载器中显示的文字  
				textVisible: true, //是否显示文字  
				theme: 'a',        //加载器主题样式a-e  
				textonly: false,   //是否只显示文字  
				html: ""           //要显示的html内容，如图片等  
			});  
			
			//发送请求，获取本单详情
			//alert("post/postAction!applyArbitration?require_id="+localStorage.getItem("require_id")+"&content="+localStorage.getItem("zhongcai")+"^"+$("#zhongcaiContent").val()+"&role="+localStorage.getItem("role"));
			$.get("post/postAction!applyArbitration?require_id="+localStorage.getItem("require_id")+"&content="+localStorage.getItem("zhongcai")+"^"+$("#zhongcaiContent").val()+"&role="+localStorage.getItem("role"),
					
					function(data,status){
				var json = eval('(' + data + ')');
				//隐藏加载器  
				$.mobile.loading('hide');
				
				//若返回no
				if(json.result=="no"){
					alert(json.reason);
					window.location.href="adminNeeder.html";
				}
				
				//若返回yes
				else{
					//标识验收页跳转至仲裁中页
					localStorage.setItem("fromWhere","zhongcai");
					localStorage.setItem("zhongcai",localStorage.getItem("zhongcai")+"^"+$("#zhongcaiContent").val());
					//跳转至仲裁中页
					clickDetail10Btn();
				}
			});
		
		}
	});
	
	
	/**
	 * 点击“选择仲裁原因1”按钮
	 */
	$("#ZhongcaiQABtn1").click(function(){
		//alert($("#ZhongcaiQABtn1").text());
		clickZhongcaiQABtn($("#ZhongcaiQABtn1").text());
	});
	$("#ZhongcaiQABtn2").click(function(){
		//alert($("#ZhongcaiQABtn2").text());
		clickZhongcaiQABtn($("#ZhongcaiQABtn2").text());
	});
	$("#ZhongcaiQABtn3").click(function(){
		//alert($("#ZhongcaiQABtn3").text());
		clickZhongcaiQABtn($("#ZhongcaiQABtn3").text());
	});
	$("#ZhongcaiQABtn4").click(function(){
		//alert($("#ZhongcaiQABtn4").text());
		clickZhongcaiQABtn($("#ZhongcaiQABtn4").text());
	});
	$("#ZhongcaiQABtn5").click(function(){
		//alert($("#ZhongcaiQABtn5").text());
		clickZhongcaiQABtn($("#ZhongcaiQABtn5").text());
	});
	$("#ZhongcaiQABtn6").click(function(){
		//alert($("#ZhongcaiQABtn6").text());
		clickZhongcaiQABtn($("#ZhongcaiQABtn6").text());
	});
	$("#ZhongcaiQABtn7").click(function(){
		//alert($("#ZhongcaiQABtn7").text());
		clickZhongcaiQABtn($("#ZhongcaiQABtn7").text());
	});
	$("#ZhongcaiQABtn8").click(function(){
		//alert($("#ZhongcaiQABtn8").text());
		clickZhongcaiQABtn($("#ZhongcaiQABtn8").text());
	});
	$("#ZhongcaiQABtn9").click(function(){
		//alert($("#ZhongcaiQABtn9").text());
		clickZhongcaiQABtn($("#ZhongcaiQABtn9").text());
	});
	
	
	function clickZhongcaiQABtn(reason){
		//alert(reason);
		//保存到本地
		localStorage.setItem("zhongcai",reason);
		//跳转页面
		window.location.href="#pageDetail10_2";
	}
	
	
	
	
	/**
	 * 点击“联系客服的提交”按钮
	 */
	$("#postFeedbackBtn").click(function(){
		//alert();
		//判断内容是否为空
		if($("#feedbackContent").val()==null || $("#feedbackContent").val()==''){
			$("#feedback_reason").text("请填写申请原因！");
		}
		else{
			//调用申请仲裁接口
			//显示loading
			$.mobile.loading('show', {  
				text: '提交中...', //加载器中显示的文字  
				textVisible: true, //是否显示文字  
				theme: 'a',        //加载器主题样式a-e  
				textonly: false,   //是否只显示文字  
				html: ""           //要显示的html内容，如图片等  
			});  
			
			//发送请求，获取本单详情
			//alert("post/postAction!postFeedBack?name="+localStorage.getItem("name")+"&user_id="+localStorage.getItem("id")+"&phone="+localStorage.getItem("phone")+"&role="+localStorage.getItem("role")+"&content="+$("#feedbackContent").val());
			$.get("post/postAction!postFeedBack?name="+localStorage.getItem("name")+"&user_id="+localStorage.getItem("id")+"&phone="+localStorage.getItem("phone")+"&role="+localStorage.getItem("role")+"&content="+$("#feedbackContent").val(),
					
					function(data,status){
				var json = eval('(' + data + ')');
				//隐藏加载器  
				$.mobile.loading('hide');
				
				//若返回no
				if(json.result=="no"){
					alert(json.reason);
				}
				
				//若返回yes
				else{
					alert("提交成功！管理员正在处理中");
					//返回
					window.history.go(-1);
				}
			});
		
		}
	});
	
	
	
	//开启定时线程，每隔10秒检查下当前成功抢单的订单数，若增多：“已有大神抢单”，并更新orderCount
	/**
	setInterval('clock()',5000);//1000为1秒钟	
	
	
	window.clock = function () {
    	
		$.get("post/postAction!getOrderListByNeederId?needer_id="+localStorage.getItem("id"),
    			  
    		function(data,status){
    			var json = eval('(' + data + ')');
    			
    			//若返回no
    			if(json.result=="no"){
    				alert("后台定时线程获取数据失败");
    			}
    			   
    			//若返回yes，显示查询到的结果
    			else{
    				//计算当前成功抢单的订单数
    				var orderCount = 0;
    				$.each(json.needList, function(index, val) {
    					if(val.state==1)
    						orderCount++;
    				});
    				
    				//若本地订单数为空，则将当前获取的订单数存至本地
    				if(localStorage.getItem("orderCount")==null || localStorage.getItem("orderCount")==''){
    					localStorage.setItem("orderCount",orderCount);
    				}
    				
    				//若本地orderCount(订单数)存在，则进行比较
    				else{
//    					alert("orderCount="+localStorage.getItem("orderCount")+",length="+orderCount);
    					//成功抢单的订单数增多，则提示“您的订单已有大神抢单”
    					if(orderCount>localStorage.getItem("orderCount")){
    						alert("您的订单已有大神抢单!点击查看");
    						localStorage.setItem("orderCount",orderCount);
    						window.location.href="adminNeeder.html";
    					}
    					
    					//成功抢单的订单数减少、不变，则不作任何处理
    				}
    			}
    	});
    };
    */
});




function fanhuiClick(){
	//判断是从哪个页面跳转过来
	//若是从抢单页面跳过来，那么返回的话需要返回到列表，并且刷新
	if(localStorage.getItem("fromWhere")=="neederPay"){
		localStorage.setItem("fromWhere","");
		window.location.href="adminNeeder.html";
	}
	
	//若是从列表页面跳过来，那么返回的话只需要简单的返回即可
	else{
		window.history.go(-1);
	}
}





/**
 * 生成指定长度随机字符串
 */
function randomString(len) {
	len = len || 32;
	var $chars = 'ABCDEFGHJKMNPQRSTWXYZabcdefhijkmnprstwxyz2345678';    /****默认去掉了容易混淆的字符oOLl,9gq,Vv,Uu,I1****/
	var maxPos = $chars.length;
	var pwd = '';
	for (i = 0; i < len; i++) {
		pwd += $chars.charAt(Math.floor(Math.random() * maxPos));
	}
	return pwd;
	}






/*
 * Javascript md5() 函数 用于生成字符串对应的md5值
 * 吴先成  www.51-n.com ohcc@163.com QQ:229256237
 * @param string string 原始字符串
 * @return string 加密后的32位md5字符串
*/
function md5(string){
        function md5_RotateLeft(lValue, iShiftBits) {
                return (lValue<<iShiftBits) | (lValue>>>(32-iShiftBits));
        }
        function md5_AddUnsigned(lX,lY){
                var lX4,lY4,lX8,lY8,lResult;
                lX8 = (lX & 0x80000000);
                lY8 = (lY & 0x80000000);
                lX4 = (lX & 0x40000000);
                lY4 = (lY & 0x40000000);
                lResult = (lX & 0x3FFFFFFF)+(lY & 0x3FFFFFFF);
                if (lX4 & lY4) {
                        return (lResult ^ 0x80000000 ^ lX8 ^ lY8);
                }
                if (lX4 | lY4) {
                        if (lResult & 0x40000000) {
                                return (lResult ^ 0xC0000000 ^ lX8 ^ lY8);
                        } else {
                                return (lResult ^ 0x40000000 ^ lX8 ^ lY8);
                        }
                } else {
                        return (lResult ^ lX8 ^ lY8);
                }
        }         
        function md5_F(x,y,z){
                return (x & y) | ((~x) & z);
        }
        function md5_G(x,y,z){
                return (x & z) | (y & (~z));
        }
        function md5_H(x,y,z){
                return (x ^ y ^ z);
        }
        function md5_I(x,y,z){
                return (y ^ (x | (~z)));
        }
        function md5_FF(a,b,c,d,x,s,ac){
                a = md5_AddUnsigned(a, md5_AddUnsigned(md5_AddUnsigned(md5_F(b, c, d), x), ac));
                return md5_AddUnsigned(md5_RotateLeft(a, s), b);
        }; 
        function md5_GG(a,b,c,d,x,s,ac){
                a = md5_AddUnsigned(a, md5_AddUnsigned(md5_AddUnsigned(md5_G(b, c, d), x), ac));
                return md5_AddUnsigned(md5_RotateLeft(a, s), b);
        };
        function md5_HH(a,b,c,d,x,s,ac){
                a = md5_AddUnsigned(a, md5_AddUnsigned(md5_AddUnsigned(md5_H(b, c, d), x), ac));
                return md5_AddUnsigned(md5_RotateLeft(a, s), b);
        }; 
        function md5_II(a,b,c,d,x,s,ac){
                a = md5_AddUnsigned(a, md5_AddUnsigned(md5_AddUnsigned(md5_I(b, c, d), x), ac));
                return md5_AddUnsigned(md5_RotateLeft(a, s), b);
        };
        function md5_ConvertToWordArray(string) {
                var lWordCount;
                var lMessageLength = string.length;
                var lNumberOfWords_temp1=lMessageLength + 8;
                var lNumberOfWords_temp2=(lNumberOfWords_temp1-(lNumberOfWords_temp1 % 64))/64;
                var lNumberOfWords = (lNumberOfWords_temp2+1)*16;
                var lWordArray=Array(lNumberOfWords-1);
                var lBytePosition = 0;
                var lByteCount = 0;
                while ( lByteCount < lMessageLength ) {
                        lWordCount = (lByteCount-(lByteCount % 4))/4;
                        lBytePosition = (lByteCount % 4)*8;
                        lWordArray[lWordCount] = (lWordArray[lWordCount] | (string.charCodeAt(lByteCount)<<lBytePosition));
                        lByteCount++;
                }
                lWordCount = (lByteCount-(lByteCount % 4))/4;
                lBytePosition = (lByteCount % 4)*8;
                lWordArray[lWordCount] = lWordArray[lWordCount] | (0x80<<lBytePosition);
                lWordArray[lNumberOfWords-2] = lMessageLength<<3;
                lWordArray[lNumberOfWords-1] = lMessageLength>>>29;
                return lWordArray;
        }; 
        function md5_WordToHex(lValue){
                var WordToHexValue="",WordToHexValue_temp="",lByte,lCount;
                for(lCount = 0;lCount<=3;lCount++){
                        lByte = (lValue>>>(lCount*8)) & 255;
                        WordToHexValue_temp = "0" + lByte.toString(16);
                        WordToHexValue = WordToHexValue + WordToHexValue_temp.substr(WordToHexValue_temp.length-2,2);
                }
                return WordToHexValue;
        };
        function md5_Utf8Encode(string){
                string = string.replace(/\r\n/g,"\n");
                var utftext = ""; 
                for (var n = 0; n < string.length; n++) {
                        var c = string.charCodeAt(n); 
                        if (c < 128) {
                                utftext += String.fromCharCode(c);
                        }else if((c > 127) && (c < 2048)) {
                                utftext += String.fromCharCode((c >> 6) | 192);
                                utftext += String.fromCharCode((c & 63) | 128);
                        } else {
                                utftext += String.fromCharCode((c >> 12) | 224);
                                utftext += String.fromCharCode(((c >> 6) & 63) | 128);
                                utftext += String.fromCharCode((c & 63) | 128);
                        } 
                } 
                return utftext;
        }; 
        var x=Array();
        var k,AA,BB,CC,DD,a,b,c,d;
        var S11=7, S12=12, S13=17, S14=22;
        var S21=5, S22=9 , S23=14, S24=20;
        var S31=4, S32=11, S33=16, S34=23;
        var S41=6, S42=10, S43=15, S44=21;
        string = md5_Utf8Encode(string);
        x = md5_ConvertToWordArray(string); 
        a = 0x67452301; b = 0xEFCDAB89; c = 0x98BADCFE; d = 0x10325476; 
        for (k=0;k<x.length;k+=16) {
                AA=a; BB=b; CC=c; DD=d;
                a=md5_FF(a,b,c,d,x[k+0], S11,0xD76AA478);
                d=md5_FF(d,a,b,c,x[k+1], S12,0xE8C7B756);
                c=md5_FF(c,d,a,b,x[k+2], S13,0x242070DB);
                b=md5_FF(b,c,d,a,x[k+3], S14,0xC1BDCEEE);
                a=md5_FF(a,b,c,d,x[k+4], S11,0xF57C0FAF);
                d=md5_FF(d,a,b,c,x[k+5], S12,0x4787C62A);
                c=md5_FF(c,d,a,b,x[k+6], S13,0xA8304613);
                b=md5_FF(b,c,d,a,x[k+7], S14,0xFD469501);
                a=md5_FF(a,b,c,d,x[k+8], S11,0x698098D8);
                d=md5_FF(d,a,b,c,x[k+9], S12,0x8B44F7AF);
                c=md5_FF(c,d,a,b,x[k+10],S13,0xFFFF5BB1);
                b=md5_FF(b,c,d,a,x[k+11],S14,0x895CD7BE);
                a=md5_FF(a,b,c,d,x[k+12],S11,0x6B901122);
                d=md5_FF(d,a,b,c,x[k+13],S12,0xFD987193);
                c=md5_FF(c,d,a,b,x[k+14],S13,0xA679438E);
                b=md5_FF(b,c,d,a,x[k+15],S14,0x49B40821);
                a=md5_GG(a,b,c,d,x[k+1], S21,0xF61E2562);
                d=md5_GG(d,a,b,c,x[k+6], S22,0xC040B340);
                c=md5_GG(c,d,a,b,x[k+11],S23,0x265E5A51);
                b=md5_GG(b,c,d,a,x[k+0], S24,0xE9B6C7AA);
                a=md5_GG(a,b,c,d,x[k+5], S21,0xD62F105D);
                d=md5_GG(d,a,b,c,x[k+10],S22,0x2441453);
                c=md5_GG(c,d,a,b,x[k+15],S23,0xD8A1E681);
                b=md5_GG(b,c,d,a,x[k+4], S24,0xE7D3FBC8);
                a=md5_GG(a,b,c,d,x[k+9], S21,0x21E1CDE6);
                d=md5_GG(d,a,b,c,x[k+14],S22,0xC33707D6);
                c=md5_GG(c,d,a,b,x[k+3], S23,0xF4D50D87);
                b=md5_GG(b,c,d,a,x[k+8], S24,0x455A14ED);
                a=md5_GG(a,b,c,d,x[k+13],S21,0xA9E3E905);
                d=md5_GG(d,a,b,c,x[k+2], S22,0xFCEFA3F8);
                c=md5_GG(c,d,a,b,x[k+7], S23,0x676F02D9);
                b=md5_GG(b,c,d,a,x[k+12],S24,0x8D2A4C8A);
                a=md5_HH(a,b,c,d,x[k+5], S31,0xFFFA3942);
                d=md5_HH(d,a,b,c,x[k+8], S32,0x8771F681);
                c=md5_HH(c,d,a,b,x[k+11],S33,0x6D9D6122);
                b=md5_HH(b,c,d,a,x[k+14],S34,0xFDE5380C);
                a=md5_HH(a,b,c,d,x[k+1], S31,0xA4BEEA44);
                d=md5_HH(d,a,b,c,x[k+4], S32,0x4BDECFA9);
                c=md5_HH(c,d,a,b,x[k+7], S33,0xF6BB4B60);
                b=md5_HH(b,c,d,a,x[k+10],S34,0xBEBFBC70);
                a=md5_HH(a,b,c,d,x[k+13],S31,0x289B7EC6);
                d=md5_HH(d,a,b,c,x[k+0], S32,0xEAA127FA);
                c=md5_HH(c,d,a,b,x[k+3], S33,0xD4EF3085);
                b=md5_HH(b,c,d,a,x[k+6], S34,0x4881D05);
                a=md5_HH(a,b,c,d,x[k+9], S31,0xD9D4D039);
                d=md5_HH(d,a,b,c,x[k+12],S32,0xE6DB99E5);
                c=md5_HH(c,d,a,b,x[k+15],S33,0x1FA27CF8);
                b=md5_HH(b,c,d,a,x[k+2], S34,0xC4AC5665);
                a=md5_II(a,b,c,d,x[k+0], S41,0xF4292244);
                d=md5_II(d,a,b,c,x[k+7], S42,0x432AFF97);
                c=md5_II(c,d,a,b,x[k+14],S43,0xAB9423A7);
                b=md5_II(b,c,d,a,x[k+5], S44,0xFC93A039);
                a=md5_II(a,b,c,d,x[k+12],S41,0x655B59C3);
                d=md5_II(d,a,b,c,x[k+3], S42,0x8F0CCC92);
                c=md5_II(c,d,a,b,x[k+10],S43,0xFFEFF47D);
                b=md5_II(b,c,d,a,x[k+1], S44,0x85845DD1);
                a=md5_II(a,b,c,d,x[k+8], S41,0x6FA87E4F);
                d=md5_II(d,a,b,c,x[k+15],S42,0xFE2CE6E0);
                c=md5_II(c,d,a,b,x[k+6], S43,0xA3014314);
                b=md5_II(b,c,d,a,x[k+13],S44,0x4E0811A1);
                a=md5_II(a,b,c,d,x[k+4], S41,0xF7537E82);
                d=md5_II(d,a,b,c,x[k+11],S42,0xBD3AF235);
                c=md5_II(c,d,a,b,x[k+2], S43,0x2AD7D2BB);
                b=md5_II(b,c,d,a,x[k+9], S44,0xEB86D391);
                a=md5_AddUnsigned(a,AA);
                b=md5_AddUnsigned(b,BB);
                c=md5_AddUnsigned(c,CC);
                d=md5_AddUnsigned(d,DD);
        }
return (md5_WordToHex(a)+md5_WordToHex(b)+md5_WordToHex(c)+md5_WordToHex(d)).toLowerCase();
}