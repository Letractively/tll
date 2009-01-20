package com.tll.model.schema;

import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Transient;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.validator.Length;
import org.hibernate.validator.NotEmpty;
import org.hibernate.validator.NotNull;

import com.tll.model.EntityUtil;
import com.tll.model.IEntity;

public final class SchemaInfo implements ISchemaInfo {

	private static final Log log = LogFactory.getLog(SchemaInfo.class);

	private static final int maxLenInt = (new Integer(Integer.MAX_VALUE).toString()).length();
	private static final int maxLenLong = (new Long(Long.MAX_VALUE).toString()).length();
	private static final int maxLenFloat = (new Float(Float.MAX_VALUE).toString()).length();
	private static final int maxLenDouble = (new Double(Double.MAX_VALUE).toString()).length();

	/**
	 * key: entity class val: serviceMap of FieldData objects keyed by the field
	 * name (a nested property path) where the property path is that relative to
	 * the parentAccount entity class.
	 */
	private final Map<Class<? extends IEntity>, Map<String, ISchemaProperty>> schemaMap =
			new HashMap<Class<? extends IEntity>, Map<String, ISchemaProperty>>();

	public Map<String, ISchemaProperty> getAllSchemaProperties(final Class<? extends IEntity> entityClass)
			throws SchemaInfoException {
		if(!schemaMap.containsKey(entityClass)) {
			// load it
			load(entityClass);
		}
		return schemaMap.get(entityClass);
	}

	public String[] getSchemaPropertyNames(final Class<? extends IEntity> entityClass) throws SchemaInfoException {
		final Set<String> set = schemaMap.get(entityClass).keySet();
		return set.toArray(new String[set.size()]);
	}

	public PropertyMetadata getPropertyMetadata(final Class<? extends IEntity> entityClass, final String propertyName)
			throws SchemaInfoException {
		final ISchemaProperty sp = getSchemaProperty(entityClass, propertyName);
		if(sp.getPropertyType().isRelational()) {
			throw new SchemaInfoException(propertyName + " for entity type " + entityClass.getName()
					+ " is not non-relational.");
		}
		return (PropertyMetadata) sp;
	}

	public RelationInfo getRelationInfo(final Class<? extends IEntity> entityClass, final String propertyName)
			throws SchemaInfoException {
		final ISchemaProperty sp = getSchemaProperty(entityClass, propertyName);
		if(!sp.getPropertyType().isRelational()) {
			throw new SchemaInfoException(propertyName + " for entity type " + entityClass.getName() + " is not relational.");
		}
		return (RelationInfo) sp;
	}

	/**
	 * @param entityClass
	 * @param propertyName
	 * @return
	 * @throws SchemaInfoException
	 */
	private ISchemaProperty getSchemaProperty(final Class<? extends IEntity> entityClass, final String propertyName)
			throws SchemaInfoException {
		if(propertyName == null || propertyName.length() < 1)
			throw new IllegalArgumentException("Unable to retreive schema property: no property name specified");

		if(!schemaMap.containsKey(entityClass)) {
			load(entityClass);
		}
		
		final Map<String, ISchemaProperty> classMap = schemaMap.get(entityClass);

		if(!classMap.containsKey(propertyName))
			throw new SchemaInfoException("No field descriptor not found for field: '" + propertyName + "' of class '"
					+ entityClass.getName() + "'");

		return classMap.get(propertyName);
	}

	/**
	 * Loads schema data for a particular entity type.
	 * @param entityClass The entity type
	 */
	private void load(Class<? extends IEntity> entityClass) {
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

				if(method.getAnnotation(Nested.class) != null) {
					// nested value object
					iterateMethods(propName, method.getReturnType(), map);
				}

				final ISchemaProperty sp = toSchemaProperty(propName, method);
				if(sp != null) {
					map.put(fullPropName, sp);
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
	 * Presumes the method has already been deemed schema related.
	 * @param method
	 * @return true/faalse
	 */
	private boolean isNonRelational(final Method method) {
		final Class<?> rt = method.getReturnType();
		if(rt != null && !Collection.class.isAssignableFrom(rt) && !IEntity.class.isAssignableFrom(rt)) {
			return true;
		}
		return false;
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

		// non-relational...
		if(isNonRelational(m)) {

			PropertyMetadata fd = null;

			final Column c = m.getAnnotation(Column.class);

			final boolean managed = (m.getAnnotation(Managed.class) != null);

			final boolean required =
					m.getAnnotation(NotNull.class) != null || m.getAnnotation(NotEmpty.class) != null
							|| (c == null ? false : !c.nullable());

			final Length aLength = m.getAnnotation(Length.class);
			int maxlen = aLength != null ? aLength.max() : -1;

			if(rt.equals(String.class)) {
				maxlen = maxlen == -1 ? 255 : maxlen;
				fd = new PropertyMetadata(PropertyType.STRING, managed, required, maxlen);
			}
			else if(rt.isEnum()) {
				maxlen = maxlen == -1 ? 255 : maxlen;
				fd = new PropertyMetadata(PropertyType.ENUM, managed, required, maxlen);
			}
			else if(int.class.equals(rt) || Integer.class.equals(rt)) {
				maxlen = maxlen == -1 ? maxLenInt : maxlen;
				fd = new PropertyMetadata(PropertyType.INT, managed, required, maxlen);
			}
			else if(boolean.class.equals(rt) || Boolean.class.equals(rt)) {
				maxlen = maxlen == -1 ? 5 : maxlen;
				fd = new PropertyMetadata(PropertyType.BOOL, managed, required, maxlen);
			}
			else if(float.class.equals(rt) || Float.class.equals(rt)) {
				maxlen = maxlen == -1 ? maxLenFloat : maxlen;
				fd = new PropertyMetadata(PropertyType.FLOAT, managed, required, maxlen);
			}
			else if(double.class.equals(rt) || Double.class.equals(rt)) {
				maxlen = maxlen == -1 ? maxLenDouble : maxlen;
				fd = new PropertyMetadata(PropertyType.DOUBLE, managed, required, maxlen);
			}
			else if(long.class.equals(rt) || Long.class.equals(rt)) {
				maxlen = maxlen == -1 ? maxLenLong : maxlen;
				fd = new PropertyMetadata(PropertyType.LONG, managed, required, maxlen);
			}
			else if(char.class.equals(rt) || Character.class.equals(rt)) {
				fd = new PropertyMetadata(PropertyType.CHAR, managed, required, 1);
			}
			else if(Date.class.equals(rt)) {
				fd = new PropertyMetadata(PropertyType.DATE, managed, required, 30);
			}

			return fd;
		}

		// relational...
		CascadeType[] cascades = null;

		// many to one?
		final ManyToOne mto = m.getAnnotation(ManyToOne.class);
		if(mto != null) {
			cascades = mto.cascade();
			return new RelationInfo(EntityUtil.entityTypeFromClass((Class<? extends IEntity>) rt), PropertyType.RELATED_ONE,
					(cascades == null || cascades.length == 0));
		}

		// one to many?
		final OneToMany otm = m.getAnnotation(OneToMany.class);
		if(otm != null) {
			Class<? extends IEntity> rmec =
					(Class<? extends IEntity>) ((ParameterizedType) m.getGenericReturnType()).getActualTypeArguments()[0];
			cascades = otm.cascade();
			return new RelationInfo(EntityUtil.entityTypeFromClass(rmec), PropertyType.RELATED_MANY,
					(cascades == null || cascades.length == 0));
		}

		if("parent".equals(propName)) {
			// NOTE: we can't determine the return type at runtime since, in the
			// IChildEntity.getParent() case, the return type is generic
			// so we are forced to pass null for the related type
			return new RelationInfo(/*EntityUtil.entityTypeFromClass((Class<? extends IEntity>) rt)*/null,
					PropertyType.RELATED_ONE, true);
		}

		return null;
	}
}
