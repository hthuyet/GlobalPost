UserWebApp.controller('UserInfoController', function ($scope, $rootScope, HttpService, $translate) {

    $scope.params = [];
    $scope.roles = [];
    $scope.header = false;
    $scope.headerAddEdit = false;
    $scope.headerInfo = true;

    var formItem = $('.formChangePassword');
    formItem.find('.inputRepeatPassword').on('change keyup paste', function () {
        var currentPassword = formItem.find('.inputPassword').val(),
                repeatPassword = formItem.find('.inputRepeatPassword').val();
        if (currentPassword !== repeatPassword) {
            formItem.find('.submitBtn').attr('disabled', true);
            formItem.find('.messageInputPasswordError').show();
        } else {
            formItem.find('.submitBtn').removeAttr('disabled');
            formItem.find('.messageInputPasswordError').hide();
        }

    });

    formItem.find('.inputPassword').on('change keyup paste', function () {
        var currentPassword = formItem.find('.inputPassword').val(),
                repeatPassword = formItem.find('.inputRepeatPassword').val();
        if (repeatPassword !== '' && currentPassword !== repeatPassword) {
            formItem.find('.submitBtn').attr('disabled', true);
            formItem.find('.messageInputPasswordError').show();
        } else {
            formItem.find('.submitBtn').removeAttr('disabled');
            formItem.find('.messageInputPasswordError').hide();
        }

    });

    formItem.submit(function (e) {
        e.preventDefault();
        beforeSubmit();
    });

    function beforeSubmit() {
        $scope.params = [];
        $scope.params.userName = formItem.find('input[name=username]').val();
        $scope.params.currentPassword = formItem.find('input[name=currentPassword]').val();
        HttpService.getData('/users/post-check-current-password', $scope.params).then(function (response) {
            if (response) {
                $scope.params.newPassword = formItem.find('input[name=newPassword]').val();
                HttpService.getData('/users/change-password', $scope.params).then(function (response) {
                    if (response) {
                        common.notifySuccess($translate.instant('changePasswordSuccess'));
                        location.replace('/user-information/' + $scope.params.userName);
                    } else {
                        common.notifyError($translate.instant('changePasswordFail'));
                    }
                });
            } else {
                common.notifyError($translate.instant('currentPasswordWrong'));
            }

        });
    }

    // Start form notification
    var javaResponseElement = $('.javaResponse');
    var userId = javaResponseElement.attr('data-userId');

    function onValidateBeforeSubmitForm(formElement) {
        if (formElement.valid()) {
            var isValid = true;
            common.notifyRemoveAll();
            var message = [];

            if (message.length > 0) {
                message.unshift($translate.instant('formInvalid'));
                common.notifyWarning(message.join('</br>'))
            } else {
                common.notifyRemoveAll();
            }

            // return check before submit
            return isValid;
        }
    }
});