UserWebApp.controller('EmployeeController', function ($scope, $rootScope, HttpService, $translate, $location, $filter) {

    $scope.lstData = [];
    $scope.totalElements = 0;

    $scope.params = [];
    $scope.params.name = '';
    $scope.params.limit = 20;
    $scope.params.page = 1;

    $scope.checklistTable = {
        selected: [],
        checkAll: false
    };

    function loadData() {
        common.spinner(true);

        var params = {
            "limit": "" + $scope.params.limit,
            "page": "" + $scope.params.page,
            "name": "" + $scope.params.name
        };

        HttpService.postData('/employee/search', params).then(function (response) {
            $scope.lstData = response;
            common.spinner(false);
        }, function error(response) {
            console.log(response);
            common.spinner(false);
        });

        HttpService.postData('/employee/count', params).then(function (response) {
            $scope.totalElements = response;
            common.spinner(false);
        }, function error(response) {
            console.log(response);
            common.spinner(false);
        });
    }

    renderDefaultRequestParams();

    function renderDefaultRequestParams() {

        var limit = common.getUrlRequestParam('limit');
        if (limit != null) {
            $scope.params.limit = limit;
        } else {
            $scope.params.limit = '20';
        }

        var page = common.getUrlRequestParam('page');
        if (page != null) {
            $scope.params.page = parseInt(page);
        } else {
            $scope.params.page = '1';
        }

        if (['20', '30', '50'].indexOf($scope.params.limit) < 0) {
            setTimeout(function () {
                $scope.$apply(function () {
                    $scope.params.limit = '20';
                    common.notifyWarning($translate.instant('requestParamError'));
                });
            }, 1);
        }

        loadData();

    }

    // Search role
    $scope.onSearch = function () {
        loadData();
    };

    $scope.onRefresh = function () {
        $scope.params = {};
        $scope.params.limit = '20';
        $scope.params.page = '1';
        $scope.params.name = '';

        $scope.checklistTable = {
            selected: [],
            checkAll: false
        };


        loadData();
        common.btnLoading($('.btnRefresh'), true);
        setTimeout(function () {
            common.btnLoading($('.btnRefresh'), false);
        }, 1000);
    };

    $scope.multi = false;
    $scope.onDeletes = function () {
        $scope.multi = true;
        $scope.deleteList = [];
        $scope.deleteList = $scope.checklistTable.selected;
        $('.modalDelete').modal('show');
    };

    $scope.onDelete = function (id) {
        $scope.multi = false;
        $scope.deleteList = [];
        $scope.deleteList.push(id);
        $('.modalDelete').modal('show');
    };

    $scope.onDeleteConfirm = function () {
        common.spinner(true);
        var param = {
            "ids": $scope.deleteList.join(",")
        };
        console.log($scope.param1);
        HttpService.postData('/employee/delete', param).then(function (data) {
            $('.modalDelete').modal('hide');
            $scope.checklistTable.selected = [];
            loadData();
            common.notifySuccess($translate.instant('deleteSuccessfully'));
            common.spinner(false);
        }, function error(response) {
            console.log(response);
            common.spinner(false);
            common.notifyError($translate.instant('deleteError'));
        });
    };

    $scope.onCheckbox = function () {
        if ($scope.checklistTable.checkAll) {
            $scope.checklistTable.selected = $scope.lstData.map(function (_item) {
                return _item.id;
            });
        } else {
            $scope.checklistTable.selected = [];
        }
    };

    $scope.$watch('checklistTable.selected', function (_newValue, _oldValue) {
        if (_newValue !== _oldValue) {
            // Update check box all status
            var element = $('.checkAllTable');
            var listChecked = $scope.checklistTable.selected;
            var list = $scope.lstData.map(function (_item) {
                return _item.id;
            });
            $scope.checklistTable.checkAll = common.updateCheckBoxAllStatus(element, listChecked, list);
        }
    }, true);

    $scope.$watch('params.page', function (_newValue, _oldValue) {
        // console.log('_oldValue : '+_oldValue);
        // console.log('_newValue : '+_newValue);
        if (_newValue !== _oldValue) {
            // console.log('params.page is change');
            //common.updateUrlRequestParam('page', _newValue);
            loadData();
        }
    }, true);

    $scope.$watch('params.limit', function (_newValue, _oldValue) {
        // console.log('params.limit : ');
        if (_newValue !== _oldValue) {
            // console.log('params.limit is change');
            //common.updateUrlRequestParam('limit', _newValue);
            loadData();
        }
    }, true);

});