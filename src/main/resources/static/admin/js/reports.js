$(document).ready(function(){
    if (localStorage.getItem("fullName")==null && localStorage.getItem("userId")==null){
          window.open("../../login.html","_self");
    } else {
          setAllConfiguration();
          $("#download_txt").hide();
          $("#table_div").hide();
    }
});

function setAllConfiguration() {
    $('#from_date_txt').datepicker({
        format: 'dd MM yyyy',
        endDate: '0d',
        minDate: new Date()
    });
    $('#to_date_txt').datepicker({
         format: 'dd MM yyyy',
         startDate: '0d',
         maxDate: new Date()
    });
    var table = $('#reportTableId').DataTable();
       table.clear().draw();
       table.destroy();
       $('#reportTableId').DataTable({
             'paging'      : true,
             'lengthChange': true,
             'searching'   : false,
             'ordering'    : true,
             'info'        : true,
             'autoWidth'   : true
       });
}

$("#search_txt").click(function(){
    var startDate=$('#start_date').val();
    var endDate=$('#end_date').val();
    var flag = 0;
    if (startDate=="") {
        swal("Warning!", "Please select from date!", "warning");
        flag=1;
        return false;
    }
    if (startDate=="") {
        swal("Warning!", "Please select to date!", "warning");
        flag=1;
        return false;
    }
    if (startDate>endDate) {
        swal("Warning!", "From Date should not be greater than To Date!", "warning");
        flag=1;
        return false;
    }
    if (endDate<startDate) {
        swal("Warning!", "To Date should not be less than From Date!", "warning");
        flag=1;
        return false;
    }
    var formData = {startDate:startDate,endDate:endDate};
    getReportData(formData);
});

$("#download_txt").click(function(){
    var startDate=$('#start_date').val();
    var endDate=$('#end_date').val();
    var flag = 0;
    if (startDate=="") {
        swal("Warning!", "Please select from date!", "warning");
        flag=1;
        return false;
    }
    if (startDate=="") {
        swal("Warning!", "Please select to date!", "warning");
        flag=1;
        return false;
    }
    if (startDate>endDate) {
        swal("Warning!", "From Date should not be greater than To Date!", "warning");
        flag=1;
        return false;
    }
    if (endDate<startDate) {
        swal("Warning!", "To Date should not be less than From Date!", "warning");
        flag=1;
        return false;
    }
    var formData = {startDate:startDate,endDate:endDate};
    generateReportWithDateRange(formData);
});

function getReportData(formData) {
    var table = $('#reportTableId').DataTable();
    table.clear().draw();
    table.destroy();
    $.ajax({
        type : "POST",
        contentType: "application/json; charset=utf-8",
        url : REST_HOST+"/api/reports/getExpenseReportData",
        dataType : "json",
        data : JSON.stringify(formData),
        success : function(data) {
            if(data.result!=null){
                  $("#download_txt").show();
                  $("#table_div").show();
                  var dataList=data.result;
                  for(var i in dataList){
                      table.row.add( [
                             dataList[i].srNo,
                             dataList[i].totalAmount,
                             dataList[i].expenseDate,
                             '<button class="btn bg-teal btn-xs" type="button" data-toggle="modal" data-target="#viewItemModal" onclick="getExpenseItems('+dataList[i].itemsList+')"><b>View</b></button>&nbsp;&nbsp;'+
                             '<button class="btn bg-teal btn-xs" type="button" onclick="generateReportByExpenseId('+dataList[i].expenseId+')"><b>Download</b></button>'
                      ] ).draw(false);
                  }
            }else{
                 swal("Error",data.errorMessage, "error");
            }
        },
        error : function(result) {
            console.log(result.status);
        }
    });
}

function generateReportWithDateRange(formData) {
       $.ajax({
       		type : "POST",
       		contentType: "application/json; charset=utf-8",
       		url : REST_HOST+"/api/reports/downloadReport",
       		dataType : "json",
       		data : JSON.stringify(formData),
       		success : function(data) {
       			if(data.result!=null){
       			    if (data.result!=null) {
                        var link = document.createElement('a');
                        link.href = "data:application/pdf;base64,"+data.result;
                        link.download = 'ExpenseReport('+formData.startDate+'-'+formData.endDate+').pdf';
                        link.dispatchEvent(new MouseEvent('click'));
       				}
       			}else{
       			    swal("Error",data.errorMessage, "error");
       			}
       		},
       		error : function(result) {
       			console.log(result.status);
       		}
       });
 }

 function generateReportByExpenseId(expenseId) {
        var formData = {entityId:expenseId};
        $.ajax({
        		type : "POST",
        		contentType: "application/json; charset=utf-8",
        		url : REST_HOST+"/api/reports/downloadReportByExpenseId",
        		dataType : "json",
        		data : JSON.stringify(formData),
        		success : function(data) {
        			if(data.result!=null){
        			    if (data.result!=null) {
                         var link = document.createElement('a');
                         link.href = "data:application/pdf;base64,"+data.result;
                         link.download = 'ExpenseReport-'+expenseId+'.pdf';
                         link.dispatchEvent(new MouseEvent('click'));
        				}
        			}else{
        			    swal("Error",data.errorMessage, "error");
        			}
        		},
        		error : function(result) {
        			console.log(result.status);
        		}
        });
  }

  function getExpenseItems(dataList) {
      var table = $('#viewExpenseItemsTableId').DataTable();
      table.clear().draw();
      table.destroy();
      for(var i in dataList){
         table.row.add( [
            dataList[i].srNo,
            dataList[i].itemName,
            dataList[i].itemPrice,
            dataList[i].paymentWith
         ] ).draw(false);
      }
  }
