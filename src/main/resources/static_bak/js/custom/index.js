jQuery(document).ready(function(){
								
	///// TRANSFORM CHECKBOX /////							
	jQuery('input:checkbox').uniform();
	
	//后台反馈的消息处理
	var msg=jQuery('#msg').val();
	if(msg!=''){
		if(msg=='用户名密码不匹配 '){
			//填充msg
			jQuery('#username').val(jQuery('#loginUser').val());
			jQuery('.nopassword .loginmsg').text(msg);			
			jQuery('.nopassword').fadeIn().find('.userlogged h4, .userlogged a span').text(jQuery('#username').val());
			jQuery('.nousername,.username').hide();
		}else{
			//填充msg
			jQuery('.nousername .loginmsg').text(msg);
			jQuery('.nousername').fadeIn();
			jQuery('.nopassword').hide();
		}
	}
	
	///// LOGIN FORM SUBMIT /////
	jQuery('#login').submit(function(){
	
		if(jQuery('#username').val() == '' && jQuery('#password').val() == '') {
			jQuery('.nousername').fadeIn();
			jQuery('.nopassword').hide();
			return false;	
		}
		if(jQuery('#username').val() != '' && jQuery('#password').val() == '') {
			jQuery('.nopassword').fadeIn().find('.userlogged h4, .userlogged a span').text(jQuery('#username').val());
			jQuery('.nousername,.username').hide();
			return false;;
		}
	});
	
	///// ADD PLACEHOLDER /////
	jQuery('#username').attr('placeholder','登录名');
	jQuery('#password').attr('placeholder','密码');
});
