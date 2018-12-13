UserWebApp
  .filter('floor', function () {
    return function (n) {
      return Math.floor(n);
    };
  })
  .filter('ceil', function () {
    return function (n) {
      return Math.ceil(n);
    };
  })
  .filter('dateFormat', function ($filter) {
    return function (input) {
      if (typeof input === 'undefined' || input === null || input === '0000-00-00' || input === '0000-00-00 00:00:00' || input == "") {
        return '';
      }
      var _date = $filter('date')(new Date(input), 'yyyy-MM-dd');
      try {
        return _date.toUpperCase();
      } catch (c) {
        console.log(input);
        console.error(c);
        return "";
      }

    };
  })
  .filter('dateTimeFormat', function ($filter) {
    return function (input) {
      if (typeof input === 'undefined' || input === null || input === '0000-00-00' || input === '0000-00-00 00:00:00' || input == "") {
        return '';
      }
      var _date = $filter('date')(new Date(input), 'yyyy-MM-dd HH:mm:ss');
      try {
        return _date.toUpperCase();
      } catch (c) {
        console.log(input);
        console.error(c);
        return "";
      }
    };
  })
  .filter('isEmpty', function () {
    var bar;
    return function (obj) {
      for (bar in obj) {
        if (obj.hasOwnProperty(bar)) {
          return false;
        }
      }
      return true;
    };
  })
  .filter('unique', function () {
    return function (collection, primaryKey) {
      var output = [];
      var keys = [];
      var splitKeys = primaryKey.split('.');

      angular.forEach(collection, function (item) {
        var key = {};
        angular.copy(item, key);
        for (var i = 0; i < splitKeys.length; i++) {
          key = key[splitKeys[i]];
        }

        if (keys.indexOf(key) === -1) {
          keys.push(key);
          output.push(item);
        }
      });

      return output;
    };
  })
;