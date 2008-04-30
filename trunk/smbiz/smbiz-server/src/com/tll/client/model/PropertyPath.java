/**
 * The Logic Lab
 * @author jpk
 * Feb 16, 2008
 */
package com.tll.client.model;

/**
 * PropertyPath - Helper class for walking a property path and extracting
 * certain properties therein that are needed when resolving them.
 * @author jpk
 */
public class PropertyPath {

	/**
	 * Node - Represents a single "node" in a property path string. Handles
	 * parsing indexed paths.
	 * @author jpk
	 */
	private static class Node {

		/**
		 * The unaltered (qualified) sub-path.
		 */
		String path;
		String name;
		int index = -1;

		Node() {
		}

		Node(String path) throws MalformedPropPathException {
			set(path);
		}

		/**
		 * Sets the name and index properties depending on whether or not the given
		 * path is indexed.
		 * @param path The possibly indexed (E.g.: path[5]) path
		 * @throws PropertyPathException When the path is found to be mal-formed.
		 */
		void set(String path) throws MalformedPropPathException {
			assert path != null;
			int rmIndx = -1; // if this prop path element contains []
			int bi = path.indexOf('[');
			if(bi > 0) {
				// indexed property: extract the prop name and index
				String rmProp = path.substring(0, bi);
				int ebi = path.indexOf(']');
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

		String getName() {
			return name;
		}

		int getIndex() {
			return index;
		}

		void setIndex(int index) {
			this.index = index;
		}
	}

	private String propPath;
	private Node[] nodes;
	private int len;

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
	 * @param propPath
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
			boolean hasPaymentData = (propPath.indexOf("paymentData.") >= 0);

			String[] props = propPath.split("\\.");
			len = props.length;
			nodes = new Node[hasPaymentData ? len - 1 : len];
			for(int i = 0; i < len; i++) {
				String prop = props[i];
				if(i < len - 1 && "paymentData".equals(prop)) {
					nodes[i] = new Node(prop + '.' + props[++i]);
				}
				else {
					nodes[i] = new Node(prop);
				}
			}
			if(hasPaymentData) len--;
		}
		assert nodes != null;
		this.propPath = propPath;
	}

	private Node nodeAt(int pos) {
		return nodes[pos];
	}

	public boolean isEmpty() {
		return nodes.length == 0;
	}

	/**
	 * Provides the name with OUT any indexing related chars.
	 * @param pos
	 * @return The unqualified name
	 */
	public String unqualifiedPropNameAt(int pos) {
		return nodeAt(pos).name;
	}

	/**
	 * Provides the name WITH indexing related chars.
	 * @param pos
	 * @return The qualified name
	 */
	public String propNameAt(int pos) {
		return nodeAt(pos).path;
	}

	/**
	 * @return The ending property path (with indexing qualifications).
	 */
	public String endPropPath() {
		return nodeAt(len - 1).path;
	}

	/**
	 * @return The ending property name (no indexing qualifications).
	 */
	public String endPropName() {
		return nodeAt(len - 1).name;
	}

	public int indexAt(int pos) {
		return nodeAt(pos).index;
	}

	public boolean isIndexedAt(int pos) {
		return indexAt(pos) >= 0;
	}

	public boolean hasNext(int pos) {
		return (pos < len - 1);
	}

	/*
	public boolean hasOneMore(int pos) {
		return (pos + 2 == len);
	}

	public boolean hasTwoMore(int pos) {
		return (pos + 3 == len);
	}
	*/

	public boolean atEnd(int pos) {
		return pos == len - 1;
	}

	public int size() {
		return len;
	}

	@Override
	public String toString() {
		return propPath;
	}
}
