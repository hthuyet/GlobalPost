UserWebApp.controller('EmployeeAddController', function ($http, $scope, HttpService, CommonService, $translate) {
  $scope.item = {};

  function renderEdit() {
    var data = $('.dataResponse').attr('data-data');
    if (data) {
      $scope.item = JSON.parse(data);
    }

    // $scope.id = $('.dataResponse').attr('data-id');
    // if ($scope.id != null && $scope.id != '') {
    //     $scope.item.id = $('.dataResponse').attr('data-id');
    // }
    // $scope.item.name = $('.dataResponse').attr('data-name');
    // $scope.item.address = $('.dataResponse').attr('data-address');
    // $scope.item.mobile = $('.dataResponse').attr('data-mobile');
    // $scope.item.email = $('.dataResponse').attr('data-email');
    // $scope.item.branchId = $('.dataResponse').attr('data-branchId');
  }

  //renderEdit();

  function getDataCommon() {
    common.spinner(true);
    var params = {
      "limit": "-1",
      "name": ""
    };
    HttpService.postData('/branch/search', params).then(function (response) {
      $scope.lstDataCommon = response.map(function (item) {
        return {"id": item.id, "name": item.name};
      });
      common.spinner(false);
      renderEdit();
    }, function error(response) {
      $scope.lstDataCommon = [];
      console.log(response);
      common.spinner(false);
    });
  }

  getDataCommon();

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
    var formElement = $('#employeeForm');
    var btnSubmitElement = $('#btnSubmit');

    if (!onBeforeSubmit(formElement)) {
      return false;
    }

    var params = {};
    if ($scope.item.id) {
      params.id = $scope.item.id;
    }
    params.fullName = $scope.item.fullName;
    params.address = $scope.item.address;
    params.mobile = $scope.item.mobile;
    params.branchId = $scope.item.branchId;

    if ($scope.id != null && $scope.id != '') {
      HttpService.postData('/employee/save', params, btnSubmitElement).then(function (response) {
        common.notifySuccess($translate.instant('saveSuccessfully'));
        location.replace('/employee');
      }, function error(response) {
        common.notifyError($translate.instant('saveError'));
        console.log(response);
      });

    } else {
      HttpService.postData('/employee/save', params, btnSubmitElement).then(function (response) {
        common.notifySuccess($translate.instant('saveSuccessfully'));
        location.replace('/employee');
      }, function error(response) {
        common.notifyError($translate.instant('saveError'));
        console.log(response);
      });
    }
  };


});