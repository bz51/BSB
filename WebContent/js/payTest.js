document.write("<script language=javascript src='js/common.js'></script>");
function pay(){
	alert("点击了pay");
	$.get("wechat/wechatAction!getJSTicket",  
			  function(data,status){
			    var json = eval('(' + data + ')');
			    
			  //1.获取ticket
			    localStorage.setItem("ticket",json.ticket);
			    alert("ticket="+json.ticket);
			    
			  //2.生成16位随机字符串
				var nonceStr = randomString(16);
				alert("随机数＝"+nonceStr);
				
				//3.生成Timestamp
				var timestamp = Math.round(new Date().getTime()/1000);
				alert("timestamp＝"+timestamp);
				
				//4.获取当前页面URL
				var url = "http://www.erhuowang.cn/payTest.html";
				alert("url＝"+url);
				
				//5.组装成字符串
				var queryString = "jsapi_ticket="+localStorage.getItem("ticket")+"&noncestr="+nonceStr+"&timestamp="+timestamp+"&url="+url;
				alert("queryString＝"+queryString);
				//6.生成$signature
				var signature = hex_sha1(queryString);
				alert("signature="+signature);
				
				
				wx.config({
				    "debug": true, // 开启调试模式,调用的所有api的返回值会在客户端alert出来，若要查看传入的参数，可以在pc端打开，参数信息会通过log打出，仅在pc端时才会打印。
				    "appId": 'wx1a4c2e86c17d1fc4', // 必填，公众号的唯一标识
				    "timestamp": timestamp, // 必填，生成签名的时间戳
				    "nonceStr": nonceStr, // 必填，生成签名的随机串
				    "signature": signature,// 必填，签名，见附录1
				    "jsApiList": ['chooseWXPay'] // 必填，需要使用的JS接口列表，所有JS接口列表见附录2
				});
				
				alert("1111");
				wx.ready(function(){
					alert("娇艳成功！");
					// config信息验证后会执行ready方法，所有接口调用都必须在config接口获得结果之后，config是一个客户端的异步操作，所以如果需要在页面加载时就调用相关接口，则须把相关接口放在ready函数中调用来确保正确执行。对于用户触发时才调用的接口，则可以直接调用，不需要放在ready函数中。
					
					
					//发送请求
					$.get("wechat/wechatAction!getPrepayId",
						  
						  function(data,status){
						    var json = eval('(' + data + ')');
						    alert("prepay_id="+json.prepay_id);
						    localStorage.setItem("prepay_id",json.prepay_id);
						    	
					
					
					
					
					
					var appId = "wx1a4c2e86c17d1fc4";
					var nonceStr = randomString(16);
					alert("nonceStr="+nonceStr);
//					var package1 = "package=prepay_id=wx201512292349410251030ef00841211146";
					var signType = "MD5";
					var timeStamp = Math.round(new Date().getTime()/1000);
					alert("timeStamp="+timeStamp);
					alert("本地的prepay_id="+localStorage.getItem("prepay_id"));
					var stringA = "appId="+appId+"&nonceStr="+nonceStr+"&package=prepay_id="+localStorage.getItem("prepay_id")+"&signType="+signType+"&timeStamp="+timeStamp;
//					var stringSignTemp = stringA+"&key=chaibozhouzhouxiaobin19930620123";
//					alert("stringSignTemp="+stringSignTemp);
					alert("stringA="+stringA);
					var paySign = md5(stringA);
					alert("paySign="+paySign);
//					paySign = paySign.toUpperCase();
					alert("upcase="+paySign);
					
				    // config信息验证后会执行ready方法，所有接口调用都必须在config接口获得结果之后，config是一个客户端的异步操作，所以如果需要在页面加载时就调用相关接口，则须把相关接口放在ready函数中调用来确保正确执行。对于用户触发时才调用的接口，则可以直接调用，不需要放在ready函数中。
					wx.chooseWXPay({
					    "timestamp": timeStamp, // 支付签名时间戳，注意微信jssdk中的所有使用timestamp字段均为小写。但最新版的支付后台生成签名使用的timeStamp字段名需大写其中的S字符
					    "nonceStr": nonceStr, // 支付签名随机串，不长于 32 位
					    "package": "prepay_id="+localStorage.getItem("prepay_id"), // 统一支付接口返回的prepay_id参数值，提交格式如：prepay_id=***）
					    "signType": signType, // 签名方式，默认为'SHA1'，使用新版支付需传入'MD5'
					    "paySign": paySign, // 支付签名
					    success: function (res) {
					    	  if(res.errMsg == "chooseWXPay:ok" ) {
					    	      //支付成功
					    	  }else{
					    	      alert(res.errMsg);
					    	  }
					    	  },
					    	cancel:function(res){
					    	  //支付取消
					    		alert("取消支付");
					    	}
					});
					

					wx.error(function(res){
						alert("娇艳失败！"+res);
					    // config信息验证失败会执行error函数，如签名过期导致验证失败，具体错误信息可以打开config的debug模式查看，也可以在返回的res参数中查看，对于SPA可以在这里更新签名。

					});
					});
					
				});
				

				
				
				wx.error(function(res){
					alert("娇艳失败！"+res);
				    // config信息验证失败会执行error函数，如签名过期导致验证失败，具体错误信息可以打开config的debug模式查看，也可以在返回的res参数中查看，对于SPA可以在这里更新签名。

				});
			  });
}




function getPrepay_id(){
	//发送请求
	$.get("wechat/wechatAction!getPrepayId",
		  
		  function(data,status){
		    var json = eval('(' + data + ')');
		    alert("prepay_id="+json.prepay_id);
		    localStorage.setItem("prepay_id",json.prepay_id);
		    	
	});
}





//function pay2(){
//	alert("点击了pay");
//	$.get("wechat/wechatAction!getJSTicket",  
//			  function(data,status){
//			    var json = eval('(' + data + ')');
//			    
//			    //隐藏加载器  
//			    $.mobile.loading('hide');
//			    
//			  //1.获取ticket
//			    localStorage.setItem("ticket",json.ticket);
//			    alert("ticket="+json.ticket);
//			    
//			  //2.生成16位随机字符串
//				var nonceStr = randomString(16);
//				alert("随机数＝"+nonceStr);
//				
//				//3.生成Timestamp
//				var timestamp = Math.round(new Date().getTime()/1000);
//				alert("timestamp＝"+timestamp);
//				
//				//4.获取当前页面URL
//				var url = "www.erhuowang.cn/payTest.html";
//				alert("url＝"+url);
//				
//				//5.组装成字符串
//				var queryString = "jsapi_ticket="+localStorage.getItem("ticket")+"&noncestr="+nonceStr+"&timestamp="+timestamp+"&url="+url;
//				alert("queryString＝"+queryString);
//				//6.生成$signature
//				var signature = hex_sha1(queryString);
//				alert("signature="+signature);
//				
//				
//				wx.config({
//				    debug: true, // 开启调试模式,调用的所有api的返回值会在客户端alert出来，若要查看传入的参数，可以在pc端打开，参数信息会通过log打出，仅在pc端时才会打印。
//				    appId: 'wx1a4c2e86c17d1fc4', // 必填，公众号的唯一标识
//				    timestamp: timestamp, // 必填，生成签名的时间戳
//				    nonceStr: nonceStr, // 必填，生成签名的随机串
//				    signature: signature,// 必填，签名，见附录1
//				    jsApiList: ['chooseWXPay'] // 必填，需要使用的JS接口列表，所有JS接口列表见附录2
//				});
//				
//				
//	
//	wx.ready(function(){
//		
//		var appId = "wx1a4c2e86c17d1fc4";
//		var nonceStr = randomString(16);
//		var package = "prepay_id=wx20151229174022693cc97e310545763501";
//		var signType = "MD5";
//		var timeStamp = Date.parse(new Date());
//		
//		var stringA = "appId="+appId+"&nonceStr="+nonceStr+"&package="+package+"&signType="+signType+"&timeStamp="+timeStamp;
//		var stringSignTemp = stringA+"&key=chaibozhouzhouxiaobin19930620123";
//		alert("stringSignTemp="+stringSignTemp);
//		var paySign = md5(stringSignTemp);
//		alert("paySign="+paySign);
//		paySign = paySign.toUpperCase();
//		alert("upcase="+paySign);
//		
//	    // config信息验证后会执行ready方法，所有接口调用都必须在config接口获得结果之后，config是一个客户端的异步操作，所以如果需要在页面加载时就调用相关接口，则须把相关接口放在ready函数中调用来确保正确执行。对于用户触发时才调用的接口，则可以直接调用，不需要放在ready函数中。
//		wx.chooseWXPay({
//		    timestamp: timestamp, // 支付签名时间戳，注意微信jssdk中的所有使用timestamp字段均为小写。但最新版的支付后台生成签名使用的timeStamp字段名需大写其中的S字符
//		    nonceStr: nonceStr, // 支付签名随机串，不长于 32 位
//		    package: package, // 统一支付接口返回的prepay_id参数值，提交格式如：prepay_id=***）
//		    signType: signType, // 签名方式，默认为'SHA1'，使用新版支付需传入'MD5'
//		    paySign: paySign, // 支付签名
//		    success: function (res) {
//		        // 支付成功后的回调函数
//		    	alert(res.err_msg);
//		    }
//		});
//
//	
//	
//	wx.error(function(res){
//
//	    // config信息验证失败会执行error函数，如签名过期导致验证失败，具体错误信息可以打开config的debug模式查看，也可以在返回的res参数中查看，对于SPA可以在这里更新签名。
//		alert("error="+res.errMsg);
//	});
//	
//	
//	});
//
//
function clickConfirmPay(){
	alert("点击了clickConfirmPay");
	
	//发送请求
	$.get("wechat/wechatAction!getPrepayId",
		  
		  function(data,status){
		    var json = eval('(' + data + ')');
		    alert("prepay_id="+json.prepay_id);
		    localStorage.setItem("prepay_id",json.prepay_id);
		    
		    
	var appId = "wx1a4c2e86c17d1fc4";
	var nonceStr = randomString(16);
	var prepay_id = json.prepay_id;
	var signType = "MD5";
	var timeStamp = Math.round(new Date().getTime()/1000);
	
	var stringA = "appId="+appId+"&nonceStr="+nonceStr+"&package=prepay_id="+json.prepay_id+"&signType="+signType+"&timeStamp="+timeStamp;
	var stringSignTemp = stringA+"&key=chaibozhouzhouxiaobin19930620123";
	alert("stringSignTemp="+stringSignTemp);
	var paySign = md5(stringSignTemp);
	alert("paySign="+paySign);
	paySign = paySign.toUpperCase();
	alert("upcase="+paySign);
	
	function onBridgeReady(){
		
			alert("onBridgeReady");
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
		    	   alert("err_msg="+res.err_msg);
		           if(res.err_msg == "get_brand_wcpay_request:ok" ) {}     // 使用以上方式判断前端返回,微信团队郑重提示:res.err_msg将在用户支付成功后返回    ok，但并不保证它绝对可靠。 
		       }
		   ); 
		}
		if (typeof WeixinJSBridge == "undefined"){
		   if( document.addEventListener ){
			   alert("1");
		       document.addEventListener('WeixinJSBridgeReady', onBridgeReady, false);
		   }else if (document.attachEvent){
			   alert("2");
		       document.attachEvent('WeixinJSBridgeReady', onBridgeReady); 
		       document.attachEvent('onWeixinJSBridgeReady', onBridgeReady);
		   }
		}else{
			alert("3");
		   onBridgeReady();
		}
		
	});
}

	
	/**
	 * 上传图片
	 */
	function clickUploadPic(){
		alert();
		//获取ticket
		$.get("wechat/wechatAction!getJSTicket",
			function(data,status){
			var json = eval('(' + data + ')');
				
			localStorage.setItem("ticket",json.ticket);
		
			var nonceStr = randomString(16);
			alert("nonceStr="+nonceStr);
			var timestamp = Math.round(new Date().getTime()/1000);
			alert("timestamp="+timestamp);
			var url = "http://www.erhuowang.cn/payTest.html";
			alert("url="+url);
			var string1 = "jsapi_ticket="+localStorage.getItem("ticket")+"&noncestr="+nonceStr+"&timestamp="+timestamp+"&url="+url;
			alert("string1="+string1);
			var signature = hex_sha1(string1);
			alert("signature="+signature);
		
			wx.config({
				debug: true, // 开启调试模式,调用的所有api的返回值会在客户端alert出来，若要查看传入的参数，可以在pc端打开，参数信息会通过log打出，仅在pc端时才会打印。
				appId: "wx1a4c2e86c17d1fc4", // 必填，公众号的唯一标识
				timestamp: timestamp, // 必填，生成签名的时间戳
				nonceStr: nonceStr, // 必填，生成签名的随机串
				signature: signature,// 必填，签名，见附录1
				jsApiList: ['chooseImage','uploadImage','downloadImage','previewImage'] // 必填，需要使用的JS接口列表，所有JS接口列表见附录2
			});
		
			wx.ready(function(){
				wx.chooseImage({
					count: 1, // 默认9
					sizeType: ['original', 'compressed'], // 可以指定是原图还是压缩图，默认二者都有
					sourceType: ['album', 'camera'], // 可以指定来源是相册还是相机，默认二者都有
					success: function (res) {
						var localIds = res.localIds; // 返回选定照片的本地ID列表，localId可以作为img标签的src属性显示图片
						localStorage.setItem("localIds",res.localIds);
						alert("localIds="+localIds);
					}
				});
			
				setTimeout(function (){
					alert("localIds[0]="+localStorage.getItem("localIds"));
					wx.uploadImage({
						localId: localStorage.getItem("localIds"), // 需要上传的图片的本地ID，由chooseImage接口获得
						isShowProgressTips: 1, // 默认为1，显示进度提示
						success: function (res) {
							var serverId = res.serverId; // 返回图片的服务器端ID
							alert("上传成功，serverId＝"+serverId);
						},
						fail: function (res) {
							alert(JSON.stringify(res));
						}
					});
				},100);
			});
		
			wx.error(function(res){

			});
		
	});}
	

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