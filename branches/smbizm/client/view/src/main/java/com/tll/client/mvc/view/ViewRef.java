/**
 * The Logic Lab
 * @author jpk
 * @since Mar 24, 2009
 */
package com.tll.client.mvc.view;

/**
 * ViewRef - A Stand-alone ref to a view at runtime able to reconstitute the
 * view to which it refers. This differs from {@link ViewKey} in that we retain
 * the {@link IViewInitializer} which is necessary for view initialization.
 * @author jpk
 */
public final class ViewRef extends AbstractViewKeyProvider {

	private final IViewInitializer init;
	private final ViewOptions options;
	private final String shortViewName, longViewName;

	/**
	 * Constructor
	 * @param init the view initializer
	 * @param options the view options
	 * @param shortViewName
	 * @param longViewName
	 */
	public ViewRef(IViewInitializer init, ViewOptions options, String shortViewName, String longViewName) {
		if(init == null || options == null) throw new IllegalArgumentException();
		this.init = init;
		this.options = options;
		this.shortViewName = shortViewName;
		this.longViewName = longViewName;
	}

	public IViewInitializer getViewInitializer() {
		return init;
	}

	public ViewOptions getOptions() {
		return options;
	}

	public String getShortViewName() {
		return shortViewName;
	}

	public String getLongViewName() {
		return longViewName;
	}

	@Override
	public ViewKey getViewKey() {
		return init.getViewKey();
	}
}
