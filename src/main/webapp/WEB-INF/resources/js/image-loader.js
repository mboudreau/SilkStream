// VARIABLES
var requests = {};

// MAIN
$(function () {
	$(window).resize(refresh);

	refresh();

	$(".silkstream").each(function () {
		loadImage($(this));
		$(this).resize(reloadImage);
	});
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
	var element = $(e.currentTarget);
	clearTimeout(timeout);
	timeout = setTimeout(function () {
		loadImage(element);
	}, 100);
}

function getDeviceInfo() {
	// DPI
//	var dpi = 0;
//	for (var i = 50; i < 500; i++) {
//		if (window.matchMedia('(resolution: '+i+'dpi)').matches) {
//			dpi = i;
//			break;
//		}
//	}

	return {/*dpi: dpi,*/ zoom: detectZoom.zoom(), pixelRatio: detectZoom.device()};
}

function loadImage(container) {
	clearTimeout(timeout);

	var info = getDeviceInfo();
	// Create query string
	var query = 'elementWidth=' + container.width()
		+ '&elementHeight=' + container.height()
		//+ '&dpi=' + info.dpi
		+ '&zoom=' + info.zoom
		+ '&pixelRatio=' + info.pixelRatio;

	// If query is different, load new image
	if(container.data('query') != query) {

		// Abort last request, it it exist for current image
		if(requests[container.data('id')]) {
			requests[container.data('id')].abort();
		}

		var request = new XMLHttpRequest();
		request.open('GET', '/api/image/' + container.data('id') + '?' + query, true);
		request.responseType = 'blob';
		request.onload = function () {
			if (this.status == 200) {
				container.html('');
				var img = new Image();
				img.src = (window.URL || window.webkitURL).createObjectURL(this.response);
				(img).load(function(){
					// Reset query after image load if it changes the dimensions
					var query = 'elementWidth=' + container.width()
						+ '&elementHeight=' + container.height()
						//+ '&dpi=' + info.dpi
						+ '&zoom=' + info.zoom
						+ '&pixelRatio=' + info.pixelRatio;
					container.data('query', query);
				});
				container.append(img);
			}
			requests[container.data('id')] = null;
		};
		request.send();
		requests[container.data('id')] = request;

	}
};
