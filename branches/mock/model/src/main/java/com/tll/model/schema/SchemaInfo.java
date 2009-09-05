package com.tll.model.schema;

import java.io.Serializable;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.validation.constraints.Length;

import com.tll.model.IEntity;
import com.tll.util.PropertyPath;

public final class SchemaInfo implements ISchemaInfo {

	/**
	 * PersistProperty - Encapsulates persistence info for a particular entity
	 * property deemed persist related.
	 * @author jpk
	 */
	static final class PersistProperty {

		/**
		 * The accessor method.
		 */
		final Method method;

		/**
		 * The associated property name.
		 */
		final String pname;

		/**
		 * Constructor
		 * @param accessorMethod the required accessor method ref
		 * @param fld the corresponding field ref to the persist related property
		 */
		public PersistProperty(Method accessorMethod, String pname) {
			super();
			if(accessorMethod == null) throw new IllegalArgumentException();
			this.method = accessorMethod;
			this.pname = pname;
		}

		@Override
		public String toString() {
			return pname;
		}

	} // PersistProperty

	private static final Log log = LogFactory.getLog(SchemaInfo.class);

	private static final int maxLenInt = Integer.valueOf(Integer.MAX_VALUE).toString().length();
	private static final int maxLenLong = Long.valueOf(Long.MAX_VALUE).toString().length();

	/**
	 * Determines the property name given a method name presumed to be in standard
	 * java bean accessor format. (E.g.: getProperty -> property)
	 * @param methodName
	 * @return The associated property name
	 */
	static String getPropertyNameFromAccessorMethodName(final String methodName) {
		String s;
		if(methodName.startsWith("get")) {
			s = methodName.substring(3);
		}
		else if(methodName.startsWith("is")) {
			s = methodName.substring(2);
		}
		else {
			throw new SchemaInfoException("Invalid accessor method name: " + methodName);
		}
		return (Character.toLowerCase(s.charAt(0)) + s.substring(1));
	}

	/**
	 * key: entity class val: serviceMap of FieldData objects keyed by the field
	 * name (a nested property path) where the property path is that relative to
	 * the parentAccount entity class.
	 */
	private final Map<Class<?>, Map<String, ISchemaProperty>> schemaMap =
			new HashMap<Class<?>, Map<String, ISchemaProperty>>();

	@Override
	public PropertyMetadata getPropertyMetadata(final Class<?> entityClass, final String propertyName)
			throws SchemaInfoException {
		final ISchemaProperty sp = getSchemaProperty(entityClass, propertyName);
		if(!sp.getPropertyType().isValue()) {
			throw new SchemaInfoException(propertyName + " for entity type " + entityClass.getName()
					+ " is not a value type property.");
		}
		return (PropertyMetadata) sp;
	}

	@Override
	public RelationInfo getRelationInfo(final Class<?> entityClass, final String propertyName) throws SchemaInfoException {
		final ISchemaProperty sp = getSchemaProperty(entityClass, propertyName);
		if(!sp.getPropertyType().isRelational()) {
			throw new SchemaInfoException(propertyName + " for entity type " + entityClass.getName() + " is not relational.");
		}
		return (RelationInfo) sp;
	}

	@Override
	public NestedInfo getNestedInfo(final Class<?> entityClass, final String propertyName) throws SchemaInfoException {
		final ISchemaProperty sp = getSchemaProperty(entityClass, propertyName);
		if(!sp.getPropertyType().isNested()) {
			throw new SchemaInfoException(propertyName + " for entity type " + entityClass.getName() + " is not nested.");
		}
		return (NestedInfo) sp;
	}

	@Override
	public ISchemaProperty getSchemaProperty(final Class<?> entityClass, final String propertyName)
			throws SchemaInfoException {
		if(propertyName == null || propertyName.length() < 1)
			throw new IllegalArgumentException("Unable to retreive schema property: no property name specified");

		if(!schemaMap.containsKey(entityClass)) {
			load(entityClass);
		}

		Map<String, ISchemaProperty> classMap = schemaMap.get(entityClass);

		if(!classMap.containsKey(propertyName)) {

			final PropertyPath p = new PropertyPath(propertyName);
			if(p.depth() > 1) {
				// attempt to resolve the given path to a relational property and a
				// localized path
				ISchemaProperty sp;
				for(int i = 0; i < p.depth(); i++) {
					sp = classMap.get(p.pathAt(i));
					if(sp == null || !sp.getPropertyType().isRelational()) break;
					final RelationInfo ri = (RelationInfo) sp;
					if(!schemaMap.containsKey(ri.getRelatedType())) {
						load(ri.getRelatedType());
					}
					classMap = schemaMap.get(ri.getRelatedType());
					final String np = p.clip(i + 1);
					sp = classMap.get(np);
					if(sp != null) return sp;
				}
			}

			throw new SchemaInfoException("Property: '" + propertyName + "' of class '" + entityClass.getName()
					+ "' doesn't exist.");
		}
		return classMap.get(propertyName);
	}

	/**
	 * Loads schema data for a particular entity type.
	 * @param entityClass The entity type
	 */
	private void load(Class<?> entityClass) {
		Map<String, ISchemaProperty> classMap;
		log.debug("Loading schema info for entity: '" + entityClass.getSimpleName() + "'...");
		classMap = new HashMap<String, ISchemaProperty>();
		iterateEntity(null, entityClass, classMap);
		schemaMap.put(entityClass, classMap);
		log.info("Schema information loaded for entity: '" + entityClass.getSimpleName() + "'");
	}

	/**
	 * @param method the method to test
	 * @return <code>true</code> if the given method is a persiste related
	 *         accessor (getter) method.
	 */
	private boolean isPersistRelatedAccessor(final Method method) {
		final String mn = method.getName();
		// handle IChildEntity.getParent() specifically since its overridden and we
		// don't want the IChildEntity method decl!
		if("getParent".equals(method.getName()) && IEntity.class.equals(method.getReturnType())) {
			return false;
		}
		return (method.getAnnotation(NoPersist.class) == null && (mn.startsWith("get") || mn.startsWith("is")));
	}

	/**
	 * Iterates the public methods for the given entity type populating the given
	 * schema map along the way.
	 * @param parentPropName
	 * @param type The type to iterate
	 * @param map The schema map
	 */
	private void iterateEntity(String parentPropName, Class<?> type, Map<String, ISchemaProperty> map) {
		String propName, fullPropName;
		final Method[] mthds = type.getMethods();
		for(final Method method : mthds) {
			if(isPersistRelatedAccessor(method)) {
				propName = getPropertyNameFromAccessorMethodName(method.getName());
				final PersistProperty fi = getFieldInfo(propName, type, method);
				if(fi != null) {
					final ISchemaProperty sp = toSchemaProperty(fi);
					if(sp != null) {
						fullPropName = parentPropName == null ? propName : parentPropName + '.' + propName;
						map.put(fullPropName, sp);
						// handle nested
						if(sp.getPropertyType().isNested()) {
							iterateEntity(propName, method.getReturnType(), map);
						}
					}
				}
			}
		}
	}

	/**
	 * Creates a new {@link PersistProperty} instance for the given entity
	 * property that is presumed to be persistence related (non-transient).
	 * @param propName the local property name
	 * @param entityClass the entity type
	 * @param accessorMethod the entity accessor method ref
	 * @return New {@link PersistProperty} instance or <code>null</code>.
	 */
	private PersistProperty getFieldInfo(final String propName, final Class<?> entityClass, final Method accessorMethod)
			throws IllegalStateException {
		return new PersistProperty(accessorMethod, propName);
	}

	/**
	 * Is the given class type an entity (persist capable)?
	 * @param type
	 * @return true/false
	 */
	private boolean isEntityType(Class<?> type) {
		return IEntity.class.isAssignableFrom(type);
	}

	/**
	 * Is the persist property relational?
	 * @param pprop the persist prop ref
	 * @return true/false
	 */
	private boolean isRelational(final PersistProperty pprop) {
		if("getParent".equals(pprop.method.getName())) return true;
		final Class<?> rt = pprop.method.getReturnType();
		if(Collection.class.isAssignableFrom(rt)) {
			try {
				final Class<?> rmec =
						(Class<?>) ((ParameterizedType) pprop.method.getGenericReturnType()).getActualTypeArguments()[0];
				return isEntityType(rmec);
			}
			catch(final Throwable t) {
				return false;
			}
		}
		return isEntityType(rt);
	}

	/**
	 * Creates an {@link ISchemaProperty} instance given a schema related method
	 * by interrogating the bound annotations and stuff.
	 * @param pprop
	 * @return New {@link ISchemaProperty} impl instance.
	 */
	@SuppressWarnings("unchecked")
	private ISchemaProperty toSchemaProperty(final PersistProperty pprop) {
		final Method m = pprop.method;
		final Class<?> rt = m.getReturnType();
		assert rt != null : "The return type is null";

		// nested
		if(m.getAnnotation(Nested.class) != null) {
			return new NestedInfo((Class<? extends Serializable>) rt);
		}

		if(isRelational(pprop)) {
			// relational
			final boolean reference = m.getAnnotation(Reference.class) != null;
			if(isEntityType(rt)) {
				// related one
				return new RelationInfo(rt, PropertyType.RELATED_ONE, reference);
			}
			else if(Collection.class.isAssignableFrom(rt)) {
				// related many
				final Class<?> rmec = (Class<?>) ((ParameterizedType) m.getGenericReturnType()).getActualTypeArguments()[0];
				return new RelationInfo(rmec, PropertyType.RELATED_MANY, reference);
			}

			// un-handled relational type (many2many for example)
			log.warn("Unhandled relational type: " + pprop + " (Skipping).");
			return null;
		}

		PropertyMetadata fd = null;

		final boolean managed = m.getAnnotation(Managed.class) != null;

		// determine requiredness
		boolean required = false;
		final NotNull aN = m.getAnnotation(NotNull.class);
		if(aN != null) {
			required = true;
		}

		// determine max length
		int maxlen = -1;
		final Length aLength = m.getAnnotation(Length.class);
		if(aLength != null) {
			maxlen = aLength.max();
		}
		else {
			// try Size anno
			final Size aSize = m.getAnnotation(Size.class);
			if(aSize != null) {
				maxlen = aSize.max();
			}
		}

		if(rt == String.class) {
			maxlen = maxlen == -1 ? 255 : maxlen;
			fd = new PropertyMetadata(PropertyType.STRING, managed, required, maxlen);
		}
		else if(rt.isEnum()) {
			maxlen = maxlen == -1 ? 255 : maxlen;
			fd = new PropertyMetadata(PropertyType.ENUM, managed, required, maxlen);
		}
		else if(int.class == rt || Integer.class == rt) {
			maxlen = maxlen == -1 ? maxLenInt : maxlen;
			fd = new PropertyMetadata(PropertyType.INT, managed, required, maxlen);
		}
		else if(boolean.class == rt || Boolean.class == rt) {
			maxlen = maxlen == -1 ? 5 : maxlen;
			fd = new PropertyMetadata(PropertyType.BOOL, managed, required, maxlen);
		}
		else if(float.class == rt || Float.class == rt) {
			fd = new PropertyMetadata(PropertyType.FLOAT, managed, required, maxlen);
		}
		else if(double.class == rt || Double.class == rt) {
			fd = new PropertyMetadata(PropertyType.DOUBLE, managed, required, maxlen);
		}
		else if(long.class == rt || Long.class == rt) {
			maxlen = maxlen == -1 ? maxLenLong : maxlen;
			fd = new PropertyMetadata(PropertyType.LONG, managed, required, maxlen);
		}
		else if(char.class == rt || Character.class == rt) {
			fd = new PropertyMetadata(PropertyType.CHAR, managed, required, 1);
		}
		else if(Date.class == rt) {
			fd = new PropertyMetadata(PropertyType.DATE, managed, required, 30);
		}
		else if(Map.class == rt) {
			// string map?
			if(String.class == ((ParameterizedType) m.getGenericReturnType()).getActualTypeArguments()[0]
					&& String.class == ((ParameterizedType) m.getGenericReturnType()).getActualTypeArguments()[1]) {
				fd = new PropertyMetadata(PropertyType.STRING_MAP, managed, required, -1);
			}
		}

		return fd;
	}
}
