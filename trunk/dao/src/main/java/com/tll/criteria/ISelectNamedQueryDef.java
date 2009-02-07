/**
 * The Logic Lab
 * @author jpk
 * Jan 21, 2009
 */
package com.tll.criteria;

/**
 * ISelectNamedQueryDef - Programmatic counterpart to entity and scalar select
 * queries declared in the persistence context (orm.xml).
 * @author jpk
 */
public interface ISelectNamedQueryDef {

	/**
	 * @return the entityType
	 */
	Class<?> getEntityType();

	/**
	 * The "base" query name w/o regard to a sorting directive in the query name.
	 * <p>
	 * 
	 * <pre>
	 * named-query name CONVENTION:
	 *   {base query name}:{sorting directive}
	 * Where:
	 *   &quot;base query name&quot;  - the name of the &quot;root&quot; query (i.e. w/o regard to sorting)
	 *   &quot;sort property&quot;    - the property alias by which the declared query is sorted
	 * </pre>
	 * @return the base query name w/o regard to a sorting directive.
	 */
	String getBaseQueryName();

	/**
	 * Is the query scalar (or entity based)?
	 * @return true/false
	 */
	boolean isScalar();

	/**
	 * Does the query support recordset-based paging?
	 * <p>
	 * If <code>true</code>, a couterpart count query is expected to be declared
	 * for this base query name having the following naming convention: <br>
	 * <code>
	 * {baseQueryName}.count
	 * </code>
	 * @return true/false
	 */
	boolean isSupportsPaging();
}
