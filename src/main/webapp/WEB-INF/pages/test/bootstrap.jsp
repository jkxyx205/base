<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
    
<%@ taglib uri="http://www.springframework.org/tags" prefix="s"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<meta name="viewport" content="width=device-width, initial-scale=1">
<title>Bootstrap</title>
</head>
<body>
<form role="form">
  <div class="form-group">
    <label for="exampleInputEmail1">Email address</label>
    <input type="email" class="form-control" id="exampleInputEmail1" placeholder="Enter email">
  </div>
  <div class="form-group">
    <label for="exampleInputPassword1">Password</label>
    <input type="password" class="form-control" id="exampleInputPassword1" placeholder="Password">
  </div>
  <div class="form-group">
    <label for="exampleInputFile">File input</label>
    <input type="file" id="exampleInputFile">
    <p class="help-block">Example block-level help text here.</p>
  </div>
  <div class="form-group">
    <label for="multiselect">Nation</label>
    <select id="multiselect" multiple="multiple">  
            <option value="1">China</option>
            <option value="2">Japan</option>
            <option value="3">Ameraica</option>
            <option value="4">Canada</option>
            <option value="5">Keraa</option>
   </select> 
  </div>
  <div class="form-group">
                <label for="dtp_input2" class="control-label">Date Picking</label>
                <div class="input-group date form_date" data-date="" data-date-format="yyyy/mm/dd" data-link-field="dtp_input2" data-link-format="yyyy/mm/dd">
                    <input class="form-control" size="8" type="text" value="" readonly>
                    <span class="input-group-addon"><span class="glyphicon glyphicon-remove"></span></span>
					<span class="input-group-addon"><span class="glyphicon glyphicon-calendar"></span></span>
                </div>
				<input type="hidden" id="dtp_input2" value="" /><br/>
            </div>
  <div class="checkbox">
    <label>
      <input type="checkbox"> Check me out
    </label>
  </div>
  <button type="submit" class="btn btn-default">Submit</button>
</form>

      <script type="text/javascript">
            $(document).ready(function() {
				$('#multiselect').multiselect({
					buttonWidth:200,
					numberDisplayed:100
				});
				$('.form_date').datetimepicker({
			        weekStart: 1,
			        todayBtn:  1,
					autoclose: 1,
					todayHighlight: 1,
					startView: 2,
					minView: 2,
					forceParse: 0
			    });
		   });
        </script>
</body>
</html>