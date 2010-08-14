package com.tll.server.marshal;

/**
 * MarshalOptions - Encapsulates marshaling options.
 * @author jpk
 */
public final class MarshalOptions {

	/**
	 * Use for unconstrained marshaling. Mainly used for testing.
	 */
	public static final MarshalOptions UNCONSTRAINED_MARSHALING = new MarshalOptions(true, -1);

	/**
	 * Use for marshaling only non-relational (rudimentary) properties. This
	 * implies a depth of zero.
	 */
	public static final MarshalOptions NON_RELATIONAL = new MarshalOptions(false, 0);

	/**
	 * Use for marshaling all non-reference type relations and properties to an
	 * unconstrained depth.
	 */
	public static final MarshalOptions NO_REFERENCES = new MarshalOptions(false, -1);

	/**
	 * Default marshal options (marshal non reference props under the root entity).
	 */
	public static final MarshalOptions DEFAULT = NON_RELATIONAL;

	/**
	 * Consider reference type relations?
	 */
	final boolean processReferenceRelations;

	/**
	 * The maximum recursion level.
	 */
	final int maxDepth;

	/**
	 * Properties the marshaler should ignore.
	 */
	final String[] ignoreProperties;

	/**
	 * Constructor
	 * @param processReferenceRelations
	 * @param maxDepth
	 * @param ignoreProperties May be <code>null</code>
	 */
	public MarshalOptions(boolean processReferenceRelations, int maxDepth, String... ignoreProperties) {
		super();
		this.processReferenceRelations = processReferenceRelations;
		this.maxDepth = maxDepth;
		this.ignoreProperties = ignoreProperties;
	}

	/**
	 * @return the processReferenceRelations
	 */
	public boolean isProcessReferenceRelations() {
		return processReferenceRelations;
	}

	/**
	 * @return the maxDepth
	 */
	public int getMaxDepth() {
		return maxDepth;
	}
}