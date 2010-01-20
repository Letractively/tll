/**
 * The Logic Lab
 * @author jpk
 * @since Aug 30, 2009
 */
package com.tll.di;

import com.tll.model.SmbizEntityAssembler;

/**
 * SmbizModelBuildModule
 * @author jpk
 */
public class SmbizModelBuildModule extends ModelBuildModule {

	/**
	 * Constructor
	 */
	public SmbizModelBuildModule() {
		super(SmbizEntityAssembler.class);
	}
}
