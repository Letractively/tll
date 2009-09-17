package com.tll.model;

import com.tll.IMarshalable;

/**
 * AdminRole - The ordinality reflects the privelage level and this aspect is
 * exploitable.
 * @author jpk
 */
public enum AdminRole implements IMarshalable {
	ASP,
	ISP,
	MERCHANT,
	CUSTOMER,
	VISITOR;

	private AdminRole() {
	}
}
