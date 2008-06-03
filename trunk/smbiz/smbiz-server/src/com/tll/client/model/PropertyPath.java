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
 * <code>propA.&propB</code> (indicates <code>propB</code> is an "unbound"
 * property)<br>
 * <code>propA.propB{i}.propC</code> (indicates <code>propB</code> is an
 * "unbound" <em>indexed</em> property)<br>
 * <em>NOTE: </em><code>propA.&propB{i}</code> is considered "undefined"
 * (not supported).
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
	 * Used to indicate a non-idexed (related one) node in a property path is
	 * "unbound"
	 */
	private static final char UNBOUND_PREFIX_CHAR = '&';

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
	 * Removes all indexing and unbound symbols from a property node String
	 * returning the property name.
	 * @param singleProp The property path node String
	 * @return The stripped property name or <code>null</code> if the given prop
	 *         is <code>null</code>.
	 */
	private static String strip(String singleProp) {
		if(singleProp == null) return null;

		if(singleProp.charAt(0) == UNBOUND_PREFIX_CHAR) {
			singleProp = singleProp.substring(1);
		}

		int si = singleProp.indexOf(LEFT_INDEX_CHAR);
		if(si > 0) return singleProp.substring(0, si);

		si = singleProp.indexOf(UNBOUND_LEFT_INDEX_CHAR);
		if(si > 0) return singleProp.substring(0, si);

		return singleProp;
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
				int rmIndx = Integer.parseInt(sindx);
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
	 * Offsets of all occurring '.' chars in the prop path
	 */
	private int[] indices;

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
	 * @throws MalformedPropPathException When the property path is found to be
	 *         mal-formed.
	 */
	public void parse(String propPath) {
		if(propPath == null || propPath.length() == 0) {
			indices = null;
			len = 0;
			this.propPath = null;
		}
		else {

			// special case: paymentInfo.paymentData.
			final int pdi = propPath.indexOf("paymentData");

			final int len = propPath.length();
			int numDots = 0;
			for(int i = 0; i < len; ++i) {
				if(propPath.charAt(i) == '.') numDots++;
			}

			indices = new int[(pdi >= 0) ? numDots : numDots + 1];

			int i, indicesIndex = 0;
			for(i = 0; i < len; ++i) {
				if(propPath.charAt(i) == '.') {
					if(pdi == -1 || i != (pdi + 1)) {
						indices[indicesIndex++] = i;
					}
				}
			}
			this.propPath = propPath;
			this.len = i + 1;
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
	 * Extracts the single path at the desired node index.
	 * @param nodeIndex The node index
	 * @return The single node property path (i.e. no dots).
	 */
	private String getNode(int nodeIndex) {
		if(propPath == null) return null;
		if(nodeIndex == 0) {
			// first prop node
			return propPath.substring(0, indices[0]);
		}
		if(nodeIndex == indices.length) {
			// last prop node
			return propPath.substring(indices[nodeIndex - 1]);
		}
		// [assume] inner prop node
		return propPath.substring(indices[nodeIndex - 1], indices[nodeIndex]);
	}

	/**
	 * Extracts the property name at the given node index stripping indexing
	 * chars.
	 * @param nodeIndex The node index (depth into the property path).
	 * @return The property name w/o indexing symbols
	 */
	public String nameAt(int nodeIndex) {
		return strip(getNode(nodeIndex));
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
		return resolveIndex(getNode(nodeIndex));
	}

	/**
	 * Provides the property path <em>upto</em> the given node index.
	 * @param index The node index
	 * @return The sub-property path
	 */
	public PropertyPath upto(int index) {
		return new PropertyPath(propPath.substring(0, indices[index]));
	}

	/**
	 * @return The number of "nodes" in the parsed property path.<br>
	 *         E.g.: <code>propA.propB[3].propC</code> has a size of 3.
	 */
	public int size() {
		return indices == null ? 0 : indices.length + 1;
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
