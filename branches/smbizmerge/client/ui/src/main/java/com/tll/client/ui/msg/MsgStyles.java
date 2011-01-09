package com.tll.client.ui.msg;

import com.google.gwt.core.client.GWT;
import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.CssResource;
import com.google.gwt.resources.client.CssResource.Import;
import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.user.client.ui.Image;
import com.tll.client.ui.Styles.TllWidget;
import com.tll.common.msg.Msg.MsgLevel;

/**
 * @author jpk
 */
public class MsgStyles {

	public interface IMsgCss extends CssResource {

		String info();
		String warn();
		String error();
		String fatal();
		
		String msg();
		
		String cmsg();
		
		String gmsg();
		
		String container();
		
		String title();
	}
	
	public interface IMsgResources extends ClientBundle {

		@Source("msg.css")
		//@NotStrict
		@Import(TllWidget.class)
		IMsgCss css();

		/**
		 * info
		 * @return the image prototype
		 */
		@Source(value = "info.gif")
		ImageResource info();

		/**
		 * warn
		 * @return the image prototype
		 */
		@Source(value = "warn.gif")
		ImageResource warn();

		/**
		 * error
		 * @return the image prototype
		 */
		@Source(value = "error.gif")
		ImageResource error();

		/**
		 * fatal
		 * @return the image prototype
		 */
		@Source(value = "fatal.gif")
		ImageResource fatal();
	}
	
	/**
	 * Provides a style name associated with the given msg level.
	 * @param level The message level
	 * @return style name
	 */
	public static String getMsgLevelStyle(MsgLevel level) {
		switch(level) {
			case WARN:
				return css().warn();
			case ERROR:
				return css().error();
			case FATAL:
				return css().fatal();
			default:
			case INFO:
				return css().info();
		}
	}
	
	/**
	 * Provides a new {@link Image} containing the associated msg level icon.
	 * @param level The message level
	 * @return Image
	 */
	public static ImageResource getMsgLevelImage(MsgLevel level) {
		switch(level) {
			case WARN:
				return resources.warn();
			case ERROR:
				return resources.error();
			case FATAL:
				return resources.fatal();
			default:
			case INFO:
				return resources.info();
		}
	}
	
	private static IMsgResources resources;
	
	static {
    resources = GWT.create(IMsgResources.class);
    resources.css().ensureInjected();
	}
	
  public static IMsgCss css() {
    return resources.css();
  }

  public static IMsgResources resources() {
    return resources;
  }
}
