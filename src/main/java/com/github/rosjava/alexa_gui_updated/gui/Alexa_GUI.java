package com.github.rosjava.alexa_gui_updated.gui;

import org.apache.commons.logging.Log;
import org.ros.*;

import java.awt.Color;
import java.awt.Component;
import java.awt.image.BufferedImage;

import javax.swing.*;
import BreezySwing.*;

public class Alexa_GUI extends GBFrame{
	//GUI components
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
	private JButton cancel;
	private BufferedImage laserIm;
	private BufferedImage cameraIm;
	
	//other variables
	private boolean laser;
	private boolean cancelling;
	
	public Alexa_GUI() {
		//Set up GUI
		drawingPanel = new ImagePanel();
		helpMenu = addMenuItem("Menu", "View List of Commands");
		exitMenu = addMenuItem("Menu", "Exit");
		cameraLidar = addPanel(drawingPanel,1,1,7,7);
		queueing = addLabel("<html>Currently queueing? <font color='red'>No</font></html>", 1,8,4,1);
		runningQueue = addLabel("<html>Currently running queue? <font color='red'>No</font></html>", 2,8,4,1);
		queue = addTextArea("Queue: \n", 3,7,4,4);
		switchCamera = addButton("Switch from Camera to Laser Scan", 8,1,6,1);
		currentCommand = addLabel("<html><p style=\"text-align:center\"><font size=5>CURRENT COMMAND: </font></p></html>",9,1,10,1);
		exit = addButton("Exit",10,1,10,1);
		cancel = addButton("CANCEL CURRENT ACTION", 8, 7, 4, 1);
		
		//Initialize everything
		cancel.setBackground(Color.RED);
		queue.setEditable(false);
		laserIm = new BufferedImage(60, 60, BufferedImage.TYPE_INT_RGB);
		cameraIm = new BufferedImage(800, 800, BufferedImage.TYPE_INT_RGB);
		updateImages();
		laser = false;
		cancelling = false;
	}
	
	
	public void buttonClicked(JButton b) {
		if(b == exit) { //Exit
			System.exit(0);
		}
		if(b == switchCamera) { //Switch between camera and laser scan
			laser = !laser;
			updateImages();
		}
		if(b == cancel) {
			if(!cancelling) {
				cancelling = true;
			}
		}
	}
	
	public void menuItemSelected(JMenuItem m) {
		if(m == exitMenu) { //Exit
			System.exit(0);
		}
		
		if(m == helpMenu) { //Help Menu
			String message = "List of commands:\n\nMove the robot [direction] [number] [unit]: Move the robot a certain "
					+ "distance. \nUnits are meters, centimetrs,"
					+ " feet or inches. \n\nTurn the robot [left/right] [number] [degrees or radians]: Turn the robot a "
					+ "certain amount in a certain direction.\nDefault is 90 degrees.\n\n"
					+ "Find the door [direction] of  you: Find a door in a specific direction, or the closest.\n"
					+ "Default is closest door.\n\n"
					+ "Go [direction] down the hallway: Go down a hallway if you're in one. \n"
					+ "Default is the direction the robot is facing.\n\n"
					+ "[Start/stop] queueing/[start/stop] recording instructions: Any instruction given while in queueing mode\n"
					+ "will be added to the queue and executed later.\n\n"
					+ "[Start/stop] running the queue: Start/stop executing the commands in the queue.\n\n"
					+ "Cancel: Cancels the current action; stops both queueing and running the queue.";
			messageBox(message,815, 400);
		}
	}
	
	public boolean getCancel() { //Return whether current command has been cancelled
		return cancelling;
	}
	
	public void setCancel(boolean b) { //Uncancel or cancel something
		cancelling = b;
	}
	
	public void setQueue(String text) { //Set the text in the queue text box
		queue.setText("Queue: \n" + text);
	}
	
	public void setCurrentCommand(String text) { //Set the current command
		if(!cancelling)
			currentCommand.setText("<html><p style=\"text-align:center\"><font size=5>CURRENT COMMAND: " + text + "</font></p></html>");
		else
			currentCommand.setText("<html><p style=\"text-align:center\"><font size=5>CURRENT COMMAND: Cancelling Current Action </font></p></html>");
	}
	
	public void setQueueing(boolean b) { //Set whether the robot is queueing
		if(b) {
			queueing.setText("<html>Currently queueing? <font color='green'>Yes</font></html>");
		} else {
			queueing.setText("<html>Currently queueing? <font color='red'>No</font></html>");
		}
	}
	
	private void updateImages() { //Update the images
		if(laser) {
			drawingPanel.setImage(laserIm);
		} else {
			drawingPanel.setImage(cameraIm);
		}
		drawingPanel.repaint();
	}
	
	public void setLaserImage(BufferedImage i) { //Set a new image for the laser scan
		laserIm = i;
		updateImages();
	}
	
	public void setCameraImage(BufferedImage i) { //Set a new image from the camera
		cameraIm = i;
		updateImages();
	}
	public void setRunningQueue(boolean b) { //Set whether the robot is running the queue
		if(b) {
			runningQueue.setText("<html>Currently running queue? <font color='green'>Yes</font></html>");
		} else {
			runningQueue.setText("<html>Currently running queue? <font color='red'>No</font></html>");
		}
	}
}
