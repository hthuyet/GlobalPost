UserWebApp.controller('BillExportController', function ($scope, $rootScope, HttpService, $translate, $location, $filter) {

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


      HttpService.postData('/bill/searchExport', params).then(function (response) {
        common.spinner(false);
        if (response && response.length == 1) {
          var obj = response[0];
          var found = false;
          if ($scope.lstAllData && $scope.lstAllData.length > 0) {
            for (var i = 0; i < $scope.lstAllData.length; i++) {
              if ($scope.lstAllData[i].id == obj.id) {
                found = true;
                break;
              }
            }
          }
          if (!found) {
            $scope.lstAllData.push(obj);
            $scope.checklistTable.selected.push(obj.id);
          }
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

  $scope.goToPage = function () {
    $scope.params.page = $scope.page;
  }

  loadData();

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


  //Modal export
  $scope.onExport = function () {
    $('#modalExport').modal('show');
    $rootScope.$broadcast('modalExport', {list: $scope.checklistTable.selected});
  };


  $rootScope.$on('exportSuccess', function (event, data) {
    $('#modalExport').modal('hide');
    $scope.checklistTable = {
      selected: [],
      checkAll: false
    };
    $scope.lstAllData = [];
  });
  

});