/*
Dependency:
1. /lib/utility.js
*/
(function(){
   var namespace = window.Stellar || {};
   namespace.UserInfoView = Backbone.View.extend({
      initialize : function(){
         var that = this,
             username_fld,
             pwd_fld;
         username_fld = this.$el.find("input[name='username']");
         pwd_fld = this.$el.find("input[name='password']");
         //this.model.bind("inputFocusOut", this.validate);
         this.$el.on("focusout", "input", function(){
             that.validate();
         });

         this.$el.on("click", "button", function(){
            //that.model.get("userInfo").set("username", username_fld.val());            
            //that.model.get("userInfo").set("password", password_fld.val());
            that.model.set("userInfo", {
               username: username_fld.val(),
               password: pwd_fld.val()
            });

         });
      },

      validate : function() {
         var that = this;
         var inputs = this.$el.find("input");
         
         inputs.each(function(index, input){
            if(!$(input).val().trim()){
               that.$el.find("#hint").show();
            }
         });
      }



   });

}());
