/*
 * Copyright Daniel Norman 2014
 */
package com.DanielNorman.ClothSimulator;

public class Link {
	PointMass pmA, pmB;
	double restingDistance, tearDistance;
	double dist;
	double stiffness = 0.5;
	
	public Link(PointMass pmA, PointMass pmB, double restingDistance, double tearDistance)
	{
		this.pmA = pmA;
		this.pmB = pmB;
		this.restingDistance = restingDistance;
		this.tearDistance = tearDistance;
	}
	
	void solve()
	{
		double diffX = pmA.x - pmB.x;
		double diffY = pmA.y - pmB.y;
		dist = Math.sqrt(diffX * diffX + diffY * diffY);
		
		double diff = dist > 10 ? (restingDistance - dist) / dist : 0;
		
		double translateX = diffX * diff * stiffness;
		double translateY = diffY * diff * stiffness;
		
		pmA.x += translateX;
		pmA.y += translateY;
		
		pmB.x -= translateX;
		pmB.y -= translateY;
	}
	
	double getDist()
	{
		double diffX = pmA.x - pmB.x;
		double diffY = pmA.y - pmB.y;
		return Math.sqrt(diffX * diffX + diffY * diffY);
	}
	
	boolean intersects(double x1, double y1, double x2, double y2)
	{
		return get_line_intersection(this.pmA.x, this.pmA.y, this.pmB.x, this.pmB.y, x1, y1, x2, y2);
	}
	boolean get_line_intersection(double p0_x, double p0_y, double p1_x, double p1_y, double p2_x, double p2_y, double p3_x, double p3_y)
	{
		double s1_x, s1_y, s2_x, s2_y;
		s1_x = p1_x - p0_x;
		s1_y = p1_y - p0_y;
		s2_x = p3_x - p2_x;
		s2_y = p3_y - p2_y;

		double s, t;
		s = (-s1_y * (p0_x - p2_x) + s1_x * (p0_y - p2_y)) / (-s2_x * s1_y + s1_x * s2_y);
		t = ( s2_x * (p0_y - p2_y) - s2_y * (p0_x - p2_x)) / (-s2_x * s1_y + s1_x * s2_y);

		return (s >= 0 && s <= 1 && t >= 0 && t <= 1);
	}
}
