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
 * <em>related one</em> property)<br>
 * <code>propA.propB{i}.propC</code> (indicates <code>propB</code> is an
 * "unbound" <em>indexed</em> property)
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
	 * Node - Represents a single "node" in a property path string. Handles
	 * parsing of indexed paths.
	 * @author jpk
	 */
	public static final class Node {

		/**
		 * The full property node String with potential indexing chars.
		 */
		String path;

		/**
		 * The property node name stripped of potential indexing characters.
		 */
		String name;

		/**
		 * The resolved index (for indexed properties only).
		 */
		int index = -1;

		/**
		 * Flag to indicate whether this node is "unbound" meaning there is no
		 * corresonding model structure.
		 */
		boolean unbound;

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
			int bi = path.indexOf(LEFT_INDEX_CHAR), ebi = -1;
			if(bi < 0) {
				bi = path.indexOf(UNBOUND_LEFT_INDEX_CHAR);
				if(bi > 0) {
					this.unbound = true;
					ebi = path.indexOf(UNBOUND_RIGHT_INDEX_CHAR);
				}
			}
			else {
				ebi = path.indexOf(RIGHT_INDEX_CHAR);
			}
			if(bi > 0) {
				// indexed property prop name
				String rmProp = path.substring(0, bi);
				String sindx = path.substring(bi + 1, ebi);
				int rmIndx;
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
				// non-indexed (related one) prop name
				if(path.charAt(0) == UNBOUND_PREFIX_CHAR) {
					this.unbound = true;
					this.name = path.substring(1);
				}
				else {
					this.name = path;
				}
				this.index = -1;
			}
			this.path = path;
		}

		public String getName() {
			return name;
		}

		public int getIndex() {
			return index;
		}

		/**
		 * Is this property path node "unbound"?
		 */
		protected boolean isUnbound() {
			return unbound;
		}

		public Node copy() {
			Node c = new Node();
			c.index = index;
			c.name = name;
			c.path = path;
			c.unbound = unbound;
			return c;
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
	private boolean unbound;

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
			unbound = false;

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
				if(node.isUnbound()) unbound = true;
			}
			if(hasPaymentData) len--;
		}
		assert nodes != null;
		this.propPath = propPath;
	}

	public Node nodeAt(int index) {
		return nodes[index];
	}

	public Node lastNode() {
		return nodes == null ? null : nodes[len - 1];
	}

	/**
	 * Provides the property path <em>upto</em> the given node index.
	 * @param index The node index
	 * @return The sub-property path
	 */
	public PropertyPath upto(int index) {
		// NOTE: no array bounds checking for speed
		if(nodes == null) return null;
		String path;
		if(index == 0) {
			path = nodes[0].path;
		}
		else if(index == (len - 1)) {
			path = propPath;
		}
		else {
			StringBuffer sb = new StringBuffer(propPath.length());
			for(int i = 0; i <= index; ++i) {
				sb.append(nodes[i].path);
				if(i < index) sb.append('.');
			}
			path = sb.toString();
		}
		try {
			return new PropertyPath(path);
		}
		catch(MalformedPropPathException e) {
			// shouldn't happen bro!
			throw new IllegalStateException();
		}
	}

	/**
	 * @return The number of "nodes" in the parsed property path.<br>
	 *         E.g.: <code>propA.propB[3].propC</code> has a size of 3.
	 */
	public int size() {
		return len;
	}

	/**
	 * Searches for a nearest unbound property path node starting at the given
	 * node index.
	 * @param nodeIndex The node index at which searching starts
	 * @return The first found node index that is unbound or <code>-1</code> if
	 *         none found.
	 */
	public int getNextUnboundNode(int nodeIndex) {
		if(unbound) {
			assert nodes != null;
			for(int i = nodeIndex; i < len; ++i) {
				if(nodes[i].unbound) return i;
			}
		}
		return -1;
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
