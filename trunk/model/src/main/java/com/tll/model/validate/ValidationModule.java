/**
 * The Logic Lab
 * @author jpk
 * Jan 19, 2009
 */
package com.tll.model.validate;

import javax.validation.Validation;
import javax.validation.ValidatorFactory;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.inject.AbstractModule;
import com.google.inject.Provider;
import com.google.inject.Scopes;

/**
 * javax.validation (jsr-303) runtime bootstrapping.
 * @author jpk
 */
public class ValidationModule extends AbstractModule {
	
	private static final Logger log = LoggerFactory.getLogger(ValidationModule.class);

	@Override
	protected void configure() {
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
