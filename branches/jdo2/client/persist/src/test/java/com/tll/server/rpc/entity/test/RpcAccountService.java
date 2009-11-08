/**
 * The Logic Lab
 * @author jpk Sep 14, 2007
 */
package com.tll.server.rpc.entity.test;

import com.tll.common.model.IEntityType;
import com.tll.common.model.Model;
import com.tll.common.model.RelatedOneProperty;
import com.tll.common.model.StringPropertyValue;
import com.tll.common.model.test.TestEntityType;
import com.tll.model.IEntity;
import com.tll.model.key.PrimaryKey;
import com.tll.model.test.Account;
import com.tll.server.rpc.entity.AbstractPersistServiceImpl;
import com.tll.server.rpc.entity.PersistContext;
import com.tll.service.entity.test.IAccountService;

public class RpcAccountService extends AbstractPersistServiceImpl {

	/**
	 * Constructor
	 * @param context
	 */
	public RpcAccountService(PersistContext context) {
		super(context);
	}

	@Override
	protected String getModelTypeName() {
		return "Account";
	}

	@Override
	protected Model entityToModel(IEntityType entityType, IEntity e) throws Exception {
		final Model m = super.entityToModel(entityType, e);
		assert m != null;
		final Account a = (Account) e;
		if(a.getParent() != null) {
			final PrimaryKey<Account> pk = new PrimaryKey<Account>(Account.class, a.getParent().getId());
			final IAccountService svc = context.getEntityServiceFactory().instance(IAccountService.class);
			final Account parent = svc.load(pk);
			final Model mparent = new Model(TestEntityType.ACCOUNT);
			mparent.set(new StringPropertyValue(Model.ID_PROPERTY, Long.toString(parent.getId())));
			mparent.set(new StringPropertyValue(Model.NAME_PROPERTY, parent.getName()));
			m.set(new RelatedOneProperty(TestEntityType.ACCOUNT, mparent, "parent", true));
		}
		return m;
	}
}
