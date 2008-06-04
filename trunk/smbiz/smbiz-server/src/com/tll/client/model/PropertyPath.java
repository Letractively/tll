/**
 * The Logic Lab
 * @author jpk
 * Feb 16, 2008
 */
package com.tll.client.model;

/**
 * PropertyPath - Represents a valid property path.
 * <p>
 * A valid property path is: <br>
 * <ol>
 * <li>Standard OGNL format.<br>
 * E.g.: <code>propA.propB.propC[i].propD</code>
 * <li>Unbound format:<br>
 * <code>propA.propB{i}.propC</code> (indicates <code>propB</code> is an
 * "unbound" <em>indexed</em> property)<br>
 * <em>NOTE: </em>Currently, only indexed properties may be marked as unbound.
 * </ol>
 * @author jpk
 */
public class PropertyPath {

	private static final char LEFT_INDEX_CHAR = '[';
	private static final char RIGHT_INDEX_CHAR = ']';

	/**
	 * Indicates in conjunction with {@link #UNBOUND_RIGHT_INDEX_CHAR} that an
	 * indexed property path node is unbound.
	 */
	private static final char UNBOUND_LEFT_INDEX_CHAR = '{';

	private static final char UNBOUND_RIGHT_INDEX_CHAR = '}';

	/**
	 * Chains the given arguments together to form the corresponding property
	 * path. Support for <code>null</code> or empty is considered for all
	 * arguments.
	 * @param grandParentPropPath The parent property path to
	 *        <code>parentPropPath</code>.
	 * @param parentPropPath The parent property path to <code>propName</code>.
	 * @param propName The property name.
	 * @return The calculated property path.
	 */
	public static String getPropertyPath(String grandParentPropPath, String parentPropPath, String propName) {
		return getPropertyPath(getPropertyPath(grandParentPropPath, parentPropPath), propName);
	}

	/**
	 * Calculates the property path. Never returns <code>null</code>. Support
	 * for <code>null</code> or empty is considered for all arguments.
	 * <p>
	 * FORMAT: property path = <code>parentPropPath</code> + '.' +
	 * <code>propName</code>
	 * @param parentPropPath Assumed to NOT end in a dot. May be <code>null</code>.
	 * @param propName Assumed to NOT have prefixing/suffixing dots. May be
	 *        <code>null</code>.
	 * @return The calculated property path.
	 */
	public static String getPropertyPath(String parentPropPath, String propName) {
		return (parentPropPath == null || parentPropPath.length() < 1) ? (propName == null ? "" : propName)
				: (propName == null || propName.length() < 1) ? parentPropPath : parentPropPath + '.' + propName;
	}

	/**
	 * Assembles an indexed property name given a the indexable property name and
	 * the desired index.
	 * @param indexablePropName
	 * @param index
	 * @return The indexed property name
	 */
	public static String indexed(String indexablePropName, int index) {
		return indexablePropName + LEFT_INDEX_CHAR + index + RIGHT_INDEX_CHAR;
	}

	/**
	 * Assembles an <em>unbound</em> indexed property name given a the indexable
	 * property name and the desired index.
	 * @param indexablePropName
	 * @param index
	 * @return The unbound indexed property name
	 */
	public static String indexedUnbound(String indexablePropName, int index) {
		return indexablePropName + UNBOUND_LEFT_INDEX_CHAR + index + UNBOUND_RIGHT_INDEX_CHAR;
	}

	/**
	 * Removes all indexing and unbound symbols from a property node String
	 * returning the property name.
	 * @param prop The property path node String
	 * @return The stripped property name or <code>null</code> if the given prop
	 *         is <code>null</code>.
	 */
	private static String strip(String prop) {
		if(prop == null) return null;

		int si = prop.indexOf(LEFT_INDEX_CHAR);
		if(si > 0) return prop.substring(0, si);

		si = prop.indexOf(UNBOUND_LEFT_INDEX_CHAR);
		if(si > 0) return prop.substring(0, si);

		return prop;
	}

	/**
	 * Resolves the numeric index from an indexed property path node String (no
	 * dots).
	 * @param path The single node property path
	 * @return The resolved index or <code>-1</code> if the given prop is not
	 *         indexed.
	 * @throws MalformedPropPathException When the index is non numeric or
	 *         negative.
	 */
	private static int resolveIndex(String path) throws MalformedPropPathException {
		if(path == null) return -1;
		int bi = path.indexOf(LEFT_INDEX_CHAR), ebi = -1;
		if(bi < 0) {
			bi = path.indexOf(UNBOUND_LEFT_INDEX_CHAR);
			if(bi > 0) {
				ebi = path.indexOf(UNBOUND_RIGHT_INDEX_CHAR);
			}
		}
		else {
			ebi = path.indexOf(RIGHT_INDEX_CHAR);
		}
		if(bi > 0) {
			// indexed property prop name
			final String sindx = path.substring(bi + 1, ebi);
			try {
				final int rmIndx = Integer.parseInt(sindx);
				if(rmIndx < 0) {
					throw new MalformedPropPathException("Negative index in property path: " + path);
				}
				return rmIndx;
			}
			catch(NumberFormatException nfe) {
				throw new MalformedPropPathException("Invalid index '" + sindx + "' in property path: " + path);
			}
		}
		return -1;
	}

	/**
	 * The property path in String form. (That which is parsed).
	 */
	private String propPath;

	/**
	 * The length of the property path String.
	 */
	private int len;

	/**
	 * The atomic nodes of the property path dictated by the indices of dot chars.
	 */
	private String[] nodes;

	/**
	 * Constructor
	 */
	public PropertyPath() {
		super();
	}

	/**
	 * Constructor
	 * @param propPath
	 */
	public PropertyPath(String propPath) {
		this();
		parse(propPath);
	}

	/**
	 * Parses a property path.
	 * @param propPath The property path String to be parsed. When
	 *        <code>null</code> or empty, en empty property path is set.
	 */
	public void parse(String propPath) {
		if(propPath == null || propPath.length() == 0) {
			nodes = null;
			len = 0;
			this.propPath = null;
		}
		else {
			final String[] props = propPath.split("\\.");
			len = nodes.length;

			// special case: paymentInfo.paymentData.
			final boolean hasPaymentData = (propPath.indexOf("paymentData") >= 0);

			nodes = new String[hasPaymentData ? len - 1 : len];

			for(int i = 0; i < len; i++) {
				String prop = props[i];
				if(i < len - 1 && "paymentData".equals(prop)) {
					nodes[i] = prop + '.' + props[++i];
				}
				else {
					nodes[i] = prop;
				}
			}
			if(hasPaymentData) len--;

			this.propPath = propPath;
		}
	}

	/**
	 * Does this property path point to an indexed property?<br>
	 * (E.g.: <code>propA.propB[1]</code> or <code>propA.propB{1}</code>)
	 * <br>
	 * <em>NOTE: </em>nested indexed properties are not considered.
	 * @return true/false
	 */
	public boolean isIndexed() {
		if(propPath == null) return false;
		final char end = propPath.charAt(len - 1);
		return (end == LEFT_INDEX_CHAR || end == UNBOUND_LEFT_INDEX_CHAR);
	}

	/**
	 * Extracts the property name at the given node index stripping indexing
	 * chars.
	 * @param nodeIndex The node index (depth into the property path).
	 * @return The property name w/o indexing symbols
	 */
	public String nameAt(int nodeIndex) {
		return nodes == null ? null : strip(nodes[nodeIndex]);
	}

	/**
	 * Extracts the resolved index of an indexed property at the given node index.
	 * @param nodeIndex The node index (depth into the property path).
	 * @return The resolved numeric index or <code>-1</code> if the property is
	 *         not indexed at the given node index.
	 * @throws MalformedPropPathException When the index is non-numeric or
	 *         negative.
	 */
	public int indexAt(int nodeIndex) throws MalformedPropPathException {
		return nodes == null ? -1 : resolveIndex(nodes[nodeIndex]);
	}

	/**
	 * Returns the first found node index that is unbound starting at the given
	 * index.
	 * @param index The node index where searching begins
	 * @return Node index of the nearest node that is unbound or <code>-1</code>
	 *         if no unbound node is found.
	 */
	public int nextUnboundNode(int index) {
		int i = index;
		do {
			final String prop = nodes[i];
			if(prop.charAt(prop.length() - 1) == UNBOUND_RIGHT_INDEX_CHAR) {
				return i;
			}
		} while(++i < len);
		return -1;
	}

	/**
	 * Provides the property path <em>upto</em> the given node index.
	 * @param index The node index
	 * @return The sub-property path
	 */
	public PropertyPath upto(int index) {
		if(nodes == null) return null;
		if(index == 0) return new PropertyPath(nodes[0]);
		if(index == len - 1) return this;

		assert propPath != null;
		StringBuffer sb = new StringBuffer(propPath.length());
		for(int i = 0; i <= index; ++i) {
			sb.append(nodes[i]);
			if(i < index) sb.append('.');
		}
		return new PropertyPath(sb.toString());
	}

	/**
	 * @return The number of "nodes" in the parsed property path.<br>
	 *         E.g.: <code>propA.propB[3].propC</code> has a size of 3.
	 */
	public int size() {
		return len;
	}

	@Override
	public boolean equals(Object obj) {
		if(this == obj) return true;
		if(obj == null) return false;
		if(getClass() != obj.getClass()) return false;
		final PropertyPath other = (PropertyPath) obj;
		if(propPath == null) {
			if(other.propPath != null) return false;
		}
		else if(!propPath.equals(other.propPath)) return false;
		return true;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((propPath == null) ? 0 : propPath.hashCode());
		return result;
	}

	@Override
	public String toString() {
		return propPath;
	}
}
