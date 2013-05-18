package com.silkstream.platform.security;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.access.AccessDeniedException;

public class CustomAccessDeniedHandler implements org.springframework.security.web.access.AccessDeniedHandler {
	private String accessDeniedUrl;

	public CustomAccessDeniedHandler() {
	}

	public CustomAccessDeniedHandler(String accessDeniedUrl) {
		this.accessDeniedUrl = accessDeniedUrl;
	}

	public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException) throws IOException, ServletException {
		request.getSession().setAttribute("ApplicationException", "Login user tried to access unauthorized content");
	}

	public String getAccessDeniedUrl() {
		return accessDeniedUrl;
	}

	public void setAccessDeniedUrl(String accessDeniedUrl) {
		this.accessDeniedUrl = accessDeniedUrl;
	}
}

