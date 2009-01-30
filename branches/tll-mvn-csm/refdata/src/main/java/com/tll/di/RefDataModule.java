/**
 * The Logic Lab
 * @author jpk
 * Nov 5, 2007
 */
package com.tll.di;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.google.inject.AbstractModule;
import com.tll.refdata.RefData;

/**
 * RefDataModule
 * @author jpk
 */
public class RefDataModule extends AbstractModule {

	protected static final Log log = LogFactory.getLog(RefDataModule.class);
	
	/**
	 * Constructor
	 */
	public RefDataModule() {
		super();
		log.info("Employing App ref data module");
	}

	@Override
	protected void configure() {

		// RefData
		bind(RefData.class).toInstance(new RefData());
	}

}
