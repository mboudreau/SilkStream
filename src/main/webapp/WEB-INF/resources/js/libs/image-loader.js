function getScreenFeatures(element) {
  // window.devicePixelRatio is the ratio between physical pixels and device-independent pixels (dips) on the device.
  return {pixelRatio:window.devicePixelRatio,
          elementWidth:element.height(),
          elementHeight:element.width()};
}

function loadImage(container) {
  // contact api to get image
  var features = getScreenFeatures(container);
  features.hash = container.data('hash');
  $.getJSON('/', features, function(data) {
    // create image object
    var img = new Image();
    var blob = new Blob(data {type: "application/octet-binary"});
    img.src =(window.URL || window.webkitURL).createObjectURL(blob);
    container.replaceWith(img);
  });
};


$(function() {
  var imageContainer = $('#some_id');
  var resizeTimer;
  $(window).resize(function() {
    clearTimeout(resizeTimer);
    resizeTimer = setTimeout(function() { loadImage(imageContainer); }, 100);
  });
  loadImage(imageContainer);
});
