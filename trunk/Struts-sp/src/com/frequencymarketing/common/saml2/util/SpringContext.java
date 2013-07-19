package com.frequencymarketing.common.saml2.util;

import org.springframework.context.ApplicationContext;
import org.springframework.web.context.ContextLoader;



public class SpringContext {

	public static Object getBean(String beanName){
		
		ApplicationContext ctx = ContextLoader.getCurrentWebApplicationContext();  
		return ctx.getBean(beanName);  
		
	}
}
