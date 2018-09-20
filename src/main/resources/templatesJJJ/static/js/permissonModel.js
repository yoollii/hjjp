function PermissonModel() {
    this.loadAllinfo = function(data, success) {
        return success({
            processId: "11",
            taskId: "22",
            formObj: [{group: "张三",col: 1,data: [{ name: "密码" }]},
              { group: "张三2", col: 1, data: [{ password: "密码2" }] },
              { group: "张三3", col: 1, data: [{ password2: "密码3" }] },
              { group: "张三4", col: 1, data: [{ password3: "密码4" }] }],
           
		// $.ajax({ 
		//     type: "POST", 	
		// 	url: "http://127.0.0.1:8000/ajaxdemo/serverjsonp.php",
		// 	data: {
		// 		name: $("#staffName").val(), 
		// 		number: $("#staffNumber").val(), 
		// 		sex: $("#staffSex").val(), 
		// 		job: $("#staffJob").val()
		// 	},
		// 	dataType: "json",
		// 	success: function(data){
		// 		if (data.success) { 
		// 			$("#createResult").html(data.msg);
		// 		} else {
		// 			$("#createResult").html("出现错误：" + data.msg);
		// 		}  
		// 	},
		// 	error: function(jqXHR){     
		// 	   alert("发生错误：" + jqXHR.status);  
		// 	},     
		// });

           
        })
    }

    this.getPermission = function(data, success) {
    	 $.ajax({ 
		     type: "POST", 	
		 	 url: "/qlms/formFlowPoint/findFields",
		 	data:data,
		 	dataType:"json",
		 	success: function(data){
		 		return success && success(data);
		 	},
		 	error: function(jqXHR){     
		 	   alert("发生错误：" + jqXHR.status);  
		 	},     
		 });
    }
    
    this.setPermission = function(data, success) {
   	 $.ajax({ 
		     type: "POST", 	
		 	 url: "/qlms/formFlowPoint/updateAuth",
		 	data:data,
		 	dataType:"json",
		 	success: function(data){
		 		return success&&success(data);
		 		
		 	},
		 	error: function(jqXHR){     
		 	   alert("发生错误：" + jqXHR.status);  
		 	},     
		 });
   }

}