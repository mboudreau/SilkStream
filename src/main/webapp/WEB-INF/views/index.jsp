<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>

<spring:eval expression="@settings['staticsBase']" var="staticsBase" scope="request"/>
<spring:eval expression="@settings['appVersion']" var="appVersion" scope="request"/>

<!DOCTYPE html>
<html lang="en" xmlns="http://www.w3.org/1999/xhtml" class="no-js">
  <head>
	  <meta name="viewport" content="width=device-width, target-densitydpi=device-dpi">

	  <link href="https://plus.google.com/116781078488522205414" rel="publisher"/>

	  <%-- styles --%>
	  <link rel="stylesheet" href="${staticsBase}/css/main.css?v=${appVersion}" media="screen, projection" type="text/css"/>

	  <%-- Modernizr needs to be in the header --%>
	  <script type="text/javascript" src="${staticsBase}/js/detect-zoom.js"></script>
	  <script type="text/javascript" src="${staticsBase}/js/jquery.min.js"></script>
	  <script type="text/javascript" src="${staticsBase}/js/jquery.ba-resize.min.js"></script>
	  <script type="text/javascript" src="${staticsBase}/js/image-loader.js"></script>
    <!-- <script src="http://cdn.binaryjs.com/0/binary.js"></script> -->
	  <title>SilkStream Demo</title>

  </head>
  <body>

    <!-- IMAGE IDS

         eda0a695-3f4c-4aa7-b5a8-33af5bd6093c
         53163555-304a-45c7-8f92-90fc56628e1e
         9a047da9-e96c-4f32-888e-1a93b94565c4
         4f68a68f-7df9-4be9-b0aa-061bd9fe5ace
         01746a60-4df3-40c1-bf5b-7c2f588b8ece
         2791f42c-d063-4ff9-81ac-b41ae94c04ad

      -->
    <div class="silkstream" data-hash="01746a60-4df3-40c1-bf5b-7c2f588b8ece" />

    <svg xmlns="http://www.w3.org/2000/svg" width="100%" height="100%" viewBox="0 0 1 1" preserveAspectRatio="none">
	    <linearGradient id="grad-ucgg-generated" gradientUnits="userSpaceOnUse" x1="0%" y1="0%" x2="0%" y2="100%">
		    <stop offset="0%" stop-color="#00b7ea" stop-opacity="1"/>
		    <stop offset="100%" stop-color="#009ec3" stop-opacity="1"/>
	    </linearGradient>
	    <rect x="0" y="0" width="1" height="1" fill="url(#grad-ucgg-generated)" />
    </svg>
  </body>
</html>
