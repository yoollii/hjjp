$(function(){				//超出范围后隐藏并使用...,获取焦点后显示详细内容
	$("table").css("table-layout","fixed");
	var tipsindex;
	$("td").each(function(){
		if($(this).siblings().text()=="公钥" || $(this).siblings().text()=="注册码"){
        	//$(this).css({"white-space":"normal"});
        }else{
        	$(this).css({"text-overflow":"ellipsis","overflow":"hidden","white-space":"nowrap"});
        }
	})
        $("td").mouseenter(function(){
        		if (this.offsetWidth < this.scrollWidth) {
        			if($(this).children().length!=0){
                		return;
                	}
        			if($(this).siblings().text()=="公钥" || $(this).siblings().text()=="注册码"){
        				$(this).css({"white-space":"normal"});
                		return;
                	}
                    var text = $(this).text();
                    tipsindex=layer.tips(text, this,{  
                        maxWidth:700,
                        tips:[3,"#F0882C"],
                        time:0
                    });
                }
        }).mouseleave(function(){  
        layer.close(tipsindex);
    })
})

    