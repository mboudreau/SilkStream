// VARIABLES
var request = null;

// MAIN
$(function () {
	$(window).resize(refresh);
	$(".silkstream").resize(reloadImage);

	refresh();
	loadImage(imageContainer);
});


function refresh() {
	var info = getDeviceInfo();
	var zoom = $('#zoom');
	var device = $('#device');
	zoom.text(info.zoom.toFixed(2));
	device.text(info.pixelRatio.toFixed(2));
}

var timeout = 0;
function reloadImage(e) {
	var element = e.currentTarget;
	clearTimeout(timeout);
	timeout = setTimeout(function(){loadImage(element);}, 100);
}

function getDeviceInfo() {
	// DPI
	var dpi = 0;
	for (var i = 50; i < 800; i++) {
		if (matchMedia('(resolution: ' + i + 'dpi)').matches) {
			dpi = i;
			break;
		}
	}

	return {dpi: dpi, zoom: window.detectZoom.zoom(), pixelRatio: window.detectZoom.device()};
}

function loadImage(container) {
	clearTimeout(timeout);
	var info = getDeviceInfo();

	if(request) {
		request.abort();
	}

	// contact api to get image
	request = new XMLHttpRequest();
	request.open('GET', '/api/image/' + container.data('hash')
		+ '?elementWidth=' + container.width()
		+ '&elementHeight=' + container.height()
		+ '&dpi=' + info.dpi
		+ '&zoom=' + info.zoom
		+ '&pixelRatio=' + info.pixelRatio
		, true);
	request.responseType = 'blob';
	request.onload = function () {
		if (this.status == 200) {
			var img = new Image();
			img.src = (window.URL || window.webkitURL).createObjectURL(this.response);
			container.html('');
			container.append(img);
		}
		request = null;
	};
	request.send();
};
