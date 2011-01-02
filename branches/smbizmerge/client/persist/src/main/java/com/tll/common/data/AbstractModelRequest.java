/**
 * The Logic Lab
 * @author jpk Nov 10, 2007
 */
package com.tll.common.data;



/**
 * AbstractModelRequest - Encapsulates the needed properties to fullfill an model
 * related request.
 * <p>
 * NOTE: Not all properties may be set for a particular model request.
 * @author jpk
 */
public abstract class AbstractModelRequest implements IModelRelatedRequest {

	/**
	 * Constructor
	 */
	public AbstractModelRequest() {
		super();
	}
}
