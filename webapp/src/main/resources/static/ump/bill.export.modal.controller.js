UserWebApp.controller('BillExportModalController', function ($scope, $rootScope, HttpService, $translate, $location, $filter) {
  $scope.title = $translate.instant('billExportModal');

  $scope.lstDataCommon = [];
  $scope.totalElements = 0;

  $scope.limit = 20;
  $scope.page = 1;


  $scope.checklistTable = {
    selected: [],
    checkAll: false
  };

  $scope.$watch('page', function (_newValue, _oldValue) {
    if (_newValue !== _oldValue) {
      $scope.loadData();
    }
  }, true);

  $scope.$watch('limit', function (_newValue, _oldValue) {
    if (_newValue !== _oldValue) {
      $scope.loadData();
    }
  }, true);

  $scope.go = function () {
    $scope.page = $scope.pageGo;
  }


  $scope.fromType = 1;

  $scope.changeFromType = function () {
    $scope.page = 1;
    $scope.loadData();
  }


  $scope.loadData = function () {
    common.spinner(true);
    var params = {
      "limit": $scope.limit,
      "page": $scope.page,
      "name": ""
    };

    if($scope.fromType == 1 || $scope.fromType == "1"){
      //Partner
      HttpService.postData('/partner/search', params).then(function (response) {
        $scope.lstDataCommon = response;
        common.spinner(false);
      }, function error(response) {
        $scope.lstDataCommon = [];
        console.log(response);
        common.spinner(false);
      });

      HttpService.postData('/partner/count', params).then(function (response) {
        $scope.totalElements = response;
        common.spinner(false);
      }, function error(response) {
        $scope.totalElements = 0;
        console.log(response);
        common.spinner(false);
      });
    }else if($scope.fromType == 2 || $scope.fromType == "2"){
      //Branch
      HttpService.postData('/branch/search', params).then(function (response) {
        $scope.lstDataCommon = response;
        common.spinner(false);
      }, function error(response) {
        $scope.lstDataCommon = [];
        console.log(response);
        common.spinner(false);
      });

      HttpService.postData('/branch/count', params).then(function (response) {
        $scope.totalElements = response;
        common.spinner(false);
      }, function error(response) {
        $scope.totalElements = 0;
        console.log(response);
        common.spinner(false);
      });

    }else if($scope.fromType == 3 || $scope.fromType == "3"){
      //Employee
      HttpService.postData('/employee/search', params).then(function (response) {
        $scope.lstDataCommon = response;
        common.spinner(false);
      }, function error(response) {
        $scope.totalElements = 0;
        console.log(response);
        common.spinner(false);
      });

      HttpService.postData('/employee/count', params).then(function (response) {
        $scope.totalElements = response;
        common.spinner(false);
      }, function error(response) {
        $scope.totalElements = 0;
        console.log(response);
        common.spinner(false);
      });
    }else if($scope.fromType == 4 || $scope.fromType == "4"){
      //customer
      HttpService.postData('/customer/search', params).then(function (response) {
        $scope.lstDataCommon = response;
        common.spinner(false);
      }, function error(response) {
        $scope.totalElements = 0;
        console.log(response);
        common.spinner(false);
      });

      HttpService.postData('/customer/count', params).then(function (response) {
        $scope.totalElements = response;
        common.spinner(false);
      }, function error(response) {
        $scope.totalElements = 0;
        console.log(response);
        common.spinner(false);
      });
    }else{
      $scope.lstDataCommon = [];
      $scope.totalElements = 0;
    }

    $scope.pageGo = $scope.page;
  }
  
  $scope.executeExport = function (item) {
    common.spinner(true);
    var params = {
      "type": $scope.fromType,
      "item": item,
      "branchId": item,
      "lstBill": $scope.listItem,
    };

    HttpService.postData('/bill/exeExport', params, $("#btnExeExport")).then(function (response) {
      common.notifySuccess($translate.instant('exportSuccessfully'));
      common.spinner(false);
      $rootScope.$broadcast('exportSuccess');
    }, function error(response) {
      common.notifyError($translate.instant('exportError'));
      console.log(response);
      common.spinner(false);
    });

  }

  $scope.listItem = [];
  $rootScope.$on('modalExport', function (event, data) {
    console.log(data.list);
    $scope.fromType = "1";
    $scope.changeFromType();
  });
  

});