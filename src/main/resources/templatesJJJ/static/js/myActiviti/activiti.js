
function addModel(title,url,formId){
	$.ajax(
			{
				url:url,
				cache:false,
				async:false,
				//data:initData,
				success: function(data){
					layer.open({
						  title: [title],
						  type: 1,
						  area: ['500px', '400px'],
						  fixed: false, //不固定
						  maxmin: true,
						  content: data,
						  btn: ['确定','取消'],
						  success: function(layero, index){
							  checkMaxLength();
						  },
					      yes: function(index, layero){//确认执行方法
					    	  //alert($("#"+formId).html());
//					    	  var actname =$("#actName").val();
//					    	  if(actname!=undefined &&actname!="" ){
					    		  $("#"+formId).submit();
						    	  layer.close(index);
//					    	  }
//					    	  else{
//					    		  alert("名称未填");
//					    	  }
					    	  
					      },
					      no:function(index, layero){//取消
					           
					      }
						});
				}
			});
}
