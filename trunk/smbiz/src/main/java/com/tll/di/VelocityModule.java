/*
 * The Logic Lab
 */
package com.tll.di;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Properties;

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

	Properties getVelocityProperties() {
		try {
			final Properties props = new Properties();
			final URL url = VelocityModule.class.getResource("velocity.properties");
			if(url == null) return null;	// can't find
			props.load(new FileReader(new File(url.toURI())));
			return props;
		}
		catch(final FileNotFoundException e) {
			throw new IllegalStateException("Can't find velocity.properties file");
		}
		catch(final IOException e) {
			throw new IllegalStateException("Can't read from velocity.properties file: " + e.getMessage());
		}
		catch(final URISyntaxException e) {
			throw new IllegalStateException("Can't read from velocity.properties file: " + e.getMessage());
		}
	}

	@Override
	protected void configure() {
		log.info("Employing Velocity engine");

		// VelocityEngine
		bind(VelocityEngine.class).toProvider(new Provider<VelocityEngine>() {

			public VelocityEngine get() {
				try {
					final Properties props = getVelocityProperties();
					if(props == null) log.warn("Could not find velocity.properties file");
					final VelocityEngine ve = props == null ? new VelocityEngine() : new VelocityEngine(props);
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
