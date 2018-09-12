//每个分组有几个列
function Group(title, formdata) {
	layer.open({
		title : title,
		content : formdata,
		area : [ '500px', '500px' ],
		fixed : false, // 不固定
		maxmin : true,
		btn : [ '确定', '取消' ],
		yes : function(index, layero) {
			var data = $("#groupId").serializeArray();
			$.each(data, function() {
				if (this.name == 'label') {
					name = this.value;
				} else if (this.name == 'number') {
					cols = this.value;
				}
			});
			// insertGroup(name, cols);
			return testUtil.addGroup({
				number : cols,
				label : name
			});
			layer.close(index);
		},
		no : function(index, layero) {
			alert("取消");
			layer.close(index);
		}
	});
}

function group2(formdata, title, fun) {
	layer.open({
		title : title,
		content : formdata,
		area : [ '500px', '500px' ],
		fixed : false, // 不固定
		maxmin : true,
		btn : [ '确定', '取消' ],
		yes : function(index, layero) {
			var data = $("#groupId").serializeArray();
			$.each(data, function() {
				if (this.name == 'label') {
					name = this.value;
				} else if (this.name == 'number') {
					cols = this.value;
				}
			});
			// insertGroup(name, cols);
			return testUtil.addGroup({
				number : cols,
				label : name
			});
			layer.close(index);
			// fun();
		},
		no : function(index, layero) {
			// alert("取消");
			layer.close(index);
		}
	});
}
/**
 * 页面创建分组
 * 
 * @param name
 * @param formId
 * @param cols
 * @param sort
 *            排序，在该页面必须是唯一的
 */
function creatGroup(title) {
	var formdata = '<div><form id="groupForm"><table>'
			+ '<tr><td>分组名：</td><td><input id="groupName" name="groupName" type="text" /></td>'
			+ '</tr><tr><td>列数：</td><td><input  id="groupCol" name="groupCol" type="text" /></td></tr></table></form></div>'

	var name = "";
	var cols;
	var sort;
	layer.open({
		title : title,
		content : formdata,
		area : [ '500px', '500px' ],
		fixed : false, // 不固定
		maxmin : true,
		btn : [ '确定', '取消' ],
		yes : function(index, layero) {
			var data = $("#groupForm").serializeArray();
			$.each(data, function() {
				if (this.name == 'groupName') {
					name = this.value;
				} else if (this.name == 'groupCol') {
					cols = this.value;
				}
			});
			// insertGroup(name, cols);
			testUtil2.addGroup({
				number : cols,
				label : name
			});
			// layer.close(index);
		},
		no : function(index, layero) {
			alert("取消");
			layer.close(index);
		}
	});

}

function insertGroup(name, cols) {

	if ($("#div" + name).length <= 0) {

		$("#centen")
				.append(
						'<div class="stepContainer"style="height: 0px;" id="div'
								+ name
								+ '">'
								+ '<div>'
								+ '<span>'
								+ name
								+ '<button  onclick=updataGroup() >修改</button></span>'
								+ '<input '
								+ 'id="col'
								+ sort
								+ '"'
								+ 'name="col'
								+ sort
								+ '"'
								+ 'type="hidden" value="'
								+ cols
								+ '" ></input>'
								+ '<div class="right">'

								+ '<button  onclick="addGroupField(table'
								+ name
								+ ')" >添加字段</button>'
								+ '</div>'
								+ '<div class="widgetcontent userlistwidget nopadding" >'
								+ '<table id="table' + name + '"></table>'
								+ '</div>' + '</div>');
		// 包存列数
		colMap.set(name, cols);
	} else {
		alert("名称为" + name + "的分组已经存在");
	}
}
function getNewGroup(name, sort, cols) {
	if ($("#div" + sort).length > 0) {
		$("#div" + sort)
				.html(
						'<div class="stepContainer"style="height: 0px;" id="div'
								+ sort
								+ '">'
								+ '<div>'
								+ '<span>'
								+ name
								+ '<button  onclick="updataGroup('
								+ name
								+ ','
								+ sort
								+ ','
								+ sort
								+ ')" >修改</button></span>'
								+ '<input '
								+ 'id="col:'
								+ sort
								+ '"'
								+ 'name="col:'
								+ sort
								+ '"'
								+ 'type="hidden" value="'
								+ cols
								+ '" />'
								+ '<div class="right">'
								+ '<button  onclick="addGroupField()" >添加字段</button>'
								+ '</div>'
								+ '<div class="widgetcontent userlistwidget nopadding" >'
								+ '<table id="table:' + sort + '"></table>'
								+ '</div>' + '</div>');
	}
}

function updataGroup() {
	alert("AAAAA");

}

/**
 * 选字段对话框
 * 
 * @param url
 *            内容url
 * @param initData
 *            初始化参数
 * @param formId
 *            提交的表单id
 * @param func
 *            成功之后执行的函数
 * @param funcParm
 */
function addGroupField(initData) {
	layer.open({
		title : "选择字段",
		type : 2,
		area : [ '800px', '500px' ],
		fixed : false, // 不固定
		maxmin : true,
		content : "../field/selectList?tableName=" + initData,
		btn : [ '确定', '取消' ],
		yes : function(index, layero) {
			layer.close(index);
		},
		no : function(index, layero) {
			layer.close(index);
		},
		success : function(layero, index) {
		}

	});
}

// 复选框全选

function quanxuan() {
	// if ($("#quanxuan").checked);
	$("#quanxuan").click(function() {
		if (this.checked) {
			$("input[name='tianjia']:checkbox").each(function() { // 遍历所有的name为tianjia的
				// checkbox
				$(this).attr("checked", true);
			})
		} else {
			$("input[name='tianjia']:checkbox").each(function() { // 遍历所有的name为tianjia的
				// checkbox
				$(this).attr("checked", false);
				// alert("f");
			})
		}
	});
}
// 保存表单临时文件
function saveFormtemp(formId) {
	var formKey = $("#formId").val();
	var varsion = $("#version").val();
	var formhtml = $("#" + formId).html();

	var formcenter = $("div[role='group']");
	var element = $("#" + formId + " p .Field");

	var elements = []; // 所有name属性
	var label = []; // 所有input select textarea转换为checkbox
	var formcode = []; // 每一个分组信息
	var orderArray = [];
	$(formcenter).each(function() {
		if ($(this).attr("style") != undefined) {
			if ($(this).attr("style").indexOf("order") != -1) {
				orderArray.push(parseInt($(this).css("order")));
			}
		}
	})
	orderArray = orderArray.sort(function(a, b) {
		return a - b;
	});
	if (orderArray.length != 0) {
		for (var a = 0; a < orderArray.length; a++) {
			for (var i = 0; i < formcenter.length; i++) {
				if (parseInt($(formcenter[i]).css("order")) == orderArray[a]) {
					formcode.push($(formcenter[i]).children().eq(0).find(
							'label').prop("outerHTML")
							+ $(formcenter[i]).children().eq(1).prop(
									"outerHTML"));
				}
			}
		}
	} else {
		for (var i = 0; i < formcenter.length; i++) {
			formcode.push($(formcenter[i]).children().eq(0).find('label').prop(
					"outerHTML")
					+ $(formcenter[i]).children().eq(1).prop("outerHTML"));
		}
	}
	for (var i = 0; i < formcenter.length; i++) {
		var namesNumber = $(formcenter[i]).children().eq(1).find(".Field");
		var namesName = $(formcenter[i]).children().eq(1).find(".label_name");
		var col = $(formcenter[i]).attr('col');
		var width = 99 / col + "%";
		label.push("<div style='width:100%'><strong>"
				+ $(formcenter[i]).children().eq(0).find('label').prop(
						"outerHTML") + "</strong><div>"
				+ "<label><input type='checkbox' readAll='read" + i
				+ "' name='' value=''>全可读</label>"
				+ "<label><input writeAll='write" + i
				+ "' type='checkbox' name='' value=''>全可写</label>"
				+ "<label><input type='checkbox' printAll='print" + i
				+ "' name='' value=''>全可打印</label>"
				+ "<label><input type='checkbox' mustAll='must" + i
				+ "' name='' value=''>全必填</label></div>" + "</div>");
		for (var j = 0; j < namesNumber.length; j++) {
			label.push("<div class='ringhts_s "
					+ $(namesNumber[j]).attr('name') + "' style=width:" + width
					+ "><label>" + $(namesName[j]).html() + "</label>"
					+ "&nbsp<label><input class='read" + i
					+ "' type='checkbox' name='"
					+ $(namesNumber[j]).attr('name') + "' value='1'>可读</label>"
					+ "&nbsp<label><input class='write" + i
					+ "' type='checkbox' name='"
					+ $(namesNumber[j]).attr('name') + "' value='2'>可写</label>"
					+ "&nbsp<label><input class='print" + i
					+ "' type='checkbox' name='"
					+ $(namesNumber[j]).attr('name')
					+ "' value='3'>可打印</label>"
					+ "&nbsp<label><input class='must" + i
					+ "' type='checkbox' name='"
					+ $(namesNumber[j]).attr('name')
					+ "' value='4'>必填</label></div>")
		}
	}

	// var element=$("#"+formId+" :input:not(:button,:hidden)");
	for (var i = 0; i < element.length; i++) {
		elements[i] = $(element[i]).attr("name");
	}
	if (elements.length <= 0) {
		layer.msg("内容为空无法保存");
		return;
	}
	$.ajax({
		url : "saveFormTemp",
		cache : false,
		async : true,
		type : "POST",
		beforeSend : function() {
			ShowDiv();
		},
		complete : function() {
			HiddenDiv();
		},
		data : {
			formKey : formKey,
			formhtml : formhtml
		},
		success : function(data) {
			var datatemp = JSON.parse(data);
			layer.msg(datatemp.msg);
			// if(datatemp.rs){
			// layer.msg(datatemp);
			// }
			// else{
			// }
		}
	});
}

// 选择全部表单元素
function findFormElement(formId) {
	var formKey = $("#formId").val();
	var formhtml = $("#" + formId).html();
	var form = $("#" + formId);
	var element = $("#" + formId + " p .Field");
	var formcenter = $("div[role='group']");

	var elements = []; // 所有name属性
	var label = []; // 所有input select textarea转换为checkbox
	var formcode = []; // 每一个分组信息
	var orderArray = [];
	$(formcenter).each(function() {
		if ($(this).attr("style") != undefined) {
			if ($(this).attr("style").indexOf("order") != -1) {
				orderArray.push(parseInt($(this).css("order")));
			}
		}
	})
	orderArray = orderArray.sort(function(a, b) {
		return a - b;
	});
	if (orderArray.length != 0) {
		for (var a = 0; a < orderArray.length; a++) {
			for (var i = 0; i < formcenter.length; i++) {
				if (parseInt($(formcenter[i]).css("order")) == orderArray[a]) {
					formcode.push($(formcenter[i]).children().eq(0).find(
							'label').prop("outerHTML")
							+ $(formcenter[i]).children().eq(1).prop(
									"outerHTML"));
				}
			}
		}
	} else {
		for (var i = 0; i < formcenter.length; i++) {
			formcode.push($(formcenter[i]).children().eq(0).find('label').prop(
					"outerHTML")
					+ $(formcenter[i]).children().eq(1).prop("outerHTML"));
		}
	}

	for (var i = 0; i < formcenter.length; i++) {
		var namesNumber = $(formcenter[i]).children().eq(1).find(".Field");
		var namesName = $(formcenter[i]).children().eq(1).find(".label_name");
		var col = $(formcenter[i]).attr('col');
		var width = 99 / col + "%";
		label.push("<div class='setAll' style='width:100%'><strong>"
				+ $(formcenter[i]).children().eq(0).find('label').prop(
						"outerHTML") + "</strong><div>"
				+ "<label><input type='checkbox' readAll='read" + i
				+ "' name='' value=''>全可读</label>"
				+ "<label><input writeAll='write" + i
				+ "' type='checkbox' name='' value=''>全可写</label>"
				+ "<label><input type='checkbox' printAll='print" + i
				+ "' name='' value=''>全可打印</label>"
				+ "<label><input type='checkbox' mustAll='must" + i
				+ "' name='' value=''>全必填</label></div>" + "</div>");
		for (var j = 0; j < namesNumber.length; j++) {
			label.push("<div class='ringhts_s "
					+ $(namesNumber[j]).attr('name') + "' style=width:" + width
					+ "><label>" + $(namesName[j]).html() + "</label>"
					+ "&nbsp<label><input class='read" + i
					+ "' type='checkbox' name='"
					+ $(namesNumber[j]).attr('name') + "' value='1'>可读</label>"
					+ "&nbsp<label><input class='write" + i
					+ "' type='checkbox' name='"
					+ $(namesNumber[j]).attr('name') + "' value='2'>可写</label>"
					+ "&nbsp<label><input class='print" + i
					+ "' type='checkbox' name='"
					+ $(namesNumber[j]).attr('name')
					+ "' value='3'>可打印</label>"
					+ "&nbsp<label><input class='must" + i
					+ "' type='checkbox' name='"
					+ $(namesNumber[j]).attr('name')
					+ "' value='4'>必填</label></div>")
		}
	}
	// var element=$("#"+formId+" :input:not(:button,:hidden)");\
	for (var i = 0; i < element.length; i++) {
		elements[i] = $(element[i]).attr("name");
	}
	if (elements.length <= 0) {
		layer.msg("内容为空无法发布");
		return;
	}

	$.ajax({
		url : "publishForm",
		cache : false,
		async : true,
		type : "POST",
		beforeSend : function() {
			ShowDiv();
		},
		complete : function() {
			HiddenDiv();
		},
		data : {
			elements : elements,
			formKey : formKey,
			formcenter : formcode,
			formlabel : label,
			formhtml : formhtml
		},
		success : function(data) {
			var dataTemp = JSON.parse(data);
			if (dataTemp.rs) {
				kwtlayer.alert(dataTemp.obj.mgasStr, function() {
					window.location.href = "content?formCode="
							+ dataTemp.obj.nextId;
				});
				/* layer.msg(dataTemp.obj.mgasStr); */
			} else {
				kwtlayer.alert(dataTemp.obj.mgasStr);
			}

		}
	});

}
/**
 * 监听打开的弹窗，关闭后刷新页面
 */
function openWin(url, text) {
	var winObj = window
			.open(
					url,
					text,
					"height="
							+ screen.height
							+ ", width="
							+ screen.width
							+ ", top=0, left=0, toolbar=no, menubar=no, scrollbars=yes, resizable=yes,location=no, status=no");
	var loop = setInterval(function() {
		if (winObj.closed) {
			clearInterval(loop);
			// alert('closed');
			parent.iFrame1.location.reload();
		}
	}, 1);
}

function ShowDiv() {
	var _PageHeight = document.documentElement.clientHeight, _PageWidth = document.documentElement.clientWidth;
	// 计算loading框距离顶部和左部的距离（loading框的宽度为215px，高度为61px）
	var _LoadingTop = _PageHeight > 61 ? (_PageHeight - 61) / 2 : 0, _LoadingLeft = _PageWidth > 215 ? (_PageWidth - 215) / 2
			: 0;
	// 在页面未加载完毕之前显示的loading Html自定义内容
	var _LoadingHtml = $('<div id="loadingDiv" style="position:fixed;left:0;width:100%;height:'
			+ _PageHeight
			+ 'px;top:0;background:#f3f8ff;opacity:0.8;filter:alpha(opacity=80);z-index:10000;"><div style="position: absolute; cursor1: wait; left: '
			+ _LoadingLeft
			+ 'px; top:'
			+ _LoadingTop
			+ 'px; width: auto; height: 57px; line-height: 57px; padding-left: 50px; padding-right: 5px; background: #fff url(/qlms/static/images/colorbox/loading.gif) no-repeat scroll 5px 10px; border: 2px solid #95B8E7; color: #696969; font-family:\'Microsoft YaHei\';">页面加载中，请等待...</div></div>');
	// 呈现loading效果
	// document.write(_LoadingHtml);
	/*
	 * _LoadingHtml=$(_LoadingHtml); document.write(_LoadingHtml);
	 */
	$("body").append(_LoadingHtml);
}
function HiddenDiv() {
	var loadingMask = document.getElementById('loadingDiv');
	loadingMask.parentNode.removeChild(loadingMask);
}

// /////////读取div数据

function readDivDate(id, code,div) {
	$.ajax({
		url : "../Approve/makeDate",
		cache : false,
		async : true,
		type : "POST",
		data : {
			id : id,
			type : code
		},
		success : function(data) {
			var obj = JSON.parse(data);
			var head = obj.head;
			var tdData = obj.data;
			var table2 = obj.tableHead;
			var tb = $("<table></table>");
			var tr = $("<tr></tr>")
			$.each(head, function(key, value) {
				tb.append("<th>" + value + "</th>");
			});
			$.each(tdData, function(index, value) {
				tr.append("<td>" + value + "</td>");
			});
			tb.append(tr);

			// /
			var tb2 = $("<span></span>");
			$.each(table2, function(key, value) {
				tb2.append(key);
				tb2.append(value);
			});

			div.apend(tb2);
			div.apend(tb);

		}
	});
}

// 追溯方法
function insp() {
	var formcode = $("#formCode").val();
	var  formCenter=$("#formCenter");
	var id = $("#id").val();
	if (formcode == "kw_dyn_inspTraceList"
			|| formcode == "kw_dyn_inspCheckList") {
		var cotdiv = $("<div></div>");
	//	readDivDate(id, formcode,cotdiv);
		
		$.ajax({
			url : "../Approve/makeDate",
			cache : false,
			async : true,
			type : "POST",
			data : {
				id : id,
				type : formcode
			},
			success : function(data) {
				var obj = JSON.parse(data);
				var head = obj.head;
				var tdData = obj.data;
				var table2 = obj.tableHead;
				var tb = $("<table class='retrospect-table'></table>");
				var trs = "";
				$.each(head, function(key, value) {
					tb.append("<th>" + value + "</th>");
				});
				$.each(tdData, function(index, value) {
					var tds="";
					for ( var i in value) {
						tds+="<td style='border: 1px solid #ddd;'>"+value[i]+"</td>";
					}
					trs="<tr>"+tds+"</tr>";
					tb.append(trs);
				});
				// /
				var tb2 = $("<p class='retrospect-p'></p>");
				$.each(table2, function(key, value) {
					var strong = $("<strong></strong>")
					strong.append(key+":");
					strong.append(value);
					tb2.append(strong);
				});
				cotdiv.append(tb2);
				cotdiv.append(tb);
				formCenter.append(cotdiv);
				//去掉所有Null
				$("td").each(function() {
					if ($(this).text()=="null") {
						$(this).text("");
					}
				})
			}
		});
		
	
	}
}
