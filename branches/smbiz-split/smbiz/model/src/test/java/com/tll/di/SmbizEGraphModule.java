/**
 * The Logic Lab
 * @author jpk
 * @since Aug 30, 2009
 */
package com.tll.di;

import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;

import com.tll.model.IEntityGraphPopulator;
import com.tll.model.SmbizEntityGraphBuilder;


/**
 * SmbizEGraphModule
 * @author jpk
 */
public class SmbizEGraphModule extends EGraphModule {

	private static final String ENTITY_DEFINITIONS_FILENAME = "mock-entities.xml";

	@Override
	protected URI getBeanDefRef() {
		try {
			URL url = Thread.currentThread().getContextClassLoader().getResource(ENTITY_DEFINITIONS_FILENAME);
			if(url == null) {
				url = SmbizEGraphModule.class.getResource(ENTITY_DEFINITIONS_FILENAME);
			}
			if(url != null) return url.toURI();
		}
		catch(final URISyntaxException e) {
			// fall through
		}
		throw new IllegalStateException("Can't find '" + ENTITY_DEFINITIONS_FILENAME + "' on the classpath.");
	}

	@Override
	protected Class<? extends IEntityGraphPopulator> getEntityGraphBuilderImplType() {
		return SmbizEntityGraphBuilder.class;
	}
}
