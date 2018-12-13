UserWebApp.directive('onFinishRender', function ($timeout) {
  return {
    restrict: 'A',
    link: function (scope, element, attr) {
      if (scope.$last === true) {
        $timeout(function () {
          scope.$emit(attr.onFinishRender);
        });
      }
    }
  }
})
  .directive('convertToNumber', convertToNumberDirective)

  .directive('dlKeyCode', dlKeyCode);

convertToNumberDirective.$inject = [];

function convertToNumberDirective() {
  return {
    require: 'ngModel',
    link: function (scope, element, attrs, ngModel) {
      ngModel.$parsers.push(function (val) {
        return parseInt(val, 10);
      });
      ngModel.$formatters.push(function (val) {
        return '' + val;
      });
    }
  }
}

function dlKeyCode() {
  return {
    restrict: 'A',
    link: function ($scope, $element, $attrs) {
      $element.bind("keypress", function (event) {
        var keyCode = event.which || event.keyCode;

        if (keyCode == $attrs.code) {
          $scope.$apply(function () {
            $scope.$eval($attrs.dlKeyCode, {$event: event});
          });

        }
      });
    }
  };
}