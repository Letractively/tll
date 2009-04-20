/*
 * The Logic Lab
 */
package com.tll.di;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.velocity.app.VelocityEngine;

import com.google.inject.AbstractModule;
import com.google.inject.Provider;

/**
 * VelocityModule
 * @author jpk
 */
public class VelocityModule extends AbstractModule {

	static final Log log = LogFactory.getLog(VelocityModule.class);

	/**
	 * Constructor
	 */
	public VelocityModule() {
		super();
		log.info("Employing Velocity engine");
	}

	@Override
	protected void configure() {

		// VelocityEngine
		bind(VelocityEngine.class).toProvider(new Provider<VelocityEngine>() {

			public VelocityEngine get() {
				try {
					final VelocityEngine ve = new VelocityEngine();
					ve.init();
					return ve;
				}
				catch(final Exception e) {
					throw new IllegalStateException("Unable to instantiate the velocity engine: " + e.getMessage(), e);
				}
			}

		});

	}

}