$(function(){
	var mkey;
	var flag;
	
	$("#btn2").click(function(){
		if(check1() == true){
			$("#uploadForm").attr("action","uploadServer");
			$("#uploadForm").submit();
		}
	})
	
	function uploadTesting(){
		if($("#file1").val() != "" && $("#dis1").attr("style") == "display:none"){
			kwtlayer.alert("请先提交附件!");
			return false;
		}
		return true;
	}
	
	function callBack(){
		
	}
	
		$("#btn1").click(function(){
			if($("#file1").val() == ""){
				kwtlayer.alert("请先选择文件");
			}else{
				$("#uploadForm").ajaxSubmit({
					url:"uploadTemp",
					type:"POST",
					contentType : "application/x-www-form-urlencoded; charset=utf-8",
					success:function(data){
						var fileName = $("#file1").val().split("\\");
						var jsonData = JSON.parse(data);
						mkey = jsonData.data;
						$("#h1").attr("value",mkey);
						kwtlayer.alert("上传成功！");
						$("#span1").html("已选择文件："+fileName[fileName.length-1]);
						$(".dis2").attr("style","display:none");
						$("#dis1").attr("style","display:inline");
					}
				});
			}
		})
		
		$("#dis1").click(function(){
			$("#dis1").attr("style","display:none");
			$(".dis2").attr("style","display:inline");
			$("#a1").attr("style","display:none");
			$("#span1").html("");
		})
		
		
		
})