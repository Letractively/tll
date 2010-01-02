package com.tll.model;

import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotEmpty;

import com.tll.model.schema.Root;

/**
 * Base class for interface options and interface option parameter definition
 * classes.
 * @author jpk
 */
@Root
public abstract class InterfaceOptionBase extends NamedTimeStampEntity {

	private static final long serialVersionUID = 342581007482865798L;

	public static final int MAXLEN_CODE = 50;
	public static final int MAXLEN_NAME = 50;
	public static final int MAXLEN_DESCRIPTION = 50;

	protected String code;
	protected String description;

	@NotEmpty
	@Length(max = MAXLEN_NAME)
	@Override
	public String getName() {
		return name;
	}

	/**
	 * @return Returns the code.
	 */
	@NotEmpty
	@Length(max = MAXLEN_CODE)
	public String getCode() {
		return code;
	}

	/**
	 * @param code The code to set.
	 */
	public void setCode(String code) {
		this.code = code;
	}

	/**
	 * @return Returns the description.
	 */
	@Length(max = MAXLEN_DESCRIPTION)
	public String getDescription() {
		return description;
	}

	/**
	 * @param description The description to set.
	 */
	public void setDescription(String description) {
		this.description = description;
	}
}
