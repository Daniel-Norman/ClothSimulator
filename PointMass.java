/*
 * Copyright Daniel Norman 2014
 */
package com.DanielNorman.ClothSimulator;

public class PointMass {
	public static int SCREEN_WIDTH = 400, SCREEN_HEIGHT = 440;
	
	double x, y, lastX, lastY, initialX, initialY, accX = 0, accY = 0.002;
	int mass, timestepSq;
	boolean pinned = false;
	int linkRight = -1, linkLeft = -1, linkUp = -1, linkDown = -1;
		
	public PointMass(double x, double y, int mass, int timestep)
	{
		this.x = x;
		this.y = y;
		this.lastX = x;
		this.lastY = y;
		this.initialX = x;
		this.initialY = y;
		this.mass = mass;
		this.timestepSq = timestep * timestep;
	}
	
	void update(int forcedX, int forcedY)
	{
		if (pinned)
		{
			x = initialX;
			y = initialY;
			lastX = x;
			lastY = y;
			return;
		}
		
		x = forcedX;
		y = forcedY;
		lastX = x;
		lastY = y;
	}
	
	void update()
	{
		if (pinned)
		{
			x = initialX;
			y = initialY;
			lastX = x;
			lastY = y;
			return;
		}
		
		double velX = x - lastX;
		double velY = y - lastY;
		
		lastX = x;
		lastY = y;
		
		x = x + velX + accX * timestepSq;
		y = y + velY + accY * timestepSq;
		
		
		//Boundary constraints
		if (x < 0) x = 0;
		if (x > SCREEN_WIDTH) x = SCREEN_WIDTH;
		if (y <= 0)
		{
			y = 0;
			lastX = x;
		}
		if (y > SCREEN_HEIGHT)
		{
			y = SCREEN_HEIGHT;
			lastX = x;

		}
	}
}
