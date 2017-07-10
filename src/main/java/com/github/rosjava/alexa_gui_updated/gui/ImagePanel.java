package com.github.rosjava.alexa_gui_updated.gui;

import BreezySwing.*;
import javax.swing.*;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;

public class ImagePanel extends GBPanel {
	BufferedImage image;

	public ImagePanel() {}

	public ImagePanel(BufferedImage i) {
		image = i;
	}

	public void setImage(BufferedImage i) {
		image = i;
	}

	/** Calculate the scale required to correctly fit the image into panel */
	private double getScale(int panelWidth, int panelHeight, int imageWidth, int imageHeight) {
		double scale = 1;
		double xScale;
		double yScale;

		// should check that denom != 0 first.
		xScale = (double) panelWidth / imageWidth;
		yScale = (double) panelHeight / imageHeight;
		scale = Math.min(xScale, yScale);
		return scale;
	}

	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		Graphics2D g2 = (Graphics2D)g;
        g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BICUBIC);

        // Grab a copy of the original image for scaling
        BufferedImage scaledImage = image;
        
        // Get the required sizes for display and calculations
        int panelWidth = this.getWidth();
        int panelHeight = this.getHeight();
        int imageWidth = scaledImage.getWidth();
        int imageHeight = scaledImage.getHeight();
        
        // Get the scale that the image should be resized with
        double scale = getScale(panelWidth, panelHeight, imageWidth, imageHeight);
                 
        // Calculate the center position of the panel -- with scale
        double xPos = (panelWidth - (scale * imageWidth))/2;
        double yPos = (panelHeight - (scale * imageHeight))/2;

        // Locate, scale and draw image
        AffineTransform at = AffineTransform.getTranslateInstance(xPos, yPos);
        at.scale(scale, scale);
        g2.drawRenderedImage(scaledImage, at);
		//g.drawImage(image, 0, 0, this); // see javadoc for more info on the parameters
	}
}
