/*
 * Copyright Daniel Norman 2014
 */
package com.DanielNorman.ClothSimulator;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

import javax.swing.JPanel;

public class GraphicsPanel extends JPanel {	
	private final static int WIDTH = 400, HEIGHT = 440;
	
	int fps = 60;
	int timestep = 1000 / fps;
	
	boolean canRun = true;
	boolean shouldReset = false;
	boolean shouldUpdateSettings = false;
	
	ArrayList<Link> constraintList = new ArrayList<Link>();
	ArrayList<ArrayList<PointMass>> pointMassList = new ArrayList<ArrayList<PointMass>>();
	
	PointMass currentPM = null;
	int mouseX, mouseY, lastMouseX, lastMouseY;
	
	//Testing variables
	boolean softGrab = true;
	boolean colorCoded = false;
	boolean fillCloth = true;
	int pointDistance = 14;
	double gravity = 0.002;
	double stiffness = 0.5;
	double tearDistance = 16 * pointDistance;
	//-----------------
	
	public GraphicsPanel()
	{
		initializeCloth();
		(new Thread() {
            @Override
            public void run(){
                while (canRun)
                {
                	loop();
                }
            }
        }).start();;
	}
	
	public void loop()
	{
		long time1 = System.currentTimeMillis();
		update();
		long time2 = System.currentTimeMillis();
		long sleepTime = timestep - (time2 - time1);
		sleepTime = sleepTime > 0 ? sleepTime : 0;
		try {
			Thread.sleep(sleepTime);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	void initializeCloth()
	{
		pointMassList.clear();
		constraintList.clear();
		
		//Create points
		for (int y = pointDistance + 40; y < ((HEIGHT - 100) - pointDistance); y += pointDistance)
		{
			pointMassList.add(new ArrayList<PointMass>());
			for (int x = pointDistance; x < (WIDTH - pointDistance); x += pointDistance)
			{
				PointMass pm = new PointMass(x, y, 1, timestep);
				pm.pinned = (y == pointDistance + 40);
				pointMassList.get(pointMassList.size() - 1).add(pm);
			}
		}
		
		//Create Links
		for (int j = 0; j < pointMassList.size(); ++j)
		{
			for (int i = 0; i < pointMassList.get(j).size(); ++i)
			{
				//Add a link to the right
				if (i != pointMassList.get(j).size() - 1)
				{
					Link link = new Link(pointMassList.get(j).get(i), pointMassList.get(j).get(i + 1), pointDistance, tearDistance);
					constraintList.add(link);
					pointMassList.get(j).get(i).linkRight = constraintList.size() - 1;
					pointMassList.get(j).get(i + 1).linkLeft = constraintList.size() - 1;
				}

				//Add a link up
				if (j != 0)
				{
					Link link = new Link(pointMassList.get(j).get(i), pointMassList.get(j - 1).get(i), pointDistance, tearDistance);
					constraintList.add(link);
					pointMassList.get(j).get(i).linkUp = constraintList.size() - 1;
					pointMassList.get(j - 1).get(i).linkDown = constraintList.size() - 1;
				}
			}
		}
	}
	
	public void handleMouseClick(int button, int x, int y, int modifier)
	{
		if (button == MouseEvent.BUTTON1)
		{
			PointMass closestPM = pointMassList.get(0).get(0);
			double dist = Math.abs(pointMassList.get(0).get(0).x - x) + Math.abs(pointMassList.get(0).get(0).y - y);
			for (ArrayList<PointMass> list : pointMassList)
	    	{
		    	for (PointMass pm : list)
		    	{
		    		double d = Math.abs(pm.x - x) + Math.abs(pm.y - y);
		    		if (d < dist)
		    		{
		    			closestPM = pm;
		    			dist = d;
		    		}
		    	}
	    	}
			if (dist < 20)
			{
				currentPM = closestPM;
				mouseX = x;
				mouseY = y;
			}
		}
		if (button == MouseEvent.BUTTON3)
		{
			mouseX = x;
			mouseY = y;
			lastMouseX = mouseX;
			lastMouseY = mouseY;
		}
	}
	
	public void handleMouseDrag(int button, int x, int y, int modifier)
	{
		if (button == MouseEvent.BUTTON1)
		{
			mouseX = x;
			mouseY = y;
		}
		if (button == MouseEvent.BUTTON3)
		{
			lastMouseX = mouseX;
			lastMouseY = mouseY;
			mouseX = x;
			mouseY = y;
		
			for(int i = 0; i < constraintList.size(); ++i) {
				if (constraintList.get(i) != null && constraintList.get(i).intersects(lastMouseX, lastMouseY, mouseX, mouseY))
				{
					constraintList.set(i, null);
				}
			}
		}
	}
	
	public void handleMouseRelease(int button, int x, int y, int modifier)
	{
		currentPM = null;
	}
	

	
	private void updateConstraints(int numbUpdatesContraints)
	{
		for (int j = 0; j < numbUpdatesContraints; ++j)
		{
			for(int i = 0; i < constraintList.size(); ++i) {
				if (constraintList.get(i) == null) continue;
				constraintList.get(i).solve();
				if (constraintList.get(i).dist > constraintList.get(i).tearDistance)
				{
					constraintList.set(i, null);
				}
			}
		}
	}
	private void updatePointMasses()
	{
		for (ArrayList<PointMass> list : pointMassList)
    	{
	    	for (PointMass pm : list)
	    	{
	    		if (pm.equals(currentPM))
	    		{
	    			pm.update(mouseX, mouseY);
	    		}
	    		else
	    		{
	    			pm.update();
	    		}
	    	}
    	}
	}
	
	public void update()
	{
		if (shouldReset)
		{
			initializeCloth();
			shouldReset = false;
		}
		if (shouldUpdateSettings)
		{
			updateSettings();
			shouldUpdateSettings = false;
		}
		
		int numbUpdatesContraints = 3;
		if (softGrab)
		{
			updatePointMasses();
			updateConstraints(numbUpdatesContraints);
		}
		else
		{
			updateConstraints(numbUpdatesContraints);
			updatePointMasses();
		}
		for (ArrayList<PointMass> list : pointMassList)
		{
		   	for (PointMass pm : list)
		   	{
		   		if (pm.pinned) pm.update();
		   	}
	    }
		
		repaint();
	}
	
	//Change settings
	public void updateSettings()
	{
		for (ArrayList<PointMass> list : pointMassList)
    	{
	    	for (PointMass pm : list)
	    	{
	    		pm.accY = gravity;
	    	}
    	}
		for (Link link : constraintList)
		{
			if (link == null) continue;
			link.stiffness = stiffness;
			link.tearDistance = tearDistance;
		}
	}
	
	
	@Override
    protected void paintComponent(Graphics g)
    {
    	super.paintComponent(g);
    	
    	((Graphics2D) g).setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
    	
    	g.setColor(Color.WHITE);
    	g.fillRect(0, 0, WIDTH, HEIGHT);
    	
    	try {
    		if (fillCloth) {
		    	g.setColor(Color.LIGHT_GRAY);
		    	for (int j = 1; j < pointMassList.size(); ++j)
		    	{
		    		for (int i = 0; i < pointMassList.get(j).size() - 1; ++i)
		    		{
		    			if (constraintList.get(pointMassList.get(j).get(i).linkRight) != null &&
		    				constraintList.get(pointMassList.get(j).get(i + 1).linkUp) != null &&
		    				constraintList.get(pointMassList.get(j - 1).get(i + 1).linkLeft) != null &&
		    				constraintList.get(pointMassList.get(j - 1).get(i).linkDown) != null)
		    			{
		    				if (colorCoded)
		    				{
			    				Link link = constraintList.get(pointMassList.get(j).get(i + 1).linkUp);
			    				double t = (1 - Math.abs(link.tearDistance - link.getDist()) / link.tearDistance) * 2;
			    				link = constraintList.get(pointMassList.get(j - 1).get(i).linkDown);
			    				t += (1 - Math.abs(link.tearDistance - link.getDist()) / link.tearDistance) * 2;
					    		t = (t < 0 ? 0 : t > 2 ? 2 : t);
					    		double green = 1 - (t - 1);
					    		double red = t;
					    		green = green > 1 ? 1 : green;
					    		red = red > 1 ? 1 : red;
					    		g.setColor(new Color((float) red, (float) green, 0));
		    				}
		    				int xPts[] = {(int) pointMassList.get(j).get(i).x, (int) pointMassList.get(j).get(i + 1).x,
		    							  (int) pointMassList.get(j - 1).get(i + 1).x, (int) pointMassList.get(j - 1).get(i).x};
		    				int yPts[] = {(int) pointMassList.get(j).get(i).y, (int) pointMassList.get(j).get(i + 1).y,
	  							  		  (int) pointMassList.get(j - 1).get(i + 1).y, (int) pointMassList.get(j - 1).get(i).y};
		    				g.fillPolygon(xPts, yPts, 4);
		    			}
		    		}
		    	}
    		}
	    	g.setColor(Color.GRAY);
	    	for (Link link : constraintList)
	    	{
	    		if (link == null) continue;
	    		g.drawLine((int) link.pmA.x, (int) link.pmA.y, (int) link.pmB.x, (int) link.pmB.y);
	    	}
	    	
	    	if (!fillCloth)
	    	{
		    	g.setColor(Color.BLACK);
		    	for (ArrayList<PointMass> list : pointMassList)
		    	{
			    	for (PointMass pm : list)
			    	{
			    		g.fillOval((int) (pm.x - 2), (int) (pm.y - 2), 4, 4);
			    	}
		    	}
	    	}
    	}
    	catch (Exception e) { }
    }
	
	
	@Override
	public Dimension getPreferredSize() {
		return new Dimension(WIDTH, HEIGHT);
	}
}
