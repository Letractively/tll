/**
 * The Logic Lab
 * @author jpk
 * Feb 16, 2008
 */
package com.tll.client.model;

/**
 * PropertyPath - Encapsulates a property path String providing convenience
 * methods for accessing its attributes.
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
	 * Assembles an indexed property name given the indexable property name and
	 * the desired index.
	 * @param indexablePropName
	 * @param index
	 * @return The indexed property name
	 */
	public static String indexed(String indexablePropName, int index) {
		return indexablePropName + LEFT_INDEX_CHAR + index + RIGHT_INDEX_CHAR;
	}

	/**
	 * Assembles an <em>unbound</em> indexed property name given the indexable
	 * property name. <br>
	 * <em>NOTE: </em>The unbound index is internally assigned and guaranteed to
	 * be unique.
	 * @param indexablePropName
	 * @return The unbound indexed property name
	 */
	public static String indexedUnbound(String indexablePropName) {
		return indexablePropName + UNBOUND_LEFT_INDEX_CHAR + unboundIndex++ + UNBOUND_RIGHT_INDEX_CHAR;
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
	 * The number of "nodes" in the property path.
	 */
	private int len;

	/**
	 * The constituent property names that make up the property path that are
	 * separated by dot chars.
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
			len = props.length;

			nodes = new String[len];

			for(int i = 0; i < len; i++) {
				String prop = props[i];
				nodes[i] = prop;
			}

			this.propPath = propPath;
		}
	}

	/**
	 * Rebuilds the property path from the {@link #nodes} array resetting the
	 * {@link #propPath} String. If a nodes element is null or empty, it is
	 * skipped in the rebuild.
	 */
	private void rebuild() {
		if(nodes == null || nodes.length == 0) {
			propPath = null;
			len = 0;
		}
		else {
			StringBuffer sb = new StringBuffer();
			for(int i = 0; i < len; ++i) {
				String node = nodes[i];
				if(node != null && node.length() > 0) {
					if(sb.length() > 0 && !(sb.charAt(sb.length() - 1) == '.')) sb.append('.');
					sb.append(node);
				}
			}
			propPath = sb.toString();
			nodes = propPath.split("\\.");
			len = nodes.length;
		}
	}

	/**
	 * @return The length, in characters, of the property path String.
	 */
	public int length() {
		return propPath == null ? 0 : propPath.length();
	}

	/**
	 * @return The number of "nodes" in the parsed property path.<br>
	 *         E.g.: <code>propA.propB[3].propC</code> has a size of 3.
	 */
	public int depth() {
		return len;
	}

	/**
	 * @return The deepest property path node index.
	 */
	public int lastNodeIndex() {
		return nodes == null ? -1 : len - 1;
	}

	/**
	 * Does this property path point to an indexed property?<br>
	 * (E.g.: <code>propA.propB[1]</code> or <code>propA.propB{1}</code>) <br>
	 * <em>NOTE: </em>nested indexed properties are not considered.
	 * @return true/false
	 */
	public boolean isIndexed() {
		if(propPath == null) return false;
		final char end = propPath.charAt(propPath.length() - 1);
		return (end == RIGHT_INDEX_CHAR || end == UNBOUND_RIGHT_INDEX_CHAR);
	}

	/**
	 * Returns the node path of the given node index.
	 * @param nodeIndex The node index
	 * @return The node path
	 */
	public String pathAt(int nodeIndex) {
		return nodes == null ? null : nodes[nodeIndex];
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
	 * Strips the indexing from the end of this property path returninng the
	 * resultant path which effectively is the parent to the indexed property.
	 * @return New property path stripped of trailing indexing or
	 *         <code>null</code> if this property path is not an indexed property.
	 */
	public PropertyPath indexedParent() {
		if(propPath == null) return null;
		int i = propPath.lastIndexOf(LEFT_INDEX_CHAR);
		if(i < 0) i = propPath.lastIndexOf(UNBOUND_LEFT_INDEX_CHAR);
		return i < 0 ? null : new PropertyPath(propPath.substring(0, i));
	}

	/**
	 * Returns the first found node index that is unbound starting at the given
	 * index.
	 * @param nodeIndex The node index where searching begins
	 * @return Node index of the nearest node that is unbound or <code>-1</code>
	 *         if no unbound node is found.
	 */
	public int nextUnboundNode(int nodeIndex) {
		int i = nodeIndex;
		do {
			final String prop = nodes[i];
			if(prop.charAt(prop.length() - 1) == UNBOUND_RIGHT_INDEX_CHAR) {
				return i;
			}
		} while(++i < len);
		return -1;
	}

	/**
	 * Calculates the offset relative to the {@link #propPath} String of a desired
	 * node index. The offset, when non-zero, points to the first character after
	 * the preceeding dot that represents that node.
	 * @param nodeIndex The node index
	 * @return The prop path offset or <code>-1</code> if no property path is
	 *         currently set.
	 */
	private int offset(int nodeIndex) {
		if(len == 0) return -1;
		if(nodeIndex == 0) return 0;
		int offset = 0;
		for(int i = 0; i < nodeIndex; ++i) {
			offset += (nodes[i].length() + 1);
		}
		return offset;
	}

	/**
	 * Calculates the node index for the given node path String presumed to be
	 * part of this property path.
	 * @param nodePath The node path String that is part of this property path.
	 * @return The corresponding node index or <code>-1</code> if not found or if
	 *         this property path has not been set.
	 */
	private int nodeIndexOf(String nodePath) {
		if(nodes == null) return -1;
		for(int i = 0; i < len; ++i) {
			if(nodePath.equals(nodes[i])) return i;
		}
		return -1;
	}

	/**
	 * Returns the ancestor property path of this property path based on a desired
	 * number of nodes to traverse up the property path starting at the last
	 * (right-most) node.
	 * @param delta The number of nodes to upward to traverse
	 * @return An ancestral property path of this property path or
	 *         <code>null</code> if the delta exceeds the depth of this property
	 *         path.
	 */
	public PropertyPath ancestor(int delta) {
		if(nodes == null || delta > len - 1) return null;
		assert propPath != null && len > 0;
		return delta == 0 ? this : new PropertyPath(propPath.substring(0, offset(len - delta) - 1));
	}

	/**
	 * Extracts a child property path from this property path starting from the
	 * top-most node traversing a desired number of child nodes.
	 * @param depth The number of nested nodes to traverse
	 * @return The nested (child) property path
	 */
	public PropertyPath nested(int depth) {
		if(nodes == null || len < 2 || depth > len - 1) return null;
		assert propPath != null && len >= 0;
		return depth == 0 ? this : new PropertyPath(propPath.substring(offset(depth)));
	}

	/**
	 * Replaces a single node in the property path.
	 * @param nodeIndex The index of the node to replace. If the given node index
	 *        exceeds the depth of this property path, this property path is
	 *        unaltered.
	 * @param prop The node replacement String. If <code>null</code> the property
	 *        path is "shortened" and will <em>not</em> contain the prop at the
	 *        given node index.
	 */
	public void replaceAt(int nodeIndex, String prop) {
		if(nodes == null || nodeIndex > len - 1) return;
		assert propPath != null && len >= 0;
		nodes[nodeIndex] = prop;
		rebuild();
	}

	/**
	 * Replaces a node path String.
	 * @param nodePath The target node path String to replace.
	 * @param replNodePath The replacement node path String.
	 * @return <code>true</code> if the replacement was successful.
	 */
	public boolean replace(String nodePath, String replNodePath) {
		if(nodes == null) return false;
		final int ni = nodeIndexOf(nodePath);
		if(ni == -1) return false;
		nodes[ni] = replNodePath;
		rebuild();
		return true;
	}

	/**
	 * Appends a property path to this property path.
	 * @param path The property path to append
	 */
	public void append(String path) {
		parse(getPropertyPath(this.propPath, path));
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
