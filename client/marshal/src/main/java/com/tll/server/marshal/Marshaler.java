/**
 * The Logic Lab
 */
package com.tll.server.marshal;

import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Stack;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.BeanWrapperImpl;

import com.google.inject.Inject;
import com.tll.common.model.BooleanPropertyValue;
import com.tll.common.model.CharacterPropertyValue;
import com.tll.common.model.DatePropertyValue;
import com.tll.common.model.DoublePropertyValue;
import com.tll.common.model.EnumPropertyValue;
import com.tll.common.model.IEntityType;
import com.tll.common.model.IModelProperty;
import com.tll.common.model.IntPropertyValue;
import com.tll.common.model.LongPropertyValue;
import com.tll.common.model.Model;
import com.tll.common.model.RelatedManyProperty;
import com.tll.common.model.RelatedOneProperty;
import com.tll.common.model.StringMapPropertyValue;
import com.tll.common.model.StringPropertyValue;
import com.tll.dao.SearchResult;
import com.tll.model.IChildEntity;
import com.tll.model.IEntity;
import com.tll.model.IEntityFactory;
import com.tll.model.IScalar;
import com.tll.model.key.PrimaryKey;
import com.tll.model.schema.ISchemaInfo;
import com.tll.model.schema.ISchemaProperty;
import com.tll.model.schema.PropertyMetadata;
import com.tll.model.schema.PropertyType;
import com.tll.model.schema.RelationInfo;
import com.tll.model.schema.SchemaInfoException;
import com.tll.model.schema.Transient;
import com.tll.server.rpc.entity.IEntityTypeResolver;

/**
 * Marshaler - Converts server-side entities to client-bound value objects and
 * vice-versa.
 * @author jpk
 */
public final class Marshaler {

	private static final Log log = LogFactory.getLog(Marshaler.class);

	private final IEntityTypeResolver etResolver;
	private final IEntityFactory entityFactory;
	private final ISchemaInfo schemaInfo;

	/**
	 * Constructor
	 * @param etResolver
	 * @param entityFactory
	 * @param schemaInfo
	 */
	@Inject
	public Marshaler(final IEntityTypeResolver etResolver, final IEntityFactory entityFactory,
			final ISchemaInfo schemaInfo) {
		this.etResolver = etResolver;
		this.entityFactory = entityFactory;
		this.schemaInfo = schemaInfo;
	}

	/**
	 * Binding - Used for walking an {@link Object}'s hiererchy to avoid infinite
	 * looping as the hierarchy may contain circular references.
	 * @author jpk
	 */
	protected static final class Binding {

		final Object source;
		final Model group;

		/**
		 * Constructor
		 * @param source
		 * @param group
		 */
		Binding(final Object source, final Model group) {
			this.source = source;
			this.group = group;
		}
	}

	/**
	 * BindingStack
	 * @author jpk
	 */
	protected static final class BindingStack extends Stack<Binding> {

		private static final long serialVersionUID = -7748594505590343080L;

		/**
		 * Searches for a {@link Binding} given an {@link Object} ref.
		 * @param source
		 * @return The found {@link Binding} or <code>null</code> if not present in
		 *         this stack.
		 */
		public Binding find(final Object source) {
			for(final Binding b : this) {
				if(b.source == source) {
					return b;
				}
			}
			return null;
		}

		/**
		 * Searches for a {@link Binding} given a {@link Model} ref.
		 * @param group
		 * @return The found {@link Binding} or <code>null</code> if not present in
		 *         this stack.
		 */
		public Binding find(final Model group) {
			for(final Binding b : this) {
				if(b.group == group) {
					return b;
				}
			}
			return null;
		}
	}

	/**
	 * Converts a {@link SearchResult} instance to a new {@link Model} instance
	 * containing all marshalable properties contained within the search result.
	 * @param searchResult May NOT be <code>null</code>.
	 * @param options May NOT be <code>null</code>.
	 * @return client ready model
	 * @throws IllegalArgumentException When neither an IEntity nor an IScalar is
	 *         resolved from the given SearchResult.
	 */
	public Model marshalSearchResult(final SearchResult<?> searchResult, final MarshalOptions options) {
		assert searchResult != null;
		assert options != null;
		Model model;
		final Object r = searchResult.getElement();
		if(r instanceof IEntity) {
			BindingStack visited = new BindingStack();
			model = marshalEntity((IEntity) r, options, 0, visited);
			visited = null;
		}
		else {
			model = marshalScalar((IScalar) r, options);
		}
		return model;
	}

	/**
	 * Marshals the given entity.
	 * @param <E>
	 * @param entity May not be <code>null</code>.
	 * @param options May not be <code>null</code>.
	 * @return New {@link Model} instance containing the marshaled entity
	 *         properties.
	 * @throws RuntimeException upon any error encountered.
	 */
	public <E extends IEntity> Model marshalEntity(final E entity, final MarshalOptions options) {
		return marshalEntity(entity, options, 0, new BindingStack());
	}

	/**
	 * Converts a source IEntity instance to a marshalable {@link Model}.
	 * @param <E> The entity type
	 * @param source The source IEntity to marshal. May not be <code>null</code>.
	 * @param options The marshaling options. May NOT be <code>null</code>.
	 * @param depth the recursion depth
	 * @param visited collection of visited nodes to avoid infinite looping
	 * @return Marshaled {@link Model}.
	 * @throws RuntimeException upon any error encountered.
	 */
	@SuppressWarnings("unchecked")
	private <E extends IEntity> Model marshalEntity(final E source, final MarshalOptions options, final int depth,
			final BindingStack visited) {

		assert source != null;

		// check visited
		Binding b = visited.find(source);
		if(b != null) {
			return b.group;
		}

		final Class<? extends IEntity> entityClass = source.entityClass();
		final Model model = new Model(etResolver.resolveEntityType(entityClass));

		b = new Binding(source, model);
		visited.push(b);

		final BeanWrapperImpl bw = new BeanWrapperImpl(source);

		for(final PropertyDescriptor pd : bw.getPropertyDescriptors()) {
			final String pname = pd.getName();

			// skip certain properties...
			if(!bw.isWritableProperty(pname) || !isMarshalableProperty(pd)) {
				continue;
			}

			final Class<?> ptype = pd.getPropertyType();
			Object obj = null;
			try {
				obj = bw.getPropertyValue(pname);
			}
			catch(final RuntimeException re) {
				continue;
			}

			final PropertyMetadata pdata = generatePropertyData(entityClass, pname);

			IModelProperty prop = createValueProperty(ptype, pname, obj, pdata);

			if(prop == null) {
				// related one
				if(IEntity.class.isAssignableFrom(ptype)) {
					final RelationInfo ri = schemaInfo.getRelationInfo(entityClass, pname);
					final boolean reference = ri.isReference();
					if(shouldMarshalRelation(reference, depth, options)) {
						final IEntity e = (IEntity) obj;
						final Model ngrp = e == null ? null : marshalEntity(e, options, depth + 1, visited);
						final IEntityType etype =
							ri.getRelatedType() == null ? null : etResolver.resolveEntityType(e == null ? ri.getRelatedType() : e.entityClass());
						prop = new RelatedOneProperty(etype, pname, reference, ngrp);
					}
				}

				// related many collection
				else if(Collection.class.isAssignableFrom(ptype)) {
					final RelationInfo ri = schemaInfo.getRelationInfo(entityClass, pname);
					final boolean reference = ri.isReference();
					if(shouldMarshalRelation(reference, depth, options)) {
						List<Model> list = null;
						if(obj != null) {
							list = new ArrayList<Model>();
							final Collection<IEntity> set = (Collection<IEntity>) obj;
							for(final IEntity e : set) {
								final Model nested = marshalEntity(e, options, depth + 1, visited);
								list.add(nested);
							}
						}
						prop = new RelatedManyProperty(etResolver.resolveEntityType(ri.getRelatedType()), pname, reference, list);
					}
				}

				// map (assume <String, String> type)
				else if(Map.class.isAssignableFrom(ptype)) {
					prop = new StringMapPropertyValue(pname, generatePropertyData(entityClass, pname), (Map) obj);
				}

				else {
					final ISchemaProperty sp = schemaInfo.getSchemaProperty(entityClass, pname);

					// nested property?
					if(sp != null && sp.getPropertyType().isNested()) {
						final BeanWrapperImpl bw2 = obj == null ? new BeanWrapperImpl(ptype) : new BeanWrapperImpl(obj);
						for(final PropertyDescriptor pd2 : bw2.getPropertyDescriptors()) {
							if(bw2.isWritableProperty(pd2.getName()) && isMarshalableProperty(pd2)) {
								try {
									final Object oval = bw2.getPropertyValue(pd2.getName());
									model.set(createValueProperty(pd2.getPropertyType(), (pname + "_" + pd2.getName()), oval,
											generatePropertyData(source.entityClass(), (pname + "." + pd2.getName()))));
								}
								catch(final RuntimeException e) {
								}
							}
						}
					}

					else
						throw new RuntimeException("Unhandled property type: " + ptype);
				}
			} // prop == null

			if(prop != null) {
				model.set(prop);
			}

		}

		return model;
	}

	/**
	 * Converts a source Object instance to a marshalable {@link Model}.
	 * @param <S> The scalar type
	 * @param source The IScalar to marshal. May not be <code>null</code>.
	 * @param options The marshaling options. May NOT be <code>null</code>.
	 * @return Marshaled object as a {@link Model}.
	 * @throws RuntimeException upon any error encountered.
	 */
	public <S extends IScalar> Model marshalScalar(final S source, final MarshalOptions options) {

		final Model model = new Model(etResolver.resolveEntityType(source.getRefType()));

		final Map<String, Object> tupleMap = source.getTupleMap();
		for(final Map.Entry<String, Object> e : tupleMap.entrySet()) {
			final String pname = e.getKey();
			final Object obj = e.getValue();
			// NOTE: if we have a null tuple value, then default the type to String!
			final Class<?> ptype = obj == null ? String.class : obj.getClass();
			final IModelProperty prop = createValueProperty(ptype, pname, obj, null);
			if(prop != null) {
				model.set(prop);
			}
		}

		return model;
	}

	/**
	 * Un-marshals a {@link Model} representing an entity to an {@link IEntity}.
	 * @param <E>
	 * @param entityClass
	 * @param model the client wise model instance
	 * @return Un-marshaled server-side {@link IEntity} instance.
	 */
	public <E extends IEntity> E unmarshalEntity(final Class<E> entityClass, final Model model) {
		BindingStack visited = new BindingStack();
		final E e = unmarshalEntity(instantiateEntity(entityClass), model, visited, 0);
		visited = null;
		return e;
	}

	/**
	 * Un-marshals a {@link Model} against an given entity instance.
	 * @param <E>
	 * @param entity
	 * @param model the client wise model instance
	 * @return Un-marshaled server-side {@link IEntity} instance.
	 */
	public <E extends IEntity> E unmarshalEntity(final E entity, final Model model) {
		BindingStack visited = new BindingStack();
		final E e = unmarshalEntity(entity, model, visited, 0);
		visited = null;
		return e;
	}

	private <E extends IEntity> E instantiateEntity(Class<E> entityClass) {
		try {
			return entityClass.newInstance();
		}
		catch(final Exception ie) {
			throw new RuntimeException("Unable to instantiate entity of class: " + entityClass.getSimpleName());
		}
	}

	/**
	 * Unmarshals a UI entity to a new {@link IEntity} instance.
	 * @param <E>
	 * @param crntEntity The "current" (recursion) entity whose properties are updated
	 * @param model The {@link Model} holding the properties to be applied to the given entity
	 * @param visited collection of visited nodes to avoid infinite looping
	 * @param depth the current recursion depth
	 * @return New <E> instance
	 * @throws RuntimeException upon any error encountered.
	 */
	@SuppressWarnings("unchecked")
	private <E extends IEntity> E unmarshalEntity(E crntEntity, Model model, final BindingStack visited, int depth) {

		// only recurse if not already visited
		Binding b = visited.find(model);
		if(b != null) {
			return (E) b.source;
		}

		b = new Binding(crntEntity, model);
		visited.push(b);

		final BeanWrapperImpl bw = new BeanWrapperImpl(crntEntity);

		for(final IModelProperty mprop : model) {
			String propName = mprop.getPropertyName();
			final Object pval = mprop.getValue();
			Object val = null;

			switch(mprop.getType()) {

			case STRING:
			case LONG:
			case INT:
			case BOOL:
			case DOUBLE:
			case DATE:
			case CHAR:
			case ENUM:
				val = pval;
				break;

			case FLOAT:
				val = ((Double) pval).floatValue();
				break;

			case RELATED_ONE: {
				final Model rltdOne = (Model) pval;
				final IEntityType rltdEntityType = rltdOne == null ? null : rltdOne.getEntityType();
				final Class<IEntity> rltdEntityClass = (Class<IEntity>) etResolver.resolveEntityClass(rltdEntityType);
				IEntity toOne;
				if(rltdOne == null || rltdOne.isMarkedDeleted()) {
					toOne = null;
				}
				else {
					try {
						toOne = (IEntity) bw.getPropertyValue(propName);
						if(toOne == null) toOne = instantiateEntity(rltdEntityClass);
					}
					catch(final RuntimeException re) {
						log.warn("Unable to get related one entity ref", re);
						toOne = null;
					}
					toOne = unmarshalEntity(toOne, rltdOne, visited, depth + 1);
				}
				val = toOne;
			}
			break;

			case NESTED:
				// no-op since these are expressed as "{parent}_{nestedA}"
				break;

			case RELATED_MANY: {
				if(pval != null) { // should always be non-null (see RelatedManyProperty)
					final List<Model> rmModelList = (List<Model>) pval;
					Set<IEntity> rmEntitySet = null;
					//try {
					rmEntitySet = (Set<IEntity>) bw.getPropertyValue(propName);
					final LinkedHashSet<IEntity> newRmEntitySet = new LinkedHashSet<IEntity>(rmModelList.size());
					if(rmEntitySet != null) {
						newRmEntitySet.addAll(rmEntitySet);
					}
					/* we want to fail fast in this case so don't trap this exception!
					}
					catch(final RuntimeException e) {
						log.warn("Unable to get related many entity collection", e);
					}
					 */
					//if(rmEntitySet != null) {
					// re-build the rm entity set
					for(final Model indexedModel : rmModelList) {
						final IEntityType indexedEntityType = indexedModel.getEntityType();
						final Class<IEntity> indexedEntityClass = (Class<IEntity>) etResolver.resolveEntityClass(indexedEntityType);
						final PrimaryKey<IEntity> imodelPk = new PrimaryKey<IEntity>(indexedEntityClass, indexedModel.getId());

						IEntity indexedEntity = null;

						// existing indexed entity?
						for(final IEntity ie : newRmEntitySet) {
							final PrimaryKey<IEntity> iepk = new PrimaryKey<IEntity>(ie);
							if(imodelPk.equals(iepk)) {
								indexedEntity = ie;
								break;
							}
						}
						if(indexedEntity == null) {
							// non-existing indexed entity
							if(indexedModel.isNew()) {
								assert indexedModel.isMarkedDeleted() == false;
								// add indexed
								indexedEntity = unmarshalEntity(instantiateEntity(indexedEntityClass), indexedModel, visited, depth + 1);
								newRmEntitySet.add(indexedEntity);
							}
							// else presume removed after it was initially sent to client by another web session..
						}
						else {
							// existing indexed entity
							if(indexedModel.isMarkedDeleted()) {
								// remove existing indexed entity
								if(!newRmEntitySet.remove(indexedEntity)) {
									throw new IllegalStateException("Unable to remove indexed entity");
								}
								indexedEntity = null;
							}
							else {
								// update existing indexed entity
								indexedEntity = unmarshalEntity(indexedEntity, indexedModel, visited, depth + 1);
							}
							// satisify bi-di relationship
							if(indexedEntity instanceof IChildEntity) {
								((IChildEntity) indexedEntity).setParent(crntEntity);
							}
						}
					} // indexed model loop
					val = newRmEntitySet;
					//}
				}
			}
			break;

			case STRING_MAP:
				val = pval;
				break;

			default:
				throw new RuntimeException("Unhandled Property Value type: " + mprop.getType());

			}// switch

			// translate nested property names
			propName = propName.replace('_', '.');

			try {
				bw.setPropertyValue(propName, val);
			}
			catch(final RuntimeException re) {
				// ok
			}

		}// for loop

		if(crntEntity.getId() == null) {
			// assume new and set generated id
			if(crntEntity.getVersion() != null) {
				throw new RuntimeException("Encountered an entity (" + crntEntity.descriptor()
						+ ") w/o an id having a non-null version.");
			}
			entityFactory.setGenerated(crntEntity);
		}

		return crntEntity;
	}

	/**
	 * Provides {@link PropertyMetadata} for a given entities' property.
	 * @param entityClass
	 * @param propName
	 * @return New {@link PropertyMetadata} instance or <code>null</code> if the
	 *         given property name does not resolve to a property value.
	 */
	private PropertyMetadata generatePropertyData(final Class<? extends IEntity> entityClass, final String propName) {
		try {
			return schemaInfo.getPropertyMetadata(entityClass, propName);
		}
		catch(final SchemaInfoException e) {
			return null;
		}
	}

	/**
	 * Returns a property value by a given class representing the property type.
	 * @param ptype The property type
	 * @param pname The property name
	 * @param obj The value. May be <code>null</code>.
	 * @param pdata The property meta data. May be <code>null</code>.
	 * @return new IModelProperty instance or <code>null</code> if the given
	 *         property type does NOT corres. to a rudimentary property value.
	 */
	@SuppressWarnings("unchecked")
	private IModelProperty createValueProperty(final Class<?> ptype, final String pname, final Object obj,
			final PropertyMetadata pdata) {
		IModelProperty prop = null;

		if(String.class == ptype) {
			prop = new StringPropertyValue(pname, pdata, (String) obj);
		}

		else if(Date.class == ptype) {
			final Date d = obj == null ? null : new Date(((Date) obj).getTime());
			prop = new DatePropertyValue(pname, pdata, d);
		}

		else if(ptype.isEnum()) {
			prop = new EnumPropertyValue(pname, pdata, obj == null ? null : (Enum<?>) obj);
		}

		else if(long.class == ptype || Long.class == ptype) {
			prop = new LongPropertyValue(pname, pdata, (Long) obj);
		}

		else if(int.class == ptype || Integer.class == ptype) {
			prop = new IntPropertyValue(pname, pdata, (Integer) obj);
		}

		else if(char.class == ptype || Character.class == ptype) {
			prop = new CharacterPropertyValue(pname, pdata, (Character) obj);
		}

		else if(boolean.class == ptype || Boolean.class == ptype) {
			prop = new BooleanPropertyValue(pname, pdata, (Boolean) obj);
		}

		else if(double.class == ptype || Double.class == ptype) {
			prop = new DoublePropertyValue(pname, pdata, (Double) obj);
		}

		else if(float.class == ptype || Float.class == ptype) {
			prop = new DoublePropertyValue(pname, pdata, ((Float) obj).doubleValue());
		}

		else if(pdata != null && pdata.getPropertyType() == PropertyType.STRING_MAP) {
			prop = new StringMapPropertyValue(pname, pdata, (Map<String, String>) obj);
		}

		return prop;
	}

	/**
	 * Ascertains whether or not a given relational type property for a given
	 * entity type should be marshaled.
	 * @param isReferenceRelation
	 * @param currentDepth
	 * @param marshalOptions
	 * @return true/false
	 */
	private boolean shouldMarshalRelation(boolean isReferenceRelation, int currentDepth, MarshalOptions marshalOptions) {
		if(!depthCheck(currentDepth, marshalOptions.getMaxDepth())) {
			return false;
		}
		if(isReferenceRelation) {
			return marshalOptions.isProcessReferenceRelations();
		}
		return true;
	}

	/**
	 * Simple check to filter out certain properties based on their name.
	 * @param pd
	 * @return true/false
	 */
	private boolean isMarshalableProperty(final PropertyDescriptor pd) {
		// check bound method annotations and honor @NoPersist
		final Method m = pd.getReadMethod();
		if(m != null) {
			return m.getAnnotation(Transient.class) == null;
		}
		return true;
	}

	/**
	 * Determines if recursion should drill down another level.
	 * @param depth The current hierarchical depth
	 * @param maxDepth The max allowed hierarchical depth
	 * @return <code>true</code> if depth check passes.
	 */
	private boolean depthCheck(final int depth, final int maxDepth) {
		return (maxDepth < 0 || (maxDepth >= 0 && ((depth + 1) <= maxDepth)));
	}
}
