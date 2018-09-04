// 名字验证 只能输入汉字英文数字下划线
$.validator.addMethod("nameVal", function(value, element) {
	 var reg=new RegExp("^[\u4E00-\u9FA5A-Za-z0-9_-]+$");
	 return this.optional(element) || (reg.test(value));
 }, "*请输入英文/汉字/数字/下划线/横线");
// 特殊字符验证 不允许输入%&#',;=?$符号
 $.validator.addMethod("specia_char", function(value, element) {
	 var reg=new RegExp("[^~%&#',;=?$\x22]+");
	 return this.optional(element) || (reg.test(value));
 }, "*请输入汉字/英文/数字/下划线");
 // 手机号码验证
 $.validator.addMethod("tel", function(value, element) {
	 var reg= /^[1][3,4,5,6,7,8,9][0-9]{9}$/;  
	 var a= reg.test(value);
	 return this.optional(element) || (reg.test(value));
 }, "*电话格式不正确");
 // 邮箱验证
 $.validator.addMethod("mailVal", function(value, element) {
	 var reg=new RegExp("^[a-zA-Z0-9_-]+@[a-zA-Z0-9_-]+(\.[a-zA-Z0-9_-]+)+$/i");
	 return this.optional(element) || (reg.test(value));
 }, "*邮箱格式不正确");
 // 名字验证 英文数字下划线  没有中文
 $.validator.addMethod("nameEnVal", function(value, element) {
	 var reg=new RegExp("^[A-Za-z0-9_]+$");
	 return this.optional(element) || (reg.test(value));
 }, "*请输入英文/数字/下划线");

 // 判断浮点型  
 jQuery.validator.addMethod("isFloat", function(value, element) {       
      return this.optional(element) || /^[-\+]?\d+(\.\d+)?$/.test(value);       
 }, "浮点数字,只能包含数字、小数点等字符"); 
 
 // 判断小于10的整型  
 jQuery.validator.addMethod("isGtInt", function(value, element) {       
      return this.optional(element) || /^([1-9]|10)$/.test(value);       
 }, "小于10的整数"); 
 
 //字符串验证 只能输入汉字英文数字下划线以及-
 $.validator.addMethod("nameVall", function(value, element) {
	 var reg=new RegExp("^[\u4E00-\u9FA5A-Za-z0-9_-]+$");
	 return true;
  }, "*请输入英文/汉字/数字/下划线/横线");
 
 
 
 