var modelUtil = null;
var userUtil = null;
var tabUtil = null;
var debug = true;
    
   
var processId = "jquery{processId}";
var taskId = "jquery{taskId}";

   
function ajaxPost(url, data, callback) {
        jquery.ajax({
            url: url,
            data: data,
            type: "post",
        success: function(data){
        	//console.log("获取的数据："+data);
        	callback(data);
        },
        error:function(data){
        	
        }
    })
}
function userOpr() {
    this.check = function (model) {
        var find = jquery("[_id='" + model.id + "']");
        return find.length > 0;
    }

    this.add = function (model) {
        if (this.check(model)) {
            return alert("该用户已经被选择");
        }
        var label = jquery("<span class='kwt_node' >" + model.name + "</span>");
        label.attr("_type", model.type);
        label.attr("_id", model.id);
        label.append("<button class='kwt_del'>删除</button>");
        jquery("#user_info").append(label);
        label.data("cell", model);
        this.bind();
    }

    this.bind = function () {
        jquery(".kwt_del").unbind("click");
        jquery(".kwt_del").click(this.remove);
    }

    this.remove = function (event) {
        var ele = event.target;
        jquery(ele.parentElement).remove();
    }
}

function Model() {
    // data =
	// {user:[userid,userid],dept:[deptid,deptid],role:[roleid,roleid]}
    this.save = function (data, callback) {
    	for(var key in data){
    		data[key] = data[key].join(",");
    	}
        var query = {
           	processId: processId,
            taskId: taskId, 
            data: data,
            dataType:"json",
        };
        ajaxPost("loadForm/findUser", query, callback);
        //ajaxPost("loadForm/choseCandidateEdit", query, callback);
    }
    
    this.labelMap = {
       /* "Dept": "部门",
        "Role": "角色",*/
        /*"User": "用户",*/
    }

    // 加载初始化
    var sortIndex = ["Dept", "Role", "User"];
    var isTree = {
        "Dept": true,
        "Role": true
    };
    var addType = function (datas, type) {
        datas.forEach(function (item) {
            item.type = type;
        });
        return datas;
    }
    
    var getResult = function (data) {
        var res = [];
        sortIndex.forEach(function (cell) {
            if (!data[cell]) {
                return;
            }
            res.push({
                name: cell,
                val: addType(data[cell], cell),
                isTree: isTree[cell]
            })
        });
        return res;
    }
    // data =
	// {dept:[deptbean,deptbean1],role:[rolebean,rolebean1],user:[userbean,userbean1]}
    
    //获取当前已经选择的部门,角色,用户数据
    
    // 异步获取服务器部门和用户数据
    //var test= {"Role":[{"pageSize":10,"totalSize":0,"pageIndex":1,"order":"","where":"","id":"00","name":"00","orders":0,"state":2,"included":null,"userId":null,"nextPageIndex":10,"skipSize":0},{"pageSize":10,"totalSize":0,"pageIndex":1,"order":"","where":"","id":"11","name":"11","orders":11,"state":1,"included":null,"userId":null,"nextPageIndex":10,"skipSize":0},{"pageSize":10,"totalSize":0,"pageIndex":1,"order":"","where":"","id":"111111","name":"权限测试用户1","orders":1,"state":1,"included":null,"userId":null,"nextPageIndex":10,"skipSize":0},{"pageSize":10,"totalSize":0,"pageIndex":1,"order":"","where":"","id":"111112","name":"权限测试2","orders":2,"state":1,"included":null,"userId":null,"nextPageIndex":10,"skipSize":0},{"pageSize":10,"totalSize":0,"pageIndex":1,"order":"","where":"","id":"1ebd852e366c49c298927306383e0ec0","name":"普通用户","orders":4,"state":1,"included":null,"userId":null,"nextPageIndex":10,"skipSize":0},{"pageSize":10,"totalSize":0,"pageIndex":1,"order":"","where":"","id":"22","name":"22","orders":22,"state":1,"included":null,"userId":null,"nextPageIndex":10,"skipSize":0},{"pageSize":10,"totalSize":0,"pageIndex":1,"order":"","where":"","id":"33","name":"33","orders":33,"state":0,"included":null,"userId":null,"nextPageIndex":10,"skipSize":0},{"pageSize":10,"totalSize":0,"pageIndex":1,"order":"","where":"","id":"44","name":"44","orders":44,"state":44,"included":null,"userId":null,"nextPageIndex":10,"skipSize":0},{"pageSize":10,"totalSize":0,"pageIndex":1,"order":"","where":"","id":"55","name":"55","orders":55,"state":55,"included":null,"userId":null,"nextPageIndex":10,"skipSize":0},{"pageSize":10,"totalSize":0,"pageIndex":1,"order":"","where":"","id":"66","name":"66","orders":66,"state":66,"included":null,"userId":null,"nextPageIndex":10,"skipSize":0},{"pageSize":10,"totalSize":0,"pageIndex":1,"order":"","where":"","id":"77","name":"77","orders":77,"state":77,"included":null,"userId":null,"nextPageIndex":10,"skipSize":0},{"pageSize":10,"totalSize":0,"pageIndex":1,"order":"","where":"","id":"88","name":"88","orders":88,"state":88,"included":null,"userId":null,"nextPageIndex":10,"skipSize":0},{"pageSize":10,"totalSize":0,"pageIndex":1,"order":"","where":"","id":"99","name":"99","orders":99,"state":99,"included":null,"userId":null,"nextPageIndex":10,"skipSize":0},{"pageSize":10,"totalSize":0,"pageIndex":1,"order":"","where":"","id":"9dfceb0274a14ae49fc11b8bc588e657","name":"Test9999","orders":1,"state":1,"included":null,"userId":null,"nextPageIndex":10,"skipSize":0},{"pageSize":10,"totalSize":0,"pageIndex":1,"order":"","where":"","id":"e6eeac3fb0ea4968b38412a7653f539e","name":"游客","orders":6,"state":1,"included":null,"userId":null,"nextPageIndex":10,"skipSize":0}],"Dept":[{"id":"0","name":"金网拓","alias":"4","pId":"1","orders":0,"state":1},{"id":"0ccd10ad6b534b2cb45eba19b8fa635d","name":"qq1233","alias":"qq","pId":"ff4a165a80c14051ae5a799d02cc9af5","orders":0,"state":2},{"id":"75359241ef474ccaa64f3aa2e220331f","name":"一","alias":"vb","pId":"0","orders":1,"state":1},{"id":"cf947e55f10741c8ada3d11294a04092","name":"二","alias":"53","pId":"0","orders":0,"state":1},{"id":"d0c8de13961d42cbbed845ef188ea190","name":"信息部","alias":"0","pId":"0","orders":0,"state":1},{"id":"ff4a165a80c14051ae5a799d02cc9af5","name":"46545","alias":"1655","pId":"0","orders":1,"state":1}]}
    this.load = function (data, callback) {
    	ajaxPost("loadForm/findUser",{},function (data){
    		 var _result = getResult(JSON.parse(data));
    		// console.log(_result);
    	     return callback && callback(_result);
    	});
    }
}

// 点击树节点选择数据
function zTreeOnDblClick(event, treeId, treeNode) {
    userUtil.add({
        id: treeNode.id,
        name: treeNode.name,
        type: treeNode.type
    });
};


var allUsers = {};
function zTreeOnClick(event, treeId, treeNode){
	function isDept(){
		return treeId == "Dept";
	}
	
	function tree(){
		return jquery.fn.zTree.getZTreeObj(treeId);
	}
	
	function appendNodes(nodes){
    	var treeObj =tree();
    	treeObj.addNodes(treeNode,nodes);
	}
	
	if(treeNode.type == "User"){
		return
	}
	
	if(typeof treeNode.children !="undefined"){
		return
	}
	
	var users = allUsers[treeId+treeNode.id];
	if(typeof users != "undefined"){
		return appendNodes(users);
	}
	
	function load(call){
		var index = layer.open({
	        icon: 1,
	        type: 3,
	    });
		call(function(){
			layer.close(index);
		})
	}
	
	
	load(function(close){
		function getData(data){
			var _list = [];
			data.forEach(function(item){
				_list.push({
					id:item.id,
					name:item.username,
					type:"User",
				})
			});
			return _list;
		}

		jquery.ajax({
			url:isDept()?"loadForm/findUserbyDept":"loadForm/findUserbyRole",
			data:{id:treeNode.id},
			dataType:"json",
			aysc:false,
			success:function(data){
				if(!data.rs){
					alert("请重新刷新页面");
				}else if(data.obj.length<=0){
					alert("未找到匹配的数据");
				}else{
					var users = getData(data.obj);
   					allUsers[treeId+treeNode.id] = users;
   					appendNodes(users);
				}
				/*close();*/
			},
			error:function(){
				/*close();*/
			}
		});	
	});
}

function beforeExpand(treeid,treeNode){
    var child = treeNode.children;
    if(typeof child != "undefined"){
        return true;
    }
    
    if(treeNode.type=="User"){
        return true;
    }
    addpendUser(event,treeId,item);
    return true;
}

function zTreeExpand(event,treeid,treeNode){
    var childs = treeNode.children;
    var _open = [];
    var treeId = treeid;
    
    childs.forEach(function(item){
        if(typeof item.children != "undefined"){
            return
        }

        if(item.type == "User"){
            return;
        }
        
        _open.push(item);
    });
    
    function addpendUser(event,treeId,item){
    	zTreeOnClick(event,treeId,item)
    }

    _open.forEach(function(item){
        var switchBtn = jquery("#"+item.tId).find(".switch").eq(0);
        switchBtn.removeClass("bottom_docu");
        switchBtn.removeClass("center_docu");
        switchBtn.addClass("roots_close");

        var button = jquery("#"+item.tId).find("a .button").eq(0);
        button.removeClass("ico_docu");
        button.addClass("ico_close");
        switchBtn.click(function(event){
            switchBtn.removeClass("roots_close");
            switchBtn.addClass("bottom_docu");
            switchBtn.unbind("click");
            addpendUser(event,treeId,item);
        });
    });
}


    
function initTree(datas) {
	 var setting = {
        data: {
            key: {
                title: "name",
                name: "name"
            },
            simpleData: {
                enable: true,
                idKey: "id",
                pIdKey: "pId",
                rootPid: 000
            }
        },
        callback: {
            onDblClick: zTreeOnDblClick,
            onExpand:zTreeExpand,
            beforeExpand:beforeExpand
        }
	 };


    function initOneTree(id, zNodes) {
    	 var _node = jquery("#" + id);
         var data = [{id:id,name:"角色",alias:"角色",open:false}]
         if(id == "Dept"){
             data = [{id:id,name:"部门",alias:"部门",open:false}]
         }
         jquery.fn.zTree.init(_node,setting,data);
         var tree = jquery.fn.zTree.getZTreeObj(id);
         tree.addNodes(tree.getNodeByTId("id"+"_1"),-1,zNodes,true);
    }

    // 用户列表
    function appendList(id, vals) {
    	if(!vals)return
        var ul = jquery("<ul><ul>");
        jquery("#" + id).append(ul);
        vals.forEach(function (ele) {
            var li = jquery("<li class='user'>" + ele.name + "<li>");
            li.attr(id, "user" + ele.id);
            li.data("cell", ele);
            ul.append(li);
        });
        jquery(".user").unbind("click");
        jquery(".user").click(function (event) {
            userUtil.add(jquery(event.target).data("cell"))
        });
    }
    
    tabUtil.config.genLabel =function(key,val){
        return modelUtil.labelMap[val.name];
    }
    tabUtil.config.genContent= function(key,val){
        return "<ul class='ztree' id='"+val.name+"' style='width:100%;height:90%;padding:0;margin:0'></ul>";
    }

    tabUtil.config.addCallback = function(key,val){
        return val.isTree?initOneTree(val.name, val.val):appendList(val.name, val.val);
    }
    
    tabUtil.init(datas,function(){
        tabUtil.select(tabUtil.genId(1));
    })
}
  // 用户生产adminv tableview
function tableview() {
        var self = this;
        this.config = {
            style:{
                width:"100%",
                height:"100%"
            },
            css:"",
            // 生产标签名
            genLabel:function(key,val){
                return key
            },

            // 生产内容 html
            genContent:function(key,val){

            },

            // 添加内容html 成功后的回调
            addCallback:function(key,val){

            }
        }

        // adminv 容器
        this.container = {
            create: function () {
                var div = jquery("<div class='tab_container'></div>");
                div.addClass("ui-tabs ui-widget ui-widget-content ui-corner-all");
                div.css("border","none");
                div.css("border-bottom","2px solid #FB9337")
                return div;
            },
            get: function () {
                return jquery(".tab_container");
            }
        }
        // tab bar
        this.bar = {
            create: function () {
                var ul = jquery("<ul class='tabs-nav'></ul>");
                ul.addClass("ui-tabs-nav ui-helper-reset ui-helper-clearfix ui-widget-header ui-corner-all");
                return ul;
            },
            get: function () {
                return jquery(".tabs-nav");
            },
            genId: function (index) {
                return self.genId(index);
            },
            hide: function (id) {
                if (id) {
                    return jquery("[data_id='" + id + "']").removeClass("ui-tabs-selected ui-state-active");
                }
                this.get().find("li").removeClass("ui-tabs-selected ui-state-active");
            },
            select: function (id) {
                jquery("[data_id='" + id + "']").addClass("ui-tabs-selected ui-state-active");
            },
            add: function (label) {
                var id = this.genId(this.get().children().length + 1);
                if(label!=undefined){
                	var li = jquery("<li class='tab_item'></li>");
                    li.addClass("ui-state-default ui-corner-top");
                    li.attr("data_id", id)

                    var alink = jquery("<a></a>");
                    alink.append(label);
                    
                    li.append(alink);
                    this.get().append(li);
                    
                    li.click(function () {
                        self.hide();
                        self.select(id);
                    });
                }
                return id;
            }
        }
        // 内容容器
        this.content = {
            create: function () {
                var div = jquery("<div class='tabview'><div>");
                div.css(self.config.style);
                return div;
            },
            get: function () {
                return jquery(".tabview");
            },
            hide: function (id) {
                if (id) {
                    return jquery("#" + id).addClass("ui-tabs-hide");
                }
                this.get().find(".ui-tabs-panel").addClass("ui-tabs-hide");
            },
            select: function (id) {
                jquery("#" + id).removeClass("ui-tabs-hide");
            },
            add: function (id, content, callback) {
                var div = jquery("<div></div>");
                div.attr("id", id);
                div.addClass("ui-tabs-panel ui-widget-content ui-corner-bottom ui-tabs-hide");
                div.append(jquery(content));
                this.get().append(div);
                return callback && callback(div);
            }
        }

        this.genId = function (index) {
            return "tabs-" + index;
        }

        // 添加一个元素
        this.add = function (title, content, callback) {
            if(this.tabExist(title)){
                return console.log("该标签已经被使用,请使用其他名称");
            }
            this.content.add(this.bar.add(title), content, callback);
        }

        // 取消选中状态
        this.hide = function (labelId) {
            this.bar.hide(labelId);
            this.content.hide(labelId);
        }

        // 选中某个标签
        this.select = function (labelId) {
            this.bar.select(labelId);
            this.content.select(labelId);
        }

        // 通过标签名获取id号
        this.getIdByLabel = function (label) {
            var lis = this.bar.get().find("a");
            for (var idx=0;idx<lis.length; idx++) {
                var cell = jquery(lis[idx]);
                if (cell.text() == label) {
                    return cell.parent().eq(0).attr("data_id");
                }
            }
            return null;
        }

        this.tabExist = function(label,isId){
            if(isId){
                return jquery("#"+label).length>0;
            }
            return this.getIdByLabel(label) !=null;
        }
        // 自动增加table
        // data = {key:value}
        // callback 数据初始化成功后的回调
        this.init=function(dataMap,callback){
            function addCall(key,val){
                return function(){
                    self.config.addCallback(key,val);
                }
            }
            for(var key=0;key<dataMap.length; key++){
                var label = this.config.genLabel(key,dataMap[key]);
                var content = this.config.genContent(key,dataMap[key]);
                this.add(label,content,addCall(key,dataMap[key]));
            }
            return callback && callback();
        }
        return this;
    }

modelUtil = new Model();
userUtil = new userOpr();
tabUtil = new tableview();

tabUtil.config.style.height = "60%";

      // 用户点击确定
 function user_click(call) {
          var children = jquery("#user_info").find(".kwt_node");
          var maps = {};
          children.each(function () {
              var cell = jquery(this).data("cell");
              var container = maps[cell.type] ? maps[cell.type] : [];
              container.push(cell.id);
              maps[cell.type] = container;
          })
          modelUtil.save(maps, function (rs) {
        	 // console.log("save"+rs);
        	  rs = JSON.parse(rs);
              if (rs!=undefined) {
            	  return call && call(rs);
              }
              return alert(rs);
          });
      }

        // 弹出对话框
 function newDialog(jobject,hasSelectData) {
            function _content() {
                var container = jquery("<div class='container'></div>");
                var top = tabUtil.container.create();
                var tab = tabUtil.bar.create();
                var content = tabUtil.content.create();
                top.append(tab);
                top.append(content);
                
                container.append(top);
                var user_info = jquery("<div id='user_info' style='height:30%' class='bot'></div>");
                container.append(user_info);
                return container.prop("outerHTML");
            }
			jobject.append(_content());
			modelUtil.load({}, function (data) {
                initTree(data);
            });
			
        };
        
        