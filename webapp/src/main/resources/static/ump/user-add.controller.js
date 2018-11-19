UserWebApp.controller('UserAddController', function ($scope, $rootScope, HttpService, $translate) {

    $scope.params = [];
    $scope.roles = [];
    $scope.header = false;
    $scope.headerAddEdit = true;
    $scope.formNotification = {
        isActive: false,
    }; // Save notification data

    $('#userName').on('keypress', function (e) {
        if (e.which == 32)
            return false;
    });

    $scope.$on('ngRepeatOptionLabels', function () {
        $('.multiselect-filtering').multiselect({
            nonSelectedText: $translate.instant('placeholderSelect'),
            enableFiltering: true,
            enableCaseInsensitiveFiltering: true,
            templates: {
                filter: '<li class="multiselect-item multiselect-filter"><i class="icon-search4"></i> <input class="form-control" type="text"></li>'
            },
            onDropdownShown: function (event) {
                this.$select.parent().find("input[type='text']").focus();
            }
        });
    });

    renderDefaultRequestParams();

    function renderDefaultRequestParams() {

        $scope.params.userName = '';
        $scope.params.deviceGroup = {
            role: []
        };
        $scope.params.branch = {
        };
        $scope.params.fullName = '';
        $scope.params.email = '';
        $scope.params.description = '';
        $scope.params.phoneNumber = '';

        HttpService.getData('/users/get-data-search', {}).then(function (response) {
            $scope.roles = JSON.parse(response.roles);
            console.log($scope.roles);
        });

        HttpService.postData('/branch/search', {}).then(function (response) {
            $scope.branches = response;
            console.log($scope.branches);
        });
    }

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

    $scope.onUserAdd = function () {

        // Start validate
        var formElement = $('.formCreateUser');
        if (!onValidateBeforeSubmitForm(formElement)) {
            return false;
        }

        // Start add user
        HttpService.getData('/users/add-user', $scope.params).then(function (response) {
            if (response != 201 && response != 202 && response != 400) {
                var userId = response.userId;
                // Start save notification
                var data = angular.copy($scope.formNotification);
                delete data.isAlarmTotal;
                delete data.isCriticalTotal;
                delete data.isMajorTotal;
                delete data.isMinorTotal;

                // Start udpate notification setting
                common.notifySuccess($translate.instant('addUserSuccessfully'));
                setTimeout(function () {
                    location.replace('/users');
                }, 100)
            } else if (response == 201) {
                common.notifyError($translate.instant('existedUser'));
            } else if (response == 202) {
                common.notifyError($translate.instant('existedEmail'));
            } else {
                common.notifyError($translate.instant('addUserFail'));
            }
        }, function error(response) {
            console.log(response);
            common.notifyError($translate.instant('addUserFail'));

        });
    }

    $('.selectRole').unbind('change').on('change', function () {
        var value = $('.selectRole').val();
        setTimeout(function () {
            $scope.$apply(function () {
                $scope.params.deviceGroup.role = value;
            });
        }, 100);
    });

});