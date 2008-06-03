/**
 * The Logic Lab
 * @author jpk
 * Feb 16, 2008
 */
package com.tll.client.model;

/**
 * PropertyPath - Helper class for walking a property path and extracting
 * properties therein.
 * @author jpk
 */
public class PropertyPath {

	private static final char LEFT_INDEX_CHAR = '[';
	private static final char RIGHT_INDEX_CHAR = ']';

	private static final char NEW_LEFT_INDEX_CHAR = '{';
	private static final char NEW_RIGHT_INDEX_CHAR = '}';

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
	 * Node - Represents a single "node" in a property path string. Handles
	 * parsing of indexed paths.
	 * @author jpk
	 */
	public static final class Node {

		/**
		 * The unaltered (qualified) sub-path.
		 */
		String path;

		/**
		 * The property name stripped of potential indexing characters ([]).
		 */
		String name;

		/**
		 * The resolved index (for indexed properties only).
		 */
		int index = -1;

		/**
		 * Is this a new indexed property? I.e., does it have curly braces ({}) in
		 * place of the standard square braces ([])? Relevant only for indexed
		 * property nodes.
		 */
		boolean newIndex;

		Node() {
		}

		/**
		 * Constructor
		 * @param path
		 * @throws MalformedPropPathException
		 */
		Node(String path) throws MalformedPropPathException {
			set(path);
		}

		/**
		 * Sets the name and index properties depending on whether or not the given
		 * path is indexed.
		 * @param path Atomic sub-path (no dots) that may be indexed ([]) at the end
		 *        of the given path String
		 * @throws PropertyPathException When the path is found to be mal-formed.
		 */
		void set(String path) throws MalformedPropPathException {
			assert path != null;
			int rmIndx = -1; // if this prop path element contains []
			int bi = path.indexOf(LEFT_INDEX_CHAR), ebi = -1;
			if(bi < 0) {
				bi = path.indexOf(NEW_LEFT_INDEX_CHAR);
				if(bi > 0) {
					this.newIndex = true;
					ebi = path.indexOf(NEW_RIGHT_INDEX_CHAR);
				}
			}
			else {
				ebi = path.indexOf(RIGHT_INDEX_CHAR);
			}
			if(bi > 0) {
				// indexed property: extract the prop name and index
				String rmProp = path.substring(0, bi);
				String sindx = path.substring(bi + 1, ebi);
				try {
					rmIndx = Integer.parseInt(sindx);
					if(rmIndx < 0) {
						throw new MalformedPropPathException("Negative index in property path: " + path);
					}
				}
				catch(NumberFormatException nfe) {
					throw new MalformedPropPathException("Invalid index '" + sindx + "' in property path: " + path);
				}
				this.name = rmProp;
				this.index = rmIndx;
			}
			else {
				this.name = path;
				this.index = -1;
			}
			this.path = path;
		}

		public String getPath() {
			return path;
		}

		public String getName() {
			return name;
		}

		public int getIndex() {
			return index;
		}

		public boolean isNewIndex() {
			return newIndex;
		}
	}

	/**
	 * The property path in String form. (That which is parsed).
	 */
	private String propPath;

	/**
	 * Array of atomic sub-paths that together make up the entire property path.
	 */
	private Node[] nodes;

	/**
	 * The number of atomic nodes in the property path.
	 */
	private int len;

	/**
	 * Does the property path have a node that represents a "new" index?
	 */
	private boolean newIndex;

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
	public PropertyPath(String propPath) throws MalformedPropPathException {
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
	public void parse(String propPath) throws MalformedPropPathException {
		if(propPath == null || propPath.length() == 0) {
			nodes = new Node[0];
			len = 0;
		}
		else {
			// special case: paymentInfo.paymentData.
			final boolean hasPaymentData = (propPath.indexOf("paymentData.") >= 0);

			final String[] props = propPath.split("\\.");

			len = props.length;
			nodes = new Node[hasPaymentData ? len - 1 : len];
			newIndex = false;

			for(int i = 0; i < len; i++) {
				String prop = props[i];
				Node node;
				if(i < len - 1 && "paymentData".equals(prop)) {
					node = new Node(prop + '.' + props[++i]);
				}
				else {
					node = new Node(prop);
				}
				nodes[i] = node;
				if(node.isNewIndex()) newIndex = true;
			}
			if(hasPaymentData) len--;
		}
		assert nodes != null;
		this.propPath = propPath;
	}

	public Node[] getNodes() {
		return nodes;
	}

	/**
	 * @return The number of "nodes" in the parsed property path.<br>
	 *         E.g.: <code>propA.propB[3].propC</code> has a size of 3.
	 */
	public int size() {
		return len;
	}

	/**
	 * Convenience method that provides the last node in the property path
	 * @return The last property name in the property path <em>stripped</em> of
	 *         indexing symbols.
	 */
	public Node lastNode() {
		return nodes == null ? null : nodes[len - 1];
	}

	/**
	 * Searches for a new index property path node starting at the given node
	 * index.
	 * @param nodeIndex The node index at which searching starts
	 * @return The first found Node that is indexed as new or <code>null</code>
	 *         if none found.
	 */
	public Node getNextNewIndexNode(int nodeIndex) {
		if(newIndex) {
			assert nodes != null;
			for(int i = nodeIndex; i < len; ++i) {
				if(nodes[i].newIndex) return nodes[i];
			}
		}
		return null;
	}

	@Override
	public String toString() {
		return propPath;
	}
}
