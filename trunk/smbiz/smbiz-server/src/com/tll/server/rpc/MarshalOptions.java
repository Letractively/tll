package com.tll.server.rpc;

/**
 * MarshalOptions - Encapsulates marshaling options.
 * @author jpk
 */
public final class MarshalOptions {

	/**
	 * Use for unconstrained marshaling
	 */
	public static final MarshalOptions UNCONSTRAINED_MARSHALING = new MarshalOptions(true, true, -1);

	boolean processRelatedMany, processRelatedOne;

	int maxDepth;

	/**
	 * Constructor
	 */
	public MarshalOptions() {
		super();
	}

	/**
	 * Constructor
	 * @param processRelatedMany
	 * @param processRelatedOne
	 * @param maxDepth
	 */
	public MarshalOptions(boolean processRelatedMany, boolean processRelatedOne, int maxDepth) {
		super();
		this.processRelatedMany = processRelatedMany;
		this.processRelatedOne = processRelatedOne;
		this.maxDepth = maxDepth;
	}

	/**
	 * @return the processRelatedMany
	 */
	public boolean isProcessRelatedMany() {
		return processRelatedMany;
	}

	/**
	 * @param processRelatedMany the processRelatedMany to set
	 */
	public void setProcessRelatedMany(boolean processRelatedMany) {
		this.processRelatedMany = processRelatedMany;
	}

	/**
	 * @return the processRelatedOne
	 */
	public boolean isProcessRelatedOne() {
		return processRelatedOne;
	}

	/**
	 * @param processRelatedOne the processRelatedOne to set
	 */
	public void setProcessRelatedOne(boolean processRelatedOne) {
		this.processRelatedOne = processRelatedOne;
	}

	/**
	 * @return the maxDepth
	 */
	public int getMaxDepth() {
		return maxDepth;
	}

	/**
	 * @param maxDepth the maxDepth to set
	 */
	public void setMaxDepth(int maxDepth) {
		this.maxDepth = maxDepth;
	}

}