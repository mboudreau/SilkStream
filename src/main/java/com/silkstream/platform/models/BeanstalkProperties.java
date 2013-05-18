package com.silkstream.platform.models;

import com.silkstream.platform.enums.EnvironmentType;

public class BeanstalkProperties {
	private EnvironmentType environment;
	private String searchUrl;
	private String documentUrl;

	public BeanstalkProperties() {
		// Sets environment, defaults to dev

		if (System.getProperty("PARAM1") != null) {
			setEnvironment(EnvironmentType.valueOf(System.getProperty("PARAM1")));
		} else {
			setEnvironment(EnvironmentType.TEST);
		}

		// Sets document url for search
		if (System.getProperty("PARAM2") != null) {
			this.documentUrl = System.getProperty("PARAM2");
		} else {
			this.documentUrl = "http://doc-silkstream-65wa5f3y6s72zsrxbuplj2kwzy.us-east-1.cloudsearch.amazonaws.com/2011-02-01/documents/batch";
		}

		// Sets search url
		if (System.getProperty("PARAM3") != null) {
			this.searchUrl = System.getProperty("PARAM3");
		} else {
			this.searchUrl = "http://search-silkstream-65wa5f3y6s72zsrxbuplj2kwzy.us-east-1.cloudsearch.amazonaws.com/2011-02-01/search?";
		}
	}

	public EnvironmentType getEnvironment() {
		if (environment.equals(EnvironmentType.LOCAL)) {
			return EnvironmentType.DEV;
		}
		return environment;
	}

	public String getDomainUrl() {
		String domain = "localhost:8080";
		if (environment == EnvironmentType.DEV) {
			domain = "silkstream.michelboudreau.com";
		} else if (environment == EnvironmentType.STAGING) {
			domain = "silkstream.michelboudreau.com";
		} else if (environment == EnvironmentType.PROD) {
			domain = "silkstream.michelboudreau.com";
		}
		return "http://" + domain;
	}

	public String getApiUrl() {
		return getDomainUrl() + "/api";
	}

	public void setEnvironment(EnvironmentType environment) {
		this.environment = environment;
	}

	public String getSearchUrl() {
		return searchUrl;
	}

	public String getDocumentUrl() {
		return documentUrl;
	}

	public Boolean isMaintenance() {
		return getEnvironment() == EnvironmentType.MAINTENANCE;
	}
}
