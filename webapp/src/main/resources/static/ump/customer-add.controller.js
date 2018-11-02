UserWebApp.controller('CustomerAddController', function ($http, $scope, HttpService, CommonService, $translate) {
  $scope.item = {};

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
  }

  renderEdit();


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
        common.notifyError($translate.instant('saveError'));
        console.log(response);
      });

    } else {
      HttpService.postData('/customer/save', params, btnSubmitElement).then(function (response) {
        common.notifySuccess($translate.instant('saveSuccessfully'));
        location.replace('/customer');
      }, function error(response) {
        common.notifyError($translate.instant('saveError'));
        console.log(response);
      });
    }
  };


});