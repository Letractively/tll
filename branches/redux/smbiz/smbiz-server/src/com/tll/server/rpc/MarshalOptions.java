package com.tll.server.rpc;

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
	 * Consider reference type relations?
	 */
	final boolean processReferenceRelations;

	/**
	 * The maximum recursion level.
	 */
	final int maxDepth;

	/**
	 * Constructor
	 * @param processReferenceRelations
	 * @param maxDepth
	 */
	public MarshalOptions(boolean processReferenceRelations, int maxDepth) {
		super();
		this.processReferenceRelations = processReferenceRelations;
		this.maxDepth = maxDepth;
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