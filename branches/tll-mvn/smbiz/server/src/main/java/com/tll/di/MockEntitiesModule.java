/*
 * The Logic Lab 
 */
package com.tll.di;

import org.springframework.beans.factory.ListableBeanFactory;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.google.inject.Scopes;
import com.tll.dao.mock.EntityGraph;
import com.tll.model.IEntityProvider;
import com.tll.model.MockEntityProvider.MockEntityBeanFactory;

/**
 * MockEntitiesModule
 * @author jpk
 */
public class MockEntitiesModule extends GModule {

	@Override
	protected void configure() {
		final ClassPathXmlApplicationContext sac = new ClassPathXmlApplicationContext("mock-entities.xml");
		bind(ListableBeanFactory.class).annotatedWith(MockEntityBeanFactory.class).toInstance(sac);
		bind(IEntityProvider.class).to(EntityGraph.class).in(Scopes.SINGLETON);
	}

}
