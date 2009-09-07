package com.tll.service.entity.intf;

import java.util.HashSet;
import java.util.LinkedHashSet;

import javax.validation.ValidatorFactory;

import org.springframework.transaction.annotation.Transactional;

import com.google.inject.Inject;
import com.tll.dao.EntityNotFoundException;
import com.tll.dao.IEntityDao;
import com.tll.model.Account;
import com.tll.model.AccountInterface;
import com.tll.model.AccountInterfaceOption;
import com.tll.model.AccountInterfaceOptionParameter;
import com.tll.model.IEntityAssembler;
import com.tll.model.Interface;
import com.tll.model.InterfaceOption;
import com.tll.model.InterfaceOptionAccount;
import com.tll.model.InterfaceOptionParameterDefinition;
import com.tll.model.key.BusinessKeyFactory;
import com.tll.model.key.BusinessKeyNotDefinedException;
import com.tll.model.key.IBusinessKey;
import com.tll.model.key.PrimaryKey;
import com.tll.service.entity.NamedEntityService;

/**
 * InterfaceService - {@link IInterfaceService} impl
 * @author jpk
 */
@Transactional
public class InterfaceService extends NamedEntityService<Interface> implements IInterfaceService {

	/**
	 * Constructor
	 * @param dao
	 * @param entityAssembler
	 * @param vfactory
	 */
	@Inject
	public InterfaceService(IEntityDao dao, IEntityAssembler entityAssembler, ValidatorFactory vfactory) {
		super(dao, entityAssembler, vfactory);
	}

	@Override
	public Class<Interface> getEntityClass() {
		return Interface.class;
	}

	/**
	 * Sets properties on the <code>aio</code> param given the <code>ioa</code> param.
	 * @param aio The recipient
	 * @param ioa The giver
	 */
	private void apply(AccountInterfaceOption aio, InterfaceOptionAccount ioa) {
		final InterfaceOption io = ioa.getOption();
		aio.setAnnualCost(io.getAnnualCost());
		aio.setAnnualPrice(io.getBaseAnnualPrice());
		aio.setBaseAnnualPrice(io.getBaseAnnualPrice());
		aio.setBaseMonthlyPrice(io.getBaseMonthlyPrice());
		aio.setBaseSetupPrice(io.getBaseSetupPrice());
		aio.setCode(io.getCode());
		aio.setDateCreated(io.getDateCreated());
		aio.setDateModified(io.getDateModified());
		aio.setDefault(io.isDefault());
		aio.setDescription(io.getDescription());
		aio.setMonthlyCost(io.getMonthlyCost());
		aio.setName(io.getName());
		aio.setSetUpCost(io.getSetUpCost());
		final LinkedHashSet<AccountInterfaceOptionParameter> aiops = new LinkedHashSet<AccountInterfaceOptionParameter>();
		for(final InterfaceOptionParameterDefinition iopd : io.getParameters()) {
			final AccountInterfaceOptionParameter aiop = new AccountInterfaceOptionParameter();
			aiops.add(aiop);
			aio.setParameters(aiops);
			aiop.setCode(iopd.getCode());
			aiop.setName(iopd.getName());
			aiop.setDescription(iopd.getDescription());
			aiop.setDateCreated(iopd.getDateCreated());
			aiop.setDateModified(iopd.getDateModified());
			aiop.setValue(ioa.getParameters().get(iopd.getName()));
		}
	}

	@Override
	public AccountInterface loadAccountOptions(String accountId, String interfaceId) {
		final Interface intf = dao.load(new PrimaryKey<Interface>(Interface.class, interfaceId));
		IBusinessKey<InterfaceOptionAccount> bk;
		try {
			bk = BusinessKeyFactory.create(InterfaceOptionAccount.class, "Option Id and Account Id");
		}
		catch(final BusinessKeyNotDefinedException e) {
			throw new IllegalStateException(e);
		}
		bk.setPropertyValue("account.id", accountId);
		final LinkedHashSet<AccountInterfaceOption> aios = new LinkedHashSet<AccountInterfaceOption>();
		InterfaceOptionAccount ioa;
		for(final InterfaceOption io : intf.getOptions()) {
			bk.setPropertyValue("option.id", io.getId());
			try {
				ioa = dao.load(bk);
				final AccountInterfaceOption aio = new AccountInterfaceOption();
				apply(aio, ioa);
				aios.add(aio);
			}
			catch(final EntityNotFoundException e) {
				// ok
			}
		}

		return new AccountInterface(accountId, interfaceId, aios);
	}

	@Override
	public void persistAccountOptions(AccountInterface accountInterface) {

		final Account account = dao.load(new PrimaryKey<Account>(Account.class, accountInterface.getAccountId()));
		final Interface intf = dao.load(new PrimaryKey<Interface>(Interface.class, accountInterface.getInterfaceId()));

		// remove existing account int options based on the given interface id
		IBusinessKey<InterfaceOptionAccount> bk;
		try {
			bk = BusinessKeyFactory.create(InterfaceOptionAccount.class, "Option Id and Account Id");
		}
		catch(final BusinessKeyNotDefinedException e) {
			throw new IllegalStateException(e);
		}
		bk.setPropertyValue("account.id", accountInterface.getAccountId());

		for(final InterfaceOption io : intf.getOptions()) {
			try {
				bk.setPropertyValue("option.id", io.getId());
				final InterfaceOptionAccount ioa = dao.load(bk);
				dao.purge(ioa);
			}
			catch(final EntityNotFoundException e) {
				// ok
			}
		}

		// transform into a collection of InterfaceOptionAccounts
		final HashSet<InterfaceOptionAccount> ioas = new HashSet<InterfaceOptionAccount>();
		for(final AccountInterfaceOption aio : accountInterface.getOptions()) {
			final InterfaceOptionAccount ioa = entityAssembler.assembleEntity(InterfaceOptionAccount.class, null, true);
			ioa.setAccount(account);
			ioa.setOption(aio.getId());
		}

		// add the replacement ioas
		// TODO finish
	}

}
