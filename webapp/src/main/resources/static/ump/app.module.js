var UserWebApp = angular.module('UserWebApp', [
    'ngSanitize',
    'ui.bootstrap',
    'checklist-model',
    'ui.select2',
    'pascalprecht.translate',
    'treeGrid',
    'ivh.treeview',
    'ui.bootstrap.datetimepicker',
    'ui.select'
]);

UserWebApp.run(['uiSelect2Config', '$translate', function(uiSelect2Config, $translate) {
  uiSelect2Config.placeholder = $translate.instant('placeholderSelect');

  $('.select2').select2({
    placeholder: $translate.instant('placeholderSelect')
  });

  $('.select2clear').select2({
    placeholder: $translate.instant('placeholderSelect'),
    allowClear: true
  });

  // Config message validate form
  jQuery.extend(jQuery.validator.messages, {
    notExisted: $translate.instant('validatorExisted'),
    required: $translate.instant('validatorRequired'),
    email: $translate.instant('validatorEmail'),
    url: $translate.instant('validatorUrl'),
    date: $translate.instant('validatorDate'),
    dateISO: $translate.instant('validatorDateISO'),
    number: $translate.instant('validatorNumber'),
    digits: $translate.instant('validatorDigits'),
    equalTo: $translate.instant('validatorEqualTo'),
    accept: $translate.instant('validatorAccept'),
    maxlength: jQuery.validator.format($translate.instant('validatorMaxlength')),
    minlength: jQuery.validator.format($translate.instant('validatorMinlength')),
    rangelength: jQuery.validator.format($translate.instant('validatorRangelength')),
    range: jQuery.validator.format($translate.instant('validatorRange')),
    max: jQuery.validator.format($translate.instant('validatorMax')),
    min: jQuery.validator.format($translate.instant('validatorMin')),
  });
}]);