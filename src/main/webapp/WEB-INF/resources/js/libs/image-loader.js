function getContainerFeatures(element) {
  // window.devicePixelRatio is the ratio between physical pixels and device-independent pixels (dips) on the device.
  return {pixelRatio:window.devicePixelRatio,
          elementWidth:element.width(),
          elementHeight:element.height()};
}

function loadImage(container) {
  // contact api to get image
  var features = getContainerFeatures(container);

  var ajaxRequest = new XMLHttpRequest();
  ajaxRequest.open('GET', '/api/image/' + container.data('hash') + '?elementWidth=' + features.elementWidth + '&elementHeight=' + features.elementHeight + '&pixelRatio=' + features.pixelRatio, true);
  ajaxRequest.responseType = 'blob';
  ajaxRequest.onload = function(e) {
    if (this.status == 200) {
      var img = new Image();
      img.src = (window.URL || window.webkitURL).createObjectURL(this.response);
      container.text('');
      container.append(img);
    }
  };
  ajaxRequest.send();
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
