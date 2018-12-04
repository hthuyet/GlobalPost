UserWebApp.controller('ModalCustomerController', function ($scope, $rootScope, HttpService, $translate, $location, $filter) {

  $scope.lstData = [];
  $scope.totalElements = 0;

  $scope.params = [];
  $scope.params.name = '';
  $scope.params.limit = 20;
  $scope.params.page = 1;

  $scope.checklistTable = {
    selected: [],
    checkAll: false
  };

  function loadData() {
    common.spinner(true);

    var params = {
      "limit": "" + $scope.params.limit,
      "page": "" + $scope.params.page,
      "name": "" + $scope.params.name
    };

    HttpService.postData('/customer/search', params).then(function (response) {
      $scope.lstData = response;
      common.spinner(false);
    }, function error(response) {
      console.log(response);
      common.spinner(false);
    });

    HttpService.postData('/customer/count', params).then(function (response) {
      $scope.totalElements = response;
      common.spinner(false);
    }, function error(response) {
      console.log(response);
      common.spinner(false);
    });
  }

  renderDefaultRequestParams();

  function renderDefaultRequestParams() {

    var limit = common.getUrlRequestParam('limit');
    if (limit != null) {
      $scope.params.limit = limit;
    } else {
      $scope.params.limit = '20';
    }

    var page = common.getUrlRequestParam('page');
    if (page != null) {
      $scope.params.page = parseInt(page);
    } else {
      $scope.params.page = '1';
    }

    if (['20', '30', '50'].indexOf($scope.params.limit) < 0) {
      setTimeout(function () {
        $scope.$apply(function () {
          $scope.params.limit = '20';
          common.notifyWarning($translate.instant('requestParamError'));
        });
      }, 1);
    }

    loadData();

  }

  // Search role
  $scope.onSearch = function () {
    loadData();
  };

  $scope.onRefresh = function () {
    $scope.params = {};
    $scope.params.limit = '20';
    $scope.params.page = '1';
    $scope.params.name = '';

    $scope.checklistTable = {
      selected: [],
      checkAll: false
    };


    loadData();
    common.btnLoading($('.btnRefresh'), true);
    setTimeout(function () {
      common.btnLoading($('.btnRefresh'), false);
    }, 1000);
  };

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


  $scope.chooseCustomer = function (item) {
    var type = $("#modalChooseUserType").val();
    if (type == "0") {
      $('#senderChoose').data('info', JSON.stringify(item)); //setter
    } else {
      $('#receiverChoose').data('info', JSON.stringify(item)); //setter
    }
    $('.modalChooseUser').modal('hide');

    $rootScope.$broadcast('modalChooseUserHide', {data: item,type: type});
  }
});