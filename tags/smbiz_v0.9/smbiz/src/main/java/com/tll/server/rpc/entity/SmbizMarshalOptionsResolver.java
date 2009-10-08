/**
 * The Logic Lab
 * @author jpk
 * @since Jun 24, 2009
 */
package com.tll.server.rpc.entity;

import com.tll.common.model.IEntityType;
import com.tll.common.model.SmbizEntityType;
import com.tll.server.marshal.MarshalOptions;

/**
 * SmbizMarshalOptionsResolver
 * @author jpk
 */
public class SmbizMarshalOptionsResolver implements IMarshalOptionsResolver {

	public static final MarshalOptions ACCOUNT_MARSHAL_OPTIONS = new MarshalOptions(false, 2, new String[] { "parent" });
	public static final MarshalOptions ACCOUNT_ADDRESS_OPTIONS = new MarshalOptions(false, 1, null);
	public static final MarshalOptions ADDRESS_OPTIONS = new MarshalOptions(false, 0, null);
	public static final MarshalOptions AUTHORITY_OPTIONS = new MarshalOptions(false, 0, null);
	public static final MarshalOptions INTERFACE_OPTIONS = MarshalOptions.UNCONSTRAINED_MARSHALING;
	public static final MarshalOptions USER_OPTIONS = new MarshalOptions(true, 2, new String[] { "parent" });
	public static final MarshalOptions USER_OPTIONS_ADMIN_CONTEXT = new MarshalOptions(true, 1, new String[] { "parent" });

	@Override
	public MarshalOptions resolve(IEntityType entityType) throws IllegalArgumentException {
		if(entityType instanceof SmbizEntityType) {
			final SmbizEntityType set = (SmbizEntityType) entityType;
			switch(set) {
				case ACCOUNT:
				case ASP:
				case ISP:
				case MERCHANT:
				case CUSTOMER:
					return ACCOUNT_MARSHAL_OPTIONS;
				case INTERFACE:
				case INTERFACE_SINGLE:
				case INTERFACE_MULTI:
				case INTERFACE_SWITCH:
					return INTERFACE_OPTIONS;
				case ACCOUNT_INTERFACE:
					// TODO should these options be different?
					return INTERFACE_OPTIONS;
				case ADDRESS:
					return ADDRESS_OPTIONS;
				case USER:
					return USER_OPTIONS;
				case ACCOUNT_ADDRESS:
					return ACCOUNT_ADDRESS_OPTIONS;
				case AUTHORITY:
					return AUTHORITY_OPTIONS;
			}
		}
		throw new IllegalArgumentException("Un-handled entity type: " + entityType);
	}
}
