package com.kh.spring.common.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.AsyncHandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

public class BoardEnterInterceptor implements AsyncHandlerInterceptor{
	private Logger logger = LoggerFactory.getLogger(BoardEnterInterceptor.class);	
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {
		//전처리
		// DispatcherServlet이 컨트롤러를 호출하기 전(컨트롤러로 요청이 들어가기 전)에 수행
		
		request.setAttribute("message", "로그인 후 이용하세요.");
		return AsyncHandlerInterceptor.super.preHandle(request, response, handler);	//항상 true를 반환
	}
	
	@Override
	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
			ModelAndView modelAndView) throws Exception {
		//후처리
		//컨트롤러에서 디스패처서블릿으로 리턴되는 순간에 수행
		
		if(logger.isDebugEnabled()) {
			logger.debug("======view======");
		}
		
		
		//AsyncHandlerInterceptor.super.postHandle(request, response, handler, modelAndView);
	}
}
