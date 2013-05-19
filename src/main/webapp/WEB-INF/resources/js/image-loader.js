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
	$('#dev .zoom label').text(info.zoom.toFixed(2));
	$('#dev .ratio label').text(info.pixelRatio.toFixed(2));
}

var timers = {};
function reloadImage(e) {
	var element = $(e.currentTarget);
	clearTimeout(timers[element.data('id')]);
	timers[element.data('id')] = setTimeout(function () {
		clearTimeout(timers[element.data('id')]);
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
	var info = getDeviceInfo();
	// Create query string
	var query = 'width=' + container.width()
		+ '&height=' + container.height()
		//+ '&dpi=' + info.dpi
		+ '&zoom=' + info.zoom
		+ '&pixelRatio=' + info.pixelRatio;

	// If query is different, load new image
	if (container.data('query') != query) {

		// Abort last request, it it exist for current image
		if (requests[container.data('id')]) {
			requests[container.data('id')].abort();
		}

		var request = new XMLHttpRequest();
		request.open('GET', '/api/image/' + container.data('id') + '?' + query, true);
		request.responseType = 'blob';
		request.onload = function () {
			if (this.status == 200) {
				container.html('');
				var img = $(new Image());
				//var svg = $('<svg xmlns="http://www.w3.org/2000/svg" width="100%" height="100%" viewBox="0 0 1 1" preserveAspectRatio="XMid YMid"><image /></svg>');
				//var img = svg.find('image');
				img.attr('src', (window.URL || window.webkitURL).createObjectURL(this.response));
				//img.attr('xlink:href', (window.URL || window.webkitURL).createObjectURL(this.response));
				img.load(function () {
					// Reset query after image load if it changes the dimensions
					var query = 'width=' + container.width()
						+ '&height=' + container.height()
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
