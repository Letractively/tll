/**
 * The Logic Lab
 * @author jpk
 * Jan 19, 2009
 */
package com.tll.di;

import javax.validation.Validation;
import javax.validation.ValidatorFactory;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.google.inject.AbstractModule;
import com.google.inject.Provider;
import com.google.inject.Scopes;

/**
 * javax.validation (jsr-303) bootstrapping.
 * @author jpk
 */
public final class ValidationModule extends AbstractModule {
	
	private static final Log log = LogFactory.getLog(ValidationModule.class);

	@Override
	protected final void configure() {
		log.info("Employing Validation module...");
		
		// ValidationFactory
		bind(ValidatorFactory.class).toProvider(new Provider<ValidatorFactory>() {

			@Override
			public ValidatorFactory get() {
				return Validation.buildDefaultValidatorFactory();
			}
		}).in(Scopes.SINGLETON);
	}
}