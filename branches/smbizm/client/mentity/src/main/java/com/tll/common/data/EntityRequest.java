/**
 * The Logic Lab
 * @author jpk Nov 10, 2007
 */
package com.tll.common.data;

import com.tll.IMarshalable;
import com.tll.common.model.IEntityType;

/**
 * EntityRequest - Encapsulates the needed properties to fullfill an entity
 * related request.
 * <p>
 * NOTE: Not all properties may be set for a particular entity request.
 * @author jpk
 */
public abstract class EntityRequest implements IMarshalable {

	/**
	 * The entity options. May be <code>null</code>.
	 */
	public EntityOptions entityOptions;

	/**
	 * Constructor
	 */
	public EntityRequest() {
		super();
	}

	/**
	 * @return The required entity type.
	 */
	public abstract IEntityType getEntityType();
}
