/**
 * The Logic Lab
 * @author jpk Aug 31, 2007
 */
package com.tll.client.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import com.tll.client.IMarshalable;
import com.tll.model.EntityType;
import com.tll.model.schema.PropertyType;

/**
 * Model - Encapsulates a set of {@link IPropertyBinding}s. This construct
 * serves to represent an entity instance object graph on the client.
 * @author jpk
 */
public final class Model implements IMarshalable, Iterable<IPropertyBinding> {

	/**
	 * Entity id property name
	 */
	public static final String ID_PROPERTY = "id";

	/**
	 * Entity version property name
	 */
	public static final String VERSION_PROPERTY = "version";

	/**
	 * Entity name property name. May not be set for entity definitions without a
	 * name property.
	 */
	public static final String NAME_PROPERTY = "name";

	/**
	 * Entity data created property name. May not be set for entity definitions
	 * without a date created property.
	 */
	public static final String DATE_CREATED_PROPERTY = "dateCreated";

	/**
	 * Entity data modified property name. May not be set for entity definitions
	 * without a date modified property.
	 */
	public static final String DATE_MODIFIED_PROPERTY = "dateModified";

	/**
	 * Set of {@link IPropertyValue}s holding the actual model data.
	 */
	private Set<IPropertyBinding> props = new HashSet<IPropertyBinding>();

	/**
	 * The entity type.
	 */
	private EntityType entityType;

	/**
	 * The marked deleted flag. When <code>true</code>, this indicates this
	 * model data is scheduled for deletion.
	 */
	private boolean markedDeleted;

	/**
	 * IPropertyBinding ref to this Model that is lazily instantiated
	 */
	private RelatedOneProperty bindingRef;

	/**
	 * Constructor
	 */
	public Model() {
		super();
	}

	/**
	 * Constructor
	 * @param entityType
	 */
	public Model(EntityType entityType) {
		super();
		this.entityType = entityType;
	}

	/**
	 * @return the ref type
	 */
	public EntityType getEntityType() {
		return entityType;
	}

	/**
	 * Provides the <em>unique</em> {@link RefKey} for this model/entity
	 * instance.
	 */
	public RefKey getRefKey() {
		Integer id = null;
		IPropertyBinding pvId = get(ID_PROPERTY);
		if(pvId != null && pvId.getType() == PropertyType.INT) {
			id = ((IntPropertyValue) pvId).getInteger();
		}

		String name = null;
		IPropertyBinding pvName = get(NAME_PROPERTY);
		if(pvName != null && pvName.getType() == PropertyType.STRING) {
			name = ((StringPropertyValue) pvName).getString();
		}

		// NOTE: we don't enforce/check id validity here as we may be a copy or a
		// cleared model!
		assert /*(id != null) && */(entityType != null);
		return new RefKey(entityType, id, name);
	}

	/**
	 * @return Reference to <em>this</em> Model expressed as an
	 *         {@link IPropertyBinding}.
	 */
	public RelatedOneProperty getBindingRef() {
		if(bindingRef == null) {
			bindingRef = new RelatedOneProperty(getEntityType(), null, true, this);
		}
		return bindingRef;
	}

	/**
	 * Finds a property value in this model's collection of property values given
	 * a non-path property name. (No property path resolution is performed.)
	 * @param name The non-path property name (i.e. no dots)
	 * @return The found {@link IPropertyValue} or <code>null</code> if not
	 *         present.
	 */
	public IPropertyBinding get(String name) {
		if(name == null) return null;
		for(IPropertyBinding prop : props) {
			if(name.equals(prop.getPropertyName())) return prop;
		}
		return null;
	}

	/**
	 * Sets the given {@link IPropertyValue} as a child to this model with no
	 * property path resolution preformed. If an existing prop val is currently
	 * mapped to the ascribed property name, it is replaced by the one given.
	 * @param propValue The replacing {@link IPropertyValue}
	 */
	public void set(IPropertyBinding propValue) {
		if(propValue == null) return;
		IPropertyBinding prop = get(propValue.getPropertyName());
		if(prop != null) {
			props.remove(prop);
		}
		props.add(propValue);
	}

	/**
	 * Provides the property value in String form
	 * <em>for self-formatting types only</em>.
	 * @param propPath The property path
	 * @return The property value as a String or <code>null</code> when the
	 *         property does not exist.
	 * @throws IllegalArgumentException When the targeted property is relational
	 *         or is not self-formatting.
	 */
	public String asString(String propPath) throws IllegalArgumentException {
		IPropertyBinding prop = getBinding(new PropertyPath(propPath));
		if(prop == null) return null;
		if(prop instanceof ISelfFormattingPropertyValue == false) {
			throw new IllegalArgumentException("Non self-formatting property: " + propPath);
		}
		return ((ISelfFormattingPropertyValue) prop).asString();
	}

	/**
	 * Resolves a property path to the nested {@link IPropertyBinding}. This is a
	 * generic way to obtain a defined model property.
	 * <p>
	 * <strong>NOTE: </strong>When a property path element having no associated
	 * property is encoutered before reaching the end of the given property path
	 * or when a given index is found out of range for an indexable property in
	 * the given property path, <code>null</code> is returned.
	 * @param propPath The property path. When <code>null</code>, a
	 *        {@link RelatedOneProperty} that references <em>this</em> model is
	 *        returned.
	 * @return The nested property or <code>null</code> if not present
	 * @throws IllegalArgumentException When the given property path is
	 *         <code>null</code> or mal-formed.
	 */
	public IPropertyBinding getBinding(PropertyPath propPath) throws IllegalArgumentException {
		if(propPath == null || propPath.length() < 1) {
			return getBindingRef();
		}
		try {
			return resolvePropertyPath(propPath).getPropertyBinding();
		}
		catch(NullNodeInPropPathException e) {
			return null;
		}
		catch(IndexOutOfRangeInPropPathException ie) {
			return null;
		}
		catch(UnsetPropertyException e) {
			return null;
		}
		catch(PropertyPathException ppe) {
			throw new IllegalArgumentException(ppe.getMessage());
		}
	}

	/**
	 * Retrieves a property value from the model given a property path.
	 * @param propPath A parsed property path
	 * @return The resolved IPropertyValue or <code>null</code> if not set.
	 * @throws IllegalArgumentException When the given property path can't be
	 *         resolved or does not map to an {@link IPropertyValue}.
	 */
	public IPropertyValue getValue(PropertyPath propPath) throws IllegalArgumentException {
		IPropertyBinding prop = getBinding(propPath);
		if(prop == null) return null;
		if(!prop.getType().isValue()) {
			throw new IllegalArgumentException("Property '" + propPath + "' does not map to a value property");
		}
		return (IPropertyValue) prop;
	}

	/**
	 * Retrieves a related one property value from the model given a property
	 * path.
	 * @param propPath The property path (E.g.: "root.relatedModelPropName")
	 * @return Related one property value or <code>null</code> if not set.
	 * @throws IllegalArgumentException When the given property path can't be
	 *         resolved or does not map to a related one property.
	 */
	public RelatedOneProperty relatedOne(PropertyPath propPath) throws IllegalArgumentException {
		IPropertyBinding prop = getBinding(propPath);
		if(prop == null) return null;
		if(prop.getType() != PropertyType.RELATED_ONE) {
			throw new IllegalArgumentException("Property '" + propPath + "' does not map to a related one property");
		}
		return (RelatedOneProperty) prop;
	}

	/**
	 * Retrieves a related many property value from the model given a property
	 * path.
	 * @param propPath The property path (E.g.: "root.listProperty")
	 * @return Related many property value or <code>null</code> if not set.
	 * @throws IllegalArgumentException When the given property path can't be
	 *         resolved or does not map to a related many property.
	 */
	public RelatedManyProperty relatedMany(PropertyPath propPath) throws IllegalArgumentException {
		IPropertyBinding prop = getBinding(propPath);
		if(prop == null) return null;
		if(prop.getType() != PropertyType.RELATED_MANY) {
			throw new IllegalArgumentException("Property '" + propPath + "' does not map to a related many property");
		}
		return (RelatedManyProperty) prop;
	}

	/**
	 * Retrieves an indexed property value from the model given a property path.
	 * This is a property value that wraps a nested Model that is a child of a
	 * related many property.
	 * @param propPath The property path. (E.g.: "root.listProperty[1]")
	 * @return Indexed property value or <code>null</code> if not set.
	 * @throws IllegalArgumentException When the given property path can't be
	 *         resolved or does not map to an indexed property.
	 */
	public IndexedProperty indexed(PropertyPath propPath) throws IllegalArgumentException {
		IPropertyBinding prop = getBinding(propPath);
		if(prop == null) return null;
		if(prop.getType() != PropertyType.INDEXED) {
			throw new IllegalArgumentException("Property '" + propPath + "' does not map to an indexed property");
		}
		return (IndexedProperty) prop;
	}

	/**
	 * Is this model entity new?
	 * @return true/false
	 */
	public boolean isNew() {
		IntPropertyValue prop = (IntPropertyValue) get(VERSION_PROPERTY);
		return prop == null ? true : (prop.getInteger() == null);
	}

	/**
	 * Retrieves the id property
	 * @return The entity id
	 */
	public Integer getId() {
		IntPropertyValue prop = (IntPropertyValue) get(ID_PROPERTY);
		return prop == null ? null : prop.getInteger();
	}

	/**
	 * Retrieves the entities' name property value
	 * @return The entities' name
	 */
	public String getName() {
		StringPropertyValue prop = (StringPropertyValue) get(NAME_PROPERTY);
		return prop == null ? null : prop.getString();
	}

	/**
	 * @return The date created. May be <code>null</code> for entity types that
	 *         don't support this property.
	 */
	public Date getDateCreated() {
		DatePropertyValue prop = (DatePropertyValue) get(DATE_CREATED_PROPERTY);
		return prop == null ? null : prop.getDate();
	}

	/**
	 * @return The date last modified. May be <code>null</code> for entity types
	 *         that don't support this property.
	 */
	public Date getDateModified() {
		DatePropertyValue prop = (DatePropertyValue) get(DATE_MODIFIED_PROPERTY);
		return prop == null ? null : prop.getDate();
	}

	/**
	 * Resolves a given property path against the hierarchy of this model.
	 * @param propPath The property path
	 * @return Property path binding
	 * @throws PropertyPathException When an error occurrs whilst resolving the
	 *         property path
	 */
	private PropPathBinding resolvePropertyPath(final PropertyPath propPath) throws PropertyPathException {
		if(propPath == null || propPath.depth() < 1) {
			throw new MalformedPropPathException("No property specified.");
		}

		IPropertyBinding prop = null;
		Model model = this, parentModel = null;
		final int len = propPath.depth();
		for(int i = 0; i < len; i++) {
			final String pname = propPath.nameAt(i);
			final int index = propPath.indexAt(i);
			boolean indexed = (index >= 0);
			boolean atEnd = (i == len - 1);

			// find the prop val under current group
			prop = model.get(pname);
			if(prop == null) {
				if(atEnd) {
					return new UnsetPropPathBinding(model, propPath);
				}
				throw new NullNodeInPropPathException(propPath.toString(), pname);
			}

			// get the bound prop val type for this prop path element
			final PropertyType pvType = prop.getType();

			// non-relational prop val
			if(!pvType.isRelational()) {
				if(!atEnd) {
					throw new PropPathNodeMismatchException(propPath.toString(), pname, pvType.toString(), "Relational");
				}
				return new PropertyValuePropPathBinding(propPath, (AbstractPropertyValue) prop);
			}

			// related one prop val
			else if(pvType == PropertyType.RELATED_ONE) {
				if(indexed) {
					throw new PropPathNodeMismatchException(propPath.toString(), pname, pvType.toString(),
							PropertyType.RELATED_MANY.toString());
				}
				ModelRefProperty gpv = (ModelRefProperty) prop;
				if(atEnd) {
					return new RelatedOnePropPathBinding(parentModel, propPath, gpv);
				}
				// get the nested group...
				Model ng = gpv.getModel();
				if(ng == null) {
					throw new NullNodeInPropPathException(propPath.toString(), pname);
				}
				// reset for next path
				parentModel = model;
				model = ng;
			}

			// related many prop val
			else if(pvType == PropertyType.RELATED_MANY) {
				RelatedManyProperty rmp = (RelatedManyProperty) prop;
				if(!indexed) {
					if(atEnd) {
						return new IndexablePropPathBinding(parentModel, propPath, rmp);
					}
					// and index is expected if were not at the end
					throw new MalformedPropPathException(propPath.toString());
				}
				else if(indexed) {
					// get the nested group prop val list...
					List<Model> nlist = rmp.getList();
					if(nlist == null) {
						// ensure we have a list to attach indexed groups to later
						nlist = new ArrayList<Model>(0);
						rmp.setList(nlist);
					}
					if(atEnd) {
						return new IndexedPropPathBinding(parentModel, propPath, rmp.isReference(), rmp.getRelatedType(), nlist,
								index);
					}
					if(index >= nlist.size()) {
						throw new IndexOutOfRangeInPropPathException(propPath.toString(), pname, index);
					}
					// reset for next path
					parentModel = model;
					model = nlist.get(index);
				}
			}
		}
		throw new MalformedPropPathException(propPath.toString());
	}

	/**
	 * Deep copies this instance.
	 * @return Clone of this instance
	 */
	public Model copy() {
		return copy(this, new BindingStack<CopyBinding>());
	}

	/**
	 * Recursive copy routine to guard against re-copying entities
	 * @param source The model to be copied
	 * @param visited
	 * @return A deep copy of the model
	 */
	private static Model copy(Model source, BindingStack<CopyBinding> visited) {

		if(source == null) return null;

		// check visited
		CopyBinding binding = (CopyBinding) visited.find(source);
		if(binding != null) return binding.target;

		Model copy = new Model(source.entityType);

		visited.add(new CopyBinding(source, copy));

		copy.markedDeleted = source.markedDeleted;

		for(IPropertyBinding prop : source.props) {
			assert prop != null;

			// related one or indexed prop...
			if(prop instanceof ModelRefProperty) {
				ModelRefProperty mrp = (ModelRefProperty) prop;
				Model model = mrp.isReference() ? mrp.getModel() : copy(mrp.getModel(), visited);
				copy.props.add(new RelatedOneProperty(mrp.getRelatedType(), mrp.getPropertyName(), mrp.isReference(), model));
			}

			// model list relation...
			else if(prop instanceof RelatedManyProperty) {
				RelatedManyProperty rmp = (RelatedManyProperty) prop;
				List<Model> list = rmp.getList();
				List<Model> nlist = null;
				if(list != null) {
					nlist = new ArrayList<Model>(list.size());
					for(Model model : list) {
						nlist.add(rmp.isReference() ? model : copy(model, visited));
					}
				}
				copy.props.add(new RelatedManyProperty(rmp.getRelatedType(), rmp.getPropertyName(), rmp.isReference(), nlist));
			}

			// prop val..
			else {
				copy.props.add(((AbstractPropertyValue) prop).copy());
			}
		}

		return copy;
	}

	/**
	 * Walks the held collection of {@link IPropertyValue}s clearing all values
	 * recursing as necessary to ensure all have been visited.
	 */
	public void clearPropertyValues() {
		clearProps(this, new BindingStack<PropBinding>());
	}

	/**
	 * Clears all nested property values of the given model.
	 * @param model The group to be cleared
	 * @param visited
	 */
	private static void clearProps(Model model, BindingStack<PropBinding> visited) {

		if(model == null) return;

		// check visited
		PropBinding binding = visited.find(model);
		if(binding != null) return;

		visited.add(new PropBinding(model));

		// TODO do we want to reset markedDeleted?
		model.markedDeleted = false;

		for(IPropertyBinding prop : model.props) {
			assert prop != null;

			// model prop (relational) val...
			if(prop instanceof ModelRefProperty) {
				ModelRefProperty gpv = (ModelRefProperty) prop;
				if(!gpv.isReference()) {
					clearProps(gpv.getModel(), visited);
				}
			}

			// model list (relational) prop val...
			else if(prop instanceof RelatedManyProperty) {
				RelatedManyProperty rmp = (RelatedManyProperty) prop;
				if(!rmp.isReference()) {
					List<Model> list = rmp.getList();
					if(list != null) {
						for(Model m : list) {
							clearProps(m, visited);
						}
					}
				}
			}

			// property values...
			else {
				((AbstractPropertyValue) prop).clear();
			}
		}
	}

	/**
	 * @return IPropertyValue Iterator for the referenced property values for this
	 *         model.
	 */
	public Iterator<IPropertyBinding> iterator() {
		assert props != null;
		return props.iterator();
	}

	/**
	 * @return <code>true</code> if this group is marked for deletion.
	 */
	public boolean isMarkedDeleted() {
		return markedDeleted;
	}

	/**
	 * Sets the makred for deletion flag.
	 * @param markedDeleted
	 */
	public void setMarkedDeleted(boolean markedDeleted) {
		this.markedDeleted = markedDeleted;
	}

	@Override
	public boolean equals(Object obj) {
		if(this == obj) return true;
		if(!(obj instanceof Model)) return false;
		return getRefKey().equals(((Model) obj).getRefKey());
	}

	@Override
	public int hashCode() {
		return getRefKey().hashCode();
	}

	@Override
	public String toString() {
		StringBuffer sb = new StringBuffer();
		sb.append("hash:");
		sb.append(hashCode());
		if(markedDeleted) {
			sb.append(" MRKD DELETED! ");
		}

		sb.append(" props[");
		for(Iterator<IPropertyBinding> itr = props.iterator(); itr.hasNext();) {
			IPropertyBinding val = itr.next();
			sb.append(val.toString());
			if(itr.hasNext()) {
				sb.append(" ");
			}
		}
		sb.append("]");

		return sb.toString();
	}

}
