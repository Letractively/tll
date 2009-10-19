/**
 * The Logic Lab
 * @author jpk
 * @since May 10, 2009
 */
package com.tll.client.model;

import com.tll.client.cache.AuxDataCache;
import com.tll.common.model.Model;
import com.tll.common.model.SmbizEntityType;

/**
 * ModelAssembler - A client side assembler of smbiz entity types that relies on
 * aux data caching.
 * @author jpk
 */
public abstract class ModelAssembler {

	/**
	 * Assembles a new {@link Model} instance having properties corresponding to
	 * the desired entity type.
	 * @param type the desired entity type
	 * @return A fresh {@link Model} instance
	 */
	public static Model assemble(SmbizEntityType type) {
		// default behavior
		final Model m = AuxDataCache.get().getEntityPrototype(type);

		// handle special cases
		if(type == SmbizEntityType.INTERFACE_SWITCH) {
			final Model op = AuxDataCache.get().getEntityPrototype(SmbizEntityType.INTERFACE_OPTION);
			m.relatedMany("options").getModelList().add(op);
		}

		return m;
	}
}
