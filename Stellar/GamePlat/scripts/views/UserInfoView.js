/*
Dependency:
1. /lib/utility.js
*/

(function(global){
   //need the global namespace here, like Stellar.user_info
   //Stellar.user_info_path

   global.user_info_view = {};
   var u_info_view = global.user_info_view;

   u_info_view = {
   		init : function(){

   		},
   	    send_signup_info : function(userInfo){
   	    	var that = this;
   	    	var options = {
   	    		url : "blahblahblah",
   	    		type : "POST",
   	    		data : userInfo || {},
   	    	}
   	    	options.success = function(res){
   	    		if(res && res.success) {
   	    			that.direct_to(options.url);
   	    		}
   	    	}
   	    	optoins.error = function(res){
   	    		console.log()
   	    	}
   	    	Utility.doAjax(options);
   	    },
   	    send_login_info : function(user_login_info){

   	    },
   	    direct_to : function(){
   	    	Utility.direct_to(url);
   	    },
   	    set_user_info : function(user_info){

   	    },


   } 

}(window))