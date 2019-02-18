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

  .directive('dlKeyCode', dlKeyCode)
  .directive('dateTimePicker', dateTimePicker)

  // allow you to format a text input field.
  // <input type="text" ng-model="test" format="number" />
  // <input type="text" ng-model="test" format="currency" />
  .directive('format', ['$filter', function ($filter) {
    return {
      require: '?ngModel',
      link: function (scope, elem, attrs, ctrl) {
        if (!ctrl) return;

        ctrl.$formatters.unshift(function (a) {
          return $filter(attrs.format)(ctrl.$modelValue)
        });

        elem.bind('blur', function (event) {
          var plainNumber = elem.val().replace(/[^\d|\-+|\.+]/g, '');
          elem.val($filter(attrs.format)(plainNumber));
        });

        elem.bind('keyup', function (event) {
          var plainNumber = elem.val().replace(/[^\d|\-+|\.+]/g, '');
          elem.val($filter(attrs.format)(plainNumber));
        });

      }
    };
  }]);

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

function dateTimePicker () {

  return {
    require: '?ngModel',
    restrict: 'AE',
    scope: {
      pick12HourFormat: '@',
      language: '@',
      useCurrent: '@',
      location: '@'
    },
    link: function (scope, elem, attrs) {
      elem.datetimepicker({
        pick12HourFormat: scope.pick12HourFormat,
        language: scope.language,
        useCurrent: scope.useCurrent
      })

      //Local event change
      elem.on('blur', function () {

        console.info('this', this);
        console.info('scope', scope);
        console.info('attrs', attrs);


        /*// returns moments.js format object
        scope.dateTime = new Date(elem.data("DateTimePicker").getDate().format());
        // Global change propagation
        $rootScope.$broadcast("emit:dateTimePicker", {
            location: scope.location,
            action: 'changed',
            dateTime: scope.dateTime,
            example: scope.useCurrent
        });
        scope.$apply();*/
      })
    }
  };
}