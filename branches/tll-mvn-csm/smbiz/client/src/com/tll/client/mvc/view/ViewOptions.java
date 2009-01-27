/**
 * The Logic Lab
 * @author jpk
 * Feb 23, 2008
 */
package com.tll.client.mvc.view;

/**
 * ViewOptions
 * @author jpk
 */
public class ViewOptions {

	private boolean closable;
	private boolean minimizable;
	private boolean refreshable;
	private boolean popable;
	private boolean initiallyPopped;

	/**
	 * Constructor
	 */
	public ViewOptions() {
		super();
	}

	/**
	 * Constructor
	 * @param closable
	 * @param minimizable
	 * @param refreshable
	 * @param popable
	 * @param initiallyPopped
	 */
	public ViewOptions(boolean closable, boolean minimizable, boolean refreshable, boolean popable,
			boolean initiallyPopped) {
		super();
		this.closable = closable;
		this.minimizable = minimizable;
		this.refreshable = refreshable;
		this.popable = popable;
		this.initiallyPopped = initiallyPopped;
	}

	/**
	 * @return the minimizable
	 */
	public boolean isMinimizable() {
		return minimizable;
	}

	/**
	 * @param minimizable the minimizable to set
	 */
	public void setMinimizable(boolean minimizable) {
		this.minimizable = minimizable;
	}

	/**
	 * @return the popable
	 */
	public boolean isPopable() {
		return popable;
	}

	/**
	 * @param popable the popable to set
	 */
	public void setPopable(boolean popable) {
		this.popable = popable;
	}

	/**
	 * @return the initiallyPopped
	 */
	public boolean isInitiallyPopped() {
		return initiallyPopped;
	}

	/**
	 * @param initiallyPopped the initiallyPopped to set
	 */
	public void setInitiallyPopped(boolean initiallyPopped) {
		this.initiallyPopped = initiallyPopped;
	}

	/**
	 * @return the closable
	 */
	public boolean isClosable() {
		return closable;
	}

	/**
	 * @param closable the closable to set
	 */
	public void setClosable(boolean closable) {
		this.closable = closable;
	}

	/**
	 * @return the refreshable
	 */
	public boolean isRefreshable() {
		return refreshable;
	}

	/**
	 * @param refreshable the refreshable to set
	 */
	public void setRefreshable(boolean refreshable) {
		this.refreshable = refreshable;
	}

}
