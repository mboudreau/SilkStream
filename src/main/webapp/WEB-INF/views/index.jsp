<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>

<spring:eval expression="@settings['staticsBase']" var="staticsBase" scope="request"/>
<spring:eval expression="@settings['appVersion']" var="appVersion" scope="request"/>

<!DOCTYPE html>
<html lang="en" xmlns="http://www.w3.org/1999/xhtml" class="no-js">
  <head>
	  <link href="https://plus.google.com/116781078488522205414" rel="publisher"/>

	  <%-- styles --%>
	  <link rel="stylesheet" href="${staticsBase}/css/main.css?v=${appVersion}" media="screen, projection" type="text/css"/>

	  <%-- Modernizr needs to be in the header --%>
	  <script type="text/javascript" src="${staticsBase}/js/libs/modernizr.js"></script>
	  <script src="//ajax.googleapis.com/ajax/libs/jquery/1.9.1/jquery.min.js"></script>
	  <script type="text/javascript" src="${staticsBase}/js/libs/image-loader.js"></script>
    <!-- <script src="http://cdn.binaryjs.com/0/binary.js"></script> -->
	  <title>SilkStream Demo</title>
  </head>
  <body>

    Yep, seems to be working. BOOGA!

    <!-- IMAGE IDS

         eda0a695-3f4c-4aa7-b5a8-33af5bd6093c
         53163555-304a-45c7-8f92-90fc56628e1e
         9a047da9-e96c-4f32-888e-1a93b94565c4
         4f68a68f-7df9-4be9-b0aa-061bd9fe5ace
         01746a60-4df3-40c1-bf5b-7c2f588b8ece
         2791f42c-d063-4ff9-81ac-b41ae94c04ad

      -->
    <div id="some_id" data-hash="01746a60-4df3-40c1-bf5b-7c2f588b8ece" class="some_class"></div>
  </body>
</html>
