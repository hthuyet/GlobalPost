$(function () {

  // ACTIVE NAV
  var arrPathname = location.pathname.split('/');
  if (arrPathname.length >= 2) {
    var navLv1 = '/' + arrPathname[1];
    $('.navUmp').find('a[href="' + navLv1 + '"].navLv1').parent('li').addClass('active');

    if (arrPathname.length === 2) {
      $('.navUmp').find('a[href="' + navLv1 + '"].navLv1Dropdown').parent('li').addClass('active');

    } else if (arrPathname.length >= 3) {
      var navLv2 = '/' + arrPathname[1] + '/' + arrPathname[2];
      $('.navUmp').find('a[href="' + navLv2 + '"].navLv2').parent('li').addClass('active')
    }
  }

});