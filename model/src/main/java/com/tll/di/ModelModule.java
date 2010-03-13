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
import com.tll.model.schema.SchemaInfo;
import com.tll.schema.ISchemaInfo;

/**
 * ModelModule - Core bootstrapping of the model module.
 * @author jpk
 */
public final class ModelModule extends AbstractModule {
	
	private static final Log log = LogFactory.getLog(ModelModule.class);

	private final boolean validation;

	/**
	 * Constructor - Validation is loaded by default
	 */
	public ModelModule() {
		this(true);
	}

	/**
	 * Constructor
	 * @param validation load entity validation? 
	 */
	public ModelModule(boolean validation) {
		super();
		this.validation = validation;
	}

	@Override
	protected final void configure() {
		log.info("Employing Model module...");
		
		// ISchemaInfo
		bind(ISchemaInfo.class).to(SchemaInfo.class).in(Scopes.SINGLETON);

		if(validation) {
			// ValidationFactory
			bind(ValidatorFactory.class).toProvider(new Provider<ValidatorFactory>() {
	
				@Override
				public ValidatorFactory get() {
					return Validation.buildDefaultValidatorFactory();
				}
			}).in(Scopes.SINGLETON);
		}
	}
}
