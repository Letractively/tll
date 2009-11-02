/*
 * The Logic Lab
 */
package com.tll.di;

import java.io.File;
import java.io.FileReader;
import java.net.URL;
import java.util.Properties;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.velocity.app.VelocityEngine;

import com.google.inject.AbstractModule;
import com.google.inject.Provider;
import com.tll.util.ClassUtil;

/**
 * VelocityModule
 * @author jpk
 */
public class VelocityModule extends AbstractModule {

	/**
	 * The resource token for the velocity.properties expected to be at the ROOT
	 * of the classpath.
	 */
	private static final String VELOCITY_PROPS_RESOURCE = "velocity.properties";

	static final Log log = LogFactory.getLog(VelocityModule.class);

	Properties loadProperties() {
		try {
			final URL url = ClassUtil.getResource(VELOCITY_PROPS_RESOURCE);
			if(url == null) return null;
			final Properties props = new Properties();
			props.load(new FileReader(new File(url.toURI())));
			log.info("Velocity properties loaded from: " + url);
			return props;
		}
		catch(final Exception e) {
			final String emsg = "Unable to load velocity.properties file: " + e.getMessage();
			log.warn(emsg);
			throw new IllegalStateException(emsg);
		}
	}

	void doInit(VelocityEngine ve, Properties props) {
		try {
			if(props != null)
				ve.init(props);
			else
				ve.init();
		}
		catch(final Exception e) {
			final String emsg = "Unable to instantiate the velocity engine: " + e.getMessage();
			log.warn(emsg);
			throw new IllegalStateException(emsg, e);
		}
	}

	@Override
	protected void configure() {
		log.info("Employing Velocity engine");

		// VelocityEngine
		bind(VelocityEngine.class).toProvider(new Provider<VelocityEngine>() {

			public VelocityEngine get() {
				VelocityEngine ve = null;
				try {
					ve = new VelocityEngine();
					doInit(ve, loadProperties());
				}
				catch(final Exception e) {
					// try to revert to default velocity init routine (w/ default
					// properties set by velocity)
					log.warn("Reverting to default velocity properties");
					ve = new VelocityEngine();
					doInit(ve, null);
				}
				return ve;
			}

		});

	}

}
