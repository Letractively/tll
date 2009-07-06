/**
 * The Logic Lab
 * @author jpk
 * @since Jun 24, 2009
 */
package com.tll.server.rpc.entity;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;

import com.tll.common.data.ModelPayload;
import com.tll.common.data.Status;
import com.tll.common.model.IEntityType;
import com.tll.common.model.IPropertyValue;
import com.tll.common.model.Model;
import com.tll.common.model.ModelKey;
import com.tll.common.msg.Msg.MsgAttr;
import com.tll.common.msg.Msg.MsgLevel;
import com.tll.common.search.IBusinessKeySearch;
import com.tll.common.search.IEntityNameSearch;
import com.tll.common.search.IPrimaryKeySearch;
import com.tll.common.search.ISearch;
import com.tll.dao.EntityExistsException;
import com.tll.dao.EntityNotFoundException;
import com.tll.model.IEntity;
import com.tll.model.key.BusinessKeyFactory;
import com.tll.model.key.BusinessKeyNotDefinedException;
import com.tll.model.key.IBusinessKey;
import com.tll.model.key.NameKey;
import com.tll.model.key.PrimaryKey;
import com.tll.model.schema.ISchemaInfo;
import com.tll.model.schema.ISchemaProperty;
import com.tll.server.marshal.MarshalOptions;
import com.tll.server.rpc.RpcServlet;
import com.tll.service.entity.IEntityService;
import com.tll.service.entity.INamedEntityService;
import com.tll.util.PropertyPath;


/**
 * AbstractPersistServiceImpl
 * @author jpk
 */
public abstract class AbstractPersistServiceImpl implements IPersistServiceImpl {

	/**
	 * Client-izes the given property path (need to account for possible nested).
	 * <p>
	 * It is assumed nested entities are only 1-level deep
	 * @param <T> the entity type
	 * @param schemaInfo
	 * @param entityClass
	 * @param path
	 * @return the clientized path
	 */
	protected static final <T> String clientizePropertyPath(ISchemaInfo schemaInfo, Class<T> entityClass, String path) {
		final PropertyPath p = new PropertyPath(path);
		if(p.depth() > 2) {
			final String ppp = p.trim(1);
			final ISchemaProperty sp = schemaInfo.getSchemaProperty(entityClass, ppp);
			if(sp.getPropertyType().isNested()) {
				path = ppp + '_' + p.last();
			}
		}
		return path;
	}

	protected final PersistContext context;

	/**
	 * Constructor
	 * @param context the required persist context
	 */
	public AbstractPersistServiceImpl(PersistContext context) {
		super();
		if(context == null) throw new IllegalArgumentException("Null persist context.");
		this.context = context;
	}

	@Override
	public void load(ISearch search, ModelPayload payload) {
		if(search == null || !search.isSet()) {
			payload.getStatus().addMsg(search == null ? "No search criteria specified" : "Unset search criteria",
					MsgLevel.ERROR, MsgAttr.STATUS.flag);
		}
		else {
			IEntity e = null;
			IEntityType et = null;
			if(search instanceof IPrimaryKeySearch) {
				final IPrimaryKeySearch pks = (IPrimaryKeySearch) search;
				e = loadEntityByPrimaryKey(pks, payload.getStatus());
				et = pks.getKey().getEntityType();
			}
			else if(search instanceof IBusinessKeySearch) {
				final IBusinessKeySearch bks = (IBusinessKeySearch) search;
				e = loadEntityByBusinesKey(bks, payload.getStatus());
				et = bks.getEntityType();
			}
			else if(search instanceof IEntityNameSearch) {
				final IEntityNameSearch ens = (IEntityNameSearch) search;
				e = loadEntityByName(ens, payload.getStatus());
				et = ens.getEntityType();
			}
			else {
				// override
				loadImpl(search, payload);
				return;
			}

			if(e != null) {
				assert et != null;
				try {
					final Model m = entityToModel(et, e);
					payload.setModel(m);
					payload.getStatus().addMsg(e.descriptor() + " loaded.", MsgLevel.INFO, MsgAttr.STATUS.flag);
				}
				catch(final Exception ex) {
					RpcServlet.exceptionToStatus(ex, payload.getStatus());
				}
			}
		}
	}

	/**
	 * Translates a target entity instance to a {@link Model} instance.
	 * @param entityType the entity type
	 * @param e the entity to translate
	 * @return the translated model instance
	 * @throws Exception upon error
	 */
	protected Model entityToModel(IEntityType entityType, IEntity e) throws Exception, Exception {
		// default simply marshals the entity
		try {
			return marshal(entityType, e);
		}
		catch(final Throwable t) {
			throw new Exception(t);
		}
	}

	@Override
	public void persist(Model model, ModelPayload payload) {
		if(model.isEntity()) {
			persistEntity(model, payload);
		}
		else {
			persistImpl(model, payload);
		}
	}

	@Override
	public void purge(Model model, ModelPayload payload) {
		purgeImpl(model, payload);
	}

	@Override
	public void purge(ModelKey ref, ModelPayload payload) {
		purgeEntity(ref, payload);
	}

	/**
	 * Resolves the approprate entity service for a particular entity type.
	 * @param entityType
	 * @return the resolved entity service
	 * @throws IllegalArgumentException when the service can't be resolved
	 */
	@SuppressWarnings("unchecked")
	protected final IEntityService<IEntity> getEntityService(IEntityType entityType)
	throws IllegalArgumentException {
		final Class<IEntity> eclass = (Class<IEntity>) context.getEntityTypeResolver().resolveEntityClass(entityType);
		return context.getEntityServiceFactory().instanceByEntityType(eclass);
	}

	/**
	 * Convenience method for entity to model marshaling.
	 * @param entityType
	 * @param entity
	 * @return new {@link Model} instance
	 * @throws RuntimeException upon marshaling related error
	 */
	protected Model marshal(IEntityType entityType, IEntity entity) throws RuntimeException {
		MarshalOptions mo;
		try {
			mo = context.getMarshalOptionsResolver().resolve(entityType);
		}
		catch(final IllegalArgumentException e) {
			// default fallback
			mo = MarshalOptions.NO_REFERENCES;
		}
		final Model m = context.getMarshaler().marshalEntity(entity, mo);
		assert m != null;
		return m;
	}

	/**
	 * @return A descriptive ui-ready name for the type of model data this
	 *         implmentation supports.
	 */
	protected abstract String getModelTypeName();

	/**
	 * Loads an entity by primary key marshaling it into a {@link Model} instance.
	 * @param search guaranteed non-<code>null</code> search criteria
	 * @param status the status to which messages are posted
	 */
	@SuppressWarnings("unchecked")
	protected IEntity loadEntityByPrimaryKey(IPrimaryKeySearch search, Status status) {
		try {
			final ModelKey mkey = search.getKey();
			final IEntityType et = mkey.getEntityType();
			final Class<IEntity> ec = (Class<IEntity>) context.getEntityTypeResolver().resolveEntityClass(et);
			final IEntityService<IEntity> svc = getEntityService(et);
			final IEntity e = svc.load(new PrimaryKey(ec, mkey.getId()));
			return e;
		}
		catch(final EntityNotFoundException e) {
			RpcServlet.exceptionToStatus(e, status);
			return null;
		}
		catch(final RuntimeException e) {
			RpcServlet.exceptionToStatus(e, status);
			throw e;
		}
	}

	/**
	 * Loads an entity by business key marshaling it into a {@link Model}
	 * instance.
	 * @param search guaranteed non-<code>null</code> search criteria
	 * @param status the status to which messages are posted
	 */
	@SuppressWarnings("unchecked")
	protected IEntity loadEntityByBusinesKey(IBusinessKeySearch search, Status status) {
		try {
			final IEntityType et = search.getEntityType();
			final Class<IEntity> ec = (Class<IEntity>) context.getEntityTypeResolver().resolveEntityClass(et);
			final String bkName = search.getBusinessKeyName();
			final IPropertyValue[] pvs = search.getProperties();
			IBusinessKey<IEntity> bk;
			bk = BusinessKeyFactory.create(ec, bkName);
			for(final IPropertyValue pv : pvs) {
				bk.setPropertyValue(pv.getPropertyName(), pv.getValue());
			}
			final IEntity e = getEntityService(et).load(bk);
			// final Model m = marshal(et, e);
			// payload.setModel(m);
			status.addMsg(e.descriptor() + " loaded.", MsgLevel.INFO, MsgAttr.STATUS.flag);
			return e;
		}
		catch(final BusinessKeyNotDefinedException e) {
			RpcServlet.exceptionToStatus(e, status);
			return null;
		}
		catch(final EntityNotFoundException e) {
			RpcServlet.exceptionToStatus(e, status);
			return null;
		}
		catch(final RuntimeException e) {
			RpcServlet.exceptionToStatus(e, status);
			throw e;
		}
	}

	/**
	 * Loads an entity by name marshaling it into a {@link Model} instance.
	 * @param search guaranteed non-<code>null</code> search criteria
	 * @param status the status to which messages are posted
	 */
	@SuppressWarnings("unchecked")
	protected IEntity loadEntityByName(IEntityNameSearch search, Status status) {
		try {
			final IEntityType et = search.getEntityType();
			final Class<IEntity> ec = (Class<IEntity>) context.getEntityTypeResolver().resolveEntityClass(et);
			final String name = search.getName();
			final IEntityService<IEntity> svc = getEntityService(et);
			if(svc instanceof INamedEntityService == false) {
				throw new RuntimeException("Entity type: " + et + "doesn't support loading by name.");
			}
			final IEntity e = ((INamedEntityService) svc).load(new NameKey(ec, name));
			// final Model m = marshal(et, e);
			// payload.setModel(m);
			status.addMsg(e.descriptor() + " loaded.", MsgLevel.INFO, MsgAttr.STATUS.flag);
			return e;
		}
		catch(final EntityNotFoundException e) {
			RpcServlet.exceptionToStatus(e, status);
			return null;
		}
		catch(final RuntimeException e) {
			RpcServlet.exceptionToStatus(e, status);
			throw e;
		}
	}

	/**
	 * Impl specific load routine. Usually employed when we are not adhering to
	 * entity boundaries.
	 * @param search
	 * @param payload
	 */
	protected void loadImpl(ISearch search, ModelPayload payload) {
		throw new UnsupportedOperationException();
	}

	/**
	 * Persists (adds or updates) an entity in the form of a {@link Model}
	 * instance.
	 * @param model the entity expressed as a {@link Model} instance
	 * @param payload The {@link ModelPayload} that is filled
	 */
	@SuppressWarnings("unchecked")
	protected void persistEntity(Model model, ModelPayload payload) {
		Class<? extends IEntity> entityClass = null;
		try {
			// core persist
			final IEntityType et = model.getEntityType();
			entityClass = (Class<? extends IEntity>) context.getEntityTypeResolver().resolveEntityClass(et);
			IEntity e = context.getMarshaler().unmarshalEntity(entityClass, model);
			final IEntityService<IEntity> svc =
				(IEntityService<IEntity>) context.getEntityServiceFactory().instanceByEntityType(entityClass);
			final boolean isNew = e.isNew();
			e = svc.persist(e);

			// marshall
			model = marshal(et, e);
			payload.setModel(model);

			payload.getStatus()
			.addMsg(e.descriptor() + (isNew ? " added." : " updated."), MsgLevel.INFO, MsgAttr.STATUS.flag);
		}
		catch(final EntityExistsException e) {
			RpcServlet.exceptionToStatus(e, payload.getStatus());
		}
		catch(final ConstraintViolationException ise) {
			for(final ConstraintViolation iv : ise.getConstraintViolations()) {
				payload.getStatus().addMsg(iv.getMessage(), MsgLevel.ERROR, MsgAttr.FIELD.flag,
						clientizePropertyPath(context.getSchemaInfo(), entityClass, iv.getPropertyPath()));
			}
		}
		catch(final RuntimeException e) {
			RpcServlet.exceptionToStatus(e, payload.getStatus());
			context.getExceptionHandler().handleException(e);
			throw e;
		}
	}

	/**
	 * Impl specific persist routine. Usually employed when we are not adhering to
	 * entity boundaries.
	 * @param model
	 * @param payload
	 */
	protected void persistImpl(Model model, ModelPayload payload) {
		throw new UnsupportedOperationException();
	}

	/**
	 * Purges an entity given a {@link ModelKey} ref.
	 * @param ref identifies the entity to be purged
	 * @param payload The {@link ModelPayload} that is filled
	 */
	@SuppressWarnings("unchecked")
	protected void purgeEntity(ModelKey ref, ModelPayload payload) {
		try {
			final Class<IEntity> entityClass =
				(Class<IEntity>) context.getEntityTypeResolver().resolveEntityClass(ref.getEntityType());
			final IEntityService<IEntity> svc = context.getEntityServiceFactory().instanceByEntityType(entityClass);
			final PrimaryKey pk = new PrimaryKey(entityClass, ref.getId());
			final IEntity e = svc.load(pk);
			svc.purge(e);
			payload.setRef(ref);
			payload.getStatus().addMsg(e.descriptor() + " purged.", MsgLevel.INFO, MsgAttr.STATUS.flag);
		}
		catch(final EntityNotFoundException e) {
			RpcServlet.exceptionToStatus(e, payload.getStatus());
		}
		catch(final RuntimeException e) {
			RpcServlet.exceptionToStatus(e, payload.getStatus());
			context.getExceptionHandler().handleException(e);
			throw e;
		}
	}

	protected void purgeImpl(Model model, ModelPayload payload) {
		throw new UnsupportedOperationException();
	}
}
