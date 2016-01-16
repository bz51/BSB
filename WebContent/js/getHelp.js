
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
	 * 点击“上传图片”按钮
	 */
	var localIds = "";
	$("#selectPicBtn").click(function(){
		wx.chooseImage({
			count: 1, // 默认9
			sizeType: ['original', 'compressed'], // 可以指定是原图还是压缩图，默认二者都有
			sourceType: ['album', 'camera'], // 可以指定来源是相册还是相机，默认二者都有
			success: function (res) {
				localIds = res.localIds; // 返回选定照片的本地ID列表，localId可以作为img标签的src属性显示图片
//				localStorage.setItem("localIds",res.localIds);
				//在界面上显示
				$("#selectedPic").attr('src',localIds); 
				$("#selectedPic").show();
			}
		});
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
		        text: '发布中...', //加载器中显示的文字  
		        textVisible: true, //是否显示文字  
		        theme: 'a',        //加载器主题样式a-e  
		        textonly: false,   //是否只显示文字  
		        html: ""           //要显示的html内容，如图片等  
		    });  

		    //判断用户是否登录了
		    //若登录了，则直接发布
		    if(localStorage.getItem("id")!=null && localStorage.getItem("id")!=''){
		    	//上传图片
//		    	var localIds = localStorage.getItem("localIds");
		    	//alert("localIds="+localIds);
		    	
		    	//若用户选择了图片，则需要先上传图片，再发布信息
		    	var serverId = "";
		    	if(localIds!=null && localIds!=''){
		    		wx.uploadImage({
					localId: localIds+"", // 需要上传的图片的本地ID，由chooseImage接口获得
					isShowProgressTips: 1, // 默认为1，显示进度提示
					success: function (res) {
						serverId = res.serverId;
						var url = "post/postAction!postNeed?needEntity.title="+$("#title").val()+"&needEntity.content="+$("#content").val()+"&needEntity.money="+$("#money").val()+"&needEntity.needer_id="+localStorage.getItem("id")+"&needEntity.needer_name="+localStorage.getItem("name")+"&needEntity.needer_phone="+localStorage.getItem("phone")+"&needEntity.needer_skill="+localStorage.getItem("skill")+"&needEntity.needer_weixin="+localStorage.getItem("open_id")+"&pic_id="+res.serverId;
						$.get(url,
								function(data,status){
								var json = eval('(' + data + ')');
									    
								//隐藏加载器  
								$.mobile.loading('hide');
									    
								//若返回no
								if(json.result=="no"){
									$("#reason3").text(json.reason);
									//若错误信息是“该条信息已经发布过！”，则跳转至个人中心
									alert("页面已失效！即将跳转至个人中心");
									window.location.href="adminNeeder.html";
								}
									   
								//若返回yes
								else{
									//页面跳转
									window.location.href="#page4";
								}
						});
					},
					fail: function (res) {
						alert(JSON.stringify(res));
					}
					});
		    	}
				
		    	//若用户没有上传图片，则直接发布信息
		    	else{
		    		var url = "";
		    		//若从“提高赏金”页面调过来，则从本地读取各个参数
//		    		alert("fromWhere="+localStorage.getItem("fromWhere"));
		    		if(localStorage.getItem("fromWhere")=="increaseMoney"){
		    			url = "post/postAction!increaseMoney?needEntity.id="+localStorage.getItem("require_id")+"&needEntity.money="+$("#money").val();
		    		}
		    		//若从发布需求一步步到这个页面，则从界面上直接获取各个参数
		    		else
		    			url = "post/postAction!postNeed?needEntity.title="+$("#title").val()+"&needEntity.content="+$("#content").val()+"&needEntity.money="+$("#money").val()+"&needEntity.needer_id="+localStorage.getItem("id")+"&needEntity.needer_name="+localStorage.getItem("name")+"&needEntity.needer_phone="+localStorage.getItem("phone")+"&needEntity.needer_skill="+localStorage.getItem("skill")+"&needEntity.needer_weixin="+localStorage.getItem("open_id");
		    	
		    		//alert(url);
		    		$.get(url,function(data,status){
		    			var json = eval('(' + data + ')');
				    
		    			//隐藏加载器  
		    			$.mobile.loading('hide');
				    
		    			//若返回no
		    			if(json.result=="no"){
		    				$("#reason3").text(json.reason);
		    				//若错误信息是“该条信息已经发布过！”，则跳转至个人中心
		    				alert("页面已失效！即将跳转至个人中心");
							window.location.href="adminNeeder.html";
		    			}
				   
		    			//若返回yes
		    			else{
		    				//将匹配到的大神人数显示
//				    		localStorage.setItem("count",json.count);//将得到的大神人数存到本地，因为注册成功后会在注册页面发布需求，然后将count值保存到本地，最后跳转到发布成功页面显示count。若count不存到本地，当从本地获取count后显示在页面上就会显示null
		    				$("#count").text(json.count);
		    				//页面跳转
		    				if(localStorage.getItem("fromWhere")=="increaseMoney"){
		    					window.location.href="#page5";
		    					localStorage.setItem("fromWhere","");
		    				}else{
		    					window.location.href="#page4";
		    				}
		    			}
		    		});
		    	}
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
	
	//进行微信验证
	getWXConfig();
	
});


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


function getWXConfig(){
	//显示loading
    $.mobile.loading('show', {  
        text: '加载中...', //加载器中显示的文字  
        textVisible: true, //是否显示文字  
        theme: 'a',        //加载器主题样式a-e  
        textonly: false,   //是否只显示文字  
        html: ""           //要显示的html内容，如图片等  
    }); 
	//获取ticket
	$.get("wechat/wechatAction!getJSTicket",  
			function(data,status){
			var json = eval('(' + data + ')');
			
			localStorage.setItem("ticket",json.ticket);
			var nonceStr = randomString(16);
			var timestamp = Math.round(new Date().getTime()/1000);
			var url = "http://www.erhuowang.cn/getHelp.html";
			var string1 = "jsapi_ticket="+json.ticket+"&noncestr="+nonceStr+"&timestamp="+timestamp+"&url="+url;
			var signature = hex_sha1(string1);
			
			wx.config({
			    debug: false, // 开启调试模式,调用的所有api的返回值会在客户端alert出来，若要查看传入的参数，可以在pc端打开，参数信息会通过log打出，仅在pc端时才会打印。
			    appId: "wx1a4c2e86c17d1fc4", // 必填，公众号的唯一标识
			    timestamp: timestamp, // 必填，生成签名的时间戳
			    nonceStr: nonceStr, // 必填，生成签名的随机串
			    signature: signature,// 必填，签名，见附录1
			    jsApiList: ['chooseImage','uploadImage','downloadImage','previewImage'] // 必填，需要使用的JS接口列表，所有JS接口列表见附录2
			});
			
			
			wx.ready(function(){
				//alert("ready");
				localStorage.setItem("isReady","yes");
				 //隐藏加载器  
			    $.mobile.loading('hide');
			});
			
			wx.error(function(res){

			});
			
	});
}