package com.github.rosjava.alexa_gui_updated.gui;

import java.awt.Color;
import java.awt.image.BufferedImage;

public class OccGrid {
	byte[] data;
	int width;
	int height;
	
	public OccGrid(byte[] data, int width, int height) {
		this.data = data;
		this.width = width;
		this.height = height;
	}
	
	/**
	 * Converts a occupancy grid into a buffered image
	 */
	public BufferedImage toImage() {
		BufferedImage im = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
		int j = 0;
		int k = 0;
		for(int i = 100; i < height * width; i++) {
			if(i % 60 == 0) {
				j = 0;
				k++;
			}
			double rgb = 255 - (data[i] * 2.55);
			Color c = new Color((int) rgb, (int) rgb, (int) rgb);
			
			if(j >= 40) {
				j -= 40;
				if(j >= 27 && j <= 33 && k >= 27 && k <= 33)
					c = Color.RED;
				im.setRGB(59 - j, k, c.getRGB());
				j += 40;
			} else { 
				j += 20;
				if(j >= 27 && j <= 33 && k >= 27 && k <= 33)
					c = Color.RED;
				im.setRGB(59 - j, k, c.getRGB());
				j -= 20;
			}
			j++;
		}
		
		return im;
	}
}
