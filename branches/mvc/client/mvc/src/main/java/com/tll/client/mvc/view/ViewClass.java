/**
 * The Logic Lab
 * @author jpk
 * Mar 12, 2008
 */
package com.tll.client.mvc.view;

import java.util.ArrayList;
import java.util.List;

/**
 * ViewClass - Serves as a view definition enabling factory style view
 * instantiation.
 * @author jpk
 */
public abstract class ViewClass {

	/**
	 * List of all registered classes in the app.
	 */
	private static final List<ViewClass> classes = new ArrayList<ViewClass>();

	/**
	 * The default view options
	 */
	protected static final ViewOptions dfltViewOptions;

	static {
		dfltViewOptions = new ViewOptions(true, true, true, true, false);
	}

	/**
	 * Add a view class
	 * @param vclass The view class
	 */
	public static void addClass(ViewClass vclass) {
		assert vclass != null && vclass.name != null;
		if(findClassByViewName(vclass.name) != null) {
			throw new IllegalArgumentException("AbstractView name: " + vclass.name
					+ " already exists in one of the existing view classes");
		}
		classes.add(vclass);
	}

	/**
	 * Finds the ViewClass with the given view name.
	 * @param viewName The view name to search for. If <code>null</code>,
	 *        <code>null</code> is returned.
	 * @return The found ViewClass or
	 *         <code>null<code> of no ViewClass exists for the given view name.
	 */
	public static final ViewClass findClassByViewName(String viewName) {
		if(viewName != null) {
			for(final ViewClass vc : classes) {
				if(viewName.equals(vc.name)) return vc;
			}
		}
		return null;
	}

	/**
	 * The view name.
	 */
	private final String name;

	/**
	 * Constructor
	 * @param name
	 */
	public ViewClass(String name) {
		super();
		assert name != null;
		this.name = name;
	}

	/**
	 * @return the name of the view
	 */
	public final String getName() {
		return name;
	}

	/**
	 * @return The defined view options for this view.
	 */
	public ViewOptions getViewOptions() {
		return dfltViewOptions;
	}

	/**
	 * @return New instance of the view this class defines.
	 */
	public abstract IView<?> newView();

	@Override
	public final boolean equals(Object obj) {
		if(this == obj) return true;
		if(obj instanceof ViewClass == false) return false;
		return this.name.equals(((ViewClass) obj).name);
	}

	@Override
	public final int hashCode() {
		return name.hashCode();
	}

	@Override
	public final String toString() {
		return name;
	}
}
