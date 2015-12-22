document.write("<script language=javascript src='js/common.js'></script>");

$(document).ready(function(){

	/**
	 * 点击“请求模板信息”按钮，向微信服务器发送post请求
	 */
	var access_token;
	$("#getMuBanButton").click(function(){
		//显示loading
	    $.mobile.loading('show', {  
	        text: '数据加载中...', //加载器中显示的文字  
	        textVisible: true, //是否显示文字  
	        theme: 'a',        //加载器主题样式a-e  
	        textonly: false,   //是否只显示文字  
	        html: ""           //要显示的html内容，如图片等  
	    }); 
	    
//    	//发送请求获取access_token
//    	$.get("https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid=wx1a4c2e86c17d1fc4&secret=ba95beea6a08dc2abf245ae8d8c7dc3e",
//		  
//		  function(data,status){
//		    var json = eval('(' + data + ')');
//		    
//		    //隐藏加载器  
//		    $.mobile.loading('hide');
//		    alert(json);
//		    
//		    access_token = json.access_token;
//		   
//		  });
    	
    	
	  //发送Post请求
	    $.post("https://api.weixin.qq.com/cgi-bin/message/template/send?access_token=VatzMEYqJ5FR1UzUsdQapq5yhxUX_gZkx_F1oRXGyqjLUT_60ZAz7G_INqzWSHp38Oy1rJ9M-1NNEG3wtXZtWVi5oEUYw2WfCE10iQm1hkkCZQfABANKB",
			  {
	    		//Post的内容
	    		touser:"o6uVGv8OUlCv-OrgeK5bWBV-6i_E",
	    		template_id:"rhTqtWR1H0hfFI3DQAZLySeetoaz15f5G6969IhZW3Q",
	    		url:"http://www.erhuowang.cn",
	    		topcolor:"#FF0000",
	    		first:"你好，这是First",
	    		OrderSn:"01212121",
	    		OrderStatus:"抢单中",
	    		remark:"这是remark呀，啦啦啦"
			  },
			  
			  function(data,status){
//			    var json = eval('(' + data + ')');
			    
			    //隐藏加载器  
			    $.mobile.loading('hide');
			    
			   alert(json);
			  });
		
	});
});
	
