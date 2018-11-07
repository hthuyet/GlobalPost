UserWebApp.controller('BillAddController', function ($http, $scope, HttpService, CommonService, $translate) {
  $scope.item = {};

  function renderEdit() {
    var data = $('.dataResponse').attr('data-data');
    if (data) {
      $scope.item = JSON.parse(data);
      console.log($scope.item);
    }
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
    var formElement = $('#branchFrm');
    var btnSubmitElement = $('#btnSubmit');

    if (!onBeforeSubmit(formElement)) {
      return false;
    }

    var params = {};
    if ($scope.item.id) {
      params.id = $scope.item.id;
    }
    params.name = $scope.item.name;
    params.address = $scope.item.address;
    params.hotline = $scope.item.hotline;

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