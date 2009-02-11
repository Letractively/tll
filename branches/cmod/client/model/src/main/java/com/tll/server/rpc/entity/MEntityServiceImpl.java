/**
 * The Logic Lab
 * @author jpk Sep 14, 2007
 */
package com.tll.server.rpc.entity;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityExistsException;
import javax.persistence.EntityNotFoundException;

import org.hibernate.validator.InvalidStateException;
import org.hibernate.validator.InvalidValue;

import com.tll.SystemError;
import com.tll.common.data.EntityLoadRequest;
import com.tll.common.data.EntityOptions;
import com.tll.common.data.EntityPayload;
import com.tll.common.data.EntityPersistRequest;
import com.tll.common.data.EntityPrototypeRequest;
import com.tll.common.data.EntityPurgeRequest;
import com.tll.common.data.RemoteListingDefinition;
import com.tll.common.model.Model;
import com.tll.common.model.RefKey;
import com.tll.common.msg.Msg.MsgAttr;
import com.tll.common.msg.Msg.MsgLevel;
import com.tll.common.search.ISearch;
import com.tll.criteria.Criteria;
import com.tll.criteria.CriteriaType;
import com.tll.criteria.ICriteria;
import com.tll.criteria.IQueryParam;
import com.tll.criteria.ISelectNamedQueryDef;
import com.tll.model.IEntity;
import com.tll.model.key.IBusinessKey;
import com.tll.model.key.PrimaryKey;
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

	public final void prototype(final IMEntityServiceContext context, final EntityPrototypeRequest request,
			final EntityPayload payload) {
		try {
			final IEntity e =
					context.getEntityFactory().createEntity(
							EntityTypeUtil.getEntityClass(request.getEntityType()),
							request.isGenerate());
			final Model group =
					context.getMarshaler().marshalEntity(e, getMarshalOptions(context));
			payload.setEntity(group);
		}
		catch(final SystemError se) {
			context.getExceptionHandler().handleException(payload.getStatus(), se, se.getMessage(), true);
		}
		catch(final RuntimeException re) {
			context.getExceptionHandler().handleException(payload.getStatus(), re, re.getMessage(), true);
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
			final EntityPayload payload) {
		
		// core entity loading
		final Class<E> entityClass = (Class<E>) EntityTypeUtil.getEntityClass(request.getEntityType());
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
			final EntityPayload payload) {
		try {
			final E e = coreLoad(context, request, payload);
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
		catch(final EntityNotFoundException e) {
			context.getExceptionHandler().handleException(payload.getStatus(), e, e.getMessage(), false);
		}
		catch(final SystemError e) {
			context.getExceptionHandler().handleException(payload.getStatus(), e, e.getMessage(), true);
		}
		catch(final RuntimeException e) {
			context.getExceptionHandler().handleException(payload.getStatus(), e, e.getMessage(), true);
			throw e;
		}
	}

	@SuppressWarnings("unchecked")
	public final void persist(final IMEntityServiceContext context, final EntityPersistRequest request,
			final EntityPayload payload) {
		try {
			// core persist
			final Class<E> entityClass = (Class<E>) EntityTypeUtil.getEntityClass(request.getEntityType());
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
		catch(final EntityExistsException e) {
			context.getExceptionHandler().handleException(payload.getStatus(), e, e.getMessage(), false);
		}
		catch(final InvalidStateException ise) {
			for(final InvalidValue iv : ise.getInvalidValues()) {
				payload.getStatus().addMsg(iv.getMessage(), MsgLevel.ERROR, MsgAttr.FIELD.flag, iv.getPropertyPath());

			}
		}
		catch(final SystemError e) {
			context.getExceptionHandler().handleException(payload.getStatus(), e, e.getMessage(), true);
		}
		catch(final RuntimeException e) {
			context.getExceptionHandler().handleException(payload.getStatus(), e, e.getMessage(), true);
			throw e;
		}
	}

	@SuppressWarnings("unchecked")
	public final void purge(final IMEntityServiceContext context, final EntityPurgeRequest request,
			final EntityPayload payload) {
		try {
			final Class<E> entityClass = (Class<E>) EntityTypeUtil.getEntityClass(request.getEntityType());
			final IEntityService<E> svc =
					context.getEntityServiceFactory().instanceByEntityType(entityClass);
			final RefKey entityRef = request.getEntityRef();
			if(entityRef == null || !entityRef.isSet()) {
				throw new EntityNotFoundException("A valid entity reference must be specified to purge an entity.");
			}
			final PrimaryKey pk = new PrimaryKey(entityClass, entityRef.getId());
			final E e = svc.load(pk);
			svc.purge(e);

			payload.setEntityRef(entityRef);
			payload.getStatus().addMsg(e.descriptor() + " purged.", MsgLevel.INFO);
		}
		catch(final EntityNotFoundException e) {
			context.getExceptionHandler().handleException(payload.getStatus(), e, e.getMessage(), false);
		}
		catch(final SystemError e) {
			context.getExceptionHandler().handleException(payload.getStatus(), e, e.getMessage(), true);
		}
		catch(final RuntimeException e) {
			context.getExceptionHandler().handleException(payload.getStatus(), e, e.getMessage(), true);
			throw e;
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
			ICriteria<E> criteria)
			throws IllegalArgumentException;

	@SuppressWarnings("unchecked")
	public final ICriteria<E> translate(final IMEntityServiceContext context, final S search)
			throws IllegalArgumentException {
		final CriteriaType criteriaType = search.getCriteriaType();
		final Class<E> entityClass = (Class<E>) EntityTypeUtil.getEntityClass(search.getEntityType());
		Criteria<E> criteria;
		final List<IQueryParam> queryParams = search.getQueryParams();

		if(criteriaType.isQuery()) {
			ISelectNamedQueryDef queryDef = context.getQueryResolver().resolveNamedQuery(search.getNamedQuery());
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
