
     var testUtil = new CreateFormUtil("roleid");
        jQuery(document).ready(function () {
            jQuery.alerts.okButton = "确定";
            jQuery.alerts.cancelButton = "取消";
            
            //从弹出框中获取内容数据
            function getMessage(event) {
                return event.target.parentElement.parentElement;
            }
            
            //创建弹出框
            function createWindow(value, callback) {
                var name = testUtil.createOne({
                    label: "标题",
                    must: true,
                    name: "label",
                    value: value && value.label ? value.label : "",
                });
                var col = testUtil.createOne({
                    label: "分几列",
                    name: "number",
                    value: value && value.number ? value.number : "",
                })
                var form = jQuery("<form class='stdform'></form>");
                form.append(name);
                form.append(col);
                var title = value ? "更新栏目" : "新增栏目";
                alert("内部2");
                jQuery.alerts.dialog(form.prop("outerHTML"), title, function (event) {
                	  alert("内部3");
                    var message = jQuery(getMessage(event));
                    var values = testUtil.getFormValues(jQuery(message.find("form").eq(0)));
                    return callback(values);
                });
            }
            //项工具类中设置出来函数
            testUtil.addOperate({
                "add": function (event) {
                    var parent = event.target.parentNode;
                    var groupid = parent.getAttribute("roleid");
                    layer.open({
                 		title : "选择字段",
                 		type : 2,
                 		area : [ '800px', '400px' ],
                 		fixed : false, // 不固定
                 		maxmin : true,
                 		content : "../field/selectList?groupid="+groupid,
                 		// btn: ['确定','取消'],
                 		success : function(layero, index) {
                 		}
                 	}); 
                },
                //重新编辑分组信息
                "edit": function (event) {
                    var elemnt = event.target.parentElement;
                    var group = elemnt.parentElement.parentElement;
                    var number = group.getAttribute("col");
                    var label = $(group).find(".row_header").eq(0);
                    createWindow({
                        label: label.text(),
                        number: number
                    }, function (values) {
                        $(group).attr(values.number);
                        label.text(values.label);
                    });
                },
                "delete": function (event) {
	                var elemnt = event.target.parentElement;
	                var group = elemnt.parentElement.parentElement;
	                $(group).remove();
	            },
                //保存数据到服务器
                "top_save": function (event) {
                    alert("top_save");
                },
                //添加分组
                "top_add": function (event) {
                	alert("xxx");
                    createWindow(null,function (values) {
                        testUtil.addGroup(values);
                    });

                },
                //字段排序
                "top_sort": function (event) {
                    alert("top_sort");
                },
                //发布
                "top_public": function (event) {
                    alert("top_public");
                }
            });
            jQuery("#roleid").append(testUtil.headerOperate());
            testUtil.bindClick();
        });
        
        function addfields(url, groupName, cols, lidata) {
    		$.ajax({
    			url : url,
    			cache : false,
    			// async:false,
    			data : {
    				fdCode : lidata
    			},
    			success : function(data) {
    				testUtil.addcol(data,groupName);
    			}
    		});
    		
    	}  
        function setUmLength(){
			$(".edui-body-container").each(function(){
				$(this).siblings().eq(0).css("width");
				$(this).css("width",length);
			})
		}
    