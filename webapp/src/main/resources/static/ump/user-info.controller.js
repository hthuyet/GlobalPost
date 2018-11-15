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
    $scope.formNotification = {isActive: false, };
    var javaResponseElement = $('.javaResponse');
    var userId = javaResponseElement.attr('data-userId');
    HttpService.getData('/users/' + userId + '/notification-setting/get-by-user', {}).then(function (_notification) {
        $scope.formNotification.isActive = _notification.isActive;
        $scope.formNotification.isSendEmail = _notification.isSendEmail;
        $scope.formNotification.isSendNotify = _notification.isSendNotify;
        $scope.formNotification.isSendNotifyList = _notification.isSendNotifyList;
        $scope.formNotification.isSendSms = _notification.isSendSms;


        $scope.formNotification.isMajorTotal = _notification.majorTotal !== null;
        $scope.formNotification.majorTotal = _notification.majorTotal;
        $scope.formNotification.isMinorTotal = _notification.minorTotal !== null;
        $scope.formNotification.minorTotal = _notification.minorTotal;
        $scope.formNotification.isCriticalTotal = _notification.criticalTotal !== null;
        $scope.formNotification.criticalTotal = _notification.criticalTotal;
        $scope.formNotification.isAlarmTotal = _notification.alarmTotal !== null;
        $scope.formNotification.alarmTotal = _notification.alarmTotal;

    });

    function onValidateBeforeSubmitForm(formElement) {
        if (formElement.valid()) {
            var isValid = true;
            common.notifyRemoveAll();
            var message = [];

            if ($scope.formNotification.isActive) {
                if (!($scope.formNotification.isAlarmTotal || $scope.formNotification.isCriticalTotal
                        || $scope.formNotification.isMajorTotal || $scope.formNotification.isMinorTotal)) {
                    isValid = false;
                    message.push($translate.instant('formCreateUserInvalidNotificationWhen'))
                }
            }

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

    $scope.onSubmitNotification = function () {
        // Start validate
        var formElement = $('.formUpdateNotification');
        if (!onValidateBeforeSubmitForm(formElement)) {
            return false;
        }

        // Start save notification
        var data = angular.copy($scope.formNotification);
        delete data.isAlarmTotal;
        delete data.isCriticalTotal;
        delete data.isMajorTotal;
        delete data.isMinorTotal;

        // Start udpate notification setting
        HttpService.postData('/users/' + userId + '/notification-setting/update-by-user', data).then(function (_notification) {
            common.notifySuccess($translate.instant('saveSuccessfully'));
            setTimeout(function () {
                // location.replace('/users');
            }, 500)

        }, function () {
            common.notifySuccess($translate.instant('saveError'));
            setTimeout(function () {
                // location.replace('/users');
            }, 500)
        });
    }


});