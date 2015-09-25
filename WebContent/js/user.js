document.write("<script language=javascript src='js/common.js'></script>");


$(document).ready(function(){
	
	//获取当前用户的个人信息
	
	//显示loading
    $.mobile.loading('show', {  
        text: '加载中...', //加载器中显示的文字  
        textVisible: true, //是否显示文字  
        theme: 'a',        //加载器主题样式a-e  
        textonly: false,   //是否只显示文字  
        html: ""           //要显示的html内容，如图片等  
    });  
    
	//发送请求
	$.get("post/postAction!getUserEntityByProviderId?provider_id="+localStorage.getItem("id"),
	  
	  function(data,status){
	    var json = eval('(' + data + ')');
	    
	    //隐藏加载器  
	    $.mobile.loading('hide');
	    
	    //若返回no
	    if(json.result=="no"){
	    	alert(json.reason);
	    }
	   
	    //若返回yes，将得到的数据写到页面上
	    else{
	    	$("#name").text(json.userEntity.name);
	    	$("#phone").text(json.userEntity.phone);
	    	$("#skill").text(skill2String(json.userEntity.skill));
	    }
	  });
	
});