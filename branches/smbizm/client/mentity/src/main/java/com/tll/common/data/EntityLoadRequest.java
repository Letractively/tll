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
 * LoadRequest
 * @author jpk
 */
public class EntityLoadRequest extends EntityModelRequest {

	private ModelKey ref;

	private ISearch search;

	private boolean loadByName;

	private boolean loadByBusinessKey;

	/**
	 * The entity options. May be <code>null</code>.
	 */
	private EntityOptions entityOptions;

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
	 * @param ref
	 */
	public EntityLoadRequest(ModelKey ref) {
		super();
		this.ref = ref;
	}

	/**
	 * Constructor - Use for loading by name.
	 * @param entityType
	 * @param name
	 */
	public EntityLoadRequest(IEntityType entityType, String name) {
		super();
		this.ref = new ModelKey(entityType, null, name);
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
	public String descriptor() {
		// TODO be instance data specific
		return "Load Request";
	}

	@Override
	public IEntityType getEntityType() {
		return loadByBusinessKey ? search.getEntityType() : ref.getEntityType();
	}

	public boolean isLoadByName() {
		return loadByName;
	}

	public boolean isLoadByBusinessKey() {
		return loadByBusinessKey;
	}

	/**
	 * @return the model ref
	 */
	public ModelKey getRef() {
		return ref;
	}

	/**
	 * @return The search used for marshalling the business key
	 */
	public ISearch getSearch() {
		return search;
	}

	/**
	 * @return the entityOptions
	 */
	public EntityOptions getEntityOptions() {
		return entityOptions;
	}

	/**
	 * @param entityOptions the entityOptions to set
	 */
	public void setEntityOptions(EntityOptions entityOptions) {
		this.entityOptions = entityOptions;
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
