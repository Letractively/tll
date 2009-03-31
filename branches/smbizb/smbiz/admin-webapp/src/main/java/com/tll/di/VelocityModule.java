/*
 * The Logic Lab 
 */
package com.tll.di;

import java.util.Properties;

import org.apache.commons.configuration.ConfigurationConverter;
import org.apache.velocity.app.VelocityEngine;

import com.google.inject.Provider;
import com.tll.config.Config;

/**
 * VelocityModule
 * @author jpk
 */
public class VelocityModule extends GModule {

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
					final Properties vps = ConfigurationConverter.getProperties(Config.instance().subset("velocity"));
					final VelocityEngine ve = new VelocityEngine(vps);
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
