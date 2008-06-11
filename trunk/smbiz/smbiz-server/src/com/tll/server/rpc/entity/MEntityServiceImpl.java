/**
 * The Logic Lab
 * @author jpk Sep 14, 2007
 */
package com.tll.server.rpc.entity;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import javax.persistence.EntityExistsException;
import javax.persistence.EntityNotFoundException;

import org.hibernate.validator.InvalidStateException;
import org.hibernate.validator.InvalidValue;

import com.tll.SystemError;
import com.tll.client.data.EntityGetEmptyRequest;
import com.tll.client.data.EntityLoadRequest;
import com.tll.client.data.EntityOptions;
import com.tll.client.data.EntityPayload;
import com.tll.client.data.EntityPersistRequest;
import com.tll.client.data.EntityPurgeRequest;
import com.tll.client.data.IListingCommand;
import com.tll.client.model.Model;
import com.tll.client.model.RefKey;
import com.tll.client.msg.Msg.MsgAttr;
import com.tll.client.msg.Msg.MsgLevel;
import com.tll.client.search.ISearch;
import com.tll.criteria.Comparator;
import com.tll.criteria.CriteriaFactory;
import com.tll.criteria.CriteriaType;
import com.tll.criteria.ICriteria;
import com.tll.criteria.IQueryParam;
import com.tll.criteria.SelectNamedQuery;
import com.tll.model.EntityType;
import com.tll.model.EntityUtil;
import com.tll.model.IEntity;
import com.tll.model.key.IBusinessKey;
import com.tll.model.key.IPrimaryKey;
import com.tll.model.key.KeyFactory;
import com.tll.server.RequestContext;
import com.tll.server.ServletUtil;
import com.tll.server.rpc.AuxDataHandler;
import com.tll.server.rpc.listing.IMarshalingListHandler;
import com.tll.server.rpc.listing.MarshalingListHandler;
import com.tll.service.entity.IEntityService;
import com.tll.util.DateRange;

/**
 * MEntityServiceImpl - Provides base methods for CRUD ops on entities.
 * @author jpk
 */
public abstract class MEntityServiceImpl<E extends IEntity, S extends ISearch> implements IMEntityServiceImpl<E, S> {

	/**
	 * Loads additional entity properties.
	 * @param e
	 * @param options
	 * @param refs The map to place related {@link EntityRef}s if they are
	 *        reuested in <code>options</code>.
	 * @param requestContext The request context.
	 * @throws SystemError When any error occurrs.
	 */
	protected abstract void handleLoadOptions(RequestContext requestContext, E e, EntityOptions options,
			Map<String, RefKey> refs) throws SystemError;

	/**
	 * Handles persist options specified in options.
	 * @param e
	 * @param options
	 * @param requestContext The request context.
	 * @throws SystemError When any error occurrs.
	 */
	protected abstract void handlePersistOptions(RequestContext requestContext, E e, EntityOptions options)
			throws SystemError;

	public final void getEmptyEntity(final RequestContext requestContext, final EntityGetEmptyRequest request,
			final EntityType entityType, final EntityPayload payload) {
		try {
			final IEntity e = requestContext.getEntityAssembler().assembleEntity(entityType, null, request.isGenerate());
			final Model group = requestContext.getMarshaler().marshalEntity(e, getMarshalOptions(requestContext));
			payload.setEntity(group);
		}
		catch(final SystemError se) {
			ServletUtil.handleException(requestContext, payload, se, se.getMessage(), true);
		}
		catch(final RuntimeException re) {
			ServletUtil.handleException(requestContext, payload, re, re.getMessage(), true);
			throw re;
		}
	}

	/**
	 * Does the core entity loading.
	 * @param requestContext
	 * @param request
	 * @param entityType
	 * @param payload
	 * @return The loaded {@link IEntity}
	 */
	@SuppressWarnings("unchecked")
	protected E coreLoad(final RequestContext requestContext, final EntityLoadRequest request,
			final EntityType entityType, final EntityPayload payload) {
		// core entity loading
		final Class<E> entityClass = EntityUtil.entityClassFromType(entityType);
		final IEntityService<E> svc = requestContext.getEntityServiceFactory().instanceByEntityType(entityClass);

		if(request.isLoadByBusinessKey()) {
			// load by business key
			S search = (S) request.getSearch();
			if(search == null) {
				payload.getStatus().addMsg("A business key wise search must be specified.", MsgLevel.ERROR);
				return null;
			}
			IBusinessKey<? extends E> key = handleBusinessKeyTranslation(search);
			return svc.load(key);
		}

		// load by primary key
		final Integer id = request.getEntityRef().getId();
		return svc.load(KeyFactory.getPrimaryKey(entityClass, id));
	}

	public final void load(final RequestContext requestContext, final EntityLoadRequest request,
			final EntityType entityType, final EntityPayload payload) {
		try {
			final E e = coreLoad(requestContext, request, entityType, payload);
			if(e == null) {
				return;
			}

			// optional loading
			final Map<String, RefKey> refs = new HashMap<String, RefKey>();
			if(request.entityOptions != null) {
				handleLoadOptions(requestContext, e, request.entityOptions, refs);
			}

			// marshal the loaded entity
			final Model group = requestContext.getMarshaler().marshalEntity(e, getMarshalOptions(requestContext));
			payload.setEntity(group);

			// set any entity refs
			for(final String propName : refs.keySet()) {
				payload.setRelatedOneRef(propName, refs.get(propName));
			}

			// load any requested auxiliary
			if(request.auxDataRequest != null) {
				AuxDataHandler.getAuxData(requestContext, request.auxDataRequest, payload);
			}
		}
		catch(final EntityNotFoundException enfe) {
			ServletUtil.handleException(requestContext, payload, enfe, enfe.getMessage(), false);
		}
		catch(final SystemError se) {
			ServletUtil.handleException(requestContext, payload, se, se.getMessage(), true);
		}
		catch(final RuntimeException re) {
			ServletUtil.handleException(requestContext, payload, re, re.getMessage(), true);
			throw re;
		}
	}

	public final void persist(final RequestContext requestContext, final EntityPersistRequest request,
			final EntityType entityType, final EntityPayload payload) {
		try {
			// core persist
			final Class<E> entityClass = EntityUtil.entityClassFromType(entityType);
			final Model entity = request.getEntity();
			E e = requestContext.getMarshaler().unmarshalEntity(entityClass, entity);
			final IEntityService<E> svc = requestContext.getEntityServiceFactory().instanceByEntityType(entityClass);
			e = svc.persist(e);

			// handle persist options
			handlePersistOptions(requestContext, e, request.entityOptions);

			// marshall
			final Model group = requestContext.getMarshaler().marshalEntity(e, getMarshalOptions(requestContext));
			payload.setEntity(group);

			payload.getStatus().addMsg(e.descriptor() + " persisted.", MsgLevel.INFO);
		}
		catch(final EntityExistsException e1) {
			ServletUtil.handleException(requestContext, payload, e1, e1.getMessage(), false);
		}
		catch(final InvalidStateException ise) {
			for(final InvalidValue iv : ise.getInvalidValues()) {
				payload.getStatus().addMsg(iv.getMessage(), MsgLevel.ERROR, MsgAttr.FIELD.flag, iv.getPropertyPath());

			}
		}
		catch(final SystemError se) {
			ServletUtil.handleException(requestContext, payload, se, null, true);
		}
		catch(final RuntimeException re) {
			ServletUtil.handleException(requestContext, payload, re, null, true);
			throw re;
		}
	}

	@SuppressWarnings("unchecked")
	public final void purge(final RequestContext requestContext, final EntityPurgeRequest entityRequest,
			final EntityType entityType, final EntityPayload p) {
		try {
			final Class<IEntity> entityClass = EntityUtil.entityClassFromType(entityType);
			final IEntityService<IEntity> svc = requestContext.getEntityServiceFactory().instanceByEntityType(entityClass);
			final RefKey entityRef = entityRequest.getEntityRef();
			if(entityRef == null || !entityRef.isSet()) {
				throw new EntityNotFoundException("A valid entity reference must be specified to purge an entity.");
			}
			final IPrimaryKey<IEntity> pk = KeyFactory.getPrimaryKey(entityClass, entityRef.getId());
			final IEntity e = svc.load(pk);
			svc.purge(e);

			p.setEntityRef(entityRef);
			p.getStatus().addMsg(e.descriptor() + " purged.", MsgLevel.INFO);
		}
		catch(final EntityNotFoundException e) {
			ServletUtil.handleException(requestContext, p, e, e.getMessage(), false);
		}
		catch(final SystemError se) {
			ServletUtil.handleException(requestContext, p, se, se.getMessage(), true);
		}
		catch(final RuntimeException re) {
			ServletUtil.handleException(requestContext, p, re, re.getMessage(), true);
			throw re;
		}
	}

	/**
	 * Translates {@link ISearch} to {@link IBusinessKey}s.
	 * @param search The search to translate
	 * @return Translated {@link IBusinessKey}
	 */
	protected abstract IBusinessKey<? extends E> handleBusinessKeyTranslation(S search);

	/**
	 * Handles the entity specific search to criteria translation.
	 * @param search
	 * @param criteria
	 * @throws IllegalArgumentException When the <code>search</code> parameter
	 *         is unsupported.
	 */
	protected abstract void handleSearchTranslation(RequestContext requestContext, S search,
			ICriteria<? extends E> criteria) throws IllegalArgumentException;

	@SuppressWarnings("unchecked")
	public final ICriteria<? extends E> translate(final RequestContext requestContext, final EntityType entityType,
			final S search) throws IllegalArgumentException {
		final CriteriaType criteriaType = search.getCriteriaType();
		final Class<E> entityClass = EntityUtil.entityClassFromType(entityType);
		ICriteria<? extends E> criteria;
		final Set<IQueryParam> queryParams = search.getQueryParams();

		if(criteriaType.isQuery()) {
			SelectNamedQuery nq = search.getNamedQuery();
			if(nq == null) {
				throw new IllegalArgumentException("No named query specified");
			}
			criteria = (ICriteria<? extends E>) CriteriaFactory.buildQueryCriteria(nq, queryParams);
		}
		else {
			// entity
			criteria = CriteriaFactory.buildEntityCriteria(entityClass);
			handleSearchTranslation(requestContext, search, criteria);
		}

		return criteria;
	}

	/*
	 * Sub-classes should override this method for specific table requirements
	 * based on the listing command particulars.
	 */
	public IMarshalingListHandler<E> getMarshalingListHandler(final RequestContext requestContext,
			final IListingCommand<S> listingCommand) {
		return new MarshalingListHandler<E>(requestContext.getMarshaler(), getMarshalOptions(requestContext));
	}

	/**
	 * Adds a date range criterion to the given criteria.
	 * @param criteria the criteria
	 * @param dr the date range
	 */
	protected void appendDateRangeCriterion(final ICriteria<? extends E> criteria, final DateRange dr) {
		if(dr != null && !dr.isEmpty()) {
			criteria.getPrimaryGroup().addCriterion(
					CriteriaFactory.buildCriterion("dateCreated", dr, Comparator.BETWEEN, false));
		}
	}
}
