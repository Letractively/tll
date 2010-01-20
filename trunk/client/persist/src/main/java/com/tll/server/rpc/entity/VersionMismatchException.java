/**
 * The Logic Lab
 * @author jpk
 * @since Sep 30, 2009
 */
package com.tll.server.rpc.entity;

import com.tll.ApplicationException;
import com.tll.model.IEntity;

/**
 * VersionMismatchException - Use when updating entities and the versions of the
 * incoming model data doesn't equal the version of the entity to update.
 * @author jpk
 */
public class VersionMismatchException extends ApplicationException {
	private static final long serialVersionUID = 3442108456064187841L;

	/**
	 * Constructor
	 * @param entityClass
	 * @param entityVersion
	 * @param modelVersion
	 */
	public VersionMismatchException(Class<? extends IEntity> entityClass, Long entityVersion, Long modelVersion) {
		super("Version mis-match [model version: %0] [entity version: %1]", new Object[] {
			modelVersion, entityVersion
		});
	}
}
