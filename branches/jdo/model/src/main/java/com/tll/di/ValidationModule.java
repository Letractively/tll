/**
 * The Logic Lab
 * @author jpk
 * @since Apr 23, 2009
 */
package com.tll.di;

import javax.validation.Validation;
import javax.validation.ValidatorFactory;

import com.google.inject.AbstractModule;
import com.google.inject.Provider;
import com.google.inject.Scopes;


/**
 * ValidationModule
 * @author jpk
 */
public class ValidationModule extends AbstractModule {

	@Override
	protected void configure() {
		bind(ValidatorFactory.class).toProvider(new Provider<ValidatorFactory>() {

			@Override
			public ValidatorFactory get() {
				return Validation.buildDefaultValidatorFactory();
			}
		}).in(Scopes.SINGLETON);
	}

}
