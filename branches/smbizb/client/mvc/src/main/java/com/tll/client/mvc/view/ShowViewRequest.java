/**
 * The Logic Lab
 * @author jpk
 * @since Mar 24, 2009
 */
package com.tll.client.mvc.view;

/**
 * ShowViewRequest
 * @author jpk
 */
public final class ShowViewRequest extends AbstractViewRequest {

	/**
	 * The view options.
	 */
	private final ViewOptions options;

	/**
	 * The view initializer responsible for providing the {@link ViewKey}.
	 */
	private final IViewInitializer init;

	/**
	 * Constructor - Use for dynamic views that will have default view options.
	 * @param init
	 */
	public ShowViewRequest(IViewInitializer init) {
		this(ViewOptions.DEFAULT_VIEW_OPTIONS, init);
	}

	/**
	 * Constructor - Use for dynamic views.
	 * @param options
	 * @param init
	 */
	public ShowViewRequest(ViewOptions options, IViewInitializer init) {
		this.options = options;
		this.init = init;
	}

	/**
	 * Constructor - Use for static views that will have default view options.
	 * @param viewClass
	 */
	public ShowViewRequest(ViewClass viewClass) {
		this(ViewOptions.DEFAULT_VIEW_OPTIONS, new StaticViewInitializer(viewClass));
	}

	/**
	 * Constructor - Used for static views.
	 * @param options
	 * @param viewClass
	 */
	public ShowViewRequest(ViewOptions options, ViewClass viewClass) {
		this(options, new StaticViewInitializer(viewClass));
	}

	@Override
	public final boolean addHistory() {
		return true;
	}

	/**
	 * @return The view initializer.
	 */
	public IViewInitializer getViewInitializer() {
		return init;
	}

	/**
	 * @return The view options.
	 */
	public ViewOptions getOptions() {
		return options;
	}

	@Override
	public ViewKey getViewKey() {
		return init.getViewKey();
	}
}
