package com.forest.handler;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import com.forest.annotation.TokenToUser;
import com.forest.pojo.User;
import com.forest.service.UserService;

public class TokenToUserMethodArgumentResolver implements HandlerMethodArgumentResolver{
	@Autowired
	private	UserService userService;
	

	public boolean supportsParameter(MethodParameter parameter) {
		if(parameter.hasParameterAnnotation(TokenToUser.class)) {
			return true;
		}
		return false;
	}

	public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer,
			NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {
		if(parameter.getParameterAnnotation(TokenToUser.class) instanceof TokenToUser) {
			User user = null;
			String token = webRequest.getHeader("token");
			//System.out.println("token:"+token);
			if(null != token && !"".equals(token))
			{
				user = userService.getUserByToken(token);
				//System.out.println(user.getRole());
				return user;
			}
		}
		return null;
	}
}
