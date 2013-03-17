/*
Dependency:
1. /lib/utility.js
*/
(function(){
   window.Stellar = window.Stellar || {};
   var namespace = window.Stellar;
   namespace.UserInfoView = BackBone.View.extend({
      initialize : function(){
         //this.model.bind("inputFocusOut", this.validate);
         this.el.on("focusout", )
      },

      validate : function() {
         var inputs = el.find("input");
         inputs.each(function(index, input){
            if(!input.val().trim()){
               el.find("#hint").show();
            }
         });
      }

   });

}());
