/**
 * The Logic Lab
 * @author jpk Nov 10, 2007
 */
package com.tll.client.data;

import com.tll.client.IMarshalable;
import com.tll.model.EntityType;

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
	 * The aux data request. May be <code>null</code>.
	 */
	public AuxDataRequest auxDataRequest;

	/**
	 * Constructor
	 */
	public EntityRequest() {
		super();
	}

	/**
	 * @return The entity type. Must corres. to a {@link EntityType} constant and
	 *         be non-<code>null</code>.
	 */
	public abstract EntityType getEntityType();
}
