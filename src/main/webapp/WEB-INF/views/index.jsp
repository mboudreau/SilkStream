<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>

<spring:eval expression="@settings['staticsBase']" var="staticsBase" scope="request"/>
<spring:eval expression="@settings['appVersion']" var="appVersion" scope="request"/>

<!DOCTYPE html>
<html lang="en" xmlns="http://www.w3.org/1999/xhtml" class="no-js">
<head>
	<link href="https://plus.google.com/116781078488522205414" rel="publisher"/>
	<%-- META tags and SEO stuff --%>
	<jsp:include page="/WEB-INF/views/common/meta.jsp"/>

	<%-- styles --%>
	<jsp:include page="/WEB-INF/views/common/styles.jsp"/>

	<%-- Modernizr needs to be in the header --%>
	<script type="text/javascript" src="${staticsBase}/js/libs/modernizr.js"></script>

	<title>SilkStream Demo</title>
</head>
<body>
Yep, seems to be working. BOOGA!
</body>
</html>