$(document).ready(function() {
    if (localStorage.getItem("fullName")==null && localStorage.getItem("userId")==null){
        window.open("../../login.html","_self");
    } else {
        getMonthlyExpenses();
    }
});

function getMonthlyExpenses(){
    var table = $('#expenseDetailsTableId').DataTable();
    table.clear().draw();
    table.destroy();
    $.ajax({
        type : "GET",
        contentType: "application/json; charset=utf-8",
        url : REST_HOST+"/api/expenses/getAllExpensesOfMonth",
        dataType : "json",
        success : function(data) {
          if(data.result!=null){
              var dataList=data.result;
              for(var i in dataList){
                  table.row.add( [
                         dataList[i].srNo,
                         dataList[i].totalAmount,
                         dataList[i].expenseDate,
                         '<button class="btn bg-teal btn-xs" type="button" data-toggle="modal" data-target="#viewItemModal" onclick="getExpenseItems('+dataList[i].expenseId+')"><b>View</b></button>&nbsp;&nbsp;'+
                         '<button class="btn bg-teal btn-xs" type="button" data-toggle="modal" data-target="#expense-modal" onclick="addOnItems('+dataList[i].expenseId+')"><b>AddOn</b></button>'
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

  function addOnItems(expenseId) {
       $("#expense_id").val(expenseId);
       var table = $('#expenseItemTableId').DataTable();
       table.clear().draw();
       table.destroy();
  }

  $("#addOnItemId").click(function(){
      var flag=0;
      var expenseId = $("#expense_id").val();
      if (itemList.length==0){
          swal("Warning!", "Please enter at least one item!", "warning");
          flag=1;
          return false;
      }
      var formData = {id:expenseId,expenseItemDtos:itemList};
      $.ajax({
             type : "POST",
             contentType: "application/json; charset=utf-8",
             url : REST_HOST+"/api/expenses/updateExpense",
             dataType : "json",
             data : JSON.stringify(formData),
             success : function(data) {
                  if(data.result!=null){
                      if (formData.id==null) {
                          swal({
                              title: "AddOn!",
                              text: "Expenses added successfully!",
                              timer: 1500,
                              type: "success",
                              showConfirmButton: false
                          });
                      }
                      clearAllData();
                      $("#expense-modal").modal("hide");
                      window.open("../../admin/pages/monthlyExpenses.html","_self");
                  }else{
                      swal("Error",data.errorMessage, "error");
                  }
             },
             error : function(result) {
                  console.log(result.status);
             }
      });
  });

  $("#cancelId").click(function() {
     clearAllData();
  });

  function clearAllData(){
      $("#expense_id").val("");
      $("#total_amount").val("");
      clearData();
      arrayList = [];
      itemList = [];
      var table = $('#expenseItemTableId').DataTable();
      table.clear().draw();
      table.destroy();
  }