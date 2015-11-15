# Introduction #
A pattern I coin the "Generic DTO" addresses the inability of serializing server-side entities having adorned data that isn't "marshalable" through GWT RPC.
For example, GWT RPC can't handle JPA or JDO entity annotations.

As background to the issue, check out:
  * [GWT-RPC broken in GAE/J](http://groups.google.com/group/google-web-toolkit-contributors/browse_thread/thread/3c768d8d33bfb1dc/5a38aa812c0ac52b?pli=1)

A common solution is to adopt [Data Transfer Objects (DTOs)](http://en.wikipedia.org/wiki/Data_transfer_object).  These objects are designed to transfer model data to and from the client.  The downside is having to create the DTO classes and, with GWT, specific serializers must be created for each of your defined DTOs.

What if DTO object creation could be automated without having to create custom GWT serializers and without having to create DTO classes?  This is the path taken in the Generic DTO pattern.

# Summary #
First off, a class named `Model` is used client-side to hold server-side model data.  This class is basically a wrapper around a `LinkedHashMap` holding properties accessible by a property path.  So all server side model data is transformed into Model instances.  The `Model` class has full support for related one, many and indexed properties as well as deep-copy and property querying using prescribed accessor methods whose arguments serve to filter the results.  For example, if we wish to access the property:
```
addresses[2].address.firstName
```

we do:
```
model.getProperty("addresses[2].address.firstName")
```

A class named `Marshaler`, residing server-side, "marshals" (converts) server-side entity POJOs to `Model` instances which are GWT-RPC-able.

# Class details #

## Model ##
The `Model` type is the _generic_ container for model data that is fully RPC-able.  All declared server-side entities are represented on the client as a `Model` instance.  It has the following members:

```
/**
 * The set of model properties. <br>
 * NOTE: can't mark as final for GWT RPC compatibility
 */
private/*final*/ModelPropSet props = new ModelPropSet();

/**
 * The bound entity type.
 */
private IEntityType entityType;

/**
 * The marked deleted flag. When <code>true</code>, this indicates this model
 * data is scheduled for deletion.
 */
boolean markedDeleted;

/**
 * Self-reference expressed as an {@link IModelRefProperty} which is
 * instantiated and cached upon demand.
 */
RelatedOneProperty selfRef;
```

The `props` member is a `ModelPropSet` instance which is a simple extension of `LinkedHashSet` which holds `IModelProperty` instances.

`IEntityType` is a generic way to identify an `Object` type without having to specify a `Class`.  This way, we are not declaratively "binding" a `Model` instance to a particular entity `Class`.  This affords some flexibility.

The `markedDeleted` member is a flag indicating just that and is used to tell the server that some entity data is marked for deletion.  We can't physically remove the model data from the `Model` instance since we have to re-constitute the server-side entity when it is marshaled.

At the core, `Model` instances are a collection of
```
/**
 * IModelProperty - Represents a single model property.
 * @author jpk
 */
public interface IModelProperty extends IPropertyNameProvider, IBindable, IMarshalable {

	/**
	 * @return The property type.
	 */
	PropertyType getType();

	/**
	 * Generic way to obtain the bound value for this property binding. Should
	 * only really be used for client/server marshaling.
	 * @return The raw bound value of the bound property.
	 */
	Object getValue();

	/**
	 * Generic way to set the model property value.
	 * @param value The value to set
	 * @throws IllegalArgumentException
	 */
	void setValue(Object value) throws IllegalArgumentException;
}
```
instances held in its `props` member.  These properties hold the information needed to re-constitute its server-side entity counter-part.  The implementations of `IModelProperty` are contained in the `PropertyType` enum:
```
/**
 * PropertyType - Generic and basic representation of bean properties such that
 * necessary info is captured for client/server entity/model marshaling.
 * @author jpk
 */
public enum PropertyType {

	STRING(1),
	CHAR(1 << 1),
	ENUM(1 << 2),
	BOOL(1 << 3),
	INT(1 << 4),
	LONG(1 << 5),
	FLOAT(1 << 6),
	DOUBLE(1 << 7),
	DATE(1 << 8), // date and time (java.util.Date)

	STRING_MAP(1 << 9),

	/**
	 * {@link #RELATED_ONE} corres. to a related one relation
	 */
	RELATED_ONE(1 << 10),

	/**
	 * {@link #RELATED_MANY} corres. to a related many type relation
	 */
	RELATED_MANY(1 << 11),

	/**
	 * {@link #INDEXED} corres. to an element referenced by index within a related
	 * many relation
	 */
	INDEXED(1 << 12),

	/**
	 * {@link #NESTED} corres. to a nested element.
	 */
	NESTED(1 << 13);

	/**
	 * {@link #VALUE_TYPES} corres. to non-collection and non-relational types
	 */
	private static final int VALUE_TYPES =
		STRING.flag | CHAR.flag | ENUM.flag | BOOL.flag | INT.flag | LONG.flag | FLOAT.flag | DOUBLE.flag | DATE.flag
		| STRING_MAP.flag;

	/**
	 * {@link #RELATIONAL_TYPES} corres. to those types that represent a relation
	 */
	private static final int RELATIONAL_TYPES = RELATED_ONE.flag | RELATED_MANY.flag;

	/**
	 * {@link #MODEL_TYPES} corres. to types that map to a single model ref.
	 */
	private static final int MODEL_TYPES = RELATED_ONE.flag | INDEXED.flag | NESTED.flag;

	/**
	 * Types able to format correctly on their own
	 */
	private static final int SELF_FORMATTING_TYPES = STRING.flag | ENUM.flag | INT.flag | LONG.flag | CHAR.flag;

	/**
	 * The bit flag
	 */
	private final int flag;

	private PropertyType(int flag) {
		this.flag = flag;
	}

}
```

So we have the following concrete `IModelProperty` impls:
```
// non-relational
BooleanPropertyValue
CharacterPropertyValue
DatePropertyValue
DoublePropertyValue
EnumPropertyValue
IntPropertyValue
LongPropertyValue
StringPropertyValue

// relational
RelatedOneProperty
RelatedManyProperty
IndexedProperty
```

The non-relational model property types wrap their implementing type.  For example, the `StringPropertyValue` wraps a `String` member.

The 3 relational types: `RelatedOneProperty`, `RelatedManyProperty` and `IndexedProperty` enable the full object graph of a server-side entity instance to be represented client-side as a `Model` instance.  `RelatedOneProperty` contains a single `Model` member, `RelatedManyProperty` contains a list of `Model` instances and `IndexedProperty` is like a `RelatedOneProperty` but represents a `Model` contained under a `RelatedManyProperty`.  I.e. it is indexed.

## Marshaler ##
The `Marshaler` is capable of marshaling all defined entity types to a RPC-able `Model` instance and, conversely, is capable of marshaling `Model` instances to their server-side entity counter-part.

`Marshaler` has the following public methods:
```
public Model marshalEntity(final IEntity entity, final MarshalOptions options)...

public <E extends IEntity> E marshalModel(final Model model, final Class<E> entityClass)...
```