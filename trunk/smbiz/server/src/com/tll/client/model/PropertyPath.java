/**
 * The Logic Lab
 * @author jpk
 * Feb 16, 2008
 */
package com.tll.client.model;

/**
 * PropertyPath - Encapsulates a property path String providing convenience
 * methods for accessing and modifying its attributes.
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
public final class PropertyPath {

	private static final char LEFT_INDEX_CHAR = '[';

	private static final char RIGHT_INDEX_CHAR = ']';

	/**
	 * Indicates in conjunction with {@link #UNBOUND_RIGHT_INDEX_CHAR} that an
	 * indexed property path node is unbound.
	 */
	private static final char UNBOUND_LEFT_INDEX_CHAR = '{';

	private static final char UNBOUND_RIGHT_INDEX_CHAR = '}';

	/**
	 * Internally maintain unique ubound indexes.
	 */
	private static int unboundIndex = 0;

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
	 * Calculates the property path. Never returns <code>null</code>. Support for
	 * <code>null</code> or empty is considered for all arguments.
	 * <p>
	 * FORMAT: property path = <code>parentPropPath</code> + '.' +
	 * <code>propName</code>
	 * @param parentPropPath Assumed to NOT end in a dot. May be <code>null</code>
	 *        .
	 * @param propName Assumed to NOT have prefixing/suffixing dots. May be
	 *        <code>null</code>.
	 * @return The calculated property path.
	 */
	public static String getPropertyPath(String parentPropPath, String propName) {
		return (parentPropPath == null || parentPropPath.length() < 1) ? (propName == null ? "" : propName)
				: (propName == null || propName.length() < 1) ? parentPropPath : parentPropPath + '.' + propName;
	}

	/**
	 * Creates the index token given the numeric index and whether or not is is to
	 * be bound or un-bound.
	 * @param index The numeric index
	 * @param isUnbound Bound or un-bound index?
	 * @return The index token.
	 */
	private static String indexToken(int index, boolean isUnbound) {
		return (isUnbound ? UNBOUND_LEFT_INDEX_CHAR : LEFT_INDEX_CHAR) + Integer.toString(index)
				+ (isUnbound ? UNBOUND_RIGHT_INDEX_CHAR : RIGHT_INDEX_CHAR);
	}

	/**
	 * Assembles an indexed property name given the indexable property name and
	 * the desired index.
	 * @param indexablePropName
	 * @param index The numeric index
	 * @param isUnbound Index as bound or un-bound?
	 * @return The indexed property name
	 */
	public static String index(String indexablePropName, int index, boolean isUnbound) {
		return indexablePropName + indexToken(index, isUnbound);
	}

	/**
	 * Assembles an <em>unbound</em> indexed property name given the indexable
	 * property name. The numeric index used is guaranteed to be unique.
	 * @param indexablePropName
	 * @return The unbound indexed property name
	 */
	public static String indexUnbound(String indexablePropName) {
		return index(indexablePropName, ++unboundIndex, true);
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
	 * The property path buffer.
	 */
	private StringBuilder buf;

	/**
	 * The number of "nodes" in the property path.
	 */
	private int len;

	/**
	 * Constructor
	 */
	public PropertyPath() {
		super();
	}

	/**
	 * Constructor
	 * @param propPath A property path String.
	 */
	public PropertyPath(String propPath) {
		this();
		parse(propPath);
	}

	/**
	 * Constructor
	 * @param parentPropPath A parent property path
	 * @param propPath A property path that is appended to the given parent
	 *        property path
	 */
	public PropertyPath(String parentPropPath, String propPath) {
		this();
		parse(getPropertyPath(parentPropPath, propPath));
	}

	/**
	 * Constructor
	 * @param parentPropPath A parent property path
	 * @param index The numeric index
	 * @param isUnbound
	 */
	public PropertyPath(String parentPropPath, int index, boolean isUnbound) {
		this();
		parse(index(parentPropPath, index, isUnbound));
	}

	/**
	 * Parses a property path.
	 * @param propPath The property path String to be parsed. When
	 *        <code>null</code> or empty, en empty property path is set.
	 */
	public void parse(String propPath) {
		if(propPath == null || propPath.length() == 0) {
			len = 0;
			this.buf = null;
		}
		else {
			len = propPath.split("\\.").length;
			this.buf = new StringBuilder(propPath);
		}
	}

	/**
	 * @return The atomic property "nodes" that makeup this property path. <br>
	 *         E.g.: <code>propA.propB</code> returns
	 *         <code>{ propA, propB }</code>
	 */
	public String[] nodes() {
		return buf == null ? null : buf.toString().split("\\.");
	}

	/**
	 * @return The length, in characters, of the property path String.
	 */
	public int length() {
		return buf == null ? 0 : buf.length();
	}

	/**
	 * @return The number of "nodes" in the parsed property path.<br>
	 *         E.g.: <code>propA.propB[3].propC</code> has a depth of 3.
	 */
	public int depth() {
		return len;
	}

	/**
	 * Does this property path point to an indexed property?<br>
	 * (E.g.: <code>propA.propB[1]</code> or <code>propA.propB{1}</code>) <br>
	 * <em>NOTE: </em>nested indexed properties are not considered.
	 * @return true/false
	 */
	public boolean isIndexed() {
		if(buf == null) return false;
		final char end = buf.charAt(buf.length() - 1);
		return (end == RIGHT_INDEX_CHAR || end == UNBOUND_RIGHT_INDEX_CHAR);
	}

	/**
	 * Indexes the property path by simply appending the given index surrounded by
	 * bound index chars. <br>
	 * <em>NOTE: </em>No checking for existing indexing is performed.
	 * @param index The index num
	 */
	public void index(int index) {
		if(buf != null) buf.append(indexToken(index, true));
	}

	/**
	 * Indexes the property path by appending an unbound index token
	 * <em>NOTE: </em>No checking for existing indexing is performed.
	 */
	public void indexUnbound() {
		if(buf != null) buf.append(indexToken(++unboundIndex, true));
	}

	/**
	 * Does this property path point to an <em>unbound</em> indexed property?<br>
	 * (E.g.: <code>propA.propB{1}</code>) <br>
	 * <em>NOTE: </em>nested indexed properties are not considered.
	 * @return true/false
	 */
	public boolean isUnboundIndexed() {
		if(buf == null) return false;
		final char end = buf.charAt(buf.length() - 1);
		return (end == UNBOUND_RIGHT_INDEX_CHAR);
	}

	/**
	 * Returns the node path of the given node index. <br>
	 * @param nodeIndex The node index
	 * @return The node path
	 * @throws ArrayIndexOutOfBoundsException When the node index is less than 0
	 *         or greater than number of nodes of this property path.
	 */
	public String pathAt(int nodeIndex) throws ArrayIndexOutOfBoundsException {
		if(buf == null) return null;
		if(nodeIndex < 0 || nodeIndex > len - 1) throw new ArrayIndexOutOfBoundsException();
		int cni = 0;
		StringBuilder sub = new StringBuilder();
		for(int i = 0; i < buf.length(); ++i) {
			if(buf.charAt(i) == '.') {
				if(cni == nodeIndex) {
					// done
					break;
				}
				++cni;
			}
			else if(cni == nodeIndex) {
				sub.append(buf.charAt(i));
			}
		}
		return sub.toString();
	}

	/**
	 * Calculates the String-wise index from a given node index.
	 * @param nodeIndex The node index
	 * @return The corres. String index of this property path.
	 */
	private int bufIndex(int nodeIndex) {
		if(nodeIndex == 0) return 0;
		if(buf != null) {
			int cni = 0;
			for(int i = 0; i < buf.length(); ++i) {
				if(buf.charAt(i) == '.') {
					if(++cni == nodeIndex) return i + 1;
				}
			}
		}
		return -1;
	}

	/**
	 * Extracts the property name at the given node index stripping indexing
	 * @param nodeIndex The node index (depth into the property path).
	 * @return The property name w/o indexing symbols
	 */
	public String nameAt(int nodeIndex) {
		return buf == null ? null : strip(pathAt(nodeIndex));
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
		return buf == null ? -1 : resolveIndex(pathAt(nodeIndex));
	}

	/**
	 * Strips the indexing from the end of this property path returninng the
	 * resultant path which effectively is the parent to the indexed property.
	 * @return New property path stripped of trailing indexing or
	 *         <code>null</code> if this property path is not an indexed property.
	 */
	public PropertyPath indexedParent() {
		if(buf.length() > 4 && isIndexed()) {
			for(int i = buf.length() - 3; i >= 0; --i) {
				final char c = buf.charAt(i);
				if(c == LEFT_INDEX_CHAR || c == UNBOUND_LEFT_INDEX_CHAR) {
					return new PropertyPath(buf.substring(0, i));
				}
			}
		}
		return null;
	}

	/**
	 * Returns the first found node index that is indexed starting at the given
	 * index.
	 * @param nodeIndex The node index where searching starts
	 * @param isUnbound Search for bound or un-bound indexed nodes?
	 * @return The index of the nearest node that is indexed or <code>-1</code> if
	 *         no unbound node is found.
	 */
	public int nextIndexedNode(int nodeIndex, boolean isUnbound) {
		int i = nodeIndex;
		do {
			final String prop = pathAt(i);
			if(prop.charAt(prop.length() - 1) == UNBOUND_RIGHT_INDEX_CHAR) {
				return i;
			}
		} while(++i < len);
		return -1;
	}

	/**
	 * Calculates the node index for the given node path String presumed to be
	 * part of this property path.
	 * @param nodePath The node path String that is part of this property path.
	 * @return The corresponding node index or <code>-1</code> if not found or if
	 *         this property path has not been set.
	 */
	private int nodeIndexOf(String nodePath) {
		if(buf == null) return -1;
		for(int i = 0; i < len; ++i) {
			if(nodePath.equals(pathAt(i))) return i;
		}
		return -1;
	}

	/**
	 * Returns the ancestor property path of this property path based on a desired
	 * number of nodes to traverse up the property path starting at the last
	 * (right-most) node.
	 * @param delta The number of nodes to traverse upward
	 * @return An ancestral property path of this property path or
	 *         <code>null</code> if the delta exceeds the depth of this property
	 *         path.
	 */
	public PropertyPath ancestor(int delta) {
		if(buf == null || delta > len - 1) return null;
		assert buf != null && len > 0;
		return delta == 0 ? this : new PropertyPath(buf.substring(0, bufIndex(len - delta) - 1));
	}

	/**
	 * Extracts a child property path from this property path starting from the
	 * top-most node traversing a desired number of child nodes.
	 * @param depth The number of nested nodes to traverse
	 * @return The nested (child) property path
	 */
	public PropertyPath nested(int depth) {
		if(buf == null || len < 2 || depth > len - 1) return null;
		assert buf != null && len >= 0;
		return depth == 0 ? this : new PropertyPath(buf.substring(bufIndex(depth)));
	}

	/**
	 * Replaces a single node in the property path.
	 * @param nodeIndex The index of the node to replace. If the given node index
	 *        exceeds the depth of this property path, this property path is
	 *        unaltered.
	 * @param prop The node replacement String. If <code>null</code> the property
	 *        path is "shortened" and will <em>not</em> contain the prop at the
	 *        given node index.
	 * @return <code>true</code> if the replacement was successful.
	 */
	public boolean replaceAt(int nodeIndex, String prop) {
		if(buf == null || nodeIndex > len - 1) return false;
		assert buf != null && len >= 0;
		int i = bufIndex(nodeIndex);
		if(i == -1) return false;
		int j;
		if(nodeIndex == len - 1) {
			j = buf.length();
		}
		else {
			j = bufIndex(nodeIndex + 1) - 1;
		}
		if(prop == null) {
			// remove the node
			buf.replace(i, j + 1, "");
			len--;
			assert len >= 0;
		}
		else {
			buf.replace(i, j, prop);
		}
		return true;
	}

	/**
	 * Replaces a node path String.
	 * @param nodePath The target node path String to replace.
	 * @param replNodePath The replacement node path String.
	 * @return <code>true</code> if the replacement was successful.
	 */
	public boolean replace(String nodePath, String replNodePath) {
		if(buf == null) return false;
		final int ni = nodeIndexOf(nodePath);
		return ni == -1 ? false : replaceAt(ni, replNodePath);
	}

	/**
	 * Appends a property path to this property path.
	 * @param path The property path to append
	 */
	public void append(String path) {
		parse(getPropertyPath(this.buf.toString(), path));
	}

	/**
	 * Appends an indexed property path to this property path. <br>
	 * E.g.: A path of <code>parameters</code> and an index of <code>2</code>
	 * would append: <code>parameters[2]</code>.
	 * @param path The indexable property name
	 * @param index The index
	 */
	public void append(String path, int index) {
		append(path, index, false);
	}

	/**
	 * Appends a bound or un-bound indexed property path to this property path. <br>
	 * E.g.: A path of <code>parameters</code> and an index of <code>2</code>
	 * would append: <code>parameters[2]</code>.
	 * @param path The indexable property name
	 * @param index The index
	 * @param isUnbound Shall the appended indexed path be bound or un-bound?
	 */
	public void append(String path, int index, boolean isUnbound) {
		append(index(path, index, isUnbound));
	}

	@Override
	public boolean equals(Object obj) {
		if(this == obj) return true;
		if(obj == null) return false;
		if(getClass() != obj.getClass()) return false;
		final PropertyPath other = (PropertyPath) obj;
		if(buf == null) {
			if(other.buf != null) return false;
		}
		else if(!buf.equals(other.buf)) return false;
		return true;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((buf == null) ? 0 : buf.hashCode());
		return result;
	}

	@Override
	public String toString() {
		return buf == null ? null : buf.toString();
	}
}
