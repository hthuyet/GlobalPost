/* ------------------------------------------------------------------------------
*
*  # Login page
*
*  Specific JS code additions for login and registration pages
*
*  Version: 1.0
*  Latest update: Aug 1, 2015
*
* ---------------------------------------------------------------------------- */

$(function () {

  // Style checkboxes and radios
  $('.styled').uniform();

  // Show message error
  if (common.getUrlRequestParam('error') !== null) {
    common.btnLoading($("#btnLogin"), false);
    $('.loginError').show();
  }
});
