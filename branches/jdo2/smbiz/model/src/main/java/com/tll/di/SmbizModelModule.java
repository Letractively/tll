/**
 * The Logic Lab
 * @author jpk
 * @since Aug 30, 2009
 */
package com.tll.di;

import com.tll.model.IEntityAssembler;
import com.tll.model.SmbizEntityAssembler;

/**
 * SmbizModelModule
 * @author jpk
 */
public class SmbizModelModule extends AbstractModelModule {

	@Override
	protected Class<? extends IEntityAssembler> getEntityAssemblerImplType() {
		return SmbizEntityAssembler.class;
	}

}
