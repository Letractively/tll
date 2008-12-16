/**
 * The Logic Lab
 * @author jpk
 * Nov 5, 2007
 */
package com.tll.di;

import com.tll.service.app.AppRefData;

/**
 * AppRefDataModule
 * @author jpk
 */
public class AppRefDataModule extends GModule {

	/**
	 * Constructor
	 */
	public AppRefDataModule() {
		super();
		log.info("Employing App ref data module");
	}

	@Override
	protected void configure() {

		// AppRefData
		bind(AppRefData.class).toInstance(new AppRefData());
	}

}
