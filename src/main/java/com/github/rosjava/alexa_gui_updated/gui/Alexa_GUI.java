package com.github.rosjava.alexa_gui_updated.gui;

import org.apache.commons.logging.Log;
import org.ros.*;
import java.awt.Component;
import java.awt.image.BufferedImage;

import javax.swing.*;
import BreezySwing.*;

public class Alexa_GUI extends GBFrame{
	private JButton exit;
	private JTextArea queue;
	private JMenuItem exitMenu;
	private JMenuItem helpMenu;
	private JLabel currentCommand;
	private JLabel queueing;
	private JLabel queueingBool;
	private JLabel runningQueue;
	private JTextField runningQueueBool;
	private GBPanel cameraLidar;
	private ImagePanel drawingPanel;
	private JButton switchCamera;
	private boolean laser;
	private BufferedImage laserIm;
	private BufferedImage cameraIm;
	
	public Alexa_GUI() {
		drawingPanel = new ImagePanel();
		helpMenu = addMenuItem("Menu", "View List of Commands");
		exitMenu = addMenuItem("Menu", "Exit");
		cameraLidar = addPanel(drawingPanel,1,1,7,7);
		queueing = addLabel("<html>Currently queueing? <font color='red'>No</font></html>", 1,8,4,1);
		runningQueue = addLabel("<html>Currently running queue? <font color='red'>No</font></html>", 2,8,4,1);
		queue = addTextArea("Queue: \n", 3,7,4,4);
		switchCamera = addButton("Switch from Camera to Laser Scan", 8,1,6,1);
		currentCommand = addLabel("<html><p style=\"text-align:center\"><font size=5>CURRENT COMMAND: </font></p></html>",9,5,5,1);
		exit = addButton("Exit",10,1,10,1);
		
		queue.setEditable(false);
		laserIm = new BufferedImage(60, 60, BufferedImage.TYPE_INT_RGB);
		cameraIm = new BufferedImage(800, 800, BufferedImage.TYPE_INT_RGB);
		updateImages();
		laser = false;
	}
	
	
	public void buttonClicked(JButton b) {
		if(b == exit) {
			System.exit(0);
		}
		if(b == switchCamera) {
			laser = !laser;
			updateImages();
		}
	}
	
	public void setQueue(String text) {
		queue.setText(text);
	}
	
	public void setCurrentCommand(String text) {
		currentCommand.setText("<html><p style=\"text-align:center\"><font size=5>CURRENT COMMAND: " + text + "</font></p></html>");
	}
	
	public void setQueueing(boolean b) {
		if(b) {
			queueing.setText("<html>Currently queueing? <font color='green'>Yes</font></html>");
		} else {
			queueing.setText("<html>Currently queueing? <font color='red'>No</font></html>");
		}
	}
	
	private void updateImages() {
		if(laser) {
			drawingPanel.setImage(laserIm);
		} else {
			drawingPanel.setImage(cameraIm);
		}
		drawingPanel.repaint();
	}
	
	public void setLaserImage(BufferedImage i) {
		laserIm = i;
		updateImages();
	}
	
	public void setCameraImage(BufferedImage i) {
		cameraIm = i;
		updateImages();
	}
	public void setRunningQueue(boolean b) {
		if(b) {
			runningQueue.setText("<html>Currently running queue? <font color='green'>Yes</font></html>");
		} else {
			runningQueue.setText("<html>Currently running queue? <font color='red'>No</font></html>");
		}
	}
}
