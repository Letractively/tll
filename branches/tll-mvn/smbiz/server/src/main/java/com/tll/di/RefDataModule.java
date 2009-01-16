/**
 * The Logic Lab
 * @author jpk
 * Nov 5, 2007
 */
package com.tll.di;

import com.tll.refdata.RefData;

/**
 * RefDataModule
 * @author jpk
 */
public class RefDataModule extends GModule {

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
