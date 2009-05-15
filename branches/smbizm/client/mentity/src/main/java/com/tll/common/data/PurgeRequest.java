/**
 * The Logic Lab
 * @author jpk
 * Mar 1, 2008
 */
package com.tll.common.data;

import com.tll.common.model.IEntityType;
import com.tll.common.model.ModelKey;

/**
 * EntityPurgeRequest
 * @author jpk
 */
public class PurgeRequest extends EntityModelRequest {

	private ModelKey ref;

	/**
	 * Constructor
	 */
	public PurgeRequest() {
		super();
	}

	/**
	 * Constructor
	 * @param ref
	 */
	public PurgeRequest(ModelKey ref) {
		super();
		if(ref == null) throw new IllegalArgumentException("Null model ref");
		this.ref = ref;
	}

	@Override
	public String descriptor() {
		return ref == null ? "Persist Request" : "Persist Request for: " + ref.descriptor();
	}

	@Override
	public IEntityType getEntityType() {
		return ref.getEntityType();
	}

	/**
	 * @return the entityRef
	 */
	public ModelKey getRef() {
		return ref;
	}

}
