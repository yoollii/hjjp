//用户生产adminv tableview
function tableview() {
    var self = this;
    this.config = {
        style:{
            width:"100%",
            height:"100%"
        },
        css:"",
        //生产标签名
        genLabel:function(key,val){
            return key
        },

        //生产内容 html
        genContent:function(key,val){

        },

        //添加内容html 成功后的回调
        addCallback:function(key,val){

        }
    }

    //adminv 容器
    this.container = {
        create: function () {
            var div = $("<div class='tab_container'></div>");
            div.addClass("ui-tabs ui-widget ui-widget-content ui-corner-all");
            div.css("border","none");
            div.css("border-bottom","2px solid #FB9337")
            return div;
        },
        get: function () {
            return $(".tab_container");
        }
    }
    //tab bar
    this.bar = {
        create: function () {
            var ul = $("<ul class='tabs-nav'></ul>");
            ul.addClass("ui-tabs-nav ui-helper-reset ui-helper-clearfix ui-widget-header ui-corner-all");
            return ul;
        },
        get: function () {
            return $(".tabs-nav");
        },
        genId: function (index) {
            return self.genId(index);
        },
        hide: function (id) {
            if (id) {
                return $("[data_id='" + id + "']").removeClass("ui-tabs-selected ui-state-active");
            }
            this.get().find("li").removeClass("ui-tabs-selected ui-state-active");
        },
        select: function (id) {
            $("[data_id='" + id + "']").addClass("ui-tabs-selected ui-state-active");
        },
        add: function (label) {
            var id = this.genId(this.get().children().length + 1);
            var li = $("<li class='tab_item'></li>");
            li.addClass("ui-state-default ui-corner-top");
            li.attr("data_id", id)

            var alink = $("<a></a>");
            alink.append(label);

            li.append(alink);
            this.get().append(li);

            li.click(function () {
                self.hide();
                self.select(id);
            });
            return id;
        }
    }
    //内容容器
    this.content = {
        create: function () {
            var div = $("<div class='tabview'><div>");
            div.css(self.config.style);
            return div;
        },
        get: function () {
            return $(".tabview");
        },
        hide: function (id) {
            if (id) {
                return $("#" + id).addClass("ui-tabs-hide");
            }
            this.get().find(".ui-tabs-panel").addClass("ui-tabs-hide");
        },
        select: function (id) {
            $("#" + id).removeClass("ui-tabs-hide");
        },
        add: function (id, content, callback) {
            var div = $("<div></div>");
            div.attr("id", id);
            div.addClass("ui-tabs-panel ui-widget-content ui-corner-bottom ui-tabs-hide");
            div.append($(content));
            this.get().append(div);
            return callback && callback(div);
        }
    }

    this.genId = function (index) {
        return "tabs-" + index;
    }

    //添加一个元素
    this.add = function (title, content, callback) {
        if(this.tabExist(title)){
            return console.log("该标签已经被使用,请使用其他名称")
        }
        this.content.add(this.bar.add(title), content, callback);
    }

    //取消选中状态
    this.hide = function (labelId) {
        this.bar.hide(labelId);
        this.content.hide(labelId);
    }

    //选中某个标签
    this.select = function (labelId) {
        this.bar.select(labelId);
        this.content.select(labelId);
    }

    //通过标签名获取id号
    this.getIdByLabel = function (label) {
        var lis = this.bar.get().find("a");
        for (var idx=0;idx<lis.length; idx++) {
            var cell = $(lis[idx]);
            if (cell.text() == label) {
                return cell.parent().eq(0).attr("data_id");
            }
        }
        return null;
    }

    this.tabExist = function(label,isId){
        if(isId){
            return $("#"+label).length>0;
        }
        return this.getIdByLabel(label) !=null;
    }
    //自动增加table
    //data = {key:value}
    //callback 数据初始化成功后的回调
    this.init=function(dataMap,callback){
        function addCall(key,val){
            return function(){
                self.config.addCallback(key,val)
            }
        }
        for(var key in dataMap){
            var label = this.config.genLabel(key,dataMap[key]);
            var content = this.config.genContent(key,dataMap[key]);
            this.add(label,content,addCall(key,dataMap[key]))
        }
        return callback && callback();
    }
    return this;
}
