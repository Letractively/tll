package com.tll.model.schema;

import java.io.Serializable;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.validation.constraints.Length;
import org.hibernate.validation.constraints.NotEmpty;

import com.tll.util.PropertyPath;

public final class SchemaInfo implements ISchemaInfo {

	private static final Log log = LogFactory.getLog(SchemaInfo.class);

	private static final int maxLenInt = (new Integer(Integer.MAX_VALUE).toString()).length();
	private static final int maxLenLong = (new Long(Long.MAX_VALUE).toString()).length();

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
	public RelationInfo getRelationInfo(final Class<?> entityClass, final String propertyName)
	throws SchemaInfoException {
		final ISchemaProperty sp = getSchemaProperty(entityClass, propertyName);
		if(!sp.getPropertyType().isRelational()) {
			throw new SchemaInfoException(propertyName + " for entity type " + entityClass.getName() + " is not relational.");
		}
		return (RelationInfo) sp;
	}

	@Override
	public NestedInfo getNestedInfo(final Class<?> entityClass, final String propertyName)
	throws SchemaInfoException {
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
		iterateMethods(null, entityClass, classMap);
		schemaMap.put(entityClass, classMap);
		log.info("Schema information loaded for entity: '" + entityClass.getSimpleName() + "'");
	}

	/**
	 * Iterates the methods for the given type populating the given schema map
	 * along the way.
	 * @param parentPropName
	 * @param type The type to iterate
	 * @param map The schema map
	 */
	private void iterateMethods(String parentPropName, Class<?> type, Map<String, ISchemaProperty> map) {
		String propName, fullPropName;
		for(final Method method : type.getMethods()) {
			if(isSchemaRelated(method)) {

				propName = getPropertyNameFromAccessorMethodName(method.getName());
				fullPropName = parentPropName == null ? propName : parentPropName + '.' + propName;

				final ISchemaProperty sp = toSchemaProperty(propName, method);
				if(sp != null) {
					map.put(fullPropName, sp);
					// handle nested
					if(sp.getPropertyType().isNested()) {
						iterateMethods(propName, method.getReturnType(), map);
					}
				}
			}
		}
	}

	/**
	 * Determines the property name given a method name presumed to be in standard
	 * java bean accessor format. (E.g.: getProperty -> property)
	 * @param methodName
	 * @return The associated property name
	 */
	private String getPropertyNameFromAccessorMethodName(final String methodName) {
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
	 * Is this method schema related?
	 * @param method
	 * @return true/false
	 */
	private boolean isSchemaRelated(final Method method) {
		final String mn = method.getName();
		return (method.getAnnotation(Transient.class) == null && (mn.startsWith("get") || mn.startsWith("is")) && !mn
				.equals("getVersion"));
	}

	/**
	 * Is the method relational?
	 * @param method
	 * @return true/false
	 */
	private boolean isRelational(final Method method) {
		return "getParent".equals(method.getName()) || method.getAnnotation(ManyToOne.class) != null
				|| method.getAnnotation(OneToMany.class) != null
		|| method.getAnnotation(OneToOne.class) != null || method.getAnnotation(ManyToMany.class) != null;
	}

	/**
	 * Creates an {@link ISchemaProperty} instance given a schema related method
	 * by interrogating the bound annotations and stuff.
	 * @param propName
	 * @param m The method
	 * @return New {@link ISchemaProperty} impl instance.
	 */
	@SuppressWarnings("unchecked")
	private ISchemaProperty toSchemaProperty(final String propName, final Method m) {
		assert m != null;

		final Class<?> rt = m.getReturnType();
		assert rt != null : "The return type is null";

		final boolean nested = (m.getAnnotation(Nested.class) != null);

		// nested
		if(nested) {
			return new NestedInfo((Class<? extends Serializable>) rt);
		}

		// relational
		else if(isRelational(m)) {
			// relational...
			CascadeType[] cascades = null;

			// many to one?
			final ManyToOne mto = m.getAnnotation(ManyToOne.class);
			if(mto != null) {
				cascades = mto.cascade();
				return new RelationInfo(rt, PropertyType.RELATED_ONE, (cascades == null || cascades.length == 0));
			}

			// one to many?
			final OneToMany otm = m.getAnnotation(OneToMany.class);
			if(otm != null) {
				final Class<?> rmec = (Class<?>) ((ParameterizedType) m.getGenericReturnType()).getActualTypeArguments()[0];
				cascades = otm.cascade();
				return new RelationInfo(rmec, PropertyType.RELATED_MANY, (cascades == null || cascades.length == 0));
			}

			if("parent".equals(propName)) {
				// NOTE: we can't determine the return type at runtime since, in the
				// IChildEntity.getParent() case, the return type is generic
				// so we are forced to pass null for the related type
				return new RelationInfo(/*EntityUtil.entityTypeFromClass((Class<?>) rt)*/null,
						PropertyType.RELATED_ONE, true);
			}

			// un-handled relational type (many2many for example)
			return null;
		}

		PropertyMetadata fd = null;

		final Column c = m.getAnnotation(Column.class);

		final boolean managed = (m.getAnnotation(Managed.class) != null);

		final boolean required =
			m.getAnnotation(NotNull.class) != null || m.getAnnotation(NotEmpty.class) != null
			|| (c == null ? false : !c.nullable());

		final Length aLength = m.getAnnotation(Length.class);
		int maxlen = aLength != null ? aLength.max() : -1;

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
