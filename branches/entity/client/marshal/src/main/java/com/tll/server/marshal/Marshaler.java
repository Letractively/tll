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
import com.tll.model.EntityFactory;
import com.tll.model.IChildEntity;
import com.tll.model.IEntity;
import com.tll.model.IScalar;
import com.tll.model.IVersionSupport;
import com.tll.model.PrimaryKey;
import com.tll.model.schema.ISchemaInfo;
import com.tll.model.schema.ISchemaProperty;
import com.tll.model.schema.PropertyMetadata;
import com.tll.model.schema.PropertyType;
import com.tll.model.schema.RelationInfo;
import com.tll.model.schema.SchemaInfoException;
import com.tll.model.schema.Transient;
import com.tll.server.rpc.entity.IEntityTypeResolver;
import com.tll.util.Binding;
import com.tll.util.BindingRefSet;

/**
 * Marshaler - Converts server-side entities to client-bound value objects and
 * vice-versa.
 * @author jpk
 */
public final class Marshaler {

	/**
	 * We only recurse to a certain depth to avoid infinite looping.
	 */
	private static final int MAX_DEPTH = 10;

	private static final Log log = LogFactory.getLog(Marshaler.class);

	private final IEntityTypeResolver etResolver;
	private final EntityFactory entityFactory;
	private final ISchemaInfo schemaInfo;

	/**
	 * Constructor
	 * @param etResolver
	 * @param entityFactory
	 * @param schemaInfo
	 */
	@Inject
	public Marshaler(final IEntityTypeResolver etResolver, final EntityFactory entityFactory,
			final ISchemaInfo schemaInfo) {
		this.etResolver = etResolver;
		this.entityFactory = entityFactory;
		this.schemaInfo = schemaInfo;
	}

	/**
	 * Converts a {@link SearchResult} instance to a new {@link Model} instance.
	 * containing all marshalable properties contained within the search result
	 * @param searchResult May NOT be <code>null</code>
	 * @param options May NOT be <code>null</code>
	 * @return client ready model
	 * @throws IllegalArgumentException When neither an IEntity nor an IScalar is
	 *         resolved from the given SearchResult.
	 */
	public Model marshalSearchResult(final SearchResult searchResult, final MarshalOptions options) {
		assert searchResult != null;
		assert options != null;
		Model model;
		final Object r = searchResult.getElement();
		if(r instanceof IEntity) {
			BindingRefSet<IEntity, Model> visited = new BindingRefSet<IEntity, Model>();
			model = marshalEntity((IEntity) r, options, visited, 0);
			visited = null;
		}
		else {
			model = marshalScalar((IScalar) r, options);
		}
		return model;
	}

	/**
	 * Converts the given {@link IEntity} instance to a new {@link Model}
	 * instance.
	 * @param entity May not be <code>null</code>.
	 * @param options May not be <code>null</code>.
	 * @return New {@link Model} instance containing the marshaled entity
	 *         properties.
	 * @throws RuntimeException upon any error encountered.
	 */
	public Model marshalEntity(final IEntity entity, final MarshalOptions options) {
		return marshalEntity(entity, options, new BindingRefSet<IEntity, Model>(), 0);
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
	 * Marshals a {@link Model} to a new {@link IEntity} instance of the given
	 * type.
	 * @param model the client wise model instance
	 * @param entityClass
	 * @param <E>
	 * @return The generated {@link IEntity} instance.
	 */
	@SuppressWarnings("unchecked")
	public <E extends IEntity> E marshalModel(final Model model, final Class<E> entityClass) {
		BindingRefSet<Model, IEntity> visited = new BindingRefSet<Model, IEntity>();
		final E e = (E) instantiateEntity(entityClass);
		marshalModel(model, e, visited, 0);
		visited = null;
		return e;
	}

	/**
	 * Marshals a {@link Model} to a given entity instance.
	 * @param <E>
	 * @param model the client wise model instance
	 * @param entity
	 * @return The generated {@link IEntity} instance.
	 */
	public <E extends IEntity> E marshalModel(final Model model, final E entity) {
		BindingRefSet<Model, IEntity> visited = new BindingRefSet<Model, IEntity>();
		marshalModel(model, entity, visited, 0);
		visited = null;
		return entity;
	}

	/**
	 * Converts an {@link IEntity} instance to a {@link Model} instance.
	 * @param source The source IEntity to marshal. May not be <code>null</code>.
	 * @param options The marshaling options. May NOT be <code>null</code>.
	 * @param visited collection of visited nodes to avoid infinite looping
	 * @param depth the recursion depth
	 * @return Marshaled {@link Model}.
	 * @throws RuntimeException upon any error encountered.
	 */
	@SuppressWarnings("unchecked")
	private Model marshalEntity(final IEntity source, final MarshalOptions options,
			final BindingRefSet<IEntity, Model> visited, final int depth) {

		assert source != null;

		// check visited
		final Binding<IEntity, Model> b = visited.findBindingBySource(source);
		if(b != null) return b.tgt;

		final Class<? extends IEntity> entityClass = source.entityClass();
		final Model model = new Model(etResolver.resolveEntityType(entityClass));

		visited.add(new Binding<IEntity, Model>(source, model));

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
				log.debug("Skipping entity prop: " + pname + " due to: " + re.getMessage());
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
						final Model m = e == null ? null : marshalEntity(e, options, visited, depth + 1);
						final IEntityType etype =
							ri.getRelatedType() == null ? null : etResolver.resolveEntityType(e == null ? ri.getRelatedType() : e
									.entityClass());
						prop = new RelatedOneProperty(etype, m, pname, reference);
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
								final Model nested = marshalEntity(e, options, visited, depth + 1);
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
									log.debug("Didn't set nested model prop: " + pname + " due to: " + e.getMessage());
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
	 * Recursively marshals a {@link Model} onto the given {@link IEntity}
	 * instance.
	 * @param model The {@link Model} holding the properties to be applied to the
	 *        given entity
	 * @param crntEntity The "current" (recursion) entity whose properties are
	 *        updated
	 * @param visited collection of visited nodes to avoid infinite looping
	 * @param depth the current recursion depth
	 * @throws RuntimeException upon any error encountered.
	 */
	@SuppressWarnings("unchecked")
	private void marshalModel(Model model, IEntity crntEntity, final BindingRefSet visited, int depth) {

		// max depth check
		if(!depthCheck(depth, -1)) {
			throw new IllegalStateException("Can't marshal model to entity: max depth reached");
		}

		// only recurse if not already visited
		Binding<Model, IEntity> b = visited.findBindingBySource(model);
		if(b != null) return;
		b = new Binding<Model, IEntity>(model, crntEntity);
		log.debug("Adding new binding to visited: " + b);
		visited.add(b);

		final BeanWrapperImpl bw = new BeanWrapperImpl(crntEntity);

		for(final IModelProperty mprop : model) {
			String propName = mprop.getPropertyName();
			final Object pval = mprop.getValue();
			Object val = null;
			log.debug("marshalModel - propName: " + propName);

			if(Model.ID_PROPERTY.equals(propName)) {
				val = pval == null ? null : Long.valueOf((String) pval);
			}
			else if(Model.VERSION_PROPERTY.equals(propName)) {
				val = pval == null ? null : Long.valueOf((String) pval);
			}
			else {
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
					val = pval == null ? null : Float.valueOf(((Double) pval).floatValue());
					break;

				case RELATED_ONE: {
					final Model rltdOne = (Model) pval;
					final IEntityType rltdEntityType =
						rltdOne == null ? ((RelatedOneProperty) mprop).getRelatedType() : rltdOne.getEntityType();
						assert rltdEntityType != null;
						IEntity toOne;
						if(rltdOne == null || rltdOne.isMarkedDeleted()) {
							toOne = null;
						}
						else {
							final Class<? extends IEntity> rltdEntityClass =
								(Class<? extends IEntity>) etResolver.resolveEntityClass(rltdEntityType);
							try {
								toOne = (IEntity) bw.getPropertyValue(propName);
								if(toOne == null) toOne = instantiateEntity(rltdEntityClass);
							}
							catch(final RuntimeException re) {
								log.warn("Unable to get related one entity ref", re);
								toOne = null;
							}
							log.debug("About to marshal related one [model: " + rltdOne + "] [entity: " + toOne + "]");
							marshalModel(rltdOne, toOne, visited, depth + 1);
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
							newRmEntitySet.addAll(rmEntitySet); // fail fast so don't trap exception here
						}
						//if(rmEntitySet != null) {
						// re-build the rm entity set
						for(final Model indexedModel : rmModelList) {
							assert indexedModel != null;
							final IEntityType indexedEntityType = indexedModel.getEntityType();
							final Class<IEntity> indexedEntityClass =
								(Class<IEntity>) etResolver.resolveEntityClass(indexedEntityType);
							final Long id = Long.valueOf(indexedModel.getId());
							final PrimaryKey<IEntity> imodelPk = new PrimaryKey<IEntity>(indexedEntityClass, id);

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
									indexedEntity = instantiateEntity(indexedEntityClass);
									marshalModel(indexedModel, indexedEntity, visited, depth + 1);
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
									marshalModel(indexedModel, indexedEntity, visited, depth + 1);
								}
							}
							// satisify bi-di relationship
							if(indexedEntity instanceof IChildEntity) {
								((IChildEntity) indexedEntity).setParent(crntEntity);
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
			}

			// translate nested property names
			propName = propName.replace('_', '.');

			try {
				bw.setPropertyValue(propName, val);
			}
			catch(final RuntimeException re) {
				log.debug("Didn't set entity prop: " + re.getMessage());
				// ok
			}

		}// for loop

		// this shouldn't be necessary!
		/*
		if(crntEntity.getId() == null) {
			// assume new and set generated id
			if(crntEntity.getVersion() != -1L) {
				throw new IllegalArgumentException("Encountered an entity (" + crntEntity.descriptor()
						+ ") w/o an id having a non-null version.");
			}
			entityFactory.assignPrimaryKey(crntEntity);
		}
		*/
	}

	/**
	 * Creates a new entity instance.
	 * @param entityClass the entity type
	 * @return Newly created entity instance of the given type.
	 */
	private IEntity instantiateEntity(Class<? extends IEntity> entityClass) {
		try {
			return this.entityFactory.createEntity(entityClass, true);
		}
		catch(final Exception ie) {
			throw new RuntimeException("Unable to instantiate entity of class: " + entityClass.getSimpleName());
		}
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

		// we convert server side ids to strings client-side
		else if(IEntity.PK_FIELDNAME.equals(pname)) {
			prop = new StringPropertyValue(pname, pdata, obj == null ? null : obj.toString());
		}

		// we convert server side version prop values to strings client-side
		else if(IVersionSupport.VERSION_FIELDNAME.equals(pname)) {
			prop = new StringPropertyValue(pname, pdata, obj == null ? null : obj.toString());
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
			prop = new DoublePropertyValue(pname, pdata, obj == null ? null : Double.valueOf(((Float) obj).doubleValue()));
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
		// check bound method annotations and honor @Transient
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
		return ((maxDepth < 0 || (maxDepth >= 0 && ((depth + 1) <= maxDepth))) || (depth >= MAX_DEPTH));
	}
}
