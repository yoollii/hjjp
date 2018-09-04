//注意：选项卡 依赖 element 模块，否则无法进行功能性操作
//alert(123);
layui.use('element', function(){
  var element = layui.element;
});

/**
 * 路径变量
 */
var ctx_url=$("#ctx_url").val();

/**
 * 单击用户选项卡
 */
function aduser(){	
	//当前为用户
	//$("#selectedObjId").val(id);
	$("#selectedObjCode").val("User");
	
	var arrStr=$("#users_json_arr").text().trim();
	var arr = eval('(' + arrStr + ')');
	var arrNew=[];
	var search=$("#searchCondition").val();
	for(var i=0;i<arr.length;i++){
		if(search==null||search==''){
			arrNew=arr;
		}else if(arr[i].truename.indexOf(search)>-1){
			arrNew.push(arr[i]);
		}
	}
	//alert(arr[0].id);
	appendEl(arrNew);
}

/**
 * 弹出框
 * @param pid 流程id
 * @param tid 任务id
 * @param isManySelected 是否为多选
 */
function selectNextOpe(pid,tid,isManySelected){
	$.ajax(
	{
		url:ctx_url+"/loadForm/findUser",
		cache:false,
		async:false,
		data:{processInstanceId:pid,TaskDefinitionId:tid},
		success: function(data){
			wd = layer.open({
				  title: ["选择下一步操作者"],
				  type: 1,
				  area: ['800px', '500px'],
				  fixed: false, //不固定
				  maxmin: true,
				  content: $.trim(data),
				  btn: ['确定','取消'],
				  success: function(layero, index){
					  //填充选择模式
					  $("#isManySelected").val(isManySelected);
					  //填充已有的
					  //登录名
					  $("#selectedUserName").val($("#selected_username").val());
					  //真实名
					    var usernames=$("#selectedUserName").val();
						if(usernames==null||usernames==""){
							return;
						}
						var unArr=usernames.split(",");	
						var trArr= $("#selected_truename").val().split(",");
						for(var i=0;i<unArr.length;i++){
							if(unArr[i]!=null&&unArr[i]!=undefined&&unArr[i]!=""){
								addTrueName(trArr[i],unArr[i]);
							}
						}
					  

				  },
			      yes: function(index, layero){//确认执行方法
			    	  //真实名拼装
			    	  var tname="";
			          var f=0;
			    	  $("div[id^='selected_div_user_']").each(function(){
			    		  var truename=$(this).text();
			    		 /* alert(truename);
			    		  truename=truename.substring(0,truename.length-1);
			    		  alert(truename);*/
			    		  if(f>0){
			    			  tname+=",";
			    		  }
			    		  tname+=truename;
			    		  f++;
			    		  //alert(truename);
			    	  });
			    	  $("#selected_truename").val(tname);
			      		//登录名拼装
			    	  $("#selected_username").val($("#selectedUserName").val());
			      	 //关闭弹出框
			      	 layer.close(layer.index);
			      	
			      },
			      no:function(index, layero){//取消
			           
			      }
				});
		}
	});
}


function selectOpe(temp){
	debugger;
	//var tep=temp;
	$.ajax(
	{
		url:ctx_url+"/loadForm/findUser",
		cache:false,
		async:false,
		data:{processInstanceId:null,TaskDefinitionId:null},
		success: function(data){
			wd = layer.open({
				  title: ["选择下一步操作者"],
				  type: 1,
				  area: ['800px', '500px'],
				  fixed: false, //不固定
				  maxmin: true,
				  content: $.trim(data),
				  btn: ['确定','取消'],
				  success: function(layero, index){
					  //填充选择模式
					  $("#isManySelected").val(true);
					  //填充已有的
					  //登录名
					  $("#selectedUserName").val($(temp).val());
					  //真实名
					    var usernames=$("#selectedUserName").val();
						if(usernames==null||usernames==""){
							return;
						}
						var unArr=usernames.split(",");	
						var trArr= $(temp).val().split(",");
						for(var i=0;i<unArr.length;i++){
							if(unArr[i]!=null&&unArr[i]!=undefined&&unArr[i]!=""){
								addTrueName(trArr[i],unArr[i]);
							}
						}
					  

				  },
			      yes: function(index, layero){//确认执行方法
			    	  //真实名拼装
			    	  var tname="";
			          var f=0;
			    	  $("div[id^='selected_div_user_']").each(function(){
			    		  var truename=$(this).text();
			    		 /* alert(truename);
			    		  truename=truename.substring(0,truename.length-1);
			    		  alert(truename);*/
			    		  if(f>0){
			    			  tname+=",";
			    		  }
			    		  tname+=truename;
			    		  f++;
			    		  //alert(truename);
			    	  });
			    	 // $("#selected_truename").val(tname);
			    	  debugger;
			    	  $(temp).val(tname);
			      		//登录名拼装
			    	 // $("#selected_username").val($("#selectedUserName").val());
			      	 //关闭弹出框
			      	 layer.close(layer.index);
			      	
			      },
			      no:function(index, layero){//取消
			           
			      }
				});
		}
	});
}

/*function removeElFromDiv(ele){
	
	  var eleId = ele.getAttribute("index");
	  var exist = $("#choseduser").find("[index='"+index+"']").length;
	  if(exist){
		  $("[index='"+index+"']").remove();
	  }
}*/


/**
 * 清理所有待选者
 */
function clearnLoaduser(){
	  $("#searchCondition").val("");
	  $("#loaduser").empty();
}


/**
 * 构建可选用户列表
 */
function bindRadioChange(){
	
	  $("#loaduser").find("input[name='waitingUser']").click(function(event){
		  
		  	var chked=this.checked;		  
		  	var val=this.value;
		  	var isManySelected=$("#isManySelected").val();
		  	var cname=$("#kxzwm_"+val).html().trim();
		  	
		  	if(chked){
			  	addTrueName(cname,val);
			  	addUserName(val);		  		
		  	}else{
		  		clearTrueName(val);
		  		clearUserName(val);
		  	}
		  	
		  	
	  });
}


/**
 * 删除一个已选用户
 * @param thzs 当前span
 */
function removeThis(thzs){
	var thisObj=$(thzs);//js对象转jquery对象
	var username=thisObj.attr("username");  
	thisObj.parent().remove();
	clearUserName(username);
	$("#kx_"+username).removeAttr("checked");
}


/**
 * 移除一个已选的展示名
 * @param username 登录名
 */
function clearTrueName(username){
	if(isManySelected=$("#isManySelected").val()=="true"){
		$("#selected_div_user_"+username).remove();
	}else{		
		$("#choseduser").empty();
	}
} 

/**
 * 移除一个已选的登录名
 * @param username 登录名
 */
function clearUserName(username){
	if(isManySelected=$("#isManySelected").val()=="true"){
		var usernames=$("#selectedUserName").val();
		if(usernames==null||usernames==""){
			return;
		}
		var unArr=usernames.split(",");
		var newArr=new Array();
		for(var i=0;i<unArr.length;i++){
			if(unArr[i]!=null&&unArr[i]!=undefined&&unArr[i]!=""&&unArr[i]!=username){
				newArr.push(unArr[i]);
			}
		}
		//赋值
		$("#selectedUserName").val(newArr.join(","));
	}else{		
		$("#selectedUserName").val("");
	}
}

/**
 * 添加一个展示名
 * @param cname 展示名
 * @param username 登录名
 */
function addTrueName(cname,username){
	if(isManySelected=$("#isManySelected").val()=="true"){
		//alert($("#selected_div_user_"+username).html());
		if(!$("#selected_div_user_"+username).html()){
			var html="<div style='display:flex;align-items:center;' id='selected_div_user_"+username+"'>"+cname+"<span style='margin-left:5px;background:url(../static/images/zTreeStandard.png) -110px -64px no-repeat;width:18px;height:17px;display:inline-block' username='"+username+"' onclick='removeThis(this)'></span></div>"
			$("#choseduser").append(html);	
		}
	}else{
		$("#choseduser").empty();		
		var html="<div style='display:flex;align-items:center;' id='selected_div_user_"+username+"'>"+cname+"<span style='margin-left:5px;background:url(../static/images/zTreeStandard.png) -110px -64px no-repeat;width:18px;height:17px;display:inline-block' username='"+username+"' onclick='removeThis(this)'></span></div>"
		$("#choseduser").append(html);	
	}
}

/**
 * 添加一个登录名
 * @param username 登陆名
 */
function addUserName(username){
	if(isManySelected=$("#isManySelected").val()=="true"){
		var usernames=$("#selectedUserName").val();
		if(usernames==null||usernames==""){
			$("#selectedUserName").val(username);
			return;
		}
		
		var unArr=usernames.split(",");
		var f=true;
		for(var i=0;i<unArr.length;i++){
			if(unArr[i]!=null&&unArr[i]!=undefined&&unArr[i]!=""&&unArr[i]==username){
				f=false;
				break;
			}
		}
		
		if(f){
			$("#selectedUserName").val($("#selectedUserName").val()+","+username);
		}
	}else{		
		$("#selectedUserName").val(username);
	}
}

/**
 * 拼接候选者的html
 * @param data 待选者json数组,格式:[{id:xx,username:xx,truename:xx}]
 */
function appendEl(data){
	  	$("#loaduser").empty();
	  	$("#searchCondition").empty();
	  	
	  	//获取类型
	  	var isManySelected=$("#isManySelected").val();
	  	var chkType="radio";
	  	if(isManySelected=="true"){
	  		chkType="checkbox";
	  	}
	 
	     for(var i=0;i<data.length;i++){
	    	 var c=data;
	    	 var d=data.length;
	    	 
	    	 //是否选中
	    	 var chked="";	    	 
	    	 var chkArr= $("#selectedUserName").val().split(",");
	    	 if(chkArr!=null&&chkArr.length>0){
	    		 for(var j=0;j<chkArr.length;j++){
	    			 if(chkArr[j]==data[i].username){
	    				 chked="checked='checked'"
	    				 break;
	    			 }
	    		 }
	    	 }

	    	 var content = "<div style='width:22%;margin:2px;'><input type='"+chkType+"' index='"+i+"' "+chked+" id='kx_"+data[i].username+"' name='waitingUser' value='"+data[i].username+"'/><label id='kxzwm_"+data[i].username+"' for='kx_"+data[i].username+"'>" + data[i].truename + "</label></div>";
			 $("#loaduser").append(content);
	     }
	     
	    
		bindRadioChange();
}

/**
 * 根据部门获取用户列表
 * @param id, search。。id部门的id,search 查询的条件，点击部门的时候查询获取查询框的值。。点击搜索的时候传入查询框的值 
 */
function findUserbyDept(id,search){
	var temp=null;
	if(search!=null && search!=undefined){
		temp=search;
	}else{
		temp=$("#searchCondition").val();
	}
	  $.ajax({
		url:ctx_url+"/loadForm/findUserbyDept",
		type : 'GET',
		dataType : "json",
		data:{id:id,searchCondition:temp},
		/* data:{id:id,searchCondition:$("#searchCondition").val()}, */
		success:function(data){
			if(!data || !data.rs){
				return layer.msg('查找失败：'+data.msg, {icon: 5});
			}
			//追加选项框
			appendEl(data.obj);
			//当前为部门
			$("#selectedObjId").val(id);
			$("#selectedObjCode").val("Dept");
		}
	  })
}

/**
 * 根据角色id获取用户列表
 * @param id, search。。id角色的id,search 查询的条件，点击角色查询的时候查询获取查询框的值。。点击搜索的时候传入查询框的值 
 */
function findUserbyRole(id,search){
	
	var temp=null;
	if(search!=null && search!=undefined){
		temp=search;
	}else{
		temp=$("#searchCondition").val();
	}
	  $.ajax({
		url:ctx_url+"/loadForm/findUserbyRole",
		type : 'GET',
		dataType : "json",
		data:{id:id,searchCondition:temp},
		success:function(data){
			if(!data || !data.rs){
				return layer.msg('查找失败：'+data.msg, {icon: 5});
			}
			//追加选项框
			appendEl(data.obj);
			//当前为角色
			$("#selectedObjId").val(id);
			$("#selectedObjCode").val("Role");
		}
	  })
}



/**
 * 搜索按钮
 * 点击搜索按钮的时候获取当前选中的是部门还是角色或者用户。在根据查询的条件做为查询的条件查询
 */
function seletedUserSearch(){
	
	var code=$("#selectedObjCode").val();
	var id=$("#selectedObjId").val();
	var search=$("#searchCondition").val();
	if(code==null||code==''){
		alert("请先选择具体项！");
		return;
	}
	if(code=="Dept"){
		findUserbyDept(id,search);
	}else if(code=="Role"){
		findUserbyRole(id,search);
	}else if(code=="User"){
		aduser();
	}
}