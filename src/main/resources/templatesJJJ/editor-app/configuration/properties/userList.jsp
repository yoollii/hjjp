<%@page contentType="text/html; charset=UTF-8"%>
<%@include file="../../../inc/inc.jsp"%>  

<form id="userList" name="userList" action="editDo" method="post" >
<table cellspacing="0" cellpadding="0" style="font-size:14px;width:90%;margin:20px auto">           
<tr><td >
</td><td > 
<input type="hidden" name="id" id="id" value=""/>
<input type="hidden" name="crtime" id="crtime" value="2017-01-01 00:00:00"/>
<input type="hidden" name="m" id="m" value=""/>
</td></tr>
<tr class="FormFontTr">
<td align="right" style="width: 15%">用户名</td>
<td align="right" style="width: 15%">邮箱</td>
<td align="right" style="width: 15%">电话</td>
<td align="right" style="width: 15%">生日</td>
<td align="right" style="width: 15%">状态</td>

</tr>
<tr class="FormFontTr">


</tr>

</table>
<div id="msg" align="center" style="color:red"></div>
</form>