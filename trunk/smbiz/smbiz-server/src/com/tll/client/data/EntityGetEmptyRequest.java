/**
 * The Logic Lab
 * @author jpk
 * Mar 1, 2008
 */
package com.tll.client.data;

/**
 * EntityGetEmptyRequest
 * @author jpk
 */
public class EntityGetEmptyRequest extends EntityRequest {

	private String entityType;

	/**
	 * Tells the server to generate (set the id) of a newly created entity.
	 */
	private boolean generate;

	/**
	 * Constructor
	 */
	public EntityGetEmptyRequest() {
		super();
	}

	/**
	 * Constructor
	 * @param entityType
	 * @param generate
	 */
	public EntityGetEmptyRequest(String entityType, boolean generate) {
		super();
		this.generate = generate;
	}

	public String descriptor() {
		return "Get empty entity request";
	}

	@Override
	public String getEntityType() {
		return entityType;
	}

	/**
	 * @return the generate
	 */
	public boolean isGenerate() {
		return generate;
	}

}
