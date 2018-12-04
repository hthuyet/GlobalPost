UserWebApp.controller('BillAddController', function ($http, $scope, HttpService, CommonService, $translate, $timeout) {
  $scope.item = {};

  $scope.senderSelected = 11;
  $scope.receiverSelected = "";

  $scope.sender = "";
  $scope.receiver = "";

  // loadData();

  $scope.init = function () {
    loadData();
  }

  $scope.lstEmployee = [];

  function loadData() {
    var params = {
      "limit": "-1",
      "name": ""
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
      $scope.lstEmployee.unshift({"id": 0,"fullName": "Please select"});
      common.spinner(false);
    }, function error(response) {
      $scope.lstEmployee = [];
      console.log(response);
      common.spinner(false);
    });
  }


  function onBeforeSubmit(formElement) {
    // $scope.customerForm.name.$setValidity("ipInvalid", true);

    if (formElement.valid()) {
      var isValid = true;
      common.notifyRemoveAll();
      var message = [];

      // if (!$scope.item.name || !validateIPAddress($scope.item.name)) {
      //   message.push("Invalid IP Address");
      //   $scope.customerForm.name.$setValidity("ipInvalid", false);
      //   $scope.customerForm.name.$valid= false;
      // }

      if (message.length > 0) {
        message.unshift($translate.instant('formInvalid'));
        common.notifyWarning(message.join('</br>'));
        isValid = false;
      }
      return isValid;
    }
    return false;
  }

  $scope.senderObj = {};
  $scope.receiverObj = {};
  $scope.changeSender = function () {
    if ($scope.senderSelected && $scope.senderSelected != "" && $scope.lstCustomer) {
      var obj = {};
      var tmp = {};
      for (var i = 0; i < $scope.lstCustomer.length; i++) {
        tmp = $scope.lstCustomer[i];
        if ($scope.senderSelected == tmp.id) {
          obj = tmp;
          break;
        }
      }

      $scope.item.sendName = ($scope.item.sendName) ? $scope.item.sendName : obj.name;
      $scope.item.sendMobile = ($scope.item.sendMobile) ? $scope.item.sendMobile : obj.mobile;
      $scope.item.address = ($scope.item.address) ? $scope.item.address : obj.mobile;
      $scope.item.sendTime = ($scope.item.sendTime) ? $scope.item.sendTime : "";
      $scope.item.sendDate = ($scope.item.sendDate) ? $scope.item.sendDate : "";
      $scope.item.sendBy = ($scope.item.sendBy) ? $scope.item.sendBy : "";
    }
  }

  $scope.changeReceiver = function () {
    if ($scope.receiverSelected && $scope.receiverSelected != "" && $scope.lstCustomer) {
      var obj = {};
      var tmp = {};
      for (var i = 0; i < $scope.lstCustomer.length; i++) {
        tmp = $scope.lstCustomer[i];
        if ($scope.receiverSelected == tmp.id) {
          obj = tmp;
          break;
        }
      }
      $scope.item.receiveName = ($scope.item.receiveName) ? $scope.item.receiveName : obj.name;
      $scope.item.receiveMobile = ($scope.item.receiveMobile) ? $scope.item.receiveMobile : obj.mobile;
      $scope.item.receiveAddress = ($scope.item.receiveAddress) ? $scope.item.receiveAddress : obj.mobile;
      $scope.item.receiveTime = ($scope.item.receiveTime) ? $scope.item.receiveTime : "";
      $scope.item.receiveDate = ($scope.item.receiveDate) ? $scope.item.receiveDate : "";
      $scope.item.receiveBy = ($scope.item.receiveBy) ? $scope.item.receiveBy : "";
    }
  }

  $scope.onSubmitFrm = function () {
    var formElement = $('#billFrm');
    var btnSubmitElement = $('#btnSubmit');

    console.log("--------1----------");
    if (!onBeforeSubmit(formElement)) {
      return false;
    }
    console.log($scope.item);

    var params = {};
    if ($scope.item.id) {
      params.id = $scope.item.id;
    }

    params.billNo = $scope.item.billNo;
    params.billType = $scope.item.billType;
    params.weight = $scope.item.weight;
    params.cost = $scope.item.cost;
    params.totalCost = $scope.item.totalCost;
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
    params.saveSender = ($scope.item.saveSender) ? "1" : "0";
    params.saveReceiver = ($scope.item.saveReceiver) ? "1" : "0";
    params.employeeSend = ($scope.item.employeeSend) ? $scope.item.employeeSend : "0";
    params.employeeReceive = ($scope.item.employeeReceive) ? $scope.item.employeeReceive : "0";
    console.log(params);

    if ($scope.id != null && $scope.id != '') {
      HttpService.postData('/bill/save', params, btnSubmitElement).then(function (response) {
        common.notifySuccess($translate.instant('saveSuccessfully'));
        // location.replace('/bill');
      }, function error(response) {
        common.notifyError($translate.instant('saveError'));
        console.log(response);
      });

    } else {
      HttpService.postData('/bill/save', params, btnSubmitElement).then(function (response) {
        common.notifySuccess($translate.instant('saveSuccessfully'));
        // location.replace('/bill');
      }, function error(response) {
        common.notifyError($translate.instant('saveError'));
        console.log(response);
      });
    }
  };


  function renderEdit() {
    var data = $('.dataResponse').attr('data-data');
    if (data) {
      common.spinner(true);

      $timeout( function(){
        $scope.item = JSON.parse(data);
        console.log($scope.item);

        $scope.testFrm();
        common.spinner(false);
      }, 1000);
    }
  }

  //Test

  $scope.$on('$viewContentLoaded', function () {
    console.log("---$viewContentLoaded-----");
    renderEdit();
  });

  angular.element(function () {
    console.log('page loading completed');
    // renderEdit();
  });

  $scope.testFrm = function () {
    console.log("-------testFrm---------")
    // $scope.senderSelected = 11;
    // $scope.receiverSelected = 11;

    $scope.changeSender();
    $scope.changeReceiver();
  }



  $scope.chooseCustomer = function (type) {
    $("#modalChooseUserType").val(type);
    $('.modalChooseUser').modal('show');
  }

  $scope.$on('modalChooseUserHide', function (event, data) {
    var obj = data.data;
    if(data.type == 0 || data.type == "0"){
      //Sender
      $scope.item.sendCustomer = obj.id;
      $scope.item.sendName = ($scope.item.sendName) ? $scope.item.sendName : obj.name;
      $scope.item.sendName = ($scope.item.sendName) ? $scope.item.sendName : obj.name;
      $scope.item.sendMobile = ($scope.item.sendMobile) ? $scope.item.sendMobile : obj.mobile;
      $scope.item.sendAddress = ($scope.item.sendAddress) ? $scope.item.sendAddress : obj.address;
      $scope.item.sendBy = ($scope.item.sendBy) ? $scope.item.sendBy : "";
    }else{
      //receiverName
      $scope.item.receiveCustomer = obj.id;
      $scope.item.receiveName = ($scope.item.receiveName) ? $scope.item.receiveName : obj.name;
      $scope.item.receiveMobile = ($scope.item.receiveMobile) ? $scope.item.receiveMobile : obj.mobile;
      $scope.item.receiveAddress = ($scope.item.receiveAddress) ? $scope.item.receiveAddress : obj.address;
      $scope.item.receiveBy = ($scope.item.receiveBy) ? $scope.item.receiveBy : "";
    }
    console.log(data);
  });

  $scope.testChangeSend = function () {
    console.log("employeeSend: " + $scope.item.employeeSend);
  }

  $scope.testChangeReceive = function () {
    console.log("employeeReceive: " + $scope.item.employeeReceive);
  }

});
