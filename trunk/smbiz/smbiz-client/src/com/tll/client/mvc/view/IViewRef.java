/**
 * The Logic Lab
 * @author jpk
 * Mar 19, 2008
 */
package com.tll.client.mvc.view;

import com.google.gwt.user.client.ui.Hyperlink;
import com.google.gwt.user.client.ui.Widget;
import com.tll.client.event.type.ViewRequestEvent;
import com.tll.client.ui.ViewRequestLink;

/**
 * IViewRef - Generic ref to a AbstractView at runtime.
 * @author jpk
 */
public interface IViewRef {

	/**
	 * Tools - AbstractView ref utility methods.
	 * @author jpk
	 */
	public static final class Tools {

		/**
		 * Generates a link to the view the given {@link IViewRef} references.
		 * @param viewRef The view ref for which a view link is generated
		 * @return A clickable Widget that invokes the display of the referenced view.
		 */
		public static Widget getViewLink(IViewRef viewRef) {
			if(viewRef instanceof ViewRequestEvent) {
				return new ViewRequestLink(viewRef.getShortViewName(), viewRef.getLongViewName(), (ViewRequestEvent) viewRef);
			}
			Hyperlink link = new Hyperlink(viewRef.getShortViewName(), viewRef.getViewKey().getViewKeyHistoryToken());
			link.setTitle(viewRef.getLongViewName());
			return link;
		}
	}

	/**
	 * @return The {@link AbstractView}'s key
	 */
	ViewKey getViewKey();

	/**
	 * @return The short view name.
	 */
	String getShortViewName();

	/**
	 * @return The long view name.
	 */
	String getLongViewName();
}