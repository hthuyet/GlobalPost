UserWebApp.controller('BillFormController', function ($http, $rootScope, $scope, HttpService, CommonService, $translate, $timeout, $http, limitToFilter) {

  console.log("----BillFormController-------");
  resetFrm();

  // loadData();

  $scope.init = function () {
    loadData();
  }

  function resetFrm() {
    $scope.billNo = "";
    $scope.asyncSelected = {
      "id": "",
      "name": "",
      "mobile": "",
      "address": ""
    };

    $scope.receiverSelected = {
      "id": "",
      "name": "",
      "mobile": "",
      "address": ""
    };
    $scope.item = {
      "billNo": "",
      "billType": "",
      "weight": "",
      "cost": "",
      "totalCost": "",
      "content": "",
      "paid": "",
      "isCod": "",
      "codValue": "",
      "billState": "",
      "whoPay": "",
      "userCreate": "",
      "branchCreate": "",
      "currentBranch": "",
      "partnerId": "",
      "employeeSend": "",
      "employeeReceive": "",
      "sendCustomer": -1,
      "sendName": "",
      "sendAddress": "",
      "sendMobile": "",
      "sendTime": "",
      "sendDate": "",
      "sendBy": "",
      "receiveCustomer": -1,
      "receiveName": "",
      "receiveAddress": "",
      "receiveMobile": "",
      "receiveTime": "",
      "receiveDate": "",
      "receiveBy": "",
      "isCod": "0",
      "saveSender": "0",
      "saveReceiver": "0",
      "employeeSend": "0",
      "employeeReceive": "0",
    }
    $("#billNo").focus();
  }

  $scope.onSearch = function () {
    if ($scope.billNo != "") {
      common.spinner(true);
      HttpService.postDataNoError('/bill/detail/' + $scope.billNo, {}).then(function (response) {
        common.spinner(false);
        $timeout(function () {
          $scope.item = response;
          $scope.testFrm();
          common.spinner(false);
        }, 1000);
      }, function error(response) {
        //
        $scope.item.billNo = $scope.billNo;
        $scope.item.billType = "-1";
        $scope.item.weight = "";
        $scope.item.cost = "";
        $scope.item.totalCost = "";
        $scope.item.content = "";
        $scope.item.paid = "";
        $scope.item.isCod = "";
        $scope.item.codValue = "";
        $scope.item.billState = "";
        $scope.item.whoPay = "";
        $scope.item.userCreate = "";
        $scope.item.branchCreate = "";
        $scope.item.currentBranch = "";
        $scope.item.partnerId = "";
        $scope.item.employeeSend = "";
        $scope.item.employeeReceive = "";
        $scope.item.sendCustomer = "";
        $scope.item.sendName = "";
        $scope.item.sendAddress = "";
        $scope.item.sendMobile = "";
        $scope.item.sendTime = "";
        $scope.item.sendDate = "";
        $scope.item.sendBy = "";
        $scope.item.receiveCustomer = "";
        $scope.item.receiveName = "";
        $scope.item.receiveAddress = "";
        $scope.item.receiveMobile = "";
        $scope.item.receiveTime = "";
        $scope.item.receiveDate = "";
        $scope.item.receiveBy = "";
        $scope.item.isCod = "0";
        $scope.item.saveSender = "0";
        $scope.item.saveReceiver = "0";
        $scope.item.employeeSend = "0";
        $scope.item.employeeReceive = "0";

        $scope.asyncSelected = {
          "id": "",
          "name": "",
          "mobile": "",
          "address": ""
        };

        $scope.receiverSelected = {
          "id": "",
          "name": "",
          "mobile": "",
          "address": ""
        };

        console.log(response);
        common.spinner(false);

        //$("#billNo").focus();
      });
    }
  }

  $scope.lstEmployee = [];

  function loadData() {
    var params = {
      "limit": "-1",
      "search": ""
    };

    HttpService.postData('/customer/search', params).then(function (response) {
      $scope.lstCustomer = response;
      renderEdit();
      common.spinner(false);
    }, function error(response) {
      console.log(response);
      common.spinner(false);
    });


    //employee
    params = {
      "limit": "0",
      "page": "0",
    };

    HttpService.postData('/employee/search', params).then(function (response) {
      $scope.lstEmployee = response;
      //$scope.lstEmployee.unshift({"id": 0,"fullName": "Vui lòng chọn"});
      common.spinner(false);
    }, function error(response) {
      $scope.lstEmployee = [];
      console.log(response);
      common.spinner(false);
    });
  }


  function onBeforeSubmit(formElement) {
    if (formElement.valid()) {
      var isValid = true;
      common.notifyRemoveAll();
      var message = [];

      if (message.length > 0) {
        message.unshift($translate.instant('formInvalid'));
        common.notifyWarning(message.join('</br>'));
        isValid = false;
      }
      return isValid;
    }
    return false;
  }

  function formatCurrencyToNumber(value) {
    return value.replace(/[\s]/g, '');
  }

  $scope.onSubmitFrm = function () {
    var formElement = $('#billFrm');
    var btnSubmitElement = $('#btnSubmit');

    if (!onBeforeSubmit(formElement)) {
      return false;
    }
    console.log($scope.item);

    var params = {};
    if ($scope.item.id) {
      params.id = $scope.item.id;
    }

    if (!$scope.item || !$scope.item.billNo) {
      $scope.item.billNo = $scope.billNo;
    }

    params.billNo = $scope.item.billNo;
    params.billType = $scope.item.billType;
    params.weight = $scope.item.weight;

    params.cost = formatCurrencyToNumber($scope.item.cost);
    params.totalCost = formatCurrencyToNumber($scope.item.totalCost);

    params.content = $scope.item.content;
    params.paid = $scope.item.paid;
    params.isCod = $scope.item.isCod;
    params.codValue = $scope.item.codValue;
    params.billState = $scope.item.billState;
    params.whoPay = $scope.item.whoPay;
    params.userCreate = $scope.item.userCreate;
    params.branchCreate = $scope.item.branchCreate;
    params.currentBranch = $scope.item.currentBranch;
    params.partnerId = $scope.item.partnerId;
    params.employeeSend = $scope.item.employeeSend;
    params.employeeReceive = $scope.item.employeeReceive;
    params.sendCustomer = $scope.item.sendCustomer;
    params.sendName = $scope.item.sendName;
    params.sendAddress = $scope.item.sendAddress;
    params.sendMobile = $scope.item.sendMobile;
    params.sendTime = $scope.item.sendTime;
    params.sendDate = $scope.item.sendDate;
    params.sendBy = $scope.item.sendBy;
    params.receiveCustomer = $scope.item.receiveCustomer;
    params.receiveName = $scope.item.receiveName;
    params.receiveAddress = $scope.item.receiveAddress;
    params.receiveMobile = $scope.item.receiveMobile;
    params.receiveTime = $scope.item.receiveTime;
    params.receiveDate = $scope.item.receiveDate;
    params.receiveBy = $scope.item.receiveBy;
    params.isCod = ($scope.item.isCod) ? "1" : "0";
    params.whoPay = ($scope.item.whoPay) ? "1" : "0";
    params.payType = ($scope.item.payType) ? "1" : "0";
    params.saveSender = ($scope.item.saveSender) ? "1" : "0";
    params.saveReceiver = ($scope.item.saveReceiver) ? "1" : "0";
    params.employeeSend = ($scope.item.employeeSend) ? $scope.item.employeeSend : "0";
    params.employeeReceive = ($scope.item.employeeReceive) ? $scope.item.employeeReceive : "0";
    if (params.isCod == 1 || params.isCod == "1") {
      params.codValue = $scope.item.codValue;
    }


    if ($scope.senderDateTime) {
      params.sendDate = formatDate($scope.senderDateTime);
      params.sendTime = formatTime($scope.senderDateTime);
    }

    if ($scope.senderDateTime) {
      params.receiveDate = formatDate($scope.receiverDateTime);
      params.receiveTime = formatTime($scope.receiverDateTime);
    }

    if ($scope.id != null && $scope.id != '') {
      HttpService.postData('/bill/save', params, btnSubmitElement).then(function (response) {
        common.notifySuccess($translate.instant('saveSuccessfully'));
        $rootScope.$broadcast('saveSuccessfully', {});
        resetFrm();
      }, function error(response) {
        common.notifyError($translate.instant('saveError'));
        console.log(response);
      });

    } else {
      HttpService.postData('/bill/save', params, btnSubmitElement).then(function (response) {
        common.notifySuccess($translate.instant('saveSuccessfully'));
        $rootScope.$broadcast('saveSuccessfully', {});
        resetFrm();
      }, function error(response) {
        common.notifyError($translate.instant('saveError'));
        console.log(response);
      });
    }
  };

  function formatTime(date) {
    return date.getHours() + ":" + date.getMinutes();
  }

  function formatDate(date) {
    var rtn = "";
    var sdate = date.getDate();
    var month = date.getMonth() + 1;
    if (sdate < 10) {
      rtn += "0" + sdate;
    } else {
      rtn += sdate;
    }
    if (month < 10) {
      rtn += "/0" + month;
    } else {
      rtn += "/" + month;
    }
    rtn += "/" + date.getFullYear();
    return rtn;
  }

  $scope.testFrm = function () {
    $scope.asyncSelected = {
      "id": $scope.item.sendCustomer,
      "name": $scope.item.sendName,
      "mobile": $scope.item.sendMobile,
      "address": $scope.item.sendAddress
    };

    $scope.receiverSelected = {
      "id": $scope.item.receiveCustomer,
      "name": $scope.item.receiveName,
      "mobile": $scope.item.receiveMobile,
      "address": $scope.item.receiveAddress
    };
  }

  //Typehead
  $scope.loadingSender = false;
  $scope.getSender = function (val) {
    $scope.loadingSender = true;
    var params = {
      "limit": "10",
      "page": "1",
      "search": "" + val
    };

    return $http.post('/customer/search', params).then(function (response) {
      $scope.loadingSender = false;
      console.log(response.data.length);
      if (response.data.length <= 0) {
        console.log("---selectSender---");
        $scope.noSender = true;
        $scope.selectSender({});
      } else {
        $scope.noSender = false;
        return response.data.map(function (item) {
          return item;
        });
      }
    });
  };

  $scope.loadingReceiver = false;
  $scope.searchReceiver = function (val) {
    $scope.loadingReceiver = true;
    var params = {
      "limit": "10",
      "page": "1",
      "search": "" + val
    };

    return $http.post('/customer/search', params).then(function (response) {
      $scope.loadingReceiver = false;
      console.log(response.data.length);
      if (response.data.length <= 0) {
        $scope.noReceiver = true;
        $scope.selectReceiver({});
      } else {
        $scope.noReceiver = false;
        return response.data.map(function (item) {
          return item;
        });
      }
    });
  };

  $scope.selectSender = function (obj) {
    console.log("------selectSender obj: " + Object.keys(obj).length);
    if (Object.keys(obj).length > 0) {
      $scope.item.sendCustomer = obj.id;
      $scope.item.sendName = obj.name;
      $scope.item.sendMobile = obj.mobile;
      $scope.item.sendAddress = obj.address;
      $scope.item.saveSender = false;
    } else {
      $scope.item.sendCustomer = -1;
      $scope.item.sendName = "";
      $scope.item.sendMobile = "";
      $scope.item.sendAddress = "";
      $scope.item.saveSender = true;
    }
  }

  $scope.receiverSelected = "";

  $scope.selectReceiver = function (obj) {
    console.log("--------selectReceiver---------");
    if (Object.keys(obj).length > 0) {
      $scope.item.receiveCustomer = obj.id;
      $scope.item.receiveName = obj.name;
      $scope.item.receiveMobile = obj.mobile;
      $scope.item.receiveAddress = obj.address;
      $scope.item.saveReceiver = false;
    } else {
      $scope.item.receiveCustomer = -1;
      $scope.item.receiveName = "";
      $scope.item.receiveMobile = "";
      $scope.item.receiveAddress = "";
      $scope.item.saveReceiver = true;
    }
  }


  //Place
  $scope.loadingSendSelected = false;
  $scope.searchSendEmployee = function (val) {
    $scope.loadingSendSelected = true;
    var params = {
      "limit": "10",
      "page": "1",
      "search": "" + val
    };

    return $http.post('/employee/search', params).then(function (response) {
      $scope.loadingSendSelected = false;
      return response.data.map(function (item) {
        return item;
      });
    });
  };

  $scope.loadingReceiveSelected = false;
  $scope.searchEmployee = function (val) {
    $scope.loadingReceiveSelected = true;
    var params = {
      "limit": "10",
      "page": "1",
      "search": "" + val
    };

    return $http.post('/employee/search', params).then(function (response) {
      $scope.loadingReceiveSelected = false;
      return response.data.map(function (item) {
        return item;
      });
    });
  };

  //DATETIME PICKER
  var that = this;

  $scope.isOpen = false;
  $scope.receiverDateTime = new Date();

  $scope.openCalendar = function (e) {
    e.preventDefault();
    e.stopPropagation();
    $scope.isOpen = true;
  };


  $scope.isOpenSender = false;
  $scope.senderDateTime = new Date();
  $scope.openCalendarSender = function (e) {
    e.preventDefault();
    e.stopPropagation();
    $scope.isOpenSender = true;
  };

});
