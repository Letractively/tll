/**
 * The Logic Lab
 * @author jpk
 * Feb 21, 2008
 */
package com.tll.server.rpc.entity;

import com.tll.common.data.ModelPayload;
import com.tll.common.model.Model;
import com.tll.common.model.SmbizEntityType;
import com.tll.common.search.AccountInterfaceDataSearch;
import com.tll.common.search.ISearch;
import com.tll.model.AccountInterface;
import com.tll.server.marshal.MarshalOptions;
import com.tll.server.marshal.Marshaler;
import com.tll.service.entity.intf.IInterfaceService;

/**
 * AccountInterfaceService - Handles account subscriptions to the defined
 * interface options.
 * @author jpk
 */
public class AccountInterfaceService extends AbstractPersistServiceImpl {

	/**
	 * Constructor
	 * @param context
	 */
	public AccountInterfaceService(PersistContext context) {
		super(context);
	}

	@Override
	public void loadImpl(ISearch search, ModelPayload payload) {
		if(search instanceof AccountInterfaceDataSearch) {
			final AccountInterfaceDataSearch ais = (AccountInterfaceDataSearch) search;

			final IInterfaceService isvc = context.getEntityServiceFactory().instance(IInterfaceService.class);
			final long aid = Long.parseLong(ais.getAccountId());
			final long iid = Long.parseLong(ais.getInterfaceId());
			final AccountInterface ai = isvc.loadAccountInterface(aid, iid);

			final Marshaler marshaler = context.getMarshaler();
			final MarshalOptions moptions = context.getMarshalOptionsResolver().resolve(SmbizEntityType.ACCOUNT_INTERFACE);
			final Model m = marshaler.marshalEntity(ai, moptions);
			payload.setModel(m);
		}
		else
			throw new IllegalArgumentException("Unhandled account interface data search type.");
	}

	@Override
	public void doAdd(Model model, ModelPayload payload) {
		doUpdate(model, payload);
	}

	@Override
	public void doUpdate(Model model, ModelPayload payload) {
		// [re-]set the account interface options for a given interface and account

		final Marshaler marshaler = context.getMarshaler();

		final IInterfaceService isvc = context.getEntityServiceFactory().instance(IInterfaceService.class);

		final String said = model.asString("accountId");
		final String siid = model.asString("interfaceId");
		final long aid = Long.parseLong(said);
		final long iid = Long.parseLong(siid);
		AccountInterface ai = isvc.loadAccountInterface(aid, iid);

		ai = marshaler.marshalModel(model, ai);
		isvc.setAccountInterface(ai);

		final MarshalOptions moptions = context.getMarshalOptionsResolver().resolve(SmbizEntityType.ACCOUNT_INTERFACE);
		final Model m = marshaler.marshalEntity(ai, moptions);
		payload.setModel(m);
	}
}
