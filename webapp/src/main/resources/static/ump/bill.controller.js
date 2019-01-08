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
    "from": "",
    "to": "",
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
      "state": $scope.params.billState,
      "from": $scope.params.from,
      "to": $scope.params.to,
      "sname": "" + $scope.params.sendName,
      "smobile": "" + $scope.params.sendMobile,
      "rname": "" + $scope.params.receiveName,
      "rmobile": "" + $scope.params.receiveMobile,
    };

    if ($scope.params.from && $scope.params.from != "") {
      try {
        var fromDate = convertToDate($scope.params.from);
        params.from = fromDate.getTime();
      } catch (c) {
        console.error(c);
      }
    } else {
      params.from = 0;
    }

    if ($scope.params.to && $scope.params.to != "") {
      try {
        var toDate = convertToDate($scope.params.to);
        params.to = toDate.getTime();
      } catch (c) {
        console.error(c);
      }
    } else {
      params.to = 0;
    }


    HttpService.postData('/bill/search', params).then(function (response) {
      $scope.lstData = response;
      common.spinner(false);
      $scope.page = $scope.params.page;
    }, function error(response) {
      console.log(response);
      common.spinner(false);
      $scope.pageGo = "";
    });

    HttpService.postData('/bill/count', params).then(function (response) {
      $scope.totalElements = response;
      common.spinner(false);
    }, function error(response) {
      console.log(response);
      common.spinner(false);
    });
  }

  loadData();

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
    var param = {};
    param.ids = $scope.deleteList.join(",");
    HttpService.postData('/bill/delete', param).then(function (data1) {
      $('.modalDelete').modal('hide');
      if (data1 == 200) {
        $scope.checklistTable = {
          selected: [],
          checkAll: false
        };
        $scope.deleteList = [];
        loadData();
        common.notifySuccess($translate.instant('deleteSuccessfully'));
      } else {
        common.notifyError($translate.instant('deleteError'));
      }
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

  $scope.goToPage = function () {
    $scope.params.page = $scope.page;
  }

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