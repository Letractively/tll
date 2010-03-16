/**
 * The Logic Lab
 * @author jpk
 * @since Aug 30, 2009
 */
package com.tll.di.test;

import com.tll.common.model.IEntityType;
import com.tll.common.model.test.TestEntityType;
import com.tll.di.MarshalModule;
import com.tll.server.marshal.IMarshalOptionsResolver;
import com.tll.server.marshal.MarshalOptions;
import com.tll.server.rpc.entity.IEntityTypeResolver;
import com.tll.server.rpc.entity.test.TestEntityTypeResolver;


/**
 * TestMarshalModule
 * @author jpk
 */
public class TestMarshalModule extends MarshalModule {

	static final MarshalOptions ACCOUNT_MARSHAL_OPTIONS = new MarshalOptions(false, 2, null);
	static final MarshalOptions ACCOUNT_ADDRESS_OPTIONS = new MarshalOptions(false, 1, null);
	static final MarshalOptions ADDRESS_OPTIONS = new MarshalOptions(false, 0, null);

	static class TestMarshalOptionsResolver implements IMarshalOptionsResolver {
		@Override
		public MarshalOptions resolve(IEntityType entityType) throws IllegalArgumentException {
			if(entityType instanceof TestEntityType) {
				final TestEntityType set = (TestEntityType) entityType;
				switch(set) {
				case ACCOUNT:
					return ACCOUNT_MARSHAL_OPTIONS;
				case ADDRESS:
					return ADDRESS_OPTIONS;
				case ACCOUNT_ADDRESS:
					return ACCOUNT_ADDRESS_OPTIONS;
				}
			}
			throw new IllegalArgumentException("Un-handled entity type: " + entityType);
		}
	}

	@Override
	protected Class<? extends IEntityTypeResolver> getEntityTypeResolverImplType() {
		return TestEntityTypeResolver.class;
	}

	@Override
	protected Class<? extends IMarshalOptionsResolver> getMarshalOptionsResolverImplType() {
		return TestMarshalOptionsResolver.class;
	}
}
