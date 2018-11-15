UserWebApp.controller('PermissionAddController', function ($http, $rootScope, $scope, HttpService, CommonService, $translate) {

    $scope.operationsList = '';
    $scope.myParameter = {
        value: ''
    };
    $scope.myClass = {
        value: ''
    };
    $scope.params = [];
    $scope.params.permissionName = '';
    $scope.params.permissionDes = '';


    function renderEdit() {
        $scope.getId = $('.dataResponse').attr('data-getId');
        if ($scope.getId != null && $scope.getId != '') {
            $scope.params.perId = $('.dataResponse').attr('data-getId');
        }
        $scope.getName = $('.dataResponse').attr('data-getName');
        $scope.getOperationIds = $('.dataResponse').attr('data-getOperationIds');
        $scope.getDescription = $('.dataResponse').attr('data-getDescription');
        $scope.params.permissionName = $('.dataResponse').attr('data-getName');
        $scope.params.permissionDes = $('.dataResponse').attr('data-getDescription');

        $scope.editValue = {
            'getId': $scope.getId,
            'getName': $scope.getName,
            'getOperationIds': $scope.getOperationIds,
            'getDescription': $scope.getDescription,
        };
    }

    renderEdit();

    function loadData() {
        HttpService.getData('/permissions/load-operation', {}).then(function (response) {
            if (response != null && response != '') {
                $scope.operationsList = JSON.stringify(response);
            }
        });
    }

    loadData();

    function validatePermission(myParameter) {
        console.log(myParameter);
        if (myParameter.length == 0) {
            return false;
        }
        return true;
    }

    function listAllOperations(listParameterObjectChecked, listClass) {
        var totalParam = '';
        var operationIdsArray = listParameterObjectChecked.toString().split(',');
        var listClassArray = listClass.toString().split(',');

        var tree = $("#tree").fancytree("getTree");
        var d = tree.toDict(true);
        var d1 = jQuery.parseJSON(JSON.stringify(d));
        for (var i = 0; i < operationIdsArray.length; i++) {
            if (typeof listClassArray[i] != 'undefined' && listClassArray[i] != 0) {
                // node
                for (var j = 0; j < d1.children.length; j++) {
                    if (d1.children[j].key == operationIdsArray[i]) {
                        var rootFather = d1.children[j].children;
                        for (var k = 0; k < rootFather.length; k++) {
                            if (rootFather[k].key != '') {
                                totalParam += ',' + rootFather[k].key;
                            }
                        }
                    }
                }
            } else {
                // child
                if (operationIdsArray[i] != '') {
                    totalParam += ',' + operationIdsArray[i];
                }
            }
        }
        totalParam = totalParam.substring(1);
        return totalParam;
    }

    $scope.onPermissionAdd = function () {
        var isValid = validatePermission($scope.myParameter.value);
        if (isValid) {
            if ($scope.getId != null && $scope.getId != '') {
                var totalOperations = listAllOperations($scope.myParameter.value, $scope.myClass.value);
                var params = {};

                params.addPermissionId = $scope.getId;
                params.addPermission = $scope.params.permissionName;
                params.addDescription = $scope.params.permissionDes;
                params.addOperations = totalOperations;

                HttpService.postData('/editPermission', params).then(function (response) {
                    if (response == 200) {
                        common.notifySuccess($translate.instant('editPermissionSuccess'));
                        location.replace('/permissions');
                    } else if (response == 201) {
                        common.notifyError($translate.instant('addPermissionExits'));
                    } else {
                        common.notifyError($translate.instant('editPermissionFail'));
                    }
                });

            } else {
                var totalOperations = listAllOperations($scope.myParameter.value, $scope.myClass.value);
                var params = {};
                params.addPermission = $scope.params.permissionName;
                params.addDescription = $scope.params.permissionDes;
                params.addOperations = totalOperations;

                HttpService.postData('/addPermission', params).then(function (response) {
                    if (response == 200) {
                        common.notifySuccess($translate.instant('addPermissionSuccess'));
                        location.replace('/permissions');
                    } else if (response == 201) {
                        common.notifyError($translate.instant('addPermissionExits'));
                    } else {
                        common.notifyError($translate.instant('addPermissionFail'));
                    }
                });
            }
        } else {
            common.notifyError($translate.instant('addPermissionEmpty'));
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
                scope.operations = JSON.parse(attrs.chartOperations);

                var listClass = '';
                var listParameterObjectChecked = '';


                $("#tree").fancytree({
                    source: scope.operations,
                    checkbox: true,
                    folder: false,
                    selectMode: 3,
                    activate: function (event, data) {
                        var node = data.node;
                    },
                    click: function (event, data) {
                        var node = data.node;
                    },
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
                        listParameterObjectChecked = $.map(data.tree.getSelectedNodes(true), function (node) {
                            return node.key;
                        });

                        scope.myParameter = listParameterObjectChecked;

                        listClass = $.map(data.tree.getSelectedNodes(true), function (node) {
                            return node.countChildren(node.key);
                        });

                        scope.myClass = listClass;

                    },
                });

                var node = $("#tree").fancytree("getRootNode");
                node.sortChildren(null, true);

                if (scope.editValue.getId != null) {

                    var operationIds = scope.editValue.getOperationIds;
                    operationIds = operationIds.substring(1, operationIds.length - 1);
                    var operationIdsArray = operationIds.trim().split(',');

                    var tree = $("#tree").fancytree("getTree");
                    var treeData = tree.toDict(true);
                    var treeData1Copy = treeData;
                    var treeData1 = jQuery.parseJSON(JSON.stringify(treeData1Copy));
                    for (var i = 0; i < treeData1.children.length; i++) {
                        var rootFather = treeData1.children[i].children;
                        for (var j = 0; j < rootFather.length; j++) {
                            $.each(operationIdsArray, function (index, value) {
                                if (value.trim() == rootFather[j].key) {
                                    rootFather[j].selected = true;
                                    listParameterObjectChecked += ',' + rootFather[j].key;
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
                    myClass: '=',
                    editValue: '='
                },
                link: linker
            };
        });