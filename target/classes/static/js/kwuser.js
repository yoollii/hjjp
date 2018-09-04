function userrols(url){
	layer.open({
		  title: "角色",
		  type: 2,
		  area: ['800px', '500px'],
		  fixed: false, //不固定
		  maxmin: true,
		  content: url,
		  btn: ['确定','取消'],
		  success: function(layero, index){
			 
		  },
	      yes: function(index, layero){//确认执行方法
	    	  //$("#"+formId).submit();
	    	  //layer.close(index);
	      },
	      no:function(index, layero){//取消
	           
	      }
		});
}