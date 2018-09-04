function setAllReqiuried(){
	$(".red_error").each(function(){
		$(this).remove();
	})
	$("input[type!=hidden]").each(function(){
		if($(this).attr("class")!=undefined){
			if($(this).attr("class").indexOf("required")!=-1 && $(this).css("display")!="none"){
				if($(this).parent().prop("tagName")=="TD"){
					$(this).parent().prev().append("<span class='red_error' style='width:0px;color:red;display:inline-block;vertical-align:middle'>*</span>")}
				else{
					$(this).siblings().append("<span class='red_error' style='color:red;width:5px;'>*</span>");
				}
			}
		}
	})
}
function checkMaxLength(){
	$("input[type!=hidden]").each(function(){
		var that=this;
		var classes=$(that).attr("class");
		/*$(that).bind("input propertychange change keydown",function(event){*/
		$(that).bind("keydown input",function(event){
			var val=$(this).val();
			if(classes.indexOf("maxlength")!=-1 && $(that).css("display")!="none"){
				var max=classes.substring(classes.indexOf("{")+1,classes.indexOf("}"));
				var arr=max.split(",");
				arr.forEach(function(item,index){
					if(item.indexOf("maxlength")!=-1){
						if(val.length>=parseInt(item.split(":")[1])){
							layer.msg("只能输入"+item.split(":")[1]+"位数",{time:1000})
						}
					}
				})
			}
		})
	})
}
