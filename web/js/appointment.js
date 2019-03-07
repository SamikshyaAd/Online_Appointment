
"use strict";
(function(){
    
$(function(){
    $("#newBtn").click(showForm);
    $("#cancelBtn").click(undoForm);
    $("#searchBtn").click(getAppointments);
    $("#clearBtn").click(reset);
    
    function showForm(){
        $(this).hide();
       // $("#tableDiv").hide();
       // $("#searchInp").val('');
        $("#formdiv").show();
    }
    function undoForm(){
        $("#formdiv").hide();
        $("#formData").trigger("reset");
        $("#errMessage").hide();
        $("#newBtn").show();
        
    }
    function reset(){
        $("#tableDiv").hide();
        $("#errAppointment").hide();
        $("#searchInp").val('');
    }
    //Retriving appointments
    function getAppointments(){
         $("#errAppointment").hide();
        var searchContent=$("#searchInp").val();
        $.ajax({
            url:"/Online_Appointment/RetriveAppointment",
            type:"POST",
            data:{searchData:searchContent},
            dataType:"json",
            success:function(response){
                 $("#tableBody").empty();
                 var appendResponse = "";
                 var index=0;
                 for(index=0;index<response.result.length;index++){
                        appendResponse += "<tr><td>"+response.result[index].date+"</td><td>"+response.result[index].time+
                                "</td><td>"+response.result[index].description+"</td></tr>";
                 }
                 $("#tableDiv").show();
                 $("#tableBody").append(appendResponse);
            },
            error: function(){
                $("#tableDiv").hide();
                $("#errAppointment").show();
            }
        });
    }
    //Form validation and submission
    $("#formData").validate({
        
        rules:{
            date:{
               required: true, 
               dpDate: true 
            }, 
           time:{
               required:true
           },
           descStr:{
               required:true
           }
        },
        messages:{
            date:{
               required:"Date is required"
           },
           time:{
               required:"Time is required"
           },
           descStr:{
               required:"Description is required"
           }
        },
        errorElement: "div",
        errorLabelContainer: "#errMessage",
        submitHandler:function(form){
           $.ajax({
               url:"/Online_Appointment/InsertAppointment",
               type:"POST",
               data:$(form).serialize(),
               success: function(result){
                  $("#submissionDiv").show();
                  $("#submissionDiv").fadeOut(10000);
                  $("#formdiv").hide();
                  $("#formData").trigger("reset");
                  $("#newBtn").show();
               },
               error: function(){   
               }
            });
        }
    });
    
    //Validating datepicker
    $("#date").datepicker({ minDate:0});
});
})();