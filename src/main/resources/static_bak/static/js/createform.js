String.prototype.format = function (args) {
    var result = this;
    if (arguments.length > 0) {
        if (arguments.length == 1 && typeof (args) == "thisect") {
            for (var key in args) {
                if (args[key] != undefined) {
                    var reg = new RegExp("({" + key + "})", "g");
                    result = result.replace(reg, args[key]);
                }
            }
        } else {
            for (var i = 0; i < arguments.length; i++) {
                if (arguments[i] != undefined) {
                    var reg = new RegExp("({)" + i + "(})", "g");
                    result = result.replace(reg, arguments[i]);
                }
            }
        }
    }
    return result;
}

function CreateFormUtil(formid) {
    this.formid = formid;
    this.operatMap = {};
    jQuery("#"+this.formid).addClass("stdform");
    //获取groupid号
    function getGroupid() {
        var group = jQuery("[role='group']");
        if (group.length > 0) {
            return group.length + 1;
        }
        return 1;
    }

    this.getFormValues = function (form) {
        var input = form.find("input");
        var datas = {};
        input.each(function (idx) {
            var name = $(this).attr("name");
            var value = $(this).val();
            datas[name] = value;
        });
        return datas;
    }
    this.createOneByOjb=function(label,input){
        if(label != ""){
            $(label).css("width","80px");
            $(label).addClass("label_name");
        }
        if(input !=""){
            $(input).addClass("longinput");
        }
        //var span = jQuery("<span class='field' style='margin-left:{0}'><span>".format($(label).css("width")));
        var span = jQuery("<span class='field' style='margin-left:100px'><span>".format($(label).css("width")));
        var p = jQuery("<p></p>");
        p.append(label);
        span.append(input);
        p.append(span);
        return p.prop("outerHTML");
    }
    //创建一个数据输入框
    this.createOne = function (value) {
        var label = value.label ? value.label : ""; //显示名称
        var _value = value.value ? value.value : ""; //默认值
        var type = value.type ? value.type : "text"; //输入框类型
        var _class = value.class ? value.class : "longinput"; //class
        var styleL = value.styleL ? value.styleL : "width:80px"; //显示label的样式
        var styleI = value.styleI ? value.styleI : "margin-left:100px"; //输入框的样式
        var label = "<label style='{1}'>{0}</label>".format(label, styleL);
        // if(value.must){
        //     label+= "<span style='color:red'>*</span>";
        // }
        var input = "<span class='field' style={2}><input type='{0}' class='{1} required' name='{3}' value='{4}'></input></span>".format(type, _class, styleI, value.name, _value);
        var p = jQuery("<p></p>");
        p.append(label);
        p.append(input);
        return p.prop("outerHTML");
    }
    this.addOperate = function (operateMap) {
        this.operateMap = operateMap;
    }
    //添加完操作行数据之后的回调
    this.bindClick = function () {
        let operate = this.operateMap;
        jQuery(".operate_btn").unbind("click");
        jQuery(".operate_btn").click(function (event) {
            var oper = event.target.getAttribute("operate");
            var _call = operate[oper];
            if (_call) {
                _call(event);
            }
        });
    }

    //添加操作行数据
    this.addGroupChild = function (value) {
        var label = jQuery("<label style='font-weight: 600;text-align:left;margin-left: 20px;' class='row_header'>{0}</label>".format(value.label));
        var add = jQuery("<button style='margin: 0px 10px;' type='button' class='group_choose operate_btn' operate='add'>{0}</button>".format("选择字段"));
        var edit = jQuery("<button  style='margin: 0px 10px;'  type='button' class='group_delete operate_btn' operate='delete'>{0}</button>".format("删除"));
        var delete_button = jQuery("<button  style='margin: 0px 10px;' type='button' class='group_edit operate_btn' operate='edit'>{0}</button>".format("编辑"));
        var add_sort = jQuery("<button  style='margin: 0px 10px;' type='button' class='group_add_sort operate_btn' operate='top_sort'>{0}</button>".format("字段排序"));
        var main = jQuery("<div class='data_header'></div>");
        
        var buttonwrap=jQuery("<div class='data_header_button style='margin-right:22px'></div>");
        var groupcontain=jQuery("<div style='display:flex;padding: 9px 0;justify-content: space-between;background-color:#F3F3F3;' class='groupcontain'></div>");
        buttonwrap.append(delete_button);
        buttonwrap.append(add);
        buttonwrap.append(edit);
        buttonwrap.append(add_sort);
        groupcontain.append(label);
        groupcontain.append(buttonwrap);
        main.append(groupcontain);
        return main;
    }

    function groupidStr(id) {
        return "group_" + id.toString();
    }

    //添加一个分组
    //value = {number:"列数",其他}
    this.addGroup = function (value) {
        var number = value.number ? value.number : 1;
        var grpid = getGroupid();
        var groupid = groupidStr(grpid);
        var base = "<div class='sort_group' role='group' id='{0}' col='{1}'></div>";
        var group = jQuery(base.format(groupid, number));
        jQuery("#{0}".format(this.formid)).append(group);

        var _obj = this.addGroupChild(value);
        _obj.attr("role", "operate");
        _obj.attr("roleid", groupid);
        jQuery(group).append(_obj);
        
        var groupContent = jQuery("<div  style='display:flex;flex-wrap:wrap'></div>");
        groupContent.attr("class", groupid);
        groupContent.addClass('groupContent');
        jQuery(group).append(groupContent);

        if (this.bindClick) {
            this.bindClick();
        }
    }

    this.has_name = function (name) {
        return jQuery("[name='{0}']".format(name)).length > 0;
    }

    //用户提供数据
    this.addError = function (erroKeys) {}

    function each(data, call) {
        for (var key in data) {
            call(key, data[key]);
        }
    }
    this.getCellName = function (cells) {
        var label = "";
        var input = "";
        for (var idx in cells) {
            var cell = cells[idx];
            if (cell.tagName !== "LABEL" && cell.getAttribute && cell.getAttribute('name')) {
                input = cell;
            }
            if(cell.tagName == "LABEL"){
                label = cell;
            }
        }
        return {
            label:label,
            input:input,
        };

    }
    this.addcol = function (colMap, groupid) {
        var erros = [],
            oks = {};
       // colMap = "<label>生日:</label> <input type='text'  id='sr' name='sr'  onFocus = WdatePicker({dateFmt:'yyyy-MM-dd'}) class='DATE_TIME Field ' value='66' />";
        if (typeof colMap == "string") {
            var obj = this.getCellName($(colMap));
            var label = obj.label;
            var input = obj.input;
            if (input == "") {
                console.log("不支持的数据结构类型,输入框必须包含name");
                return console.log(colMap);
            }
            var name = input.getAttribute("name");
            var str = this.createOneByOjb(label,input);
            colMap = {};
            colMap[name] = str;
        }
        each(colMap, function (key, value) {
            if (this.has_name(key)) {
                erros.push(key);
            } else {
                oks[key] = colMap[key];
            }
        }.bind(this));

        if (erros.length > 0) {
            if (!this.addError(erros)) {
                return;
            }
        }

        each(oks, function (key, val) {
            this.addChild(key, val, groupid);
        }.bind(this));
    }

    this.dataType = function (key, value) {
        return "<input type='text'></input>";
    }

    /*
        添加一个单元数据行
        col: 列号,
        key: 输入框的 name字段,
        value:输入框的类型数据字段,
        格式 { label:"显示名称"},
    */
    this.addCell = function (key, value) {
        //var string = jQuery("<td role='col'></td>");
    	var string = jQuery("<div role='col' style='flex:1' class='test'></div>");
        if(typeof value == "string"){
            string.append($(value))
        }else{
            var label = jQuery("<label role='cell_label' for='key'>{0}</label>".format(key, value.label));
            var input = jQuery(this.dataType(key, value));
            string.append(label);
            string.append(input);
        }
        return string;
    }

    this.addRow = function (groupid) {
        //return jQuery("<tr role ='row'></tr>");
    	return jQuery("<div role ='row' style='display:flex;align-items:center' class='updateRow'></div>");
    }
    
   /* this.getAllkey = function(groupid){
    	var allNames = jQuery("#{0}".format(groupid)).find("*[name]");
    	var names = [];
    	allNames.each(function(){
    		names.push($(this).attr("name"));
    	});
    	return names;
    }
    
    this.addAll(groupId,groupNumer,allNames){
    	this.addChild()
    }*/
    
    var maps  ={};
    this.addChild = function (key, value, groupid) {
        var group = jQuery("#{0}".format(groupid));
        var groupContent=$("." + groupid);
        maps[key] = value;
        if (!group) {
            return console.log("分组id号不存在", groupid);
        }
        var cols = parseInt(group.attr("col"));
        //var last = group.find("[role='row']").last();
        var last = group.find("[role='group']").last();
        var child = last.children();
        var snap = cols - child.length;
        if (snap > 0 && child.length > 0) {
            return last.append(this.addCell(key, value));
        }
        var width=99/cols+'%';
        last = this.addRow(groupid);
        last.css("width",width);
        last.append(this.addCell(key, value));
        last.append("<span class='deleteRow' style='display: inline-block;width: 35px;margin-left:5px' onclick='deleteRow($(this));'>删除</span>");
        groupContent.append(last);
    }
    

    //baseOperate

    this.headerOperate = function () {
        //var save = "<button type='button' class='top_header_save operate_btn stdformbutton' operate ='top_save'>保存</sapn>";
       // var public1 = "<button  type='button' class='top_header_public operate_btn stdformbutton' operate ='top_public'>发布到新版本</button>";
        var add_group_sort = "<button  type='button' class='add_group_sort operate_btn stdformbutton' operate='add_group_sort'>分组排序</button>"
        var add = "<button  type='button' class='top_header_add operate_btn stdformbutton' operate='top_add'>新增分组</button>";
        //var left = jQuery("<div class='top_header_btn_left' style='float:left'></div>");
        var right = jQuery("<div class='top_header_btn_right'></div>");
        var obj = jQuery("<div style='margin-bottom: 7px;' class='top_header'></div>");
       // left.append(save);
      // left.append(public1);
        right.append(add);
        right.append(add_group_sort);
        

       // obj.append(left);
        obj.append(right);
        return obj;
    }
    this.headerOperate_top = function () {
        var save = "<button type='button' class='top_header_save operate_btn stdformbutton' operate ='top_save'>保存</sapn>";
        var public1 = "<button  type='button' class='top_header_public operate_btn stdformbutton' operate ='top_public'>发布到新版本</button>";
        var left = jQuery("<div class='top_header_btn_left' style='float:left'></div>");
        var obj = jQuery("<div  class='top_header' style='margin-right:22px;height:40px;'></div>");
        left.append(save);
        left.append(public1);
        obj.append(left);
        return obj;
    }
    this.genBtn = function (label) {
        return "<button  type='button' class='operate_btn'>{0}</button>".format(label);
    }
}
