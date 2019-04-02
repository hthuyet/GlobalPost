UserWebApp.controller('ReportController', function ($scope, $rootScope, HttpService, $translate, $location, $filter, $http) {
  $scope.typeReport = "1";

  $scope.lstAllData = [];
  $scope.lstData = [];
  $scope.totalElements = 0;

  $scope.limit = 20;
  $scope.page = 1;
  $scope.pageGo = $scope.page;


  function loadData() {
    var params = validateForm();

    if (params === false) {
      return false;
    }

    common.spinner(true);

    HttpService.postData('/report/search', params).then(function (response) {
      $scope.totalElements = response.length;
      $scope.lstAllData = response;
      searchData();
      common.spinner(false);
      $scope.pageGo = $scope.page;
    }, function error(response) {
      console.log(response);
      common.spinner(false);
      $scope.pageGo = "";
    });
  }

  // Search role
  $scope.onSearch = function () {
    loadData();
  };

  function searchData() {
    common.spinner(true);
    $scope.lstData = [];
    if (!$scope.lstAllData || $scope.lstAllData.length <= 0) {
      $scope.isNoData = true;
    } else {
      var begin = ($scope.page - 1) * $scope.limit;
      var end = ($scope.page) * $scope.limit;

      end = (end > $scope.lstAllData.length) ? $scope.lstAllData.length : end;

      for (var i = begin; i < end; i++) {
        $scope.lstData.push($scope.lstAllData[i]);
      }

      $scope.isNoData = (!$scope.lstData || $scope.lstData.length <= 0);
    }
    setTimeout(function () {
      common.spinner(false);
    }, 500);
  }

  $scope.onRefresh = function () {
    $scope.limit = 20;
    $scope.page = 1;

    loadData();
    common.btnLoading($('.btnRefresh'), true);
    setTimeout(function () {
      common.btnLoading($('.btnRefresh'), false);
    }, 1000);
  };

  $scope.goToPage = function () {
    $scope.page = $scope.pageGo;
  }

  $scope.$watch('page', function (_newValue, _oldValue) {
    if (_newValue !== _oldValue) {
      loadData();
    }
  }, true);

  $scope.$watch('limit', function (_newValue, _oldValue) {
    if (_newValue !== _oldValue) {
      $scope.page = 1;
      $scope.pageGo = $scope.page;
      loadData();
    }
  }, true);

  //Load data
  //Typehead customer
  $scope.selectedCustomer = "";
  $scope.loadingCustomer = false;
  $scope.loadCustomer = function (val) {
    $scope.loadingCustomer = true;
    var params = {
      "limit": "10",
      "page": "1",
      "search": "" + val
    };

    return $http.post('/customer/search', params).then(function (response) {
      $scope.loadingCustomer = false;
      if (response.data.length <= 0) {
        $scope.noCustomer = true;
        $scope.selectCustomer({});
      } else {
        $scope.noCustomer = false;
        return response.data.map(function (item) {
          return item;
        });
      }
    });
  };

  $scope.selectCustomer = function (obj) {
    $scope.selectedCustomer = obj;
    console.log($scope.selectedCustomer);
  }

  //Typehead employee
  $scope.selectedEmployee = "";
  $scope.loadingEmployee = false;
  $scope.loadEmployee = function (val) {
    $scope.loadingEmployee = true;
    var params = {
      "limit": "10",
      "page": "1",
      "search": "" + val
    };

    return $http.post('/employee/search', params).then(function (response) {
      $scope.loadingEmployee = false;
      if (response.data.length <= 0) {
        $scope.noEmployee = true;
        $scope.selectEmployee({});
      } else {
        $scope.noEmployee = false;
        return response.data.map(function (item) {
          return item;
        });
      }
    });
  };

  $scope.selectEmployee = function (obj) {
    $scope.selectedEmployee = obj;
    console.log($scope.selectedEmployee);
  }

  //Typehead branch
  $scope.selectedBranch = "";
  $scope.loadingBranch = false;
  $scope.loadBranch = function (val) {
    $scope.loadingBranch = true;
    var params = {
      "limit": "10",
      "page": "1",
      "search": "" + val
    };

    return $http.post('/branch/search', params).then(function (response) {
      $scope.loadingBranch = false;
      if (response.data.length <= 0) {
        $scope.noBranch = true;
        $scope.selectBranch({});
      } else {
        $scope.noBranch = false;
        return response.data.map(function (item) {
          return item;
        });
      }
    });
  };

  $scope.selectBranch = function (obj) {
    $scope.selectedBranch = obj;
    console.log($scope.selectedBranch);
  }

  //DATETIME PICKER
  var that = this;

  $scope.isOpen = false;
  $scope.reportTime = new Date();

  $scope.dateOptions = {
    minMode: "month",
    formatYear: 'yyyy',
  };


  $scope.openCalendar = function () {
    $scope.isOpen = true;
  };

  function validateForm() {
    $scope.reportTime.setDate(1);
    var params = {
      "type": $scope.typeReport,
      "id": "",
      "reportTime": formatStringDateToParam($scope.reportTime)
    };

    if ($scope.typeReport == "0") {
      if (!$scope.selectedEmployee || !$scope.selectedEmployee.id) {
        common.notifyWarning($translate.instant('pleaseChooseEmployee'));
        return false;
      }
      params.id = $scope.selectedEmployee.id;
    }
    if ($scope.typeReport == "1") {
      if (!$scope.selectedCustomer || !$scope.selectedCustomer.id) {
        common.notifyWarning($translate.instant('pleaseChooseCustomer'));
        return false;
      }
      params.id = $scope.selectedCustomer.id;
    }
    if ($scope.typeReport == "2") {
      if (!$scope.selectedBranch || !$scope.selectedBranch.id) {
        common.notifyWarning($translate.instant('pleaseChooseBranch'));
        return false;
      }
      params.id = $scope.selectedBranch.id;
    }
    return params;
  }

  $scope.onReport = function () {
    var params = validateForm();

    if (params === false) {
      return false;
    }

    common.spinner(true);
    $http({
      url: '/report',
      method: "POST",
      data: params, //this is your json data string
      headers: {
        'Content-type': 'application/json'
      },
      responseType: 'arraybuffer'
    }).then(function (data, status, headers, config) {
      common.spinner(false);
      var blob = new Blob([data.data], {type: "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"});
      var objectUrl = URL.createObjectURL(blob);
      window.open(objectUrl);
    }), function error(data, status, headers, config) {
      //upload failed
      common.spinner(false);
    };
  };


});