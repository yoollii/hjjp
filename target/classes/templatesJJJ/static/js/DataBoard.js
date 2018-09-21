
var personalUnfinAll=0;
var personalfinAll=0;
var deptUnfinAll=0;
var deptfinAll=0;
var rolefinAll=0;
var roleUnfinAll=0;
var allfinAll=0;
var allUnfinAll=0;

$(function(){
	jQuery.ajax({
		url:"myUserTask/getDataBoard",
		cache:false,
		type : 'GET',
		dataType : "json",
		success:function(data1){
			var tb1 = $("#tb1");
			var indextr = $("#indextr");
			// 没有表单
			if (!data1) {
				var tr = $("<tr><td align='center' colspan='9'><strong>没有表单</strong></td></tr>")
				tb1.append(tr);
			}
			else {
			// 循环表单内容
			for(var i=0;i<data1.length;i++){
				var tr = $("<tr></tr>")
				var td1 = $("<td>"+data1[i].formName+"</td>");
				tr.append(td1);
				 personalUnfinAll+=data1[i].personalUnfinNum;
				 personalfinAll+=data1[i].personalfinNum;
				 deptUnfinAll+=data1[i].deptUnfinNum;
				 deptfinAll+=data1[i].deptfinNum;
				 rolefinAll+=data1[i].rolefinNum;
				 roleUnfinAll+=data1[i].roleUnfinNum;
				 allfinAll+=data1[i].allfinNum;
				 allUnfinAll+=data1[i].allUnfinNum;
				var td = $("<td><a href='myUserTask/myTasklist?formCode="+data1[i].formCode+"&state=1'>"+data1[i].personalUnfinNum+"</a></td>"
						 +"<td><a href='myUserTask/myTasklist?formCode="+data1[i].formCode+"&state=2'>"+data1[i].personalfinNum+"</a></td>"
						 +"<td><a href='myUserTask/myTasklist?formCode="+data1[i].formCode+"&state=3'>"+data1[i].deptUnfinNum+"</a></td>"
						 +"<td><a href='myUserTask/myTasklist?formCode="+data1[i].formCode+"&state=4'>"+data1[i].deptfinNum+"</a></td>"
						 +"<td><a href='myUserTask/myTasklist?formCode="+data1[i].formCode+"&state=5'>"+data1[i].rolefinNum+"</a></td>"
						 +"<td><a href='myUserTask/myTasklist?formCode="+data1[i].formCode+"&state=6'>"+data1[i].roleUnfinNum+"</a></td>"
						 +"<td><a href='myUserTask/myTasklist?formCode="+data1[i].formCode+"&state=7'>"+data1[i].allfinNum+"</a></td>"
						 +"<td><a href='myUserTask/myTasklist?formCode="+data1[i].formCode+"&state=8'>"+data1[i].allUnfinNum+"</a></td>"
				);
				tr.append(td);
				tb1.append(tr);
				}
			}
			var td2=$("<td>"+personalUnfinAll+"</td>"
					+"<td>"+personalfinAll+"</td>"
					+"<td>"+deptUnfinAll+"</td>"
					+"<td>"+deptfinAll+"</td>"
					+"<td>"+rolefinAll+"</td>"
					+"<td>"+roleUnfinAll+"</td>"
					+"<td>"+allfinAll+"</td>"
					+"<td>"+allUnfinAll+"</td>"
			);
			indextr.append(td2);
			
		}
	});

});