/**
 * The Logic Lab
 * @author jpk Sep 14, 2007
 */
package com.tll.server.rpc.entity;

import com.tll.common.model.IEntityType;
import com.tll.common.model.Model;
import com.tll.common.model.RelatedOneProperty;
import com.tll.common.model.SmbizEntityType;
import com.tll.model.Account;
import com.tll.model.IEntity;
import com.tll.model.key.PrimaryKey;
import com.tll.service.entity.account.IAccountService;

public class AccountService extends AbstractPersistServiceImpl {

	/**
	 * Constructor
	 * @param context
	 */
	public AccountService(PersistContext context) {
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
		final PrimaryKey<Account> pk = new PrimaryKey<Account>(Account.class, a.getParent().getId());
		final IAccountService svc = context.getEntityServiceFactory().instance(IAccountService.class);
		final Account parent = svc.load(pk);
		final Model mparent = new Model(SmbizEntityType.ACCOUNT, true);
		mparent.setProperty(Model.ID_PROPERTY, parent.getId());
		mparent.setProperty(Model.NAME_PROPERTY, parent.getName());
		m.set(new RelatedOneProperty(SmbizEntityType.ACCOUNT, "parent", true, mparent));
		return m;
	}
}
