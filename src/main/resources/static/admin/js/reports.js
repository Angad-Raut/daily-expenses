$(document).ready(function(){
    /*if (localStorage.getItem("fullName")==null && localStorage.getItem("userId")==null){
          window.open("../../login.html","_self");
    } else {

    }*/
    setAllConfiguration();
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
});
