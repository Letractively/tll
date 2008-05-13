package com.tll.model.schema;

import java.lang.reflect.Method;
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
import com.tll.model.impl.PaymentData;
import com.tll.model.impl.PaymentInfo;

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

	/**
	 * Constructor
	 * @throws SchemaInfoException
	 */
	public SchemaInfo() throws SchemaInfoException {
		super();
		load();
	}

	@SuppressWarnings("unchecked")
	private void load() {
		Map<String, ISchemaProperty> classMap;
		final Class<? extends IEntity>[] entityClasses = EntityUtil.getEntityClasses();
		for(final Class<? extends IEntity> entityClass : entityClasses) {
			log.debug("Loading schema info for '" + entityClass.getSimpleName() + "'...");

			classMap = new HashMap<String, ISchemaProperty>();

			for(final Method method : entityClass.getMethods()) {
				if(isSchemaRelated(method)) {
					final String propName = getPropertyNameFromAccessorMethodName(method.getName());
					final ISchemaProperty sp = toSchemaProperty(propName, method);
					if(sp != null) {
						classMap.put(propName, sp);
					}
				}
			}

			this.schemaMap.put(entityClass, classMap);
			log.info("Schema information loaded for entity: '" + entityClass.getSimpleName() + "'");

		}

		// special case: PaymentInfo (PaymentData)
		classMap = schemaMap.get(PaymentInfo.class);
		assert classMap != null;
		final Class<PaymentData> pdc = PaymentData.class;
		for(final Method method : pdc.getMethods()) {
			if(isSchemaRelated(method)) {
				final String propName = getPropertyNameFromAccessorMethodName(method.getName());
				final ISchemaProperty sp = toSchemaProperty(propName, method);
				if(sp != null) {
					classMap.put("paymentData." + propName, sp);
				}
			}
		}

		log.info("Schema information loading complete");
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

		// non-relational...
		if(isNonRelational(m)) {

			FieldData fd = null;

			final Column c = m.getAnnotation(Column.class);

			final boolean managed = (m.getAnnotation(Managed.class) != null);

			final boolean required =
					m.getAnnotation(NotNull.class) != null || m.getAnnotation(NotEmpty.class) != null
							|| (c == null ? false : !c.nullable());

			final Length aLength = m.getAnnotation(Length.class);
			int maxlen = aLength != null ? aLength.max() : -1;

			final Class<?> rt = m.getReturnType();
			assert rt != null : "The return type is null";
			if(rt.equals(String.class)) {
				maxlen = maxlen == -1 ? 255 : maxlen;
				fd = new FieldData(PropertyType.STRING, propName, managed, required, maxlen);
			}
			else if(rt.isEnum()) {
				maxlen = maxlen == -1 ? 255 : maxlen;
				fd = new FieldData(PropertyType.ENUM, propName, managed, required, maxlen);
			}
			else if(int.class.equals(rt) || Integer.class.equals(rt)) {
				maxlen = maxlen == -1 ? maxLenInt : maxlen;
				fd = new FieldData(PropertyType.INT, propName, managed, required, maxlen);
			}
			else if(boolean.class.equals(rt) || Boolean.class.equals(rt)) {
				maxlen = maxlen == -1 ? 5 : maxlen;
				fd = new FieldData(PropertyType.BOOL, propName, managed, required, maxlen);
			}
			else if(float.class.equals(rt) || Float.class.equals(rt)) {
				maxlen = maxlen == -1 ? maxLenFloat : maxlen;
				fd = new FieldData(PropertyType.FLOAT, propName, managed, required, maxlen);
			}
			else if(double.class.equals(rt) || Double.class.equals(rt)) {
				maxlen = maxlen == -1 ? maxLenDouble : maxlen;
				fd = new FieldData(PropertyType.DOUBLE, propName, managed, required, maxlen);
			}
			else if(long.class.equals(rt) || Long.class.equals(rt)) {
				maxlen = maxlen == -1 ? maxLenLong : maxlen;
				fd = new FieldData(PropertyType.LONG, propName, managed, required, maxlen);
			}
			else if(char.class.equals(rt) || Character.class.equals(rt)) {
				fd = new FieldData(PropertyType.CHAR, propName, managed, required, 1);
			}
			else if(Date.class.equals(rt)) {
				fd = new FieldData(PropertyType.DATE, propName, managed, required, 30);
			}

			return fd;
		}

		// relational...
		CascadeType[] cascades = null;
		// determine if the relation is cascaded
		final ManyToOne mto = m.getAnnotation(ManyToOne.class);
		if(mto != null) {
			cascades = mto.cascade();
			return new RelationInfo(PropertyType.RELATED_ONE, (cascades == null || cascades.length == 0));
		}

		final OneToMany otm = m.getAnnotation(OneToMany.class);
		if(otm != null) {
			cascades = otm.cascade();
			return new RelationInfo(PropertyType.RELATED_MANY, (cascades == null || cascades.length == 0));
		}

		// TODO make this more generic!
		if("parent".equals(propName)) {
			return new RelationInfo(PropertyType.RELATED_ONE, true);
		}

		return null;
	}

	private Class<? extends IEntity> verify(final Class<? extends IEntity> entityClass) {

		// resolve a possibly sub-classed entity class to its root (usu. due to
		// hibernate chaching mechanism: CGLIB)
		for(final Class<? extends IEntity> clz : schemaMap.keySet()) {
			if(clz.isAssignableFrom(entityClass) && entityClass.getName().startsWith(clz.getName())) {
				return clz;
			}
		}

		throw new IllegalArgumentException("Un-mapped IEntity class '" + entityClass.getName() + "'");
	}

	public Map<String, ISchemaProperty> getAllSchemaProperties(final Class<? extends IEntity> entityClass)
			throws SchemaInfoException {
		return schemaMap.get(verify(entityClass));
	}

	public String[] getSchemaPropertyNames(final Class<? extends IEntity> entityClass) throws SchemaInfoException {
		final Set<String> set = schemaMap.get(verify(entityClass)).keySet();
		return set.toArray(new String[set.size()]);
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

		final Map<String, ISchemaProperty> classMap = schemaMap.get(verify(entityClass));

		if(!classMap.containsKey(propertyName))
			throw new SchemaInfoException("No field descriptor not found for field: '" + propertyName + "' of class '"
					+ entityClass.getName() + "'");

		return classMap.get(propertyName);
	}

	public FieldData getFieldData(final Class<? extends IEntity> entityClass, final String propertyName)
			throws SchemaInfoException {
		final ISchemaProperty sp = getSchemaProperty(entityClass, propertyName);
		if(sp.getPropertyType().isRelational()) {
			throw new SchemaInfoException(propertyName + " for entity type " + entityClass.getName()
					+ " is not non-relational.");
		}
		return (FieldData) sp;
	}

	public RelationInfo getRelationInfo(final Class<? extends IEntity> entityClass, final String propertyName)
			throws SchemaInfoException {
		final ISchemaProperty sp = getSchemaProperty(entityClass, propertyName);
		if(!sp.getPropertyType().isRelational()) {
			throw new SchemaInfoException(propertyName + " for entity type " + entityClass.getName() + " is not relational.");
		}
		return (RelationInfo) sp;
	}

}
