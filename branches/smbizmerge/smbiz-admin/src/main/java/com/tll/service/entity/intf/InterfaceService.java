package com.tll.service.entity.intf;

import java.util.Collection;
import java.util.HashMap;
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
import com.tll.model.EntityMetadata;
import com.tll.model.IEntityAssembler;
import com.tll.model.Interface;
import com.tll.model.InterfaceOption;
import com.tll.model.InterfaceOptionAccount;
import com.tll.model.InterfaceOptionParameterDefinition;
import com.tll.model.bk.BusinessKeyFactory;
import com.tll.model.bk.BusinessKeyNotDefinedException;
import com.tll.model.bk.IBusinessKey;
import com.tll.service.entity.NamedEntityService;

/**
 * InterfaceService - {@link IInterfaceService} impl
 * @author jpk
 */
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
	 * Sets properties on the <code>aio</code> param given the <code>ioa</code>
	 * param.
	 * @param io The required interface option
	 * @param ioa The optional {@link InterfaceOptionAccount}
	 */
	private AccountInterfaceOption generateAccountInterfaceOption(InterfaceOption io, InterfaceOptionAccount ioa) {
		assert io != null;
		final AccountInterfaceOption aio = entityAssembler.assembleEntity(AccountInterfaceOption.class, null);
		aio.setId(io.getId());
		aio.setVersion(Integer.valueOf(1));	// mimic non-new entity
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
		aio.setSubscribed(ioa != null);	// account is subscribed simply if there is a InterfaceOptionAccount record
		final LinkedHashSet<AccountInterfaceOptionParameter> aiops = new LinkedHashSet<AccountInterfaceOptionParameter>();
		aio.setParameters(aiops);
		for(final InterfaceOptionParameterDefinition iopd : io.getParameters()) {
			final AccountInterfaceOptionParameter aiop = entityAssembler.assembleEntity(AccountInterfaceOptionParameter.class, null);
			aiop.setId(iopd.getId());
			aiop.setVersion(Integer.valueOf(1));	// mimic non-new entity
			aiops.add(aiop);
			aiop.setCode(iopd.getCode());
			aiop.setName(iopd.getName());
			aiop.setDescription(iopd.getDescription());
			aiop.setDateCreated(iopd.getDateCreated());
			aiop.setDateModified(iopd.getDateModified());
			aiop.setValue(ioa == null ? null : ioa.getParameters().get(iopd.getName()));
		}
		return aio;
	}

	@Transactional(readOnly = true)
	@Override
	public AccountInterface loadAccountInterface(Long accountKey, Long interfaceKey) {
		IBusinessKey<InterfaceOptionAccount> bk;
		try {
			BusinessKeyFactory bkf = new BusinessKeyFactory(new EntityMetadata());
			bk = bkf.create(InterfaceOptionAccount.class, "Option Id and Account Id");
		}
		catch(final BusinessKeyNotDefinedException e) {
			throw new IllegalStateException(e);
		}
		bk.setPropertyValue("account.id", accountKey);

		final Interface intf = dao.load(Interface.class, interfaceKey);

		final LinkedHashSet<AccountInterfaceOption> aios = new LinkedHashSet<AccountInterfaceOption>();
		InterfaceOptionAccount ioa;
		for(final InterfaceOption io : intf.getOptions()) {
			bk.setPropertyValue("option.id", io.getId());
			try {
				ioa = dao.load(bk);
			}
			catch(final EntityNotFoundException e) {
				ioa = null;
			}
			final AccountInterfaceOption aio = generateAccountInterfaceOption(io, ioa);
			aios.add(aio);
		}

		final AccountInterface ai = entityAssembler.assembleEntity(AccountInterface.class, null);
		ai.setVersion(Integer.valueOf(1)); // mimic non-new entity
		ai.setAccountKey(accountKey);
		ai.setInterfaceKey(interfaceKey);
		ai.setName(intf.getName());
		ai.setCode(intf.getCode());
		ai.setDescription(intf.getDescription());
		ai.setAvailableAsp(intf.getIsAvailableAsp());
		ai.setAvailableIsp(intf.getIsAvailableIsp());
		ai.setAvailableMerchant(intf.getIsAvailableMerchant());
		ai.setAvailableCustomer(intf.getIsAvailableCustomer());
		ai.setRequiredAsp(intf.getIsRequiredAsp());
		ai.setRequiredIsp(intf.getIsRequiredIsp());
		ai.setRequiredMerchant(intf.getIsRequiredMerchant());
		ai.setRequiredCustomer(intf.getIsRequiredCustomer());
		ai.setOptions(aios);

		return ai;
	}

	@Transactional
	@Override
	public void setAccountInterface(AccountInterface accountInterface) {

		final Account account = dao.load(Account.class, accountInterface.getAccountKey());
		final Interface intf = dao.load(Interface.class, accountInterface.getInterfaceKey());

		// remove existing account subscribed options
		IBusinessKey<InterfaceOptionAccount> bk;
		try {
			BusinessKeyFactory bkf = new BusinessKeyFactory(new EntityMetadata());
			bk = bkf.create(InterfaceOptionAccount.class, "Option Id and Account Id");
		}
		catch(final BusinessKeyNotDefinedException e) {
			throw new IllegalStateException(e);
		}
		bk.setPropertyValue("account.id", accountInterface.getAccountKey());

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

		// add the replacement ioas
		for(final AccountInterfaceOption aio : accountInterface.getOptions()) {
			final InterfaceOptionAccount ioa = entityAssembler.assembleEntity(InterfaceOptionAccount.class, null);
			ioa.setAccount(account);
			final InterfaceOption io = dao.load(InterfaceOption.class, aio.getId());
			ioa.setOption(io);
			ioa.setSetUpPrice(aio.getSetUpPrice());
			ioa.setMonthlyPrice(aio.getMonthlyPrice());
			ioa.setAnnualPrice(aio.getAnnualPrice());

			final HashMap<String, String> mparams = new HashMap<String, String>();
			for(final AccountInterfaceOptionParameter aiop : aio.getParameters()) {
				mparams.put(aiop.getName(), aiop.getValue());
			}
			ioa.setParameters(mparams);

			dao.persist(ioa);
		}

	}

	@Transactional
	@Override
	public void setAccountInterfaces(Collection<AccountInterface> accountInterfaces) {
		if(accountInterfaces == null || accountInterfaces.size() < 1) {
			throw new IllegalArgumentException("At least one account interface must be present");
		}
		for(final AccountInterface ai : accountInterfaces) {
			setAccountInterface(ai);
		}
	}

	@Transactional
	@Override
	public void purgeAccountInterface(Long accountKey, Long interfaceKey) {
		Interface intf;
		try {
			intf = load(interfaceKey);
		}
		catch(final EntityNotFoundException e) {
			// ok
			return;
		}

		IBusinessKey<InterfaceOptionAccount> bk;
		try {
			BusinessKeyFactory bkf = new BusinessKeyFactory(new EntityMetadata());
			bk = bkf.create(InterfaceOptionAccount.class, "Option Id and Account Id");
		}
		catch(final BusinessKeyNotDefinedException e) {
			throw new IllegalStateException(e);
		}
		bk.setPropertyValue("account.id", accountKey);

		for(final InterfaceOption io : intf.getOptions()) {
			bk.setPropertyValue("option.id", io.getId());
			try {
				final InterfaceOptionAccount ioa = dao.load(bk);
				dao.purge(ioa);
			}
			catch(final EntityNotFoundException e) {
				// ok
			}
		}
	}

	@Transactional
	@Override
	public void purgeAccountInterfacess(Long accountKey) {
		final Collection<Interface> intfs = dao.loadAll(Interface.class);
		for(final Interface i : intfs) {
			purgeAccountInterface(accountKey, i.getId());
		}
	}
}
