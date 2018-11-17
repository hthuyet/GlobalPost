UserWebApp.controller('BillAddController', function ($http, $scope, HttpService, CommonService, $translate) {
  $scope.item = {};

  function renderEdit() {
    var data = $('.dataResponse').attr('data-data');
    if (data) {
      $scope.item = JSON.parse(data);
      console.log($scope.item);
    }
  }

  $scope.sender = "";
  $scope.receiver = "";

  renderEdit();

  getCustomer();

  function getCustomer(){
    var params = {
      "limit": "-1",
      "name": ""
    };

    HttpService.postData('/customer/search', params).then(function (response) {
      $scope.lstCustomer = response;
      common.spinner(false);
    },function error(response) {
      console.log(response);
      common.spinner(false);
    });
  }


  function onBeforeSubmit(formElement) {
    // $scope.customerForm.name.$setValidity("ipInvalid", true);

    console.log("---------------1.1----------")
    if (formElement.valid()) {
      console.log("---------------1.2----------")
      var isValid = true;
      common.notifyRemoveAll();
      var message = [];

      // if (!$scope.item.name || !validateIPAddress($scope.item.name)) {
      //   message.push("Invalid IP Address");
      //   $scope.customerForm.name.$setValidity("ipInvalid", false);
      //   $scope.customerForm.name.$valid= false;
      // }

      console.log("---------------1.3----------")
      if (message.length > 0) {
        console.log("---------------1.4----------")
        message.unshift($translate.instant('formInvalid'));
        common.notifyWarning(message.join('</br>'));
        isValid = false;
      }
      console.log("---------------1.5----------")
      return isValid;
    }
    console.log("---------------1.6----------")
    return false;
  }

  $scope.senderObj = {};
  $scope.receiverObj = {};
  $scope.changeSender = function () {
    console.log($scope.sender);
    if($scope.sender && $scope.sender != ""){
      $scope.senderObj= JSON.parse($scope.sender);
    }
  }

  $scope.changeReceiver = function () {
    if($scope.receiver && $scope.receiver != ""){
      $scope.receiverObj= JSON.parse($scope.receiver);
    }
  }

  $scope.onSubmitFrm = function () {
    var formElement = $('#billFrm');
    var btnSubmitElement = $('#btnSubmit');

    console.log("--------1----------");
    if (!onBeforeSubmit(formElement)) {
      return false;
    }
    console.log("--------2----------");

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

    if ($scope.id != null && $scope.id != '') {
      HttpService.postData('/bill/save', params, btnSubmitElement).then(function (response) {
        common.notifySuccess($translate.instant('saveSuccessfully'));
        location.replace('/bill');
      }, function error(response) {
        common.notifyError($translate.instant('saveError'));
        console.log(response);
      });

    } else {
      HttpService.postData('/bill/save', params, btnSubmitElement).then(function (response) {
        common.notifySuccess($translate.instant('saveSuccessfully'));
        location.replace('/bill');
      }, function error(response) {
        common.notifyError($translate.instant('saveError'));
        console.log(response);
      });
    }
  };


});
