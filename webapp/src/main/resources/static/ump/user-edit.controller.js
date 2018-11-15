UserWebApp.controller('UserEditController', function ($scope, $rootScope, HttpService, $translate) {

    $scope.params = [];
    $scope.roles = [];
    $scope.header = false;
    $scope.headerAddEdit = true;
    $scope.headerInfo = false;
    $rootScope.userId = $('.dataResponse').attr('data-userId');
    $rootScope.userName = $('.dataResponse').attr('data-userName');
    $rootScope.fullName = $('.dataResponse').attr('data-fullName');
    $rootScope.phoneNumber = $('.dataResponse').attr('data-phoneNumber');
    $rootScope.description = $('.dataResponse').attr('data-description');
    $rootScope.email = $('.dataResponse').attr('data-email');
    $rootScope.role = $('.dataResponse').attr('data-role');
    $rootScope.group = $('.dataResponse').attr('data-group');
    renderDefaultRequestParams();
    $('#userName').on('keypress', function (e) {
        if (e.which == 32)
            return false;
    });

    $scope.$on('ngRepeatOptionLabels', function () {
        $(".selectRole").val(JSON.parse($rootScope.role));
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


    function renderDefaultRequestParams() {
        HttpService.getData('/users/get-data-search', {}).then(function (response) {
            $scope.roles = JSON.parse(response.roles);
            $scope.params.checkExistedUsername = false;
            $scope.params.checkExistedEmail = false;
            $scope.params.userId = $rootScope.userId;
            $scope.params.userName = $rootScope.userName;
            $scope.params.deviceGroup = {
                role: JSON.parse($rootScope.role)
            };
            $scope.params.fullName = $rootScope.fullName;
            $scope.params.email = $rootScope.email;
            $scope.params.description = $rootScope.description;
            $scope.params.phoneNumber = $rootScope.phoneNumber;

        });

    }

    $scope.onUserEdit = function () {
        $scope.params.checkExisted = false;
        if ($rootScope.userName != $scope.params.userName) {
            $scope.params.checkExistedUsername = true;
        }
        if ($rootScope.email != $scope.params.email) {
            $scope.params.checkExistedEmail = true;
        }
        $scope.params.userNameOld = $rootScope.userName;
        HttpService.getData('/users/edit', $scope.params).then(function (response) {
            if (response == 200) {
                common.notifySuccess($translate.instant('editUserSuccess'));
                location.replace('/users');
            } else if (response == 201) {
                common.notifyError($translate.instant('existedUser'));
            } else if (response == 202) {
                common.notifyError($translate.instant('existedEmail'));
            } else {
                common.notifyError($translate.instant('editUserFail'));
            }
        });
    };

    $('.selectRole').unbind('change').on('change', function () {
        var value = $('.selectRole').val();
        setTimeout(function () {
            $scope.$apply(function () {
                $scope.params.deviceGroup.role = value;
            });
        }, 100);
    });

});