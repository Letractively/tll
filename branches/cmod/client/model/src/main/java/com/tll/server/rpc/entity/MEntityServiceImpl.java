/**
 * The Logic Lab
 * @author jpk Sep 14, 2007
 */
package com.tll.server.rpc.entity;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityExistsException;
import javax.persistence.EntityNotFoundException;

import org.hibernate.validator.InvalidStateException;
import org.hibernate.validator.InvalidValue;
import org.springframework.mail.MailSendException;

import com.tll.SystemError;
import com.tll.common.data.EntityFetchPrototypeRequest;
import com.tll.common.data.EntityLoadRequest;
import com.tll.common.data.EntityOptions;
import com.tll.common.data.EntityPayload;
import com.tll.common.data.EntityPersistRequest;
import com.tll.common.data.EntityPurgeRequest;
import com.tll.common.data.RemoteListingDefinition;
import com.tll.common.data.Status;
import com.tll.common.model.Model;
import com.tll.common.model.RefKey;
import com.tll.common.msg.Msg.MsgAttr;
import com.tll.common.msg.Msg.MsgLevel;
import com.tll.common.search.ISearch;
import com.tll.config.Config;
import com.tll.criteria.Criteria;
import com.tll.criteria.CriteriaType;
import com.tll.criteria.ICriteria;
import com.tll.criteria.IQueryParam;
import com.tll.criteria.ISelectNamedQueryDef;
import com.tll.mail.MailManager;
import com.tll.mail.NameEmail;
import com.tll.model.EntityTypeUtil;
import com.tll.model.IEntity;
import com.tll.model.IEntityType;
import com.tll.model.key.IBusinessKey;
import com.tll.model.key.PrimaryKey;
import com.tll.server.rpc.AuxDataHandler;
import com.tll.server.rpc.listing.IMarshalingListHandler;
import com.tll.server.rpc.listing.MarshalingListHandler;
import com.tll.server.rpc.listing.PropKeyListHandler;
import com.tll.service.entity.IEntityService;

/**
 * MEntityServiceImpl - Provides base methods for CRUD ops on entities.
 * @author jpk
 * @param <E>
 * @param <S>
 */
public abstract class MEntityServiceImpl<E extends IEntity, S extends ISearch> implements IMEntityServiceImpl<E, S> {

	/**
	 * Used for doling out exception notification emails.
	 */
	private static final SimpleDateFormat sdf = new SimpleDateFormat("EEE, d MMM yyyy HH:mm:ss");

	/**
	 * Unified way to handle exceptions for an RPC call.
	 * @param context
	 * @param status The Status that will be sent to the client.
	 * @param t The exception.
	 * @param uiDisplayText The text to display to the user in the UI. May be
	 *        <code>null</code> in which case, the message will not be displayed
	 *        in the UI.
	 * @param emailException Whether or not a notification email is sent
	 *        containing the exception stack trace etc.
	 */
	public static void handleException(final IMEntityServiceContext context, final Status status, final Throwable t,
			final String uiDisplayText, final boolean emailException) {
		assert status != null && t != null;
		String emsg = t.getMessage();
		if(emsg == null) {
			emsg = t.getClass().getSimpleName();
		}
		assert emsg != null;
		if(t instanceof RuntimeException) {
			status.addMsg(emsg, MsgLevel.FATAL, MsgAttr.EXCEPTION.flag | MsgAttr.NODISPLAY.flag);
			if(uiDisplayText != null) {
				status.addMsg(uiDisplayText, MsgLevel.FATAL, MsgAttr.EXCEPTION.flag);
			}
		}
		else {
			status.addMsg(emsg, MsgLevel.ERROR, MsgAttr.EXCEPTION.flag | MsgAttr.NODISPLAY.flag);
			if(uiDisplayText != null) {
				status.addMsg(uiDisplayText, MsgLevel.ERROR, MsgAttr.EXCEPTION.flag);
			}
		}
		if(emailException) {
			final Map<String, Object> data = new HashMap<String, Object>();
			data.put("header", "Exception Notification (" + t.getClass().getSimpleName() + ")");
			data.put("datetime", sdf.format(new Date()));
			data.put("error", emsg);
			final StackTraceElement ste =
					(t.getStackTrace() == null || t.getStackTrace().length < 1) ? null : t.getStackTrace()[0];
			data.put("trace", ste == null ? "[NO STACK TRACE]" : ste.toString());
			try {
				final MailManager mailManager = context.getMailManager();
				final String onErrorEmail = Config.instance().getString("mail.onerror.ToAddress");
				final String onErrorName = Config.instance().getString("mail.onerror.ToName");
				final NameEmail ne = new NameEmail(onErrorName, onErrorEmail);
				mailManager.sendEmail(mailManager.buildTextTemplateContext(mailManager.buildAppSenderMailRouting(ne),
						"exception-notification", data));
			}
			catch(final MailSendException mse) {
				status.addMsg("Unable to send exception notification email: " + mse.getMessage(), MsgLevel.ERROR,
						MsgAttr.NODISPLAY.flag);
			}
		}
	}

	/**
	 * Loads additional entity properties.
	 * @param e
	 * @param options
	 * @param refs The map to place related entity references ({@link RefKey}) if
	 *        they are reuested in <code>options</code>.
	 * @param context The request context.
	 * @throws SystemError When any error occurrs.
	 */
	protected abstract void handleLoadOptions(IMEntityServiceContext context, E e, EntityOptions options,
			Map<String, RefKey> refs) throws SystemError;

	/**
	 * Handles persist options specified in options.
	 * @param e
	 * @param options
	 * @param context The request context.
	 * @throws SystemError When any error occurrs.
	 */
	protected abstract void handlePersistOptions(IMEntityServiceContext context, E e, EntityOptions options)
			throws SystemError;

	public final void getEmptyEntity(final IMEntityServiceContext context, final EntityFetchPrototypeRequest request,
			final IEntityType entityType, final EntityPayload payload) {
		try {
			final IEntity e =
					context.getEntityFactory().createEntity(
							EntityTypeUtil.entityClassFromType(entityType),
							request.isGenerate());
			final Model group =
					context.getMarshaler().marshalEntity(e, getMarshalOptions(context));
			payload.setEntity(group);
		}
		catch(final SystemError se) {
			handleException(context, payload.getStatus(), se, se.getMessage(), true);
		}
		catch(final RuntimeException re) {
			handleException(context, payload.getStatus(), re, re.getMessage(), true);
			throw re;
		}
	}

	/**
	 * Does the core entity loading.
	 * @param context
	 * @param request
	 * @param entityType
	 * @param payload
	 * @return The loaded {@link IEntity}
	 */
	@SuppressWarnings("unchecked")
	protected E coreLoad(final IMEntityServiceContext context, final EntityLoadRequest request,
			final IEntityType entityType, final EntityPayload payload) {
		// core entity loading
		final Class<E> entityClass = EntityTypeUtil.entityClassFromType(entityType);
		final IEntityService<E> svc =
				context.getEntityServiceFactory().instanceByEntityType(entityClass);

		if(request.isLoadByBusinessKey()) {
			// load by business key
			S search = (S) request.getSearch();
			if(search == null) {
				payload.getStatus().addMsg("A business key wise search must be specified.", MsgLevel.ERROR);
				return null;
			}

			IBusinessKey key = handleBusinessKeyTranslation(search);

			return svc.load(key);
		}

		// load by primary key
		final Integer id = request.getEntityRef().getId();
		return svc.load(new PrimaryKey(entityClass, id));
	}

	public final void load(final IMEntityServiceContext context, final EntityLoadRequest request,
			final IEntityType entityType, final EntityPayload payload) {
		try {
			final E e = coreLoad(context, request, entityType, payload);
			if(e == null) {
				return;
			}

			// optional loading
			final Map<String, RefKey> refs = new HashMap<String, RefKey>();
			if(request.entityOptions != null) {
				handleLoadOptions(context, e, request.entityOptions, refs);
			}

			// marshal the loaded entity
			final Model group =
					context.getMarshaler().marshalEntity(e, getMarshalOptions(context));
			payload.setEntity(group);

			// set any entity refs
			for(final String propName : refs.keySet()) {
				payload.setRelatedOneRef(propName, refs.get(propName));
			}

			// load any requested auxiliary
			if(request.auxDataRequest != null) {
				AuxDataHandler.getAuxData(context, request.auxDataRequest, payload);
			}
		}
		catch(final EntityNotFoundException enfe) {
			handleException(context, payload.getStatus(), enfe, enfe.getMessage(), false);
		}
		catch(final SystemError se) {
			handleException(context, payload.getStatus(), se, se.getMessage(), true);
		}
		catch(final RuntimeException re) {
			handleException(context, payload.getStatus(), re, re.getMessage(), true);
			throw re;
		}
	}

	public final void persist(final IMEntityServiceContext context, final EntityPersistRequest request,
			final IEntityType entityType, final EntityPayload payload) {
		try {
			// core persist
			final Class<E> entityClass = EntityTypeUtil.entityClassFromType(entityType);
			final Model entity = request.getEntity();
			E e = context.getMarshaler().unmarshalEntity(entityClass, entity);
			final IEntityService<E> svc =
					context.getEntityServiceFactory().instanceByEntityType(entityClass);
			e = svc.persist(e);

			// handle persist options
			handlePersistOptions(context, e, request.entityOptions);

			// marshall
			final Model group =
					context.getMarshaler().marshalEntity(e, getMarshalOptions(context));
			payload.setEntity(group);

			payload.getStatus().addMsg(e.descriptor() + " persisted.", MsgLevel.INFO);
		}
		catch(final EntityExistsException e1) {
			handleException(context, payload.getStatus(), e1, e1.getMessage(), false);
		}
		catch(final InvalidStateException ise) {
			for(final InvalidValue iv : ise.getInvalidValues()) {
				payload.getStatus().addMsg(iv.getMessage(), MsgLevel.ERROR, MsgAttr.FIELD.flag, iv.getPropertyPath());

			}
		}
		catch(final SystemError se) {
			handleException(context, payload.getStatus(), se, null, true);
		}
		catch(final RuntimeException re) {
			handleException(context, payload.getStatus(), re, null, true);
			throw re;
		}
	}

	@SuppressWarnings("unchecked")
	public final void purge(final IMEntityServiceContext context, final EntityPurgeRequest entityRequest,
			final IEntityType entityType, final EntityPayload p) {
		try {
			final Class<IEntity> entityClass = EntityTypeUtil.entityClassFromType(entityType);
			final IEntityService<IEntity> svc =
					context.getEntityServiceFactory().instanceByEntityType(entityClass);
			final RefKey entityRef = entityRequest.getEntityRef();
			if(entityRef == null || !entityRef.isSet()) {
				throw new EntityNotFoundException("A valid entity reference must be specified to purge an entity.");
			}
			final PrimaryKey pk = new PrimaryKey(entityClass, entityRef.getId());
			final IEntity e = svc.load(pk);
			svc.purge(e);

			p.setEntityRef(entityRef);
			p.getStatus().addMsg(e.descriptor() + " purged.", MsgLevel.INFO);
		}
		catch(final EntityNotFoundException e) {
			handleException(context, p.getStatus(), e, e.getMessage(), false);
		}
		catch(final SystemError se) {
			handleException(context, p.getStatus(), se, se.getMessage(), true);
		}
		catch(final RuntimeException re) {
			handleException(context, p.getStatus(), re, re.getMessage(), true);
			throw re;
		}
	}

	/**
	 * Translates {@link ISearch} to {@link IBusinessKey}s.
	 * @param search The search to translate
	 * @return Translated {@link IBusinessKey}
	 */
	protected abstract IBusinessKey<E> handleBusinessKeyTranslation(S search);

	/**
	 * Handles the entity specific search to criteria translation.
	 * @param search
	 * @param criteria
	 * @throws IllegalArgumentException When the <code>search</code> parameter is
	 *         unsupported.
	 */
	protected abstract void handleSearchTranslation(IMEntityServiceContext context, S search,
			ICriteria<? extends E> criteria) throws IllegalArgumentException;

	public final ICriteria<E> translate(final IMEntityServiceContext context, final IEntityType entityType, final S search)
			throws IllegalArgumentException {
		final CriteriaType criteriaType = search.getCriteriaType();
		final Class<E> entityClass = EntityTypeUtil.entityClassFromType(entityType);
		Criteria<E> criteria;
		final List<IQueryParam> queryParams = search.getQueryParams();

		if(criteriaType.isQuery()) {
			//SelectNamedQueries nq = EnumUtil.fromString(SelectNamedQueries.class, search.getNamedQuery());
			ISelectNamedQueryDef queryDef = getQueryResolver().resolveNamedQuery(search.getNamedQuery());
			if(queryDef == null) {
				throw new IllegalArgumentException("Unable to resolve named query: " + search.getNamedQuery());
			}
			criteria = new Criteria<E>(queryDef, queryParams);
		}
		else {
			// entity
			criteria = new Criteria<E>(entityClass);
			handleSearchTranslation(context, search, criteria);
		}

		return criteria;
	}

	/*
	 * Sub-classes should override this method for specific table requirements
	 * based on the listing command particulars.
	 */
	public IMarshalingListHandler<E> getMarshalingListHandler(final IMEntityServiceContext context,
			final RemoteListingDefinition<S> listingDefinition) {
		if(listingDefinition.getPropKeys() != null) {
			return new PropKeyListHandler<E>(context.getMarshaler(),
					getMarshalOptions(context),
					listingDefinition.getPropKeys());
		}
		return new MarshalingListHandler<E>(context.getMarshaler(),
				getMarshalOptions(context));
	}
}
