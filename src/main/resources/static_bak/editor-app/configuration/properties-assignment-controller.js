/*
 * Activiti Modeler component part of the Activiti project
 * Copyright 2005-2014 Alfresco Software, Ltd. All rights reserved.
 * 
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.

 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301  USA
 */
/*普通js相关*/
function zTreeOnClick(event, treeId, treeNode) {
	//选中节点时后台查出相应的数据
	alert("选中树节点的触发事件");

};

var setting = {
		callback : {
			onClick : zTreeOnClick,
		},
		data : {
			simpleData : {
				enable : true
			},
		key: {
			name: "alias"
		}

		}
	};





/*
 * Assignment
 */
var KisBpmAssignmentCtrl = [ '$scope', '$modal', function($scope, $modal) {	
	$scope.opened = function(){
		alert(1);
	}
    var opts = {
        template:  'editor-app/configuration/properties/assignment-popup.html?version=' + Date.now(),
        scope: $scope
    };
    $modal(opts);
}];


var KisBpmAssignmentPopupCtrl = [ '$scope',"$http", function($scope,$http) {	
	
	//$scope.aa="";
	$scope.xxx=function(){
		var index = layer.open({
			type:2,
			title: '预选操作团队或者操作者',
			content:"loadForm/choseCandidate",
			area:["800px","500px"],
			btn: ['确定','取消'],
			yes:function(index, layero){
//				  //显示名拼装
		    	  var tname="";
		          var f=0;
		          jQuery("#layui-layer-iframe"+layer.index).contents().find("div[id^='selected_div_user_']").each(function(){
		    		  var truename=jQuery(this).text();
		    		  if(f>0){
		    			  tname+=",";
		    		  }
		    		  tname+=truename;
		    		  f++;
		    		  //alert(truename);
		    	  });
				var val=jQuery("#layui-layer-iframe"+layer.index).contents().find("#selectedCollections").val();
				//显示的名称s
				//alert(val);
				if(val==""||val==null){
					$scope.assignment.candidateGroups = [{"value":" "}];	
				}else{
					$scope.assignment.candidateGroups = [{"value":val}];
				}
					
				//$scope.assignment.candidateGroups = [{"value":val}];	
				$scope.assignment.candidateTrueUsers =tname;
				$scope.$apply();
		      	layer.close(layer.index);	
			},
			success:function(){
				var idStr =jQuery.trim($scope.assignment.candidateGroups[0].value);
				var labeStr = $scope.assignment.candidateTrueUsers;
				jQuery("#layui-layer-iframe"+layer.index).contents().find("#selectedCollections").val(idStr);
				jQuery("#layui-layer-iframe"+layer.index).contents().find("#forDate").val(labeStr);	
			}
		});
	}

	
	
    // Put json representing assignment on scope
    if ($scope.property.value !== undefined && $scope.property.value !== null
        && $scope.property.value.assignment !== undefined
        && $scope.property.value.assignment !== null) 
    {
        $scope.assignment = $scope.property.value.assignment;
    } else {
        $scope.assignment = {};
    }

    if ($scope.assignment.candidateUsers == undefined || $scope.assignment.candidateUsers.length == 0)
    {
    	$scope.assignment.candidateUsers = [{value: ''}];
    }
    
    if ($scope.assignment.candidateTrueUsers == undefined || $scope.assignment.candidateTrueUsers.length == 0)
    {
    	$scope.assignment.candidateTrueUsers ="";
    }
    
    // Click handler for + button after enum value
    var userValueIndex = 1;
    $scope.addCandidateUserValue = function(index) {
        $scope.assignment.candidateUsers.splice(index + 1, 0, {value: 'value ' + userValueIndex++});
    };

    // Click handler for - button after enum value
    $scope.removeCandidateUserValue = function(index) {
        $scope.assignment.candidateUsers.splice(index, 1);
    };
    
    if ($scope.assignment.candidateGroups == undefined || $scope.assignment.candidateGroups.length == 0)
    {
    	$scope.assignment.candidateGroups = [{value: ''}];
    }
    
    var groupValueIndex = 1;
    $scope.addCandidateGroupValue = function(index) {
        $scope.assignment.candidateGroups.splice(index + 1, 0, {value: 'value ' + groupValueIndex++});
    };

    // Click handler for - button after enum value
    $scope.removeCandidateGroupValue = function(index) {
        $scope.assignment.candidateGroups.splice(index, 1);
    };

    $scope.save = function() {

        $scope.property.value = {};
        handleAssignmentInput($scope);
        $scope.property.value.assignment = $scope.assignment;
        $scope.updatePropertyInModel($scope.property);
        $scope.close();
    };

    // Close button handler
    $scope.close = function() {
    	handleAssignmentInput($scope);
    	$scope.property.mode = 'read';
    	$scope.$hide();
    };
    
    var handleAssignmentInput = function($scope) {
    	if ($scope.assignment.candidateUsers)
    	{
	    	var emptyUsers = true;
	    	var toRemoveIndexes = [];
	        for (var i = 0; i < $scope.assignment.candidateUsers.length; i++)
	        {
	        	if ($scope.assignment.candidateUsers[i].value != '')
	        	{
	        		emptyUsers = false;
	        	}
	        	else
	        	{
	        		toRemoveIndexes[toRemoveIndexes.length] = i;
	        	}
	        }
	        
	        for (var i = 0; i < toRemoveIndexes.length; i++)
	        {
	        	$scope.assignment.candidateUsers.splice(toRemoveIndexes[i], 1);
	        }
	        
	        if (emptyUsers)
	        {
	        	$scope.assignment.candidateUsers = undefined;
	        }
    	}
        
    	if ($scope.assignment.candidateGroups)
    	{
	        var emptyGroups = true;
	        var toRemoveIndexes = [];
	        for (var i = 0; i < $scope.assignment.candidateGroups.length; i++)
	        {
	        	if ($scope.assignment.candidateGroups[i].value != '')
	        	{
	        		emptyGroups = false;
	        	}
	        	else
	        	{
	        		toRemoveIndexes[toRemoveIndexes.length] = i;
	        	}
	        }
	        
	        for (var i = 0; i < toRemoveIndexes.length; i++)
	        {
	        	$scope.assignment.candidateGroups.splice(toRemoveIndexes[i], 1);
	        }
	        
	        if (emptyGroups)
	        {
	        	$scope.assignment.candidateGroups = undefined;
	        }
    	}
    };
}];