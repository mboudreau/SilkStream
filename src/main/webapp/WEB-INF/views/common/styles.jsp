<%@ page import="java.io.File" %>
<link href='http://fonts.googleapis.com/css?family=Open+Sans:600italic,400' rel='stylesheet' type='text/css'>
<link rel="stylesheet" href="${staticsBase}/css/fancybox/jquery.fancybox.css" media="screen, projection"
      type="text/css"/>
<link rel="stylesheet" href="${staticsBase}/css/fancybox/jquery.fancybox-buttons.css" media="screen, projection"
      type="text/css"/>
<link rel="stylesheet" href="${staticsBase}/css/bootstrap.css" media="screen, projection" type="text/css"/>
<link rel="stylesheet" href="${staticsBase}/css/timepicker.css" media="screen, projection" type="text/css"/>
<link rel="stylesheet" href="${staticsBase}/css/datepicker.css" media="screen, projection" type="text/css"/>
<link rel="stylesheet" href="${staticsBase}/css/glyphicons.css" media="screen, projection" type="text/css"/>
<link rel="stylesheet" href="${staticsBase}/css/defaults.css?v=${appVersion}" media="screen, projection" type="text/css"/>
<link rel="stylesheet" href="${staticsBase}/css/main.css?v=${appVersion}" media="screen, projection" type="text/css"/>
<link rel="stylesheet" href="${staticsBase}/css/markitup.css?v=${appVersion}" media="screen, projection" type="text/css"/>

<link rel="shortcut icon" type="image/x-icon" href="${staticsBase}/images/favicon.png"/>
<link rel="icon" type="image/vnd.microsoft.icon" href="${staticsBase}/images/favicon.png"/>
<%--
<link rel="apple-touch-icon-precomposed" href="apple-touch-icon-iphone.png"/>
<link rel="apple-touch-icon-precomposed" sizes="72x72" href="apple-touch-icon-ipad.png"/>
<link rel="apple-touch-icon-precomposed" sizes="114x114" href="apple-touch-icon-iphone4.png"/>--%>

<!--[if IE]>
<link rel="stylesheet" type="text/css" href="${staticsBase}/css/ie.css?v=${appVersion}" />
<![endif]-->

<!--[if lt IE 9]>
<link rel="stylesheet" type="text/css" href="${staticsBase}/css/ie8-lower.css?v=${appVersion}" />
<![endif]-->

<%--RANDOMIZED BACKGROUNDS--%>
<%--<style type="text/css">
	html,body {
	<%
		 File directory = new File(application.getRealPath("/WEB-INF/resources/images/backgrounds"));
		 File[] list = directory.listFiles();
    %>
		background: url('${staticsBase}/images/backgrounds/<%= list[(int) (Math.random() * list.length)].getName() %>') no-repeat center top fixed;
		-webkit-background-size: cover;
		-moz-background-size: cover;
		-o-background-size: cover;
		background-size: cover;
	}
</style>--%>
