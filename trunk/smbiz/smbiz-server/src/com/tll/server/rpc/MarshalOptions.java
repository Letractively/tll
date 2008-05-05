package com.tll.server.rpc;

/**
 * MarshalOptions - Encapsulates marshaling options.
 * @author jpk
 */
public final class MarshalOptions {

	/**
	 * Use for unconstrained marshaling
	 */
	public static final MarshalOptions UNCONSTRAINED_MARSHALING = new MarshalOptions(true, -1);

	/**
	 * Use for marshaling entities where reference relations are NOT marshaled.
	 */
	public static final MarshalOptions NO_REFERENCE_ENTITY_MARSHALING = new MarshalOptions(false, -1);

	final boolean processReferenceRelations;

	final int maxDepth;

	/**
	 * Constructor
	 * @param processReferenceRelations
	 * @param maxDepth
	 */
	private MarshalOptions(boolean processReferenceRelations, int maxDepth) {
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