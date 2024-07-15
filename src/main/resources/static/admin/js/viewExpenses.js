$(document).ready(function(){
    if (localStorage.getItem("fullName")==null && localStorage.getItem("userId")==null){
          window.open("../../login.html","_self");
    } else {
        getAllExpenses();
    }
});

function getAllExpenses(){
    var table = $('#expenseDetailsTableId').DataTable();
    table.clear().draw();
    table.destroy();
    initializationTable();
    $.ajax({
        type : "GET",
        contentType: "application/json; charset=utf-8",
        url : REST_HOST+"/api/expenses/getAllExpenses",
        dataType : "json",
        success : function(data) {
          if(data.result!=null){
              var dataList=data.result;
              for(var i in dataList){
                  table.row.add( [
                         dataList[i].srNo,
                         dataList[i].totalAmount,
                         dataList[i].expenseDate,
                         '<button class="btn bg-teal btn-xs" type="button" data-toggle="modal" data-target="#viewItemModal" onclick="getExpenseItems('+dataList[i].expenseId+')"><b>View</b></button>'
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

  function getExpenseItems(expenseId) {
       var formData = {entityId:expenseId};
       var table = $('#viewExpenseItemsTableId').DataTable();
       table.clear().draw();
       table.destroy();
       viewExpenseItemsTable();
       $.ajax({
               type : "POST",
               contentType: "application/json; charset=utf-8",
               url : REST_HOST+"/api/expenses/getExpenseItemsByExpenseId",
               dataType : "json",
               data : JSON.stringify(formData),
               success : function(data) {
                 if(data.result!=null){
                     var dataList=data.result;
                     for(var i in dataList){
                         table.row.add( [
                                dataList[i].srNo,
                                dataList[i].itemName,
                                dataList[i].itemPrice,
                                dataList[i].paymentWith
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

  function initializationTable() {
     $('#expenseDetailsTableId').DataTable({
           'paging'      : true,
           'lengthChange': true,
           'searching'   : false,
           'ordering'    : true,
           'info'        : true,
           'autoWidth'   : true
     });
  }
  function viewExpenseItemsTable() {
     $('#viewExpenseItemsTableId').DataTable({
           'paging'      : false,
           'lengthChange': false,
           'searching'   : false,
           'ordering'    : false,
           'info'        : false,
           'autoWidth'   : false
     });
  }
