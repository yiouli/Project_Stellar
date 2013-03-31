/*
Dependency:
1. /lib/utility.js
*/
(function(){
   window.Stellar = window.Stellar || {};
   var namespace = window.Stellar;
   namespace.UserInfoView = Backbone.View.extend({
      initialize : function(){
         var that = this,
             username_fld,
             pwd_fld;
         username_fld = this.$el.find("input[name='username']");
         pwd_fld = this.$el.find("input[name='password']");
         //this.model.bind("inputFocusOut", this.validate);
         this.$el.on("focusout", "input", that.validate);

         this.$el.on("click", "button", function(){
            that.model.set("userInfo", {
               username: username_fld.val(),
               password: pwd_fld.val()
            });

         });
      },

      try : function(){
         
      },

      validate : function() {
         var func_map = {
            "username" : function($fld) {
               return !!$fld.val();   //username field should not be empty
            },
            "password" : function($fld) {
               return !!$fld.val() && $fld.val().length >= 8;  //password field should not be empty and char length should larger than 8
            }
         }
         console.log(func_map[$(this).attr("name")]($(this)));
         
      }



   });

}());
