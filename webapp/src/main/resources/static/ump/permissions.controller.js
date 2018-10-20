UserWebApp.controller('PermissionsController', function ($scope, $rootScope, HttpService, $translate, $location, $filter) {

    $scope.permissionsList = [];
    $scope.totalElements = 0;
    $scope.params = [];
    $scope.params.permissionsName = '';

    $scope.checklistTable = {
      permission: [],
      checkAll: false
    };

    renderDefaultRequestParams();

    function renderDefaultRequestParams() {

        var limit = common.getUrlRequestParam('limit');
        if(limit != null){
            $scope.params.limit = limit;
        } else {$scope.params.limit = '20';}

        var page = common.getUrlRequestParam('page');
        if(page != null){
            $scope.params.page = parseInt(page);
        } else {$scope.params.page = '1';}

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

    function loadData() {
        HttpService.getData('/permissions/search', $scope.params).then(function (response) {
          $scope.permissionsList = response.permissions;
          $scope.totalElements = response.totalElements;
        });
    }

    // Search permission
    $scope.onSearch = function () {
        loadData();
    };

    $scope.onRefresh = function () {
        $scope.params = [];
        $scope.params.limit = '20';
        $scope.params.page = '1';
        loadData();
        common.btnLoading($('.btnRefresh'), true);
        setTimeout(function () {
          common.btnLoading($('.btnRefresh'), false);
        }, 1000);
    };

    $scope.onPermissionsDelete = function () {
        $scope.perDeleteList = [];
        $scope.perDeleteList = $scope.checklistTable.permission;
        $('.modalDelete').modal('show');
    };

    $scope.onPermissionDelete = function (id) {
        $scope.perDeleteList = [];
        $scope.perDeleteList.push(id);
        $('.modalDelete').modal('show');
    };

    $scope.onDeleteConfirm = function () {
        $scope.param1 = [];
        $scope.param1.id = JSON.stringify($scope.perDeleteList);
        HttpService.getData('/deletePermission', $scope.param1).then(function (data1) {
            $('.modalDelete').modal('hide');
            if(data1 == 200){
                $scope.checklistTable.permission = [];
                loadData();
                common.notifySuccess($translate.instant('deletePermissionSuccessfully'));
            } else {
                common.notifyError($translate.instant('deletePermissionFail'));
            }
        });
    };

    $scope.onCheckbox = function () {
      if ($scope.checklistTable.checkAll) {
          $scope.checklistTable.permission = $scope.permissionsList.map(function(_item) { return _item.id; });
      } else {
          $scope.checklistTable.permission = [];
      }
    };

    $scope.$watch('checklistTable.permission', function (_newValue, _oldValue) {
      if (_newValue !== _oldValue) {
          // Update check box all status
          var element = $('.checkAllTable');
          var listChecked = $scope.checklistTable.permission;
          var list = $scope.permissionsList.map(function(_item) { return _item.id; });
          $scope.checklistTable.checkAll = common.updateCheckBoxAllStatus(element, listChecked, list);
      }
    }, true);

    $scope.$watch('params.page', function (_newValue, _oldValue) {
        // console.log('_oldValue : '+_oldValue);
        // console.log('_newValue : '+_newValue);
        if (_newValue !== _oldValue) {
            // console.log('params.page is change');
            common.updateUrlRequestParam('page', _newValue);
            loadData();
        }
    }, true);

    $scope.$watch('params.limit', function (_newValue, _oldValue) {
        // console.log('params.limit : ');
        if (_newValue !== _oldValue) {
            // console.log('params.limit is change');
            common.updateUrlRequestParam('limit', _newValue);
            loadData();
        }
    }, true);

});