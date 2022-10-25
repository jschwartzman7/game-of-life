package Stuff;

import java.awt.Color;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;

import edu.princeton.cs.introcs.StdDraw;

public class gameOfLifeNew {

	public static int scale = 5;
	public static double[] center = new double[] {0, 0};
	public static double updateXMin = -scale + .5;
	public static double updateXMax = scale;
	public static double updateYMin = -scale + .5;
	public static double updateYMax = scale;
	private static HashMap<String, Boolean> map = new HashMap<String, Boolean>();
	private static HashSet<Double[]> updateSet = new HashSet<Double[]>();
	
	
	public static void resetScale() {
		scale = 5;
		center[0] = 0;
		center[1] = 0;
		StdDraw.setXscale(center[0] - scale, center[0] + scale);
		StdDraw.setYscale(center[1] - scale, center[1] + scale);
	}
	
	
	public static double getXMin() {
		updateXMin = Double.MAX_VALUE;
		for(Double[] cord : updateSet) {
			if(cord[0] < updateXMin) {
				updateXMin = cord[0];
			}
		}
		return updateXMin;
	}
	public static double getXMax() {
		updateXMax = Double.MIN_VALUE;
		for(Double[] cord : updateSet) {
			if(cord[0] > updateXMax) {
				updateXMax = cord[0];
			}
		}
		return updateXMax;
	}
	public static double getYMin() {
		updateYMin = Double.MAX_VALUE;
		for(Double[] cord : updateSet) {
			if(cord[1] < updateYMin) {
				updateYMin = cord[1];
			}
		}
		return updateYMin;
	}
	public static double getYMax() {
		updateYMax = Double.MIN_VALUE;
		for(Double[] cord : updateSet) {
			if(cord[1] > updateYMax) {
				updateYMax = cord[1];
			}
		}
		return updateYMax;	
	}
	
	public static boolean updateBoard() {
		updateXMin = getXMin() - 2;
		updateXMax = getXMax() + 2;
		updateYMin = getYMin() - 2;
		updateYMax = getYMax() + 2;
		updateSet.clear();
		for(double i = updateXMin; i < updateXMax; ++i) {
			for(double j = updateYMin; j < updateYMax; ++j) {
				if(map.get(i + ", " + j) != null && liveNeighbors(i, j) != 2 && liveNeighbors(i, j) != 3) {
					updateSet.add(new Double[] {i, j});
				}
				if(map.get(i + ", " + j) == null && liveNeighbors(i, j) == 3) {
					updateSet.add(new Double[] {i, j});
				}
			}
		}
		if(updateSet.isEmpty()) {
			return false;
		}
		else {
			for(Double[] cord : updateSet) {
				map.put(cord[0] + ", " + cord[1], map.get(cord[0] + ", " + cord[1]) == null ? true : null);
			}
			return true;
		}
		
	}
	public static int liveNeighbors(double i, double j) {
		int liveNeighbors = 0;
		for(int n = -1; n <= 1; ++n) {
			for(int m = -1; m <= 1; ++m) {
				if(n != 0 || m != 0) {
					if(map.get((i + n) + ", " + (j + m)) != null) {
						++liveNeighbors;
					}
				}
			}
		}
		return liveNeighbors;
	}
	
	public static void checkView() {
		if(StdDraw.isKeyPressed(KeyEvent.VK_Q) && scale > 1){ // zoom in
			--scale;
		}
		if(StdDraw.isKeyPressed(KeyEvent.VK_E)){ // zoom out
			++scale;
		}
		if(StdDraw.isKeyPressed(KeyEvent.VK_W)){ // shift up
			++center[1];
		}
		if(StdDraw.isKeyPressed(KeyEvent.VK_S)){ // shift down
			--center[1];
		}
		if(StdDraw.isKeyPressed(KeyEvent.VK_D)){ // shift right
			++center[0];
		}
		if(StdDraw.isKeyPressed(KeyEvent.VK_A)){ // shift left
			--center[0];
		}
		StdDraw.setXscale(center[0] - scale, center[0] + scale);
		StdDraw.setYscale(center[1] - scale, center[1] + scale);
	}
	
	public static void drawBoard() {
		StdDraw.setPenColor(0, 0, 0);
		StdDraw.filledSquare(center[0], center[1], scale);
		for(double i = center[0] - scale + .5; i < center[0] + scale; ++i) {
			for(double j = center[1] - scale + .5; j < center[1] + scale; ++j) {
				if(map.get(i + ", " + j) != null) {
					StdDraw.setPenColor(0, 255, 0);
				}
				else {
					StdDraw.setPenColor(0, 0, 175);
				}
				StdDraw.filledSquare(i, j, .495);
			}
		}
	}
	
	
	public static void gameOfLife() {
		StdDraw.enableDoubleBuffering();
		StdDraw.clear();
		resetScale();
		map.clear();
		drawBoard();
		StdDraw.show(500);
		boolean ready = false;
		while(!ready) {
			StdDraw.clear();
			while(!StdDraw.isMousePressed()) {
				StdDraw.clear();
				if(StdDraw.isKeyPressed(32)) {
					ready = true;
					break;
				}
				checkView();
				drawBoard();
				StdDraw.show(50);
			}
			while(StdDraw.isMousePressed()) {}
			if(!ready) {
				int flooredX = StdDraw.mouseX() < 0 ? (int)(StdDraw.mouseX() - 1) : (int)(StdDraw.mouseX());
				int flooredY = StdDraw.mouseY() < 0 ? (int)(StdDraw.mouseY() - 1) : (int)(StdDraw.mouseY());
				map.put((flooredX + .5) + ", " + (flooredY + .5), map.get((flooredX + .5) + ", " + (flooredY + .5)) == null ? true : null);
				updateSet.add(new Double[] {flooredX + .5, flooredY + .5});
			}
			drawBoard();
			StdDraw.show(50);
		}
		while(updateBoard()) {
			StdDraw.clear();
			for(int i = 0; i < 7; ++i) {
				StdDraw.clear();
				if(StdDraw.isMousePressed()) {
					gameOfLife();
				}
				checkView();
				drawBoard();
				StdDraw.show(30);
			}
			StdDraw.show(30);
		}
		StdDraw.setPenColor(Color.WHITE);
		StdDraw.text(center[0], center[1], "Game terminated");
		StdDraw.show();
		while(!StdDraw.isMousePressed()) {}
		while(StdDraw.isMousePressed()) {}
		gameOfLife();
	}
	
	
	public static void main(String[] args) {
		gameOfLife();
		
		
		
		
	
	}

}
