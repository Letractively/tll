/*
 * The Logic Lab 
 */
package com.tll.guice;

import com.google.inject.Binder;
import com.google.inject.Module;

/**
 * CompositeModule
 * @author jpk
 */
public abstract class CompositeModule implements Module {

	/**
	 * Constructor
	 */
	public CompositeModule() {
		super();
	}

	/**
	 * @return A list of desired modules that will be bound.
	 */
	protected abstract Module[] getModulesToBind();

	public final void configure(Binder binder) {
		final Module[] modules = getModulesToBind();
		if(modules != null) {
			for(final Module module : modules) {
				module.configure(binder);
			}
		}
	}

}
