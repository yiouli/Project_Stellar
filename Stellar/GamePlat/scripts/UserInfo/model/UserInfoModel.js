//Dependency Backbone, Stellar, Utility

(function(){
	window.Stellar = window.Stellar || {};
	var namespace = window.Stellar;
	namespace.UserInfoModel = Backbone.Model.extend({
		defaults : {
			userInfo : {}
		},

		initialize : function() {
			//url, path, userInfo
			var that = this;
			this.bind("change:userInfo", function(){
				console.log(that.get("userInfo"));
			});
         this.bind("submitLogin", this.send_login_info);
		},
		
		sendSignUpInfo : function() {
			var that = this;
			var userInfo = this.get("userInfo");
   	    	var options = {
   	    		url : that.get("path"),
   	    		type : "POST",
   	    		data : userInfo || {},
   	    	}
   	    	options.success = function(res){
   	    		if(res && res.success) {
   	    			that.direct_to(options.url);
   	    		}
   	    	}
   	    	options.error = function(res){
   	    		console.log()
   	    	}
   	    	Utility.doAjax(options);
		},
		 
		/*validateInput : function(input) {

		},*/

		directTo : function(url) {
			Utility.direct_to(this.get("url"));
		},


    send_login_info : function(){
    	var that = this;//, json_obj = $.toJSON();
      var userInfo = this.get("userInfo");
	//var userInfo = this.get("userInfo");
    	var options = {
    		url : that.get("path"),
    		type : "GET",
    		data : $.toJSON(userInfo)//user_login_info || {}, //username, password
    	}
    	options.success = function(res){
    		if(res && res.success) {
    			if(res.directTo){
    				that.direct_to(res.directTo);
    			}
    			
    		}
    	}
    	options.error = function(res){
    		console.log()
    	}
    	Utility.doAjax(options);
    },

    set_user_info : function(user_info){
    	this.set("userInfo", user_info);
    },
	});

}(window))