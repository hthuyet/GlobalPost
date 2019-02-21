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

    loadData();
    common.btnLoading($('.btnRefresh'), true);
    setTimeout(function () {
      common.btnLoading($('.btnRefresh'), false);
    }, 1000);
  };

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


  $rootScope.$on('saveSuccessfully', function (event, data) {
    loadData();
  });

  $scope.onEditItem = function(item){
    $rootScope.$broadcast('onEditItem', {item: item});
  }


});