(function(){
	var defaults = {};
	window.Stellar = window.Stellar || {};
	var namespace = window.Stellar;
	
	var model = new namespace.UserInfoModel(defaults);
	var userInfoView = new namespace.UserInfoView({model: model, el: $("#user_info_containter")});

}());