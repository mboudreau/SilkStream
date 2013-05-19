<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>

<spring:eval expression="@settings['staticsBase']" var="staticsBase" scope="request"/>
<spring:eval expression="@settings['appVersion']" var="appVersion" scope="request"/>

<!DOCTYPE html>
<html lang="en" xmlns="http://www.w3.org/1999/xhtml">
<head>
	<meta name='viewport'
	      content='width=device-width, maximum-scale=1.0, user-scalable=no, target-densityDpi=device-dpi'>
	<link rel="stylesheet" href="${staticsBase}/css/main.css?v=${appVersion}" media="screen, projection"
	      type="text/css"/>

	<!-- <script src="http://cdn.binaryjs.com/0/binary.js"></script> -->
	<title>SilkStream Demo</title>

</head>
<body>
<div id="dev">
	<div class="zoom">Zoom Level: <label/></div>
	<div class="ratio">Device Pixel Ratio: <label/></div>
</div>

<!-- IMAGE IDS

	 eda0a695-3f4c-4aa7-b5a8-33af5bd6093c
	 53163555-304a-45c7-8f92-90fc56628e1e
	 9a047da9-e96c-4f32-888e-1a93b94565c4
	 4f68a68f-7df9-4be9-b0aa-061bd9fe5ace
	 01746a60-4df3-40c1-bf5b-7c2f588b8ece
	 2791f42c-d063-4ff9-81ac-b41ae94c04ad

  -->

<h1>Original Image Resized to 600px Wide</h1>
<img src="${staticsBase}/images/eda0a695-3f4c-4aa7-b5a8-33af5bd6093c.jpg" style="width:600px;"/>

<h1>Silkstream Image</h1>
<div class="silkstream" data-id="eda0a695-3f4c-4aa7-b5a8-33af5bd6093c" style="width:600px;"/>

<script type="text/javascript" src="${staticsBase}/js/jquery.min.js"></script>
<script type="text/javascript" src="${staticsBase}/js/jquery.ba-resize.min.js"></script>
<script type="text/javascript" src="${staticsBase}/js/detect-zoom.js"></script>
<script type="text/javascript" src="${staticsBase}/js/image-loader.js"></script>
</body>
</html>
