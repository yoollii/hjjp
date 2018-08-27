
var KisBpmMultiInstanceSelectCtrl111 = [ '$scope', function($scope) {
	
	console.log('request url:'+KISBPM.URL.getSelect());
	new Ajax.Request(KISBPM.URL.getSelect(), {
		asynchronous: false,
		method: 'get',
		onSuccess: function(result){
			$scope.property.options = JSON.parse(result.responseText);
		},
		onFailure: function(){
			console.log("get select value error");
		}
	});
	
    if ($scope.property.value == undefined && $scope.property.value == null)
    {
    	console.log('$scope.property value is null ');
    	$scope.property.value = 'no';
    }
        
    $scope.multiInstanceSelectChanged = function() {
    	console.log('$scope.property multiInstanceSelectChanged :'+$scope.property);
    	$scope.updatePropertyInModel($scope.property);
    };
}];