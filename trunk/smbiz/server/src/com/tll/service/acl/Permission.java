package com.tll.service.acl;

import org.acegisecurity.acl.basic.SimpleAclEntry;

import com.tll.util.INameValueProvider;
import com.tll.util.StringUtil;

/**
 * Security related permission enumeration intended to correspond with the Acegi
 * security framework. Provides loose coupling mainly for a UI context to
 * exploit.
 * @see org.acegisecurity.acl.basic.SimpleAclEntry
 * @author jpk
 */
public enum Permission implements INameValueProvider {
	NOTHING(SimpleAclEntry.NOTHING),
	ADMINISTRATION(SimpleAclEntry.ADMINISTRATION),

	READ(SimpleAclEntry.READ),
	WRITE(SimpleAclEntry.WRITE),
	CREATE(SimpleAclEntry.CREATE),
	DELETE(SimpleAclEntry.DELETE),

	READ_WRITE_CREATE_DELETE(SimpleAclEntry.READ | SimpleAclEntry.WRITE | SimpleAclEntry.CREATE | SimpleAclEntry.DELETE),
	READ_WRITE_CREATE(SimpleAclEntry.READ | SimpleAclEntry.WRITE | SimpleAclEntry.CREATE),
	READ_WRITE(SimpleAclEntry.READ | SimpleAclEntry.WRITE),
	READ_WRITE_DELETE(SimpleAclEntry.READ | SimpleAclEntry.WRITE | SimpleAclEntry.DELETE);

	private final int ival;

	Permission(int ival) {
		this.ival = ival;
	}

	public int intValue() {
		return ival;
	}

	public String getName() {
		return StringUtil.formatEnumValue(name());
	}

	public Object getValue() {
		return name();
	}
}
