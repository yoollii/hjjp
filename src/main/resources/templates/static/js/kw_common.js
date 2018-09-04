// JavaScript Document
var wd;
/**
 * “新建”对话框
 * @param title 标题
 * @param url 内容url
 * @param initData 初始化参数
 * @param formId 表单id
 * @param func	成功之后执行的函数
 * @param funcParm 函数参数
 */
function add4Btn(title,url,initData,formId,func,funcParm){
	$.ajax(	
			{
				url:url,
				cache:false,
				async:false,
				data:initData,
				success: function(data){
					wd = layer.open({
						  title: [title],
						  type: 1,
						  area: ['800px', '500px'],
						  fixed: false, //不固定
						  maxmin: true,
						  content: data,
						  btn: ['确定','取消'],
						  success: function(layero, index){
							  setValue(initData);
							  initForm(formId,"i",func,funcParm);
							  addAfterLoad();//新增，初始化完成后执行
						  },
					      yes: function(index, layero){//确认执行方法
					    	  //alert($("#"+formId).html());
					    	  $("#"+formId).submit();
					    	  //layer.close(index);
					      },
					      no:function(index, layero){//取消
					           
					      }
						});
				}
			});
}


/**
 * 带模板选择的新增窗口
 * @param title 标题
 * @param url 
 * @param initData 初始化数据
 * @param tempUrl 模板url
 * @param tempData 请求模板数据
 * @param formId 表单id
 * @param func	完成后执行的函数
 * @param funcParm 完成后执行的函数的参数
 */
function add4BtnWithTemp(title,url,initData,tempUrl,tempData,formId,func,funcParm){
	$.ajax(
			{
				url:url,
				cache:false,
				async:false,
				data:initData,
				success: function(data){
					wd = layer.open({
						  title: [title],
						  type: 1,
						  area: ['800px', '500px'],
						  fixed: false, //不固定
						  maxmin: true,
						  content: data,
						  btn: ['确定','模板','取消'],
						  success: function(layero, index){
							  setValue(initData);
							  initForm(formId,"i",func,funcParm);							  
							  addAfterLoad();//新增，初始化完成后执行
						  },
					      yes: function(index, layero){//确认执行方法
					    	  //alert($("#"+formId).html());
					    	  $("#"+formId).submit();
					    	  //layer.close(index);
					      },
					      btn2: function(index, layero){
					    	 loadTemp(tempUrl,tempData);
					    	 return false;
					    	 //return false 开启该代码可禁止点击该按钮关闭
					      },
					      no:function(index, layero){//取消
					           
					      }
						});
				}
			});
}


var tempDataArr=null;
function loadTemp(url,tdata){
	$.ajax(
			{
				url:url,
				cache:false,
				async:false,
				data:tdata,
				dataType:'json',
				success: function(data){
					var c="<ul>";
					tempDataArr=data;
					for(var i in data){
						c+="<li id='";
						c+="tdata_"+i+"' onclick='loadTempData("+i+")'>"
						c+=data[i].name;
						c+="</li>"
					}
					c+="</ul>"
					
					layer.open({
						  type: 1,
						  title: false,
						  closeBtn: 0,
						  shadeClose: true,
						  skin: 'bs-example',
						  content: c
						});
				}
			});
}

function loadTempData(idx){
	setValue(tempDataArr[idx].data);
	layer.close(layer.index);
}


function addAfterLoad(){
	
}

function makeBtn(formId){
	var btn = $('<input type="submit" value="保存" />');
	return $("#"+formId).append(btn);
}
/**
 * 获取二级字典
 * @param
 * @param dicDent  字典唯一标识 
 * @param func	删除成功之后执行的函数，默认为刷新当前页
 * @param funcParm	删除成功之后执行的函数的参数
 */
function logicDic(dicDent,func,funcParm){
	if(isEmpty(ids)){
		return;
	}	
	$.ajax(
			{
				url:"../conf/dic_edit_do.jsp",
				cache:false,
				async:false,
				data:{
	    			m:"dic",
	    			ids:dicDent
	    		},
		    		success:function(data){
		    			if(data.rs){
//		    				layer.msg('获取成功！', {icon: 6});
		    				if(func==undefined){		    					
		    					location.reload();
		    				}else{
		    					if(funcParm==undefined){		    						
		    						func();
		    					}else{
		    						func(funcParm);
		    					}
		    				}
		    			}else{
//		    				layer.msg('获取失败：'+data.msg, {icon: 5}); 
		    			}
		    			
		    		}
		    });			  
}

function load2Id(url,dataparam,destId){
	$.ajax(
			{
				url:url,
				cache:false,
				async:false,
				data:dataparam,
				success: function(data){
					$("#"+destId).html(data); 
				}
			});
}
var ronghvalidate=false;
function editEx(title,url,dataparam,formId,isLock){
	$.ajax(
	{
		url:url,
		cache:false,
		async:false,
		data:dataparam,
		success: function(data){
			wd = $.dialog(
			{
				title:title,
				content:data,
				lock:isLock,
				init: function(){
					ronghvalidate=false;
					initValidate(formId);
					setValue(dataparam);
				}
			}
			);
		}
	});
}
/**
 * 修改
 * @param title 标题
 * @param url 编辑页面url
 * @param dataurl 提交数据url
 * @param dataparam 参数
 * @param formId 表单ID
 * @param func	成功之后执行的函数
 * @param funcParm 函数参数
 */
function edit4Btn(title,url,dataurl,dataparam,formId,func,funcParm){
	$.ajax(
	{
		url:url,
		cache:false,
		async:false,
		data:dataparam,
		success: function(data){
			wd = layer.open({
				  title: [title],
				  type: 1,
				  area: ['800px', '500px'],
				  fixed: false, //不固定
				  maxmin: true,
				  content: $.trim(data),
				  btn: ['确定','取消'],
				  success: function(layero, index){
					  //setValue(dataparam);
					  initForm(formId,"u",func,funcParm);
					  loadData(dataurl,dataparam);
					  afterLoad();
				  },
			      yes: function(index, layero){//确认执行方法
			    	  $("#"+formId).submit();
			    	  //layer.close(index);
			      },
			      no:function(index, layero){//取消
			           
			      }
				});
		}
	});
}
/**
 * 修改
 * 去除日志查询详情确定/取消、minmax按钮
 */
function edit4Btn_logContents(title,url,dataurl,dataparam,formId,func,funcParm){
	$.ajax(
	{
		url:url,
		cache:false,
		async:false,
		data:dataparam,
		success: function(data){
			wd = layer.open({
				  title: [title],
				  type: 1,
				  area: ['800px', '500px'],
				  fixed: false, //不固定
				  content: $.trim(data),
				  success: function(layero, index){
					  //setValue(dataparam);
					  initForm(formId,"u",func,funcParm);
					  loadData(dataurl,dataparam);
					  afterLoad();
				  },
			      yes: function(index, layero){//确认执行方法
			    	  $("#"+formId).submit();
			    	  //layer.close(index);
			      },
			      no:function(index, layero){//取消
			           
			      }
				});
		}
	});
}


/**
 * 修改
 * 去除查看角色权限确定/取消按钮
 */
function edit4Btn_power(title,url,dataurl,dataparam,formId,func,funcParm){		
	$.ajax(
	{
		url:url,
		cache:false,
		async:false,
		data:dataparam,
		success: function(data){
			wd = layer.open({
				  title: [title],
				  type: 1,
				  area: ['800px', '500px'],
				  fixed: false, //不固定
				  maxmin: true,
				  content: $.trim(data) + "<input type='text' id='roleid' style='display:none' value='"+dataparam.roleid +"' />",
				  success: function(layero, index){
					  //setValue(dataparam);
					  initForm(formId,"u",func,funcParm);
					  loadData(dataurl,dataparam);
					  afterLoad();
				  },
				});
		}
	});
}
/**
 * 删除确认提示
 * @param url	处理数据的url
 * @param datas 参数
 * @param func	删除成功之后执行的函数，默认为刷新当前页
 */
function del(url,datas,func,funcParm){	
	layer.confirm(
			'确认删除该数据?',
			{icon: 3, title:'删除确认'}, 
			function(index){
			  //do something
				jQuery.ajax({
		    		url:url,
		    		cache:false,
		    		type : 'GET',
		    		dataType : "json",
		    		data:datas,
		    		success:function(data){
		    			if(data.rs){
		    				layer.msg('删除成功！', {icon: 6});
		    				if(func==undefined){		    					
		    					location.reload();
		    				}else{
		    					if(funcParm==undefined){		    						
		    						func();
		    					}else{
		    						func(funcParm);
		    					}
		    				}
		    			}else{
		    				layer.msg('删除失败：'+data.msg, {icon: 5}); 
		    			}
		    			
		    		}
		    	});			  
			}
		);
}
/**
 * 用于删除前的检测提示
 * @param title 标题
 * @param url 链接
 * @param datas
 */
function delCheck4Btn(title,url,datas,formId,max,min,isLock){
	$.ajax(
			{
				url:url,
				cache:false,
				async:false,
				data:datas,
				success: function(data){
					wd = $.dialog(
					{
						title:title,
						content:data,
						lock:isLock,
						max:max,
						min:min,
						ok:function(){
							$("#"+formId).submit();
						},
						cancelVal: '取消',
						cancel:true
					}
					);
				}
			});
}
/**
 * 操作确认提示
 * @param url	处理数据的url
 * @param datas 参数
 * @param func	操作成功之后执行的函数，默认为刷新当前页
 * @param funcParm 操作成功之后执行的函数的参数
 */
function ope(url,datas,func,funcParm){	
	layer.confirm(
			'确认操作?',
			{icon: 3, title:'操作确认'}, 
			function(index){
			  //do something
				jQuery.ajax({
		    		url:url,
		    		cache:false,
		    		type : 'GET',
		    		dataType : "json",
		    		data:datas,
		    		success:function(data){
		    			if(data.rs){
		    				layer.msg(data.msg, {icon: 6});
		    				if(func==undefined){		    					
		    					location.reload();
		    				}else{
		    					if(funcParm==undefined){		    						
		    						func();
		    					}else{
		    						func(funcParm);
		    					}
		    				}
		    			}else{
		    				layer.msg('操作失败：'+data.msg, {icon: 5}); 
		    			}
		    			
		    		}
		    	});			  
			}
		);
}
function closeAllWd(){
	layer.closeAll();
}

function reload(){
	location.reload();
}
/**
 * 初始化表单验证
 * @param id
 */
function initForm(id,m,func,funcParm){
	jQuery("#m").val(m);
	//var xx=0;
	jQuery("#"+id).validate({
		//debug: true,
		submitHandler: function(form) {
			//if(xx==0){
			ronghvalidate=false;
			save(form,func,funcParm);	
				//xx=1;
	//}		
		}
	});	
	
	setAllReqiuried();
}
function initValidate(id){
	jQuery("#"+id).validate({
		submitHandler: function(form) {
			ronghvalidate=true;
			save(form);
		}
	});
}
/**
 * 保存数据
 * @param form
 */
function save(form,func,funcParm){
	jQuery.ajax({
		url : form.action,
		cache:false,
		type : 'POST',
		data:getFormJson(form),
		dataType : "json",
		success : function(data){
			if(!data.rs)
			{
				$("#msg").text(data.msg);
				ronghvalidate=false;
			}else{
				if(func==undefined){
					location.reload();
				}else{
					if(funcParm==undefined){
						func();						
					}else{
						func(funcParm);
					}
				}
			}
		}
	});
}
/**
 * 获取表单对象
 * @param frm
 * @returns
 */
function getFormJson(frm) {
	var a = jQuery(frm).serializeArray();
	return a;
}

/**
 * 初始化表单数据
 * @param url
 * @param datas
 */
function loadData(url,datas){
	jQuery.ajax({
		url:url,
		cache:false,
		async:false,
		type : 'GET',
		dataType : "json",
		data:datas,
		success:function(data){
			setValue(data);			
		}
	});
}
/**
 * 填充form表单
 * @param obj
 */
function setValue(obj) {
	// 开始遍历     
	for ( var p in obj) {
		// 方法  
		if (typeof (obj[p]) == "function") {
			// obj[p]();       
		}else if(typeof(obj[p])=="object"){
			if(obj[p] instanceof Array){
				procArray(obj[p]);
			}
		} else {
			var o=jQuery("#"+p).val();
			if(o==null||o==undefined){
				jQuery("input[name='"+p+"'][value='"+obj[p]+"']").attr("checked",true);
			}else{
				jQuery("#" + p).val($.trim(obj[p]));
			}
			// p 为属性名称，obj[p]为对应属性的值  
			//alert(jQuery("#" + p).html());
		}
	}
	//afterLoad();
	
}
function afterLoad(){
	
}

/**
 * 处理json数组,根据需要实现
 * @param ar
 */
function procArray(ar){
}
function addBox(param){
	initParam(param);
	$.ajax(
			{
				url:param.url,
				cache:false,
				async:false,
				data:param.dataparam,
				success: function(data){
					wd = $.dialog(
					{
						title:param.title,
						content:data,
						lock:param.lock,
						init: function(){
							ronghvalidate=false;
							setValue(param.initdata);
							initForm(param.formId,'i',param.func);
							loadData(param.dataurl,param.dataparam);
							},
						ok:function(){
							$("#"+param.formId).submit();
							return ronghvalidate;
						},
						cancel:true
					}
					);
				}
			});
}
/**
 * 编辑editbox
 * @param param
 * param={title,'',url:'',initdata:{m:'i'},dataurl:'',dataparam:'',formId:'',lock:true,func:function(){}}
 * 
 */
function editBox(param){
	initParam(param);
	$.ajax(
			{
				url:param.url,
				cache:false,
				async:false,
				data:param.dataparam,
				success: function(data){
					wd = $.dialog(
					{
						title:param.title,
						content:data,
						lock:param.lock,
						init: function(){
							ronghvalidate=false;
							setValue(param.initdata);
							initForm(param.formId,param.initdata.m,param.func);
							loadData(param.dataurl,param.dataparam);
							},
						ok:function(){
							$("#"+param.formId).submit();
							return ronghvalidate;
						},
						cancel:true
					}
					);
				}
			});
}
function initParam(param){
	param.title=param.title||'标题';
	param.url=param.url||'';
	param.initdata=param.initdata||{};
	param.dataurl=param.dataurl||'';
	param.dataparam=param.dataparam||{};
	param.formId=param.formId||'form1';
	param.lock=param.lock||true;
	param.func=param.func||function(){location.reload();};
}
function showBox(param){
	initParam(param);
	$.ajax(
			{
				url:param.url,
				cache:false,
				async:false,
				data:param.dataparam,
				success: function(data){
					$.dialog(
							{
								title:param.title,
								content:data,
								lock:param.lock,
								init: function(){
									ronghvalidate=false;
									},
								ok:function(){
									return ronghvalidate;
								},
								cancel:true
							}
							);
				}
			}
			);
}


/**
 * 获取方法的方法名，匿名方法返回undefined
 * @param fn 方法对象
 * @returns
 */
function getFName(fn){
	//alert(fn.toString().substring(0,30).replace(/\s+/g,""));
	if(fn.toString().substring(0,30).replace(/\s+/g,"").indexOf("function(") >= 0){
		return undefined;
	}
	return (/^[\s\(]*function(?:\s+([\w$_][\w\d$_]*))?\(/).exec(fn.toString())[1] || '';
}


//将表单对象序列化成json对象
jQuery.fn.serializeObject = function()
{
    var o = {};
    var a = this.serializeArray();
    $.each(a, function() {
        if (o[this.name] !== undefined) {
            if (!o[this.name].push) {
                o[this.name] = [o[this.name]];
            }
            o[this.name].push(this.value || '');
        } else {
            o[this.name] = this.value || '';
        }
    });
    return o;
};

