/*
    描述: 对layer弹出框的进一步封装
    author:micometer
    telephone:15882002328
*/

! function (e) {
    if (!e.layer) {
        return alert("请加载layer组件");
    }
    var jq = e.$ ? e.$ : e.JQuery;
    if (!jq) {
        return alert("请加载Jquery组件");
    }
    
    var kwtlayer = e.kwtlayer ? e.kwtlayer : {};
    var operare = e.layer;

    function titelcss() {
        return "font-size:14px;font-weight:400;"
    }
    function baseMsg(info, config) {
        var _config = {
            title: [info, titelcss()],
            area: ["300px", "160px"],
        }
        if (config) {
            jq.extend(true, _config, config);
        }
        return _config;
    }

    function _close(index, callback, options) {
        operare.close(index);
        return callback && callback(options);
    }

    ! function () {
        //显示指定弹出框
        //index 弹出框索引值
        kwtlayer.wshow = function (index) {
            jq("#" + "layui-layer" + index).css("display", "block");
        };

        //隐藏指定弹出框
        //index 弹出框索引值
        kwtlayer.whide = function (index) {
            jq("#" + "layui-layer" + index).css("display", "none");
        }

        //关闭自定弹出框
        //index 弹出框索引值
        kwtlayer.wclose = function (index) {
            operare.close(index);
        }
        
        //关闭所有弹出框
        kwtlayer.wcloseAll = function () {
            operare.closeAll();
        }
        //获取弹出框
        //index 弹出框的索引值,每次创建后自动返回
        kwtlayer.getLayer = function (index) {
            return jq("#" + "layui-layer" + index);
        }

        //弹出确认信息框
        //msg 提示信息
        //callback 操作回调
        //config 用户自定义配置,优先级最高
        kwtlayer.confirm = function (msg, callback, config) {
            var _config = baseMsg("确认", config);
            var msg = "<center>" + msg + "</center>";
            var _index = operare.confirm(msg, _config,
                function () {
                    _close(_index, callback, true)
                },
                function () {
                    _close(_index, callback, false)
                });
        }

        //弹出提示框
        //msg 提示信息
        //callback 确定操作后的回调
        //config 用户自定义配置,优先级最高
        kwtlayer.alert = function (msg, callback, config) {
            var _config = baseMsg("提示", config),
                msg = "<center>" + msg + "</center>",
                _index = operare.alert(msg, _config, function () {
                    _close(_index, callback, true)
                });
            return _index;
        }

        //弹出对话框
        //header 对话框表头
        //content 对话框中的html内容体
        //callback 默认的 确定,取消操作回调
        //config 用户自定义配置,优先级最高
        kwtlayer.dialog = function (header, content, callback, config) {
            var _index = null;
            var _config = baseMsg(header, config);
            _config.content = content;
            _config.type = 1;

            if(!_config.area) _config.area = ["500px", "340px"];
            if (!_config.btn && _config.noBtn) {
                return operare.open(_config)
            }

            if (!_config.btn) {
                _config.btn = ["确定", "取消"];
                _config.yes = function () {
                    _close(_index, callback, true)
                };
                _config.bt2 = function () {
                    _close(_index, callback, false)
                };
            }
            _index = operare.open(_config);
            return _index;
        }

        //数据请求,转圈提示框
        //callback 转圈启动后的回调,用户需要在回调中自己确定什么是转圈提示框
        kwtlayer.load = function (callback) {
            var index = operare.open({
                icon: 1,
                type: 3,
            });

            return callback && callback(function () {
                operare.close(index);
            });
        }
        //ifram信息框
        //url iframe请求的网页地址,
        //header 信息框表头
        //config 自己写的配置,优先级最高
        kwtlayer.iframe = function (url,header,config) {
            var _config = baseMsg(header?header:"", config);
            _config.type = 2;
            _config.area = ["500px", "340px"];
            return operare.open(_config);
        }

        //某个位置显示tips提示信息
        //content 显示的内容,
        //parentId 显示位置容器的Id号
        //config 自己写的配置,优先级最高
        kwtlayer.tips = function (content, parentId,config) {
            var _config = {
                type: 4,
                shade: 0,
                area: ["200px", "40px"],
                shadeClose: true,
                time: 2000,
                content: ["<div style='line-height:40px'>" + content + "</div>", parentId]
            }
            if(config)jq.extend(true,_config,config);
            return operare.open(_config);
        }

        //右下角,显示重要的提示信息
        //content 显示的内容,
        //time 延迟多久关闭,默认 3000
        //config 自己写的配置,优先级最高
        kwtlayer.showImp = function (content, time,config) {
            var _config = {
                type: 0,
                offset: 'rb',
                content: "<center>" + content + "</center>",
                anim: 2,
                shade: 0,
                time: time ? time : 3000,
                resize: false,
            };
            if(config)jq.extend(true,_config,config);
            return operare.open(_config);
        }
        //显示提示新
        //msg 提示的消息
        kwtlayer.show = function (msg) {
            return operare.msg("<center>" + msg + "</center>", {
                time: 1000
            })
        }
    }()
    e.kwtlayer = kwtlayer;
}(window);