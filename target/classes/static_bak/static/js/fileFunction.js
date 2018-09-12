
// 所检报验单产品型号
$(document).ready(function (){
	// 请求所有的产品型号
	$.ajax({	
	url:"/qlms/inspProductTree/allProduct",
	cache:false,
	async:false,
// data:16,
	success:function(data){
		var obj = JSON.parse(data);
		$("#productModelName").empty();
		$("#productModelName").append("<option value=''>无</option>");
	for(var idx in obj){
		var cell = obj[idx];
		var op;
		op = "<option value='"+cell.id+"'>"+cell.graphCode+"/"+cell.batch+"</option>";
		$("#productModelName").append(op);
	
	}
// $("#productModelName").append(0);
	}

});
	findSelectOP();
});


var treeNode1;
function selectValueByTemplate(){
	var self = {
			productionId:"productModelName1",
			productDrId:"productDr",
			productBatchId:"productBatch",
			productNumberId:"productNumber"
	};
	
	// 追加选项
	self._setSelect = function(optionId,values){
		$("#"+optionId).empty();
		values = [{key:"",value:""}].concat(values);
		var op='';
		values.forEach(function(item){
			if (op.indexOf("value='"+item.key+"'") == -1 ) {	
				op += "<option value='"+item.key+"'>"+item.value+"</option>";
			}
		});
		
		$("#"+optionId).append(op);
	}
	
	self._getProduction = function(translateData){
		$.ajax({	
			url:"/qlms/inspQualifiedProduct/allInspQualifiedProduct",
			cache:false,
			async:false,
			success:function(data){
				self._setSelect(self.productionId,translateData(data));
			}
		});
	}
	
	self.appendProductionSelect=function(){
		self._getProduction(function(data){
			if (data=='') {
				return [];
			}
			var obj = JSON.parse(data);
			treeNode1 = obj;
			
			var _res =[];
			var keyStr = "";
			for(var idx in obj){
				var cell = obj[idx];
				
				if(keyStr.indexOf(">"+cell.ptId+"</option>") != -1){
					continue;
				}
				
				_res.push({
					key:cell.ptId,
					value:cell.ptId,
				});
				keyStr +="@>"+cell.ptId+"</option>";
			}
			return _res;
		});
	}
	
	self._getProductionDr=function(translateData){
		self._setSelect(self.productDrId,translateData(treeNode1));
	}
	
	self.productionChange = function(){
		self._getProductionDr(function(data){
			var select_val = $("#"+self.productionId).children('option:selected').val();
			if("" == select_val){
				return [];
			}
			var _res = [];
			var keyStr = ""

			for(var idx in treeNode1){
				var cell = treeNode1[idx];
				if (cell.ptId==select_val) {
					if(keyStr.indexOf(">"+cell.graphCode+"</option>") !=-1){
						continue;
					}
					keyStr +="@>"+cell.graphCode+"</option>";
					_res.push({
						key:cell.graphCode,
						value:cell.graphCode,
					})
				}
			}
			return _res;
		});
	}
	
	self._getproductBatch=function(translateData){
		self._setSelect(self.productBatchId,translateData(treeNode1));
	}
	
	self.productDrChange = function(){
		self._getproductBatch(function(data){
			var select_val = $("#"+self.productDrId).children('option:selected').val();
			var select_val2 = $("#"+self.productionId).children('option:selected').val();
			
			if (""==select_val) {
				return [];
			}
			
			var _res = [];
			var keyStr="";
		
			for(var idx in treeNode1){
				var cell = treeNode1[idx];
				if (cell.graphCode==select_val&&cell.ptId==select_val2) {
					if(keyStr.indexOf(">"+cell.batchNo+"</option>") !=-1){
						continue;
					}
					_res.push({
						key:cell.batchNo,
						value:cell.batchNo,
					});
					keyStr += "@>"+cell.batchNo+"</option>";
				}
			}
			return _res;
		});
	}
	
	self._getProductNumber = function(translateData){
		self._setSelect(self.productNumberId,translateData(treeNode1));
	}
	
	self.productBatchChange = function(){
		self._getProductNumber(function(data){
			var select_val = $("#"+self.productBatchId).children('option:selected').val();
			var ptid = $("#"+self.productionId).children('option:selected').val();
			var productDr = $("#"+self.productDrId).children('option:selected').val();
			
			$("#"+self.productNumberId).empty();
			$("#"+self.productNumberId).append("<option value=''>无</option>");
			if(select_val == ''){
				return [];
			}
			var keyStr = "";
			var _res = [];
			for(var idx in treeNode1){
				var cell = treeNode1[idx];
				if (cell.batchNo==select_val&&cell.graphCode==productDr&&cell.ptId==ptid) {
					if(keyStr.indexOf(">"+cell.num+"</option>") != -1){
						continue;
					}
					_res.push({
						key:cell.num,
						value:cell.num
					})
					keyStr += "@>"+cell.num+"</option>";
				}
			}
			return _res;	
		});		
	}
	
	//绑定选择事件
	self.selectChange = function(){
		function bind(id,key,callback){
			$("#"+id).unbind(key);
			$("#"+id).bind(key,callback);
		}
		
		bind(self.productionId,"change",self.productionChange);
		bind(self.productDrId,"change",self.productDrChange);
		bind(self.productBatchId,"change",self.productBatchChange);
	}
	
	return self;
}

// 故障单所有产品型号
function AppendProductionSelect(){
	var self = selectValueByTemplate();
	// 先绑定事件,然后设置选择值
	self.selectChange();
	self.appendProductionSelect();
	// 下方的其他事件。其他关联控件（需要自己建立字典的事件）。
	findSelectOP();
	
	
}

function findSelectOP() {
	
	if ($("[layered=1]").val()!='') {
		return;
	}
	$("[layered=1]").empty();
	$.ajax({
		url : '/qlms/form/findSelectOP',
		cache : true,
		async : false,
		type : 'post',
		data : {
			fddictionaries: $("[layered=1]").attr('fddictionaries'),
			layered:1
		},
		success : function(data) {
			$("[layered=1]").eq(0).append("<option value=''>无</option>");
			$("[layered=1]").eq(0).append(data);
		} 
	 });
}




function appChange(thiz){
	var next = Number($(thiz).attr("layered"))+1;
	var value = $(thiz).find("option:selected").text();
	
    $.ajax({
		url : '/qlms/form/findSelectOP',
		cache : true,
		async : false,
		type : 'post',
		data : {
			fddictionaries: $("[layered="+next+"]").attr('fddictionaries'),
			layered:next,
			value:value
		},
		success : function(data) {
			$("[layered="+next+"]").eq(0).empty();
			$("[layered="+next+"]").eq(0).append("<option value=''>无</option>");
			$("[layered="+next+"]").eq(0).append(data);
		} 
	 });
	
}


function turnTo(link,data,blank,type,data2){
			if (blank=='layer') {
				/* blank!='_blank' */
				var data3 = {};
			
			// data2是页面的元素的id们，用逗号分割
			if (''!=data2&&data2!= null ) {
				var strarr = data2.split(',');
				for (var i = 0; i < strarr.length; i++) {
					var key=strarr[i];
					
					data3[key]=$("#"+strarr[i]).val();
					/* debugger; */
				}
			}
			
			var b=JSON.stringify(data3);
			
			var json = eval('('+(data+b).replace(/}{/,',')+')');
			
			 $.ajax({
				url : link,
				cache : false,
				async : false,
				type : type,
				data : json,
				success : function(data) {
					 // 弹出框
					layer.open({
						title : "产品清单表",
						type : 1,
						area : [ '1100px',
								'500px' ],
						fixed : false, // 不固定
						maxmin : true,
						content : data,
						// btn: ['确定','取消'],
						success : function(layero, index) {

						}
					});
					} 
			 });
			 }	 
		 else if('_blank'==blank){
				// 跳转页面
				window.open(link+'?'+data); 
				/* window.location.href = link ; */
			}else{
				
				window.location.href = link+'?'+data;
				
			}
		}
	
	var timeoutflag = null;
	// 500ms内只执行一次
function linkageChange(thiz) {
		
	 if(timeoutflag != null){
	   clearTimeout(timeoutflag);
	 }
	   timeoutflag=setTimeout(function(){
		   linkageChange1(thiz);
	   },500);
	}

function linkageChange1(thiz){
			// 有值就查询数据库关联其他数据
			var value2 = thiz.value;
			// 找到objCode
			var objCode = $('#'+thiz.id).parent().prev().find('lable').eq(0).text();
			if (value2!='') {
				$.ajax({
					url : "findLinkageKVP",
					cache : false,
					async : false,
					type : "POST",
					data : {
						name : value2,
						objCode : objCode
					},
					success : function(data) {
						var data=JSON.parse(data).obj;
						var namearr=[];
						// 把name拿出来
						for(var i=0;i<data.length;i++){
							namearr.push(data[i].name);
						}
						$('#'+thiz.id).autocomplete({
							// 弹出候选name
						      source: namearr,
						      select: function( event, ui ) {
						    	   // 用户选择候选的name
						    	  $('#'+thiz.id).val( ui.item.label );
						    	   // 给其他框赋值
						    	  for(var i=0;i<data.length;i++){
									if(data[i].name == ui.item.label){
										
										var json=JSON.parse(data[i].kvp);
										for ( var o in json) {
											$("#"+o).val(json[o]);
										}
									}	
								 }
					               return false;
					           }
						    });
					}
				});
			}
		}