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
         this.$el.on("focusout", "input", function(){
            that.validate($(this));
         });//that.validate);

         this.$el.on("click", "button", function(){  //submit
            if(that.valid){
               that.model.set("userInfo", {
                  username: username_fld.val(),
                  password: pwd_fld.val()
               });
               that.model.trigger("submitLogin");
            }
         });
      },

      try : function(){
         
      },

      validate : function($ele) {
         var valid = true;
         var validObj;
         var func_map = {
            "username" : function($fld) {
               return {
                  isValid : !!$fld.val(),   //username field should not be empty
                  errMsg : "Please fill in your username"
               }
            },
            "password" : function($fld) {
               return {
                  isValid : !!$fld.val() && $fld.val().length >= 8,  //password field should not be empty and char length should larger than 8,
                  errMsg : !!$fld.val() ? "Password length should not be less than 8 characters" : "Please fill in your password"
               }
            }

         }
         validObj = func_map[$ele.attr("name")]($ele);
         this.valid = typeof(validObj.isValid)==='undefined' ? false : validObj.isValid;
         if (!validObj.isValid) {
               $ele.next(".valid-error").html(validObj.errMsg).show();
            }
         else {
               $ele.next(".valid-error").html("").hide();
         }
        // console.log(func_map[$(this).attr("name")]($(this)));
         
      }



   });

}());
