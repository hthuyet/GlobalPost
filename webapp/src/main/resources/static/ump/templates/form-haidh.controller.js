$(function() {
    var trigger = '';
    // Basic examples
    // ------------------------------
    // Default initialization
    // $('.select').select2({
    //     minimumResultsForSearch: Infinity
    // });
    //
    // // Select with search
    // $('.select-search').select2();
    //
    // // Menu border and text color
    // $('.select-border-color').select2({
    //     dropdownCssClass: 'border-primary',
    //     containerCssClass: 'border-primary text-primary-700'
    // });
    // Single picker
    $('.daterange-single').daterangepicker({
        singleDatePicker: true
    });
    // $('form').on("submit",function () {
    //     var data = $(this).serialize();
    //     data += '&select1='+ $('#select1').val();
    //     data += '&selectSearch1='+ $('#selectSearch1').val();
    //     data += '&multipleSelect='+ $('#multipleSelect').val();
    //
    //     console.log(data);
    //     $.ajax({
    //         type: 'POST',
    //         url: '/templates/postForm',
    //         data: data,
    //         success: function(response) {
    //             if(response == '200'){
    //                 console.log('vao roi');
    //             } else {
    //                 $('#alert_danger span.text-bold').text(response+"");
    //                 $('#alert_danger').show();
    //             }
    //         },
    //         error: function(e) {
    //             if(e.status == '403'){alert(e.responseText);}
    //         },
    //         async: true
    //     });
    // });
});