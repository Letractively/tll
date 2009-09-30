/**
 * The Logic Lab
 * @author jpk
 * @since Aug 30, 2009
 */
package com.tll.di;

import java.net.URI;
import java.net.URISyntaxException;

import com.tll.di.test.EGraphModule;
import com.tll.model.SmbizEntityGraphBuilder;
import com.tll.model.test.IEntityGraphPopulator;


/**
 * SmbizEGraphModule
 * @author jpk
 */
public class SmbizEGraphModule extends EGraphModule {

	@Override
	protected URI getBeanDefRef() {
		try {
			return new URI("target/classes/mock-entities.xml");
		}
		catch(final URISyntaxException e) {
			throw new IllegalStateException("Can't find mock entities file", e);
		}
	}

	@Override
	protected Class<? extends IEntityGraphPopulator> getEntityGraphBuilderImplType() {
		return SmbizEntityGraphBuilder.class;
	}
}
