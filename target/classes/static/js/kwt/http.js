! function (e) {
    var kwtHttp = e.kwtHttp ? e.kwtHttp : {};
    var jq = e.$ ? e.$ : e.JQuery;
    function resultFilter(data) {
    	if(typeof data == "string"){
    		try{
    			data = JSON.parse(data);
    		}catch(e){
    			return {
    				state:true,
    				data:data,
    			}
    		}
    	}
    	
    	if(data.rs){
    		return {
    			state:true,
    			data:data,
    		}
    	}
    	
    	return {
    		state:false,
    		data:[],
	    	msg:data.msg
    	}
    }

    function getUrl(url) {
        return jq.trim((kwtHttp.baseUrl + url));
    }

    function extend(_new, old) {
        if (!jq) {
            alert("请加载jquery");
            return _new;
        }
        return jq.extend(true, _new, old);
    }

    function baseOperate(_type, url, data, success, config) {
        var _config = {
            url: getUrl(url),
            data: data ? data : {},
            type: _type,
            cache:false,
            success: function (data) {
                var _data = kwtHttp.resultFilter(data);
                if (_data.state) {
                
                    return typeof success == "function" && success(_data.data);
                }
                kwtlayer.alert(_data.msg ? _data.msg : "获取数据失败");
            }
        }
        if (_type == "POST") {
//            _config["contentType"] = "application/json;charset=utf-8;"
        }
        jq.extend(_config, config);
        if (!_config.error && kwtHttp.error) {
            _config.error = kwtHttp.error;
        }

        jq.ajax(_config);
    }


    //功能列表
    kwtHttp.debug = false; //调试模式
    kwtHttp.baseUrl = ""; //页面根地址
    /*
        对服务器返回的数据,进行统一的预处理函数
        data:后台返回数据,
        return{
            state:"数据结果正常的状态值",
            data:处理后的数据,
            msg:"提示信息",
        }
    */
    kwtHttp.resultFilter = resultFilter;

    /*
        网络请求异常的回调
    */
    kwtHttp.error = function (error) {
        return kwtHttp.debug && console.log(error);
    }
    /*
        url:"请求的页面地址",
        data:"请求数据体",
        success:"成功时的回调",
        config:额外配置参数
    */
    kwtHttp.post = function (url, data, success, config) {
        baseOperate("POST", url, data, success, config);
    }
    kwtHttp.get = function (url, data, success, config) {
        baseOperate("GET", url, data, success, config);
    }

    /*
        创建一个新的kwtHttp对象
    */
    kwtHttp.new = function () {
        var _new = {};
        jq.extend(true, _new, kwtHttp);
        return _new;
    }
    window.kwtHttp = kwtHttp;
}(window)