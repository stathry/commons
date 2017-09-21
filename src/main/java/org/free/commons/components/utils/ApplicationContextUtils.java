/**
 * Copyright 2016-2100 Deppon Co., Ltd.
 */
package org.free.commons.components.utils;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

/**
 * @author Demon
 *
 * 2016年8月18日
 */
public class ApplicationContextUtils implements ApplicationContextAware {
	
	private static ApplicationContext context;

	@Override
	public void setApplicationContext(ApplicationContext applicationContext)
			throws BeansException {
		ApplicationContextUtils.context = applicationContext;
	}
	
	public static <T> T getBean(Class<T> clazz) {
		return context.getBean(clazz);
	}
	
	public static Object getBean(String beanName) {
		return context.getBean(beanName);
	}
	
	public static ApplicationContext getContext() {
		return context;
	}

}
