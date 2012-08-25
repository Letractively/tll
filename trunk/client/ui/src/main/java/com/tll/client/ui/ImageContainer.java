/**
 * The Logic Lab
 * @author jpk Dec 30, 2007
 */
package com.tll.client.ui;

import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.SimplePanel;

/**
 * ImageContainer - Wraps an {@link Image} element in a DIV.
 * @author jpk
 */
public final class ImageContainer extends Composite {

	private final SimplePanel sp = new SimplePanel();

	/**
	 * Constructor - Use this constructor for 'clipped' images (i.e. the image IS
	 * aware of its dimensions).
	 * @param img The image.
	 */
	public ImageContainer(Image img) {
		setClippedImage(img);
		initWidget(sp);
		setStyleName(Styles.tllWidgetStyle().imageContainer());
	}

	/**
	 * Constructor - Use this constructor for 'unclipped' images (i.e. the immage
	 * is NOT aware of its dimensions).
	 * @param img The image.
	 * @param width In pixels
	 * @param height In pixels
	 */
	public ImageContainer(Image img, int width, int height) {
		setUnclippedImage(img, width, height);
		initWidget(sp);
	}
	
	/**
	 * Constructor - Use this constructor for 'clipped' images (i.e. the image IS
	 * aware of its dimensions).
	 * @param img The image.
	 */
	public ImageContainer(ImageResource img) {
		setClippedImage(new Image(img));
		initWidget(sp);
		setStyleName(Styles.tllWidgetStyle().imageContainer());
	}

	/**
	 * Constructor
	 * @param ir
	 * @param width
	 * @param height
	 */
	public ImageContainer(ImageResource ir, int width, int height) {
		setUnclippedImage(new Image(ir), width, height);
	}

	public Image getImage() {
		return (Image) sp.getWidget();
	}

	public void setClippedImage(Image img) {
		sp.setWidget(img);
		sp.setPixelSize(img.getWidth(), img.getHeight());
	}

	public void setUnclippedImage(Image img, int width, int height) {
		sp.setWidget(img);
		sp.setPixelSize(width, height);
	}
}
