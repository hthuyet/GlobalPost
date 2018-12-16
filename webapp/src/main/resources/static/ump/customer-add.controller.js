UserWebApp.controller('CustomerAddController', function ($http, $scope, HttpService, CommonService, $translate) {
  $scope.item = {};
  $scope.lstUser = [];

  function renderEdit() {
    $scope.id = $('.dataResponse').attr('data-id');
    if ($scope.id != null && $scope.id != '') {
      $scope.item.id = $('.dataResponse').attr('data-id');
    }
    $scope.item.code = $('.dataResponse').attr('data-code');
    $scope.item.name = $('.dataResponse').attr('data-name');
    $scope.item.taxCode = $('.dataResponse').attr('data-taxCode');
    $scope.item.taxAddress = $('.dataResponse').attr('data-taxAddress');
    $scope.item.address = $('.dataResponse').attr('data-address');
    $scope.item.mobile = $('.dataResponse').attr('data-mobile');
    $scope.item.email = $('.dataResponse').attr('data-email');
    $scope.item.note = $('.dataResponse').attr('data-note');
    $scope.item.userId = $('.dataResponse').attr('data-userId');

    var params = {
      "limit": "0",
      "page": "0",
    };

    HttpService.postData('/users/getall', params).then(function (response) {
      angular.forEach(response, function(value) {
        $scope.lstUser.push({"id": value.userId,"name": (value.branchName + " - " +value.userName + " - " + value.fullName)});
      });
      $scope.lstUser.unshift({"id": 0,"name": "Please select"});
      console.log($scope.lstUser);
    }, function error(response) {
      $scope.lstUser = [];
    });
  }

  renderEdit();


  function onBeforeSubmit(formElement) {
    $scope.customerForm.code.$setValidity("exits", true);

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

  $scope.onSubmitFrm = function () {
    var formElement = $('#customerForm');
    var btnSubmitElement = $('#btnSubmit');

    if (!onBeforeSubmit(formElement)) {
      return false;
    }

    var params = {};
    if ($scope.item.id) {
      params.id = $scope.item.id;
    }
    params.code = $scope.item.code;
    params.name = $scope.item.name;
    params.address = $scope.item.address;
    params.taxCode = $scope.item.taxCode;
    params.taxAddress = $scope.item.taxAddress;
    params.mobile = $scope.item.mobile;
    params.email = $scope.item.email;
    params.note = $scope.item.note;
    params.userId = $scope.item.userId;

    if ($scope.id != null && $scope.id != '') {
      HttpService.postData('/customer/save', params, btnSubmitElement).then(function (response) {
        common.notifySuccess($translate.instant('saveSuccessfully'));
        location.replace('/customer');
      }, function error(response) {
        console.log(response);
        if(response.data != null && (response.data.status == "4" || response.data.status == 4)){
          $scope.customerForm.code.$setValidity("exits", false);
          //$scope.customerForm.code.$valid = false;
          common.notifyError(response.data.message);
        }else{
          common.notifyError($translate.instant('saveError'));
        }
      });

    } else {
      HttpService.postData('/customer/save', params, btnSubmitElement).then(function (response) {
        common.notifySuccess($translate.instant('saveSuccessfully'));
        location.replace('/customer');
      }, function error(response) {
        if(response.data != null && (response.data.status == "4" || response.data.status == 4)){
          $scope.customerForm.code.$setValidity("exits", false);
          $scope.customerForm.code.$valid = false;
          common.notifyError(response.data.message);
        }else{
          common.notifyError($translate.instant('saveError'));
        }
      });
    }
  };


});