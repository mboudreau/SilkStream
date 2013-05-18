package com.silkstream.platform.exception;

import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.AbstractHandlerExceptionResolver;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


public class ExceptionHandler extends AbstractHandlerExceptionResolver {


	@Override
	protected ModelAndView doResolveException(HttpServletRequest request,
											  HttpServletResponse response,
											  Object handler,
											  Exception ex) {
		ModelAndView mv = new ModelAndView();
		mv.addObject("Error", ex.getMessage());
		return mv;
	}
}
