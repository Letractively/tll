/**
 * The Logic Lab
 * @author jpk
 * Mar 1, 2008
 */
package com.tll.common.data;

import com.tll.common.model.IEntityType;
import com.tll.common.model.ModelKey;
import com.tll.common.search.ISearch;

/**
 * EntityLoadRequest
 * @author jpk
 */
public class EntityLoadRequest extends EntityRequest {

	private ModelKey entityRef;

	private ISearch search;

	private boolean loadByName;

	private boolean loadByBusinessKey;

	/**
	 * The aux data request. May be <code>null</code>.
	 */
	private AuxDataRequest auxDataRequest;

	/**
	 * Constructor
	 */
	public EntityLoadRequest() {
		super();
	}

	/**
	 * Constructor - Use for loading by primary key.
	 * @param entityRef
	 */
	public EntityLoadRequest(ModelKey entityRef) {
		super();
		this.entityRef = entityRef;
	}

	/**
	 * Constructor - Use for loading by name.
	 * @param entityType
	 * @param name
	 */
	public EntityLoadRequest(IEntityType entityType, String name) {
		super();
		this.entityRef = new ModelKey(entityType, null, name);
		this.loadByName = true;
	}

	/**
	 * Constructor
	 * @param search
	 */
	public EntityLoadRequest(ISearch search) {
		super();
		this.search = search;
		this.loadByBusinessKey = true;
	}

	@Override
	public IEntityType getEntityType() {
		return loadByBusinessKey ? search.getEntityType() : entityRef.getEntityType();
	}

	public boolean isLoadByName() {
		return loadByName;
	}

	public boolean isLoadByBusinessKey() {
		return loadByBusinessKey;
	}

	/**
	 * @return the entityRef
	 */
	public ModelKey getEntityRef() {
		return entityRef;
	}

	/**
	 * @return The search used for marshalling the business key
	 */
	public ISearch getSearch() {
		return search;
	}

	/**
	 * @return the auxDataRequest
	 */
	public AuxDataRequest getAuxDataRequest() {
		return auxDataRequest;
	}

	/**
	 * @param auxDataRequest the auxDataRequest to set
	 */
	public void setAuxDataRequest(AuxDataRequest auxDataRequest) {
		this.auxDataRequest = auxDataRequest;
	}
}
