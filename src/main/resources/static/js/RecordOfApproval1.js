$(function(){
	var id = $("#id").val();
	var formCode = $("#formCode").val();
	findData(id,formCode);
	function findData(id,formCode){
		var url = "findData";
		$.ajax({
			url:url,	
			type:"POST",
			data:{objId:id,objCode:formCode},
			success:function(result){
				var file = "";
				var str = "";
				for(var i=0;i < result.length;i++){
					var date1 = new Date(parseInt(result[i].operateTime));
					var dateTime = date1.format("yyyy-MM-dd hh:mm:ss");
					if(result[i].fileSrcName.length > 0){
						for(var j=0;j < result[i].fileSrcName.length;j++){
							file += "<a href='/qlms/fileUpload/download?realFileName="+result[i].fileSrcName[j]+"&downloadURL="+result[i].fileWebPath[j]+"' target='_Blank' id='download'>下载</a>&nbsp;&nbsp"+result[i].fileSrcName[j]+"<br/>"
						}
					}else{
						file="无";
					}
					str += "<tr><td style='text-align: center;'>"
					+eval(i+1)
					+"</td><td style='text-align: center;'>"
					+result[i].operatorName
					+"</td><td style='text-align: center;'>"
					+result[i].taskName
					+"</td><td style='text-align: center;'>"
					+result[i].operateResult
					+"</td><td style='text-align: center;'>"
					+dateTime
					+"</td><td style='text-align: center;'>"
					+result[i].operateOpinions
					+"</td><td style='text-align: center;'>"+
					file
					+"</td></tr>";
					file = "";
				}
				$("#tb1").html(str);
				tdStyle1();
			}
		})
	}
	
	function tdStyle1(){
		
		$("table").css("table-layout","fixed");
		var tipsindex;
		$("td").each(function(){
			if($(this).siblings().text()=="公钥" || $(this).siblings().text()=="注册码"){
	        	//$(this).css({"white-space":"normal"});
	        }else{
	        	$(this).css({"text-overflow":"ellipsis","overflow":"hidden","white-space":"nowrap"});
	        }
		})
	        $("td").mouseenter(function(){
	        		if (this.offsetWidth < this.scrollWidth) {
	        			if($(this).siblings().text()=="公钥" || $(this).siblings().text()=="注册码"){
	        				$(this).css({"white-space":"normal"});
	                		return;
	                	}
	                    var text = $(this).text();
	                    tipsindex=layer.tips(text, this,{  
	                        maxWidth:700,
	                        tips:[3,"#F0882C"],
	                        time:0
	                    });
	                }
	        }).mouseleave(function(){  
	        layer.close(tipsindex);
	    })
	}
	
	Date.prototype.format = function (format) {
	    var date = {
	        "M+": this.getMonth() + 1,
	        "d+": this.getDate(),
	        "h+": this.getHours(),
	        "m+": this.getMinutes(),
	        "s+": this.getSeconds(),
	        "q+": Math.floor((this.getMonth() + 3) / 3),
	        "S+": this.getMilliseconds()
	    };
	    if (/(y+)/i.test(format)) {
	        format = format.replace(RegExp.$1, (this.getFullYear() + '').substr(4 - RegExp.$1.length));
	    }
	    for (var k in date) {
	        if (new RegExp("(" + k + ")").test(format)) {
	          format = format.replace(RegExp.$1, RegExp.$1.length == 1 ? date[k] : ("00" + date[k]).substr(("" + date[k]).length));
	        }
	    }
	    return format;
	}
})