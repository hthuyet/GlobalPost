$(function () {

    $('.forgotPasswordBtn').click(function () {
        $('.modalForgotPassword').modal('show');

        $('.searchBtn').click(function () {
            var inputEmail = $('.modalForgotPassword').find('input[name="email"]').val();
            // console.log(';');
            $.ajax({
                type: "GET",
                url: '/users/get-existed-email?email=' + inputEmail,
                success: function (response) {
                    // console.log('lol : '+response);
                    if (response !== null && response) {

                        $('.modalForgotPassword').modal('hide');
                        $('.modalConfirmForgotPassword').modal('show');
                        $('.modalConfirmForgotPassword').find('.inputEmail').html(inputEmail);

                        // Submit
                        $('.btnContinue').unbind('click').click(function () {
                            $.ajax({
                                type: "POST",
                                url: '/users/forgot-password-with-email',
                                data: {email: inputEmail, redirectUrl: location.origin},
                                success: function () {
                                    location.replace('/login')
                                }
                            });
                        });

                        $('.messageNotFound').hide();

                    } else {
                        $('.messageNotFound').show();
                    }
                },
                error: function (e) {
                    // console.log(e);
                }
            });
        })
    })
});