$(function(){
		$('input[placeholder]').placeholder();
		$("#w_container").unmask();
		
		$(document).ajaxStart(function() {
			$("#w_container").mask("Waiting...");
		}).ajaxStop(function() {
			$("#w_container").unmask();
		});
});

var common = function() {
	function colNamesI18n(colNames) {
		//i18n
		for(var i in colNames) {
			var name = colNames[i];
			var name2 = I18nUtil.getMessageByCode(name);
			if(name2)
				colNames[i] = name2;
		}
	}
	return {
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
			},
			grid : function(_settings) {
				var settings = {};
				var defaultSetting = {
						url:"jqrid",
		                datatype: "json",
		                height: 250,
		                viewrecords:true,
		                rowNum:10,
		                multiselect:true,
		                rownumbers:true,
		                rowList:[10,20,30],
		        };
				
				$.extend(settings,defaultSetting,_settings);
				//i18n
				colNamesI18n(settings.colNames);
				
				//get param
				var $grid =  $(settings.id) ;
				settings.postData = getParam();
				$grid.jqGrid(settings);
				
				//register event
				$(settings.queryform + " button[name=search]").click(function() {
					var param = getParam();
					$grid.jqGrid("setGridParam", { url:"jqrid",postData:param}).trigger("reloadGrid", [{page:1}]);
				});
				
				$(settings.queryform + " button[name=reset]").click(function() {
					$(settings.queryform).resetForm();
					$(settings.queryform).clearForm(true);
					//reset multi select
					$("select[multiple]").each(function() {
						$(this).find("option").each(function() {
				                $(this).prop('selected', false);
				        });
				        $(this).multiselect('refresh');
					});
		           
				});
				
				$(settings.queryform + " button[name=export]").on("click",function() {
					var param = getParam();
					var json = {
							queryName:settings.queryName,
							fileName:settings.fileName,
							sidx:$grid.jqGrid("getGridParam","sortname"),
							sord:$grid.jqGrid("getGridParam","sortorder"),
							colNames:$grid.jqGrid("getGridParam","colNames"),
							colModel:$grid.jqGrid("getGridParam","colModel")
					};
					param.jqridJson = JSON.stringify(json); 
					common.postSubmit("jqrid/export",param);
				});
				
				function getParam() {
					return $(settings.queryform).form2json({allowEmptyMultiVal:true});
				}
				return $grid;
			},
			exportExcel:function(setting, param) {
				var colModel = setting.colModel;
			
				var modelTemplate = {hidden:false,width:100};
				
				colNamesI18n(setting.colNames);
				
				var json = {
						queryName:setting.queryName,
						fileName:setting.fileName,
						sidx:"",
						sord:"",
						colNames:setting.colNames,
						colModel:(function() {
							var _colModel = [];
							for(var i in colModel) {
								var model = {};
								$.extend(model,modelTemplate,model);
								model['index'] = model['name'] = colModel[i];
								_colModel.push(model);
							}
							return _colModel;
						})()
				};
				param.jqridJson = JSON.stringify(json); 
				common.postSubmit("jqrid/export",param);
			},
			getRootPath :function(){  
			    //获取当前网址，如： http://localhost:8083/proj/meun.jsp  
			    var curWwwPath = window.document.location.href;  
			    //获取主机地址之后的目录，如： proj/meun.jsp  
			    var pathName = window.document.location.pathname;  
			    var pos = curWwwPath.indexOf(pathName);  
			    //获取主机地址，如： http://localhost:8083  
			    var localhostPath = curWwwPath.substring(0, pos);  
			    //获取带"/"的项目名，如：/proj  
			    var projectName = pathName.substring(0, pathName.substr(1).indexOf('/')+1);  
			    return(localhostPath + projectName);  
			},
			getBase :function(){  
			    var pathName = window.document.location.pathname;  
			    //获取带"/"的项目名，如：/proj  
			    var projectName = pathName.substring(0, pathName.substr(1).indexOf('/')+1);  
			    return projectName;  
			},
			/**
			 * 获取字典键值对
			 * {"GB/T 2659-2000.0":{"004":"阿富汗","008":"阿尔巴尼亚共和国"}}
			 * @param {} key
			 * @return {}
			 */
			formatDic4Map:function(keyOrAligns) {
				var map;
				if((map =dicMap[keyOrAligns.toUpperCase()]) || 
					(map =dicMap[keyOrAligns.toLowerCase()]) || 
					(map =dicMap[aligns[keyOrAligns.toLowerCase()]]) || 
					(map =dicMap[aligns[keyOrAligns.toUpperCase()]])) {
				}
				return map;
			},
			formatDic4Text:function(keyOrAligns,value) {
				if(!value)
					return "";
				var trueValue = "";
				var map = formatDic4Map(keyOrAligns);
				
				if(map) {
					 if(map[value])
					 	trueValue = map[value];
				}
				if(trueValue)
					return trueValue;
				return value;
			}
	};
}(); 
