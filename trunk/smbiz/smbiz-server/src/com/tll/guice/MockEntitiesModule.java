/*
 * The Logic Lab 
 */
package com.tll.guice;

import org.springframework.beans.factory.BeanFactory;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.tll.model.MockEntityProvider.MockEntityBeanFactory;

/**
 * MockEntitiesModule
 * @author jpk
 */
public class MockEntitiesModule extends GModule {

	@Override
	protected void configure() {
		final ClassPathXmlApplicationContext sac = new ClassPathXmlApplicationContext("mock-entities.xml");
		bind(BeanFactory.class).annotatedWith(MockEntityBeanFactory.class).toInstance(sac);
	}

}
