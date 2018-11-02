UserWebApp.controller('RoleAddController', function ($http, $scope, HttpService, CommonService, $translate) {

  $scope.permissionsList = '';
  $scope.myParameter = {
    value: ''
  };
  $scope.params = [];
  $scope.params.roleName = '';
  $scope.params.roleDes = '';

  function renderEdit() {
    $scope.id = $('.dataResponse').attr('data-id');
    if ($scope.id != null && $scope.id != '') {
      $scope.params.roleId = $('.dataResponse').attr('data-id');
    }
    $scope.name = $('.dataResponse').attr('data-name');
    $scope.operationIds = $('.dataResponse').attr('data-operationIds');
    $scope.permissionsIds = $('.dataResponse').attr('data-permissionsIds');
    $scope.description = $('.dataResponse').attr('data-description');
    $scope.params.roleName = $('.dataResponse').attr('data-name');
    $scope.params.roleDes = $('.dataResponse').attr('data-description');

    $scope.editValue = {
      'id': $scope.id,
      'name': $scope.name,
      'operationIds': $scope.operationIds,
      'description': $scope.description,
      'permissionsIds': $scope.permissionsIds
    };
  }

  renderEdit();


  function loadData() {
    HttpService.getData('/roles/load-permission', {}).then(function (response) {
      if (response != null && response != '') {
        $scope.permissionsList = JSON.stringify(response);
      }
    });
  }

  function validateRole(myParameter) {
    if (myParameter == '') {
      return false;
    }
    return true;
  }

  function listAllPermissions(listParameterObjectChecked) {
    var totalParam = '';
    var listChecked = listParameterObjectChecked.toString().substring(1);
    var operationIdsArray = [];
    if (listChecked.indexOf(',~') > -1) {
      operationIdsArray = listChecked.split(',~');
    } else {
      operationIdsArray.push(listChecked);
    }

    var tree = $("#tree").fancytree("getTree");
    var d = tree.toDict(true);
    var d1 = jQuery.parseJSON(JSON.stringify(d));
    for (var i = 0; i < operationIdsArray.length; i++) {
      if (operationIdsArray[i].indexOf('-') == -1) {
        // node
        for (var j = 0; j < d1.children.length; j++) {
          if (d1.children[j].key == operationIdsArray[i]) {
            var rootFather = d1.children[j].children;
            for (var k = 0; k < rootFather.length; k++) {
              totalParam += '@' + rootFather[k].key;
            }
          }
        }
      } else {
        // child
        totalParam += '@' + operationIdsArray[i];
      }
    }
    totalParam = totalParam.substring(1);
    return totalParam;
  }

  loadData();

  $scope.onRoleAdd = function () {

    var isValid = validateRole($scope.myParameter.value);
    if (isValid) {

      if ($scope.id != null && $scope.id != '') {

        var totalPermissions = listAllPermissions($scope.myParameter.value);
        var params = {};
        params.addId = $scope.id;
        params.addNameRole = $scope.params.roleName;
        params.addDescriptionRole = $scope.params.roleDes;
        params.addOperationRole = "";
        params.addPermission = totalPermissions + '*';

        HttpService.postData('/editRole', params, $("#btnSubmit")).then(function (response) {
          if (response == 200) {
            common.notifySuccess($translate.instant('editRoleSuccess'));
            location.replace('/roles');
          } else if (response == 201) {
            common.notifyError($translate.instant('addRoleExits'));
          } else {
            common.notifyError($translate.instant('editRoleFail'));
          }
        });

      } else {
        var totalPermissions = listAllPermissions($scope.myParameter.value);
        var params = {};
        params.addNameRole = $scope.params.roleName;
        params.addDescriptionRole = $scope.params.roleDes;
        params.addOperationRole = "";
        params.addPermission = totalPermissions + '*';

        HttpService.postData('/addRole', params, $("#btnSubmit")).then(function (response) {
          if (response == 200) {
            common.notifySuccess($translate.instant('addRoleSuccess'));
            location.replace('/roles');
          } else if (response == 201) {
            common.notifyError($translate.instant('addRoleExits'));
          } else {
            common.notifyError($translate.instant('addRoleFail'));
          }
        });
      }

    } else {
      common.notifyWarning($translate.instant('addRoleEmpty'));
    }
  };


})
  .directive('postprocess', function () {
    var getTemplate = function () {
      var template = '<div id="tree">';
      return template;
    };

    var linker = function (scope, element, attrs) {
      element.html(getTemplate());
      scope.id = attrs.chartId;
      scope.permissions = JSON.parse(attrs.chartPermission);

      var listParameterObjectChecked = '';
      var listTemp = '';

      $("#tree").fancytree({
        source: scope.permissions,
        checkbox: true,
        folder: false,
        selectMode: 3,
        select: function (event, data) {
          var node = data.node;
          if (node.isSelected()) {
            if (node.isUndefined()) {
              // Load and select all child nodes
              node.load().done(function () {
                node.visit(function (childNode) {
                  childNode.setSelected(true);
                });
              });
            } else {
              // Select all child nodes
              node.visit(function (childNode) {
                childNode.setSelected(true);
              });
            }
          }

          listParameterObjectChecked += '@' + node.key;

          listTemp = $.map(data.tree.getSelectedNodes(true), function (node) {
            return '~' + node.key;
          });
          listParameterObjectChecked = listTemp;

          scope.myParameter = listParameterObjectChecked;
        },

      });

      var node = $("#tree").fancytree("getRootNode");
      node.sortChildren(null, true);


      if (scope.editValue.id != null) {
        var permissionsIds = scope.editValue.permissionsIds;
        permissionsIds = permissionsIds.substring(1, permissionsIds.length - 1);
        var permissionsIdsArray = permissionsIds.trim().split(',');

        var tree = $("#tree").fancytree("getTree");
        var treeData = tree.toDict(true);
        var treeData1Copy = treeData;
        var treeData1 = jQuery.parseJSON(JSON.stringify(treeData1Copy));
        for (var i = 0; i < treeData1.children.length; i++) {
          var rootFather = treeData1.children[i].children;
          for (var j = 0; j < rootFather.length; j++) {
            var temp = rootFather[j].key.trim().split('-');
            $.each(permissionsIdsArray, function (index, value) {
              if (parseInt(temp[0]) == parseInt(value)) {
                rootFather[j].selected = true;
                listParameterObjectChecked += "#" + rootFather[j].key;
              }
            });
          }
        }
        tree.reload(treeData1);
        scope.myParameter = listParameterObjectChecked;
      }


    };

    return {
      restrict: 'E',
      transclude: 'true',
      scope: {
        myParameter: '=',
        editValue: '='
      },
      link: linker
    };
  });