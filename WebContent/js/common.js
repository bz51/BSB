
	
	/**
	 * 将TimeStamp——>易读的字符串
	 */
	function timeStamp2String(time){
		if(time==null || time=='')
			return "传入的时间为空";
		
		var timeDate = time.substring(0,10);
		var timeTime = time.substring(11,16);
		
		var timeDateS = new Array(3);
		var timeTimeS = new Array(3);
		
//		alert("timeDate="+timeDate+",timeTime="+timeTime);
		
		timeDateS = timeDate.split("-");
//		alert(timeDateS);
//		timeTimeS = timeTimeS.split("\\:");
//		alert(timeTimeS);
		return timeDateS[1]+"月"+timeDateS[2]+"日"+timeTime;
	}
	
	/**
	 * 将skill——>String
	 */
	function skill2String(skill){
		if(skill==null || skill=='')
			return "skill不正确";
		
		var arr = skill.split("");
		var result = "";
		$.each(arr, function(index, val) {
			if(index==0){
				if(val=="1")
					result = result + "Java";
			}
			if(index==1){
				if(val=="1")
					result = result + "、C/C++";
			}
			if(index==2){
				if(val=="1")
					result = result + "、Python";
			}
			if(index==3){
				if(val=="1")
					result = result + "、C#";
			}
			if(index==4){
				if(val=="1")
					result = result + "、Android";
			}
			if(index==5){
				if(val=="1")
					result = result + "、IOS";
			}
			if(index==6){
				if(val=="1")
					result = result + "、JSP";
			}
			if(index==7){
				if(val=="1")
					result = result + "、ASP/.NET";
			}
			if(index==8){
				if(val=="1")
					result = result + "、PHP";
			}
			if(index==9){
				if(val=="1")
					result = result + "、j2EE";
			}
			if(index==10){
				if(val=="1")
					result = result + "、算法";
			}
			if(index==11){
				if(val=="1")
					result = result + "、大数据";
			}
			if(index==12){
				if(val=="1")
					result = result + "、软件测试";
			}
			if(index==13){
				if(val=="1")
					result = result + "、游戏";
			}
			if(index==14){
				if(val=="1")
					result = result + "、其他";
			}
		});
		
		//去除开头的顿号
		if(result.charAt(0)=="、"){
			result = result.substr(1);
		}
		
		//判断skill长度，若超过xx个字符就截取
		if(result.length>21)
			result = result.substr(0,21)+"……";
		return result;
	}
	
	/**
	 * 将skill——>String(不省略)
	 */
	function skill2String_new(skill){
		if(skill==null || skill=='')
			return "skill不正确";
		
		var arr = skill.split("");
		var result = "";
		$.each(arr, function(index, val) {
			if(index==0){
				if(val=="1")
					result = result + "Java";
			}
			if(index==1){
				if(val=="1")
					result = result + "、C/C++";
			}
			if(index==2){
				if(val=="1")
					result = result + "、Python";
			}
			if(index==3){
				if(val=="1")
					result = result + "、C#";
			}
			if(index==4){
				if(val=="1")
					result = result + "、Android";
			}
			if(index==5){
				if(val=="1")
					result = result + "、IOS";
			}
			if(index==6){
				if(val=="1")
					result = result + "、JSP";
			}
			if(index==7){
				if(val=="1")
					result = result + "、ASP/.NET";
			}
			if(index==8){
				if(val=="1")
					result = result + "、PHP";
			}
			if(index==9){
				if(val=="1")
					result = result + "、j2EE";
			}
			if(index==10){
				if(val=="1")
					result = result + "、算法";
			}
			if(index==11){
				if(val=="1")
					result = result + "、大数据";
			}
			if(index==12){
				if(val=="1")
					result = result + "、软件测试";
			}
			if(index==13){
				if(val=="1")
					result = result + "、游戏";
			}
			if(index==14){
				if(val=="1")
					result = result + "、其他";
			}
		});
		
		//去除开头的顿号
		if(result.charAt(0)=="、"){
			result = result.substr(1);
		}
		
		return result;
	}
	
	
	/**
	 * 判断是否符合金钱格式
	 * @param s
	 * @returns {Boolean}
	 */
	function isMoney( s )
	{
	    var regu = "[1-9][0-9]*";
	    var re = new RegExp(regu);
	    if (re.test(s)) {
	        return true;
	    }
	    else {
	        return false;
	    }
	};
	
	
	
	
	
	
	/**
	 * 解析合同内容，并显示到界面上
	 */
	function AnalysisContract2(contract){
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