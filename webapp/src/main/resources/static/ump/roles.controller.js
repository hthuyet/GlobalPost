UserWebApp.controller('RolesController', function ($scope, $rootScope, HttpService, $translate, $location, $filter) {

  $scope.rolesList = [];
  $scope.totalElements = 0;
  $scope.params = [];
  $scope.params.roleName = '';
  $scope.checklistTable = {
    role: [],
    checkAll: false
  };

  $scope.goToPage = function () {
    $scope.params.page = $scope.page;
  }

  function loadData() {
    HttpService.getData('/roles/search', $scope.params).then(function (response) {
      $scope.rolesList = response.roles;
      $scope.totalElements = response.totalElements;
      $scope.page = $scope.params.page;
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
    $scope.params = [];
    $scope.params.limit = '20';
    $scope.params.page = '1';
    loadData();
    common.btnLoading($('.btnRefresh'), true);
    setTimeout(function () {
      common.btnLoading($('.btnRefresh'), false);
    }, 1000);
  };

  $scope.onRolesDelete = function () {
    $scope.roleDeleteList = [];
    $scope.roleDeleteList = $scope.checklistTable.role;
    $('.modalDelete').modal('show');
  };

  $scope.onRoleDelete = function (id) {
    $scope.roleDeleteList = [];
    $scope.roleDeleteList.push(id);
    $('.modalDelete').modal('show');
  };

  $scope.onDeleteConfirm = function () {
    $scope.param1 = [];
    $scope.param1.id = JSON.stringify($scope.roleDeleteList);
    HttpService.getData('/deleteRole', $scope.param1).then(function (data1) {
      $('.modalDelete').modal('hide');
      if (data1 == 200) {
        $scope.checklistTable.role = [];
        loadData();
        common.notifySuccess($translate.instant('deletePermissionSuccessfully'));
      } else if (data1 == 400) {
        common.notifyError($translate.instant('deletePermissionFail'));
      } else if (data1 == 300) {
        common.notifyError($translate.instant('deletePermissionFailBecauseUsed'));
      }
    });
  };

  $scope.onCheckbox = function () {
    if ($scope.checklistTable.checkAll) {
      $scope.checklistTable.role = $scope.rolesList.map(function (_item) {
        return _item.id;
      });
    } else {
      $scope.checklistTable.role = [];
    }
  };

  $scope.$watch('checklistTable.role', function (_newValue, _oldValue) {
    if (_newValue !== _oldValue) {
      // Update check box all status
      var element = $('.checkAllTable');
      var listChecked = $scope.checklistTable.role;
      var list = $scope.rolesList.map(function (_item) {
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