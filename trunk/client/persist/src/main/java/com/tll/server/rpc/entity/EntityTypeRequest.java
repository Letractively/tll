/**
 * The Logic Lab
 * @author jpk
 * @since May 17, 2009
 */
package com.tll.server.rpc.entity;

import com.tll.common.data.IModelRelatedRequest;
import com.tll.common.model.IEntityType;
import com.tll.common.model.IEntityTypeProvider;

/**
 * EntityTypeRequest - A model related request driven soley by an entity type.
 * @author jpk
 */
final class EntityTypeRequest implements IModelRelatedRequest, IEntityTypeProvider {

	private final IEntityType etype;
	private final String descriptor;

	/**
	 * Constructor
	 * @param etype
	 * @param descriptor
	 */
	public EntityTypeRequest(IEntityType etype, String descriptor) {
		super();
		this.etype = etype;
		this.descriptor = descriptor;
	}

	@Override
	public IEntityType getEntityType() {
		return etype;
	}

	@Override
	public String descriptor() {
		return descriptor;
	}
}