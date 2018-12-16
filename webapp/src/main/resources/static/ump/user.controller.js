UserWebApp.controller('UsersController', function ($scope, $rootScope, HttpService, $translate) {

  $scope.params = [];
  $scope.roles = [];
  $scope.header = true;
  $scope.headerAddEdit = false;
  $scope.headerInfo = false;
  $scope.checklistTable = {
    user: [],
    checkAll: false
  };

  $scope.goToPage = function () {
    $scope.params.page = $scope.page;
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

    getData();

  }

  function getData() {

    HttpService.getData('/users/get-data-search', {}).then(function (response) {
      $scope.roles = JSON.parse(response.roles);
      loadDataList();

    });
  }

  function loadDataList() {

    HttpService.getData('/users/search', $scope.params).then(function (response) {
      $scope.userList = response.userList;
      $scope.totalElements = response.totalItem;
      $scope.userLogin = response.userLogin;
      $scope.page = $scope.params.page;
    });
  }

  $scope.recoverPassword = function (userName, email) {
    $scope.reUserName = userName;
    $scope.reEmail = email;
    $('.modalRecoverPassword').modal('show');
  };

  $scope.onRecoverPassword = function () {
    $scope.param1 = [];
    $scope.param1.reUserName = $scope.reUserName;
    $scope.param1.reEmail = $scope.reEmail;
    $scope.param1.redirectUrl = location.origin;
    HttpService.getData('/users/forgot-password', $scope.param1).then(function (data1) {
      $('.modalRecoverPassword').modal('hide');
      if (data1 == 200) {
        common.notifySuccess($translate.instant('recoverPasswordSuccess'));
      } else {
        common.notifyError($translate.instant('recoverPasswordError'));
      }
    });
  };

  $scope.onUserSearch = function (item) {
    loadDataList();
  };


  $scope.$watch('params.page', function (_newValue, _oldValue) {
    // console.log('_oldValue : '+_oldValue);
    // console.log('_newValue : '+_newValue);
    if (_newValue !== _oldValue) {
      // console.log('params.page is change');
      common.updateUrlRequestParam('page', _newValue);
      loadDataList();
    }
  }, true);

  $scope.$watch('params.limit', function (_newValue, _oldValue) {
    // console.log('params.limit : ');
    if (_newValue !== _oldValue) {
      // console.log('params.limit is change');
      common.updateUrlRequestParam('limit', _newValue);
      loadDataList();
    }
  }, true);

  $scope.deleteUser = function (id) {
    $scope.userNames = [];
    $scope.userNames.push(id);
    $('.modalDelete').modal('show');
  };

  $scope.onDeleteConfirm = function () {
    $scope.param1 = [];
    $scope.param1.userNames = JSON.stringify($scope.userNames);
    HttpService.getData('/users/delete', $scope.param1).then(function (data1) {
      $('.modalDelete').modal('hide');
      if (data1 == 200) {
        $scope.checklistTable.user = [];
        loadDataList();
        if ($scope.userNames.indexOf($scope.userLogin) >= 0) {
          common.notifySuccess($translate.instant('deleteSuccessfullyExceptUserNameUsed') + $scope.userLogin);
        } else {
          common.notifySuccess($translate.instant('deleteSuccessfully'));
        }
      } else {
        common.notifyError($translate.instant('deleteError'));
      }
    });
  };

  $scope.onUserDelete = function () {
    $scope.userNames = [];
    $scope.userNames = $scope.checklistTable.user;
    $('.modalDelete').modal('show');
  };

  $scope.onCheckbox = function () {
    if ($scope.checklistTable.checkAll) {
      $scope.checklistTable.user = $scope.userList.map(function (_item) {
        return _item.userName;
      });
    } else {
      $scope.checklistTable.user = [];
    }
  };

  $scope.onUserRefresh = function () {
    $scope.params = [];
    if ($scope.params.limit == null) {
      $scope.params.limit = '20';
    }
    if ($scope.params.page == null) {
      $scope.params.page = '1';
    }
    $scope.checklistTable.user = [];
    loadDataList();
  };

  $scope.$watch('checklistTable.user', function (_newValue, _oldValue) {
    if (_newValue !== _oldValue) {
      // Update check box all status
      var element = $('.checkAllTable');
      var listChecked = $scope.checklistTable.user;
      var list = $scope.userList.map(function (_item) {
        return _item.userName;
      });
      $scope.checklistTable.checkAll = common.updateCheckBoxAllStatus(element, listChecked, list);
    }
  }, true);

});