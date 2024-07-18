var expenseItemsList=[];
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
    $('#month_year_txt').datepicker({
        format: "MM yyyy",
        viewMode: "months",
        minViewMode: "months",
        enableOnReadonly: true
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
    var month=$('#month_txt').val();
    var flag = 0;
    if (month=="") {
        swal("Warning!", "Please select month of year!", "warning");
        flag=1;
        return false;
    }
    var formData = {monthName:month};
    getMonthReportData(formData);
});

$("#download_txt").click(function(){
    var month=$('#month_txt').val();
    var flag = 0;
    if (month=="") {
        swal("Warning!", "Please select month of year!", "warning");
        flag=1;
        return false;
    }
    var formData = {monthName:month};
    generateMonthReport(formData);
});

function getMonthReportData(formData) {
    var table = $('#reportTableId').DataTable();
    table.clear().draw();
    table.destroy();
    $.ajax({
        type : "POST",
        contentType: "application/json; charset=utf-8",
        url : REST_HOST+"/api/reports/getMonthReportData",
        dataType : "json",
        data : JSON.stringify(formData),
        success : function(data) {
            if(data.result!=null){
                  $("#download_txt").show();
                  $("#table_div").show();
                  expenseItemsList=[];
                  var dataList=data.result;
                  expenseItemsList=data.result;
                  for(var i in dataList){
                      table.row.add( [
                             dataList[i].srNo,
                             dataList[i].expenseDate,
                             dataList[i].totalAmount,
                             '<button class="btn bg-teal btn-xs" type="button" data-toggle="modal" data-target="#viewItemModal" onclick="getExpenseItems('+dataList[i].expenseId+')"><b>View</b></button>&nbsp;&nbsp;'+
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

function generateMonthReport(formData) {
       $.ajax({
       		type : "POST",
       		contentType: "application/json; charset=utf-8",
       		url : REST_HOST+"/api/reports/generateMonthReport",
       		dataType : "json",
       		data : JSON.stringify(formData),
       		success : function(data) {
       			if(data.result!=null){
       			    if (data.result!=null) {
                        var link = document.createElement('a');
                        link.href = "data:application/pdf;base64,"+data.result;
                        link.download = formData.monthName+' ExpenseReport.pdf';
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

  function getExpenseItems(expenseId) {
      var table = $('#viewExpenseItemsTableId').DataTable();
      table.clear().draw();
      table.destroy();
      for (var i in expenseItemsList) {
           if (expenseItemsList[i].expenseId==expenseId) {
               var itemList = expenseItemsList[i].itemsList;
               for(var j in itemList){
                   table.row.add( [
                           itemList[j].srNo,
                           itemList[j].itemName,
                           itemList[j].itemPrice,
                           itemList[j].paymentWith
                   ] ).draw(false);
               }
               break;
           }
      }
  }
