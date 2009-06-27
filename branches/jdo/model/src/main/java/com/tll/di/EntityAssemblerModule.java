/**
 * The Logic Lab
 * @author jpk
 * Jan 28, 2009
 */
package com.tll.di;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.google.inject.AbstractModule;
import com.tll.model.IEntityAssembler;


/**
 * EntityAssemblerModule
 * @author jpk
 */
public abstract class EntityAssemblerModule extends AbstractModule {

	private static final Log log = LogFactory.getLog(EntityAssemblerModule.class);

	/**
	 * Responsible for binding an {@link IEntityAssembler} implmementation.
	 */
	protected abstract void bindEntityAssembler();

	@Override
	protected void configure() {
		log.info("Employing entity asssembler module.");
		bindEntityAssembler();
	}

}
