var common = function() {
	var utils = {
			/**
			  * 模拟POST提交跳转
			  * @param url 跳转url	  ("xx.do")
			  * @param data 传递参数 ({"username":"xx"})
			  */
			postSubmit:function(url,data) {
				var form = jQuery('<form  action="'+url+'" method="POST"></form>');	
				if(data){
					for(var i in data) {
						var $hidden = $('<input type="hidden" name="' + i + '" />');
						$hidden.val(data[i]);
						$hidden.appendTo(form);
					}			
				}		
				jQuery(form).appendTo('body');
				form.submit();
			}
	};
	return utils;
}(); 
