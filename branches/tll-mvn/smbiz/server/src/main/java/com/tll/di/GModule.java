/*
 * The Logic Lab 
 */
package com.tll.di;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.google.inject.AbstractModule;

/**
 * GModule Abstract Guice module. Use this in place of {@link AbstractModule}.
 * @author jpk
 */
public abstract class GModule extends AbstractModule {

	protected final Log log = LogFactory.getLog(this.getClass());

	/**
	 * Constructor
	 */
	public GModule() {
		super();
	}

}
