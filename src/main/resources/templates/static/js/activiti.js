




function removeElFromDiv(ele){
	
	  var eleId = ele.getAttribute("index");
	  var exist = $("#choseduser").find("[index='"+index+"']").length;
	  if(exist){
		  $("[index='"+index+"']").remove();
	  }
}

function appendElToDiv(ele){
	  
	  $("#choseduser").empty();
	  var index = ele.getAttribute("index");
	  var exist = $("#choseduser").find("[index='"+index+"']").length;
	  if(exist<=0){
		  var label = "<label index='"+index+"'>"+ele.parentElement.innerText+"</label>";
		  $("#choseduser").append(label);
	  }
}

function clearnLoaduser(){
	  
	  $("#loaduser").empty();
}


function bindRadioChange(divId){
	
	  $(divId).find("input[name='waitingUser']").click(function(event){
		  
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
		  	
		  	
		 	/* var radio = event.target;
		 	return radio.checked ? appendElToDiv(radio):removeElFromDiv(radio); */
	  });
}
function addChoseUser(cname,username){
	var html="<div id='selected_div_user_"+username+"'>"+cname+"<span username='"+username+"' onclick='removeThis(this)'>x</span></div>"
	$("#selectedUserName").val(username);
	$("#choseduser").append(html);
}

function removeThis(thzs){
	var thisObj=$(thzs);//js对象转jquery对象
	var username=thisObj.attr("username");  
	thisObj.parent().remove();
	clearUserName(username);
}


function clearTrueName(username){
	if(isManySelected=$("#isManySelected").val()=="true"){
		$("#selected_div_user_"+username).remove();
	}else{		
		$("#choseduser").empty();
	}
} 

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

function addTrueName(cname,username){
	if(isManySelected=$("#isManySelected").val()=="true"){
		//alert($("#selected_div_user_"+username).html());
		if(!$("#selected_div_user_"+username).html()){
			var html="<div id='selected_div_user_"+username+"'>"+cname+"<span username='"+username+"' onclick='removeThis(this)'>x</span></div>"
			$("#choseduser").append(html);
		}
	}else{
		$("#choseduser").empty();		
		var html="<div id='selected_div_user_"+username+"'>"+cname+"<span username='"+username+"' onclick='removeThis(this)'>x</span></div>"
		$("#choseduser").append(html);
	}
}

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

function seletedUserSearch(){
	var code=$("#selectedObjCode").val();
	var id=$("#selectedObjId").val();
	if(code=null||code==''){
		alert("请先选择具体项！");
		return;
	}
	if(code=="Dept"){
		findUserbyDept(id);
	}
}

function appendEl(data){
	  
	  	$("#loaduser").empty();
	  	
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

	    	 var content = "<input type='"+chkType+"' index='"+i+"' "+chked+" id='kx_"+data[i].username+"' name='waitingUser' value='"+data[i].username+"'/><label id='kxzwm_"+data[i].username+"' for='kx_"+data[i].username+"'>" + data[i].truename + "</label></br>";
			 $("#loaduser").append(content);
	     }
	     
	    
		bindRadioChange("#loaduser");
}

function Candidate (){
	
}



