/*
Dependency: 
1 jQuery

*/
var Utility = {};

Utility = {

	direct_to : function(url) {
		if(url){
			window.location.href = url;
		} 
		else {
			return false;
		}
	},

	doAjax : function(options) {
		var default_opt = {
			type : "post",
			success : function(response) {

			},
			async : true,
			error : function(response) {

			}
		}
		options = options || {};
		options = $.extend(default_opt, options);

		$.ajax(options);
	},

	parseJSON : function(jsonString) {
		jsonString = jsonString || "";
		
		return JSON.parseJSON(jsonString);
	},

	toJSON : function(obj) {
		obj = obj || {};
		
		return JSON.toJSON(obj);
	}
}