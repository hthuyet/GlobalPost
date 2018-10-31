UserWebApp.controller('BranchController', function ($scope, $rootScope, HttpService, $translate, $location, $filter) {

  $scope.lstData = [];
  $scope.totalElements = 0;

  $scope.limit = 20;
  $scope.page = 1;


  $scope.params = [];
  $scope.params.name = '';
  $scope.checklistTable = {
    selected: [],
    checkAll: false
  };

  function loadData() {

    var params = {
      "limit": "" + $scope.limit,
      "page": "" + $scope.page,
      "name": "" + $scope.params.name
    };


    HttpService.postData('/branch/search', params).then(function (response) {
      $scope.lstData = response;
    });

    HttpService.postData('/branch/count', params).then(function (response) {
      $scope.totalElements = response;
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
    loadData();
    common.btnLoading($('.btnRefresh'), true);
    setTimeout(function () {
      common.btnLoading($('.btnRefresh'), false);
    }, 1000);
  };

  $scope.onDeletes = function () {
    $scope.deleteList = [];
    $scope.deleteList = $scope.checklistTable.selected;
    $('.modalDelete').modal('show');
  };

  $scope.onDelete = function (id) {
    $scope.deleteList = [];
    $scope.deleteList.push(id);
    $('.modalDelete').modal('show');
  };

  $scope.onDeleteConfirm = function () {
    $scope.param1 = [];
    $scope.param1.id = JSON.stringify($scope.deleteList);
    HttpService.getData('/deleteRole', $scope.param1).then(function (data1) {
      $('.modalDelete').modal('hide');
      if (data1 == 200) {
        $scope.checklistTable.selected = [];
        loadData();
        common.notifySuccess($translate.instant('deletePermissionSuccessfully'));
      } else {
        common.notifyError($translate.instant('deletePermissionFail'));
      }
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

  $scope.$watch('checklistTable.role', function (_newValue, _oldValue) {
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