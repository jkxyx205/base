
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
    
<%@ taglib uri="http://www.springframework.org/tags" prefix="s"%>
<%@ taglib uri="http://rick.xu.com/selecttag" prefix="r" %>  

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<title>jQgrid Test</title>
</head>
<body>
<style type="text/css">
	form.query .tbName {
	 	padding-left:5px;
	}
	
	form.query tr {
		height: 40px;
	}
	
	form.query tr td {
		padding-right:5px;
	}
</style>
<div class="row">
	<form id="queryForm" class="query">
	
		
	    <div class="col-md-12">
		<table class="tb_validate">
			<tr>
				<td><label for="STORE_CODE"><s:message code="store_code"/>:</label></td>
				<td><input name="STORE_CODE" id="STORE_CODE" type="text" class="form-control" placeholder="<s:message code="store_code"/>"></td>
				<td class="tbName"><label for="STORE_NAME"><s:message code="store_name"/>:</label></td>
				<td><input name="STORE_NAME" id="STORE_NAME" type="text" class="form-control" placeholder="<s:message code="store_name"/>"></td>
				<td class="tbName"><label for="STATUS"><s:message code="store_status"/>:</label></td>
				<td>
					<r:select name="STATUS" key="STATUS" multiple="true"/>
				</td>
			</tr>
			<tr>
				<td><label for="OPENING_DATE"><s:message code="OPENING_DATE"/>:</label></td>
				<td><input id="OPENING_DATE" name="OPENING_DATE" size="16" type="text" value="2012/06/15" readonly class="form-control form_datetime"></td>
				<td><label for="REGION"><s:message code="REGION"/>:</label></td>
				<td>
					<r:select name="REGION" key="REGION"/>
				</td>
			</tr>
		</table>
		</div>
		<div class="col-md-12">
			<div style="float: right; margin: 5px;">
				<button name="search" type="button" class="btn btn-primary"><s:message code="submit"/></button>
				<button name="reset"  type="button" class="btn btn-primary"><s:message code="reset"/></button>
				<button authId = "store.export" name="export" type="button" class="btn btn-primary"><s:message code="export"/></button>
				<button name="autoExport" type="button" class="btn btn-primary" onclick="exportExcel();">自定义</button>
			</div>
		</div>
	</form>
	<div class="col-md-12" style="margin-top: 2px;">
		<table id="gridTable"></table>	
  			<div id="gridPager"></div>
	</div>
</div>
<script type="text/javascript">
var $gird;
$(function() {
		$('#STATUS').multiselect({
			buttonWidth:200
			//numberDisplayed:100
 		});
	
		 $gird = common.grid({
					id:"#gridTable",
					pager:"#gridPager",
					queryName:"testReport",
					fileName:"ashley",
					width:800,
					colNames:['store_code','store_name','Region','City','Open Date','STATUS','NON_TRADING_AREA','SEGMENT'], //i18n
					colModel:[
		                     {name:'STORE_CODE',index:'STORE_CODE', width:60, sorttype:"int",key:true},
		                     {name:'STORE_NAME',index:'STORE_NAME', width:100},
		                     {name:'REGION',index:'REGION', width:100}, 
		                     {name:'CITY',index:'CITY', width:100}, 
		                     {name:'OPENING_DATE',index:'OPENING_DATE', width:100}, 
		                     {name:'STATUS',index:'STATUS', width:100}, 
		                     {name:'NON_TRADING_AREA',index:'NON_TRADING_AREA', width:100}, 
		                     {name:'SEGMENT',index:'SEGMENT', width:100,hidden:true} 
		             ], 
		             sortname:"STORE_CODE",
		             sortorder:'asc',
		             multiselect:true,
		             queryform:"#queryForm" //optional
		});
			
		 

		 $("#OPENING_DATE").datetimepicker({
			 weekStart: 1,
		     todayBtn:  1,
		     autoclose: 1,
			 todayHighlight: 1,
			 startView: 2,
			 minView: 2,
			 forceParse: 0,
			 format: 'yyyy/mm/dd'
		 });

		 //alert((I18nUtil.lang));
  }); 

	function getSelectedData() {
	 //单选	
	 var store_code = $gird.jqGrid('getGridParam','selrow');
	 //alert(store_code);
	 var rowDatas = $gird.jqGrid('getRowData2', store_code);
	 //alert(rowDatas["STORE_NAME"]);
	 //多选
	 var store_codes = $gird.jqGrid("getGridParam", "selarrrow");
	}
	
	function exportExcel() {
		common.exportExcel(
							{ //setting
								colNames:['store_code','store_name'],
								colModel:['STORE_CODE','STORE_NAME'],
								queryName:'testReport',
								fileName:"rick"
							}
							,{} //param
						   );
	}
</script>
</body>
</html>