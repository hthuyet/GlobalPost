UserWebApp.controller('BillImportController', function ($scope, $rootScope, HttpService, $translate, $location, $filter) {

  $scope.lstAllData = [];
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
  };

  $scope.checklistTable = {
    selected: [],
    checkAll: false
  };

  function loadData() {
    common.spinner(true);
    if ($scope.params.billNo) {
      var params = {
        "limit": "" + $scope.params.limit,
        "page": "" + $scope.params.page,
        "billno": "" + $scope.params.billNo,
        "state": $scope.params.billState,
      };


      HttpService.postData('/bill/search', params).then(function (response) {
        common.spinner(false);
        if (response && response.length == 1) {
          $scope.lstAllData.push(response[0]);
          $scope.checklistTable.selected.push(response[0].id);
          $scope.params.billNo = "";
        }
      }, function error(response) {
        console.log(response);
        common.spinner(false);
      });
    }else{
      common.spinner(false);
    }
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
    console.log('-----------onSearch----------');
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
  
  $scope.onClear = function () {
    $scope.lstAllData = [];
    $scope.multi = false;
    $scope.checklistTable = {
      selected: [],
      checkAll: false
    };
  }

  $scope.onCheckbox = function () {
    if ($scope.checklistTable.checkAll) {
      $scope.checklistTable.selected = $scope.lstAllData.map(function (_item) {
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
      var list = $scope.lstAllData.map(function (_item) {
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


  //Modal imoport
  $scope.onImport = function () {
    $scope.deleteList = [];
    $scope.deleteList = $scope.checklistTable.selected;
    $('#modalImport').modal('show');
  };


  $scope.executeImport = function () {
    common.spinner(true);
    var params = {};
    HttpService.postData('/customer/save', params, $("#btnExeImport")).then(function (response) {
      common.notifySuccess($translate.instant('importSuccessfully'));
      common.spinner(false);
      $('#modalImport').modal('hide');
      $scope.checklistTable = {
        selected: [],
        checkAll: false
      };
      $scope.lstAllData = [];
    }, function error(response) {
      common.notifyError($translate.instant('importError'));
      console.log(response);
      common.spinner(false);
    });
  };

  $scope.fromType = 1;
  $scope.lstDataCommon = [];
  $scope.changeFromType = function () {
    common.spinner(true);
    var params = {
      "limit": "-1",
      "name": ""
    };

    if($scope.fromType == 1 || $scope.fromType == "1"){
      //Partner
      HttpService.postData('/partner/search', params).then(function (response) {
        $scope.lstDataCommon = response.map(function(item) {
          return {"id": item.id,"name": item.name};
        });
        common.spinner(false);
      }, function error(response) {
        $scope.lstDataCommon = [];
        console.log(response);
        common.spinner(false);
      });
    }else if($scope.fromType == 2 || $scope.fromType == "2"){
      //Branch
      HttpService.postData('/branch/search', params).then(function (response) {
        $scope.lstDataCommon = response.map(function(item) {
          return {"id": item.id,"name": item.name};
        });
        common.spinner(false);
      }, function error(response) {
        $scope.lstDataCommon = [];
        console.log(response);
        common.spinner(false);
      });
    }else if($scope.fromType == 3 || $scope.fromType == "3"){
      //Employee
      HttpService.postData('/employee/search', params).then(function (response) {
        $scope.lstDataCommon = response.map(function(item) {
          return {"id": item.id,"name": item.fullName};
        });
        common.spinner(false);
      }, function error(response) {
        $scope.lstDataCommon = [];
        console.log(response);
        common.spinner(false);
      });
    }else{
      $scope.lstDataCommon = [];
    }

  }
  

});