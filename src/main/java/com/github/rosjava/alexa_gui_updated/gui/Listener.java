/*
 * Copyright (C) 2014 Alex Meier.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */

package com.github.rosjava.alexa_gui_updated.gui;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import org.apache.commons.logging.Log;
import org.jboss.netty.buffer.ChannelBuffer;
import org.ros.concurrent.CancellableLoop;
import org.ros.message.MessageListener;
import org.ros.namespace.GraphName;
import org.ros.node.AbstractNodeMain;
import org.ros.node.ConnectedNode;
import org.ros.node.NodeMain;
import org.ros.node.topic.Subscriber;
import org.ros.node.topic.Publisher;


import nav_msgs.OccupancyGrid;
import std_msgs.Bool;
import std_msgs.String;

/**
 * A simple {@link Subscriber} {@link NodeMain}.
 */
public class Listener extends AbstractNodeMain {

	@Override
	public GraphName getDefaultNodeName() {
		return GraphName.of("rosjava/listener");
	}

	@Override
	public void onStart(ConnectedNode connectedNode) {
		final Log log = connectedNode.getLog();
		
		//Set up the GUI
		final Alexa_GUI gui = new Alexa_GUI();
		gui.setSize(800, 500);
		gui.setVisible(true);
		gui.setTitle("Pioneer Directions Robot");

		final Publisher<std_msgs.Bool> cancelPub =
			    connectedNode.newPublisher("/move_to_point/cancel_goal", std_msgs.Bool._TYPE);
		Subscriber<sensor_msgs.Image> imageSub = connectedNode.newSubscriber("/camera/image_raw",
				sensor_msgs.Image._TYPE);
		imageSub.addMessageListener(new MessageListener<sensor_msgs.Image>() {
			@Override
			public void onNewMessage(sensor_msgs.Image message) {
				Image im = new Image(message.getHeader(), message.getHeight(), message.getWidth(),
						message.getEncoding(), message.getIsBigendian(), message.getStep(), message.getData().array());
				BufferedImage i = im.toBufferedImage();
				gui.setCameraImage(i);
			}
		});
		Subscriber<nav_msgs.OccupancyGrid> mapSub = connectedNode.newSubscriber("/move_base/local_costmap/costmap",
				nav_msgs.OccupancyGrid._TYPE);
		mapSub.addMessageListener(new MessageListener<nav_msgs.OccupancyGrid>() {
			@Override
			public void onNewMessage(nav_msgs.OccupancyGrid message) {
				OccGrid o = new OccGrid(message.getData().array(), message.getInfo().getWidth(), message.getInfo().getHeight());
				System.out.println(message.getInfo().getOrigin().getPosition().getX()
						+ " " + message.getInfo().getOrigin().getPosition().getY());
				BufferedImage i = o.toImage();
				gui.setLaserImage(i);
			}
		});
		Subscriber<std_msgs.Bool> runningQueueSub = connectedNode.newSubscriber("/queue/running_queue", std_msgs.Bool._TYPE);
		runningQueueSub.addMessageListener(new MessageListener<std_msgs.Bool>() {
			@Override
			public void onNewMessage(std_msgs.Bool message) {
				gui.setRunningQueue(message.getData());
			}
		});
		Subscriber<std_msgs.Bool> queueingSub = connectedNode.newSubscriber("/queue/queueing", std_msgs.Bool._TYPE);
		queueingSub.addMessageListener(new MessageListener<std_msgs.Bool> () {
			@Override
			public void onNewMessage(std_msgs.Bool message) {
				gui.setQueueing(message.getData());
			}
		});
		Subscriber<std_msgs.String> queueSub = connectedNode.newSubscriber("/queue/current_queue", std_msgs.String._TYPE);
		queueSub.addMessageListener(new MessageListener<std_msgs.String> () {
			@Override
			public void onNewMessage(std_msgs.String message) {
				gui.setQueue(message.getData());
			}
		});
		Subscriber<std_msgs.String> currentCommandSub = connectedNode.newSubscriber("/queue/current_command", std_msgs.String._TYPE);
		currentCommandSub.addMessageListener(new MessageListener<std_msgs.String> () {
			@Override
			public void onNewMessage(std_msgs.String message) {
				gui.setCurrentCommand(message.getData());
			}
		});
		
		connectedNode.executeCancellableLoop(new CancellableLoop() {
			@Override
			protected void loop() throws InterruptedException {
				if(gui.getCancel()) {
					std_msgs.Bool msg = cancelPub.newMessage();
					msg.setData(true);
					cancelPub.publish(msg);
					try {
						Thread.sleep(2000);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					gui.setCancel(false);
					gui.messageBox("Current action cancelled.\nNo longer queueing or running the queue.");
				}
			}
		});
	}

}
