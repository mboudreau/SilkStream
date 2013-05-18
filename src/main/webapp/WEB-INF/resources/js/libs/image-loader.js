function getContainerFeatures(element) {
  // window.devicePixelRatio is the ratio between physical pixels and device-independent pixels (dips) on the device.
  return {pixelRatio:window.devicePixelRatio,
          elementWidth:element.height(),
          elementHeight:element.width()};
}

function loadImage(container) {
  // contact api to get image
  var features = getContainerFeatures(container);
  features.hash = container.data('hash');
  $.get('/api/image/'+features.hash, features, function(data) {
    // create image object
    var img = new Image();
    var blob = new Blob([data], {type: "application/octet-stream"});
    img.src = (window.URL || window.webkitURL).createObjectURL(blob);
    container.text('');
    container.append(img);
  });
};


$(function() {
  var imageContainer = $('#some_id');
  var resizeTimer, currentHeight, currentWidth;
  $(window).resize(function() {
    if (imageContainer.height() == currentHeight &&
        imageContainer.width() == currentWidth)
      return;
    currentWidth = imageContainer.width();
    currentHeight = imageContainer.height();
    clearTimeout(resizeTimer);
    resizeTimer = setTimeout(function() { loadImage(imageContainer); }, 100);
  });
  loadImage(imageContainer);
});
