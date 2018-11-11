UserWebApp.controller('BillController', function ($scope, $rootScope, HttpService, $translate, $location, $filter) {

  $scope.lstData = [];
  $scope.totalElements = 0;

  $scope.limit = 20;
  $scope.page = 1;


  $scope.params = [];
  $scope.params = {
    "page": 1,
    "limit": 20,
    "billNo": "",
    "billState": -1,
    "from": 0,
    "to": 0,
    "sendName": "",
    "sendMobile": "",
    "receiveName": "",
    "receiveMobile": "",
  };

  $scope.checklistTable = {
    selected: [],
    checkAll: false
  };

  function loadData() {
    common.spinner(true);
    var params = {
      "limit": "" + $scope.params.limit,
      "page": "" + $scope.params.page,
      "billno": "" + $scope.params.billNo,
      "state":  $scope.params.billState,
      "from":  $scope.params.from,
      "to": $scope.params.to,
      "sname": "" + $scope.params.sendName,
      "smobile": "" + $scope.params.sendMobile,
      "rname": "" + $scope.params.receiveName,
      "rmobile": "" + $scope.params.receiveMobile,
    };


    HttpService.postData('/bill/search', params).then(function (response) {
      $scope.lstData = response;
      common.spinner(false);
    },function error(response) {
      console.log(response);
      common.spinner(false);
    });

    HttpService.postData('/bill/count', params).then(function (response) {
      $scope.totalElements = response;
      common.spinner(false);
    },function error(response) {
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
    $scope.param1 = [];
    $scope.param1.id = JSON.stringify($scope.deleteList);
    HttpService.getData('/deleteRole', $scope.param1).then(function (data1) {
      $('.modalDelete').modal('hide');
      if (data1 == 200) {
        $scope.checklistTable.selected = [];
        loadData();
        common.notifySuccess($translate.instant('deleteSuccessfully'));
      } else {
        common.notifyError($translate.instant('deleteError'));
      }
      common.spinner(false);
    },function error(response) {
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