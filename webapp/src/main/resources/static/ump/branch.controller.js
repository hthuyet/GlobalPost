UserWebApp.controller('BranchController', function ($scope, $rootScope, HttpService, $translate, $location, $http) {

  $scope.lstData = [];
  $scope.totalElements = 0;

  $scope.limit = 20;
  $scope.page = 1;


  $scope.params = {
    "name": "",
    "limit": 20,
    "page": 1,
  };

  $scope.checklistTable = {
    selected: [],
    checkAll: false
  };

  function loadData() {
    common.spinner(true);
    var params = {
      "limit": "" + $scope.params.limit,
      "page": "" + $scope.params.page,
      "search": "" + $scope.params.name
    };


    HttpService.postData('/branch/search', params).then(function (response) {
      $scope.lstData = response;
      common.spinner(false);
      $scope.page = $scope.params.page;
    }, function error(response) {
      console.log(response);
      common.spinner(false);
      $scope.page = "";
    });

    HttpService.postData('/branch/count', params).then(function (response) {
      $scope.totalElements = response;
      common.spinner(false);
    }, function error(response) {
      console.log(response);
      common.spinner(false);
    });
  }

  $scope.goToPage = function () {
    $scope.params.page = $scope.page;
  }

  loadData();

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

  $scope.multi = false;
  $scope.onDeletes = function () {
    $scope.multi = true;
    $scope.deleteList = [];
    $scope.deleteList = $scope.checklistTable.selected;
    $('.modalDelete').modal('show');
  };

  $scope.onDelete = function (id) {
    $scope.multi = false;
    $scope.deleteList = [];
    $scope.deleteList.push(id);
    $('.modalDelete').modal('show');
  };

  $scope.onDeleteConfirm = function () {
    common.spinner(true);
    var param = {
      "ids": $scope.deleteList.join(",")
    };
    HttpService.getData('/branch/delete', param).then(function (data1) {
      $('.modalDelete').modal('hide');
      $scope.checklistTable.selected = [];
      loadData();
      common.notifySuccess($translate.instant('deleteSuccessfully'));
      common.spinner(false);
    }, function error(response) {
      $('.modalDelete').modal('hide');
      console.log(response);
      common.spinner(false);
      common.notifyError($translate.instant('deleteError'));
    });
  };

  $scope.onCheckbox = function () {
    if ($scope.checklistTable.checkAll) {
      $scope.checklistTable.selected = $scope.lstData.map(function (_item) {
        return _item.id;
      });
    } else {
      $scope.checklistTable.selected = [];
    }
  };

  $scope.$watch('checklistTable.selected', function (_newValue, _oldValue) {
    if (_newValue !== _oldValue) {
      // Update check box all status
      var element = $('.checkAllTable');
      var listChecked = $scope.checklistTable.selected;
      var list = $scope.lstData.map(function (_item) {
        return _item.id;
      });
      $scope.checklistTable.checkAll = common.updateCheckBoxAllStatus(element, listChecked, list);
    }
  }, true);

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


  //For export
  $scope.dt = new Date();
  $scope.format = "dd/MM/yyyy";
  $scope.options = {
    type: "month",
  };

  $scope.report = function (item) {
    $scope.export = {
      "id": item.id,
      "type": 3
    };
    $('.modalExport').modal('show');
  };

  $scope.popup = {opened: false};
  $scope.open = function () {
    $scope.popup.opened = true;
  };

  $scope.onExport = function () {
    common.spinner(true);
    var params = {
      "type": $scope.export.type,
      "id": $scope.export.id
    };

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
  }

});