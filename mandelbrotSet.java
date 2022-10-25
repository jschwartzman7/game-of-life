package Stuff;

import java.awt.Color;

import edu.princeton.cs.introcs.StdDraw;

public class mandelbrotSet{
	
	public static int n = 800;
	public static int[][] iterationsArray = new int[n][n];
	public static double[] zn = new double[] {0, 0};
	public static double[] c = new double[2];
	public static double xMinDefault = -2.2;
	public static double xMaxDefault = 2.2;
	public static double xMin =xMinDefault;
	public static double xMax = xMaxDefault;
	public static double xRange = xMax - xMin;
	public static double yMinDefault = -2.2;
	public static double yMaxDefault = 2.2;
	public static double yMin = yMinDefault;
	public static double yMax = yMaxDefault;
	public static double yRange = yMax - yMin;
	public static int maxIterations = 200;
	
	public static double absValCmplxNum(double[] num) {
		return Math.hypot(num[0], num[1]);
	}
	
	public static double[] mndlbrtEqtn(double[] zn, double[] c) {
		double realPart = zn[0];
		double imagPart = zn[1];
		zn[0] = realPart*realPart-imagPart*imagPart + c[0];
		zn[1] = 2*realPart*imagPart + c[1];
		return zn;
		}
	
	public static int isInSet(double[] zn, double[] c, int iterations) {
		if(iterations >= maxIterations){ // in set
			return -1;
		}
		else if(absValCmplxNum(zn) > 2) { // not in set
			return iterations;
		}
		else {
			return isInSet(mndlbrtEqtn(zn, c), c, iterations + 1);
		}
	}
	
	public static void defaultScale() {
		xMin = xMinDefault;
		xMax = xMaxDefault;
		xRange = xMax - xMin;
		yMin = yMinDefault;
		yMax = yMaxDefault;
		yRange = yMax - yMin;
		StdDraw.setXscale(xMinDefault, xMaxDefault);
		StdDraw.setYscale(yMinDefault, yMaxDefault);
	}
	
	public static void changeScale(double xMinNew, double xMaxNew, double yMinNew, double yMaxNew) {
		xMin = xMinNew;
		xMax = xMaxNew;
		xRange = xMaxNew - xMinNew;
		yMin = yMinNew;
		yMax = yMaxNew;
		yRange = yMaxNew - yMinNew;
		StdDraw.setXscale(xMin, xMax);
		StdDraw.setYscale(yMin, yMax);
	}
	
	
	
	public static void generateBoard() {
	//	int iterationsMin = Integer.MAX_VALUE;
		// loop through middle of a range or something instead of every point to decrease speed
	/*	for(int i = 0; i < n; ++i) {
			for(int j = 0; j < n; ++j) {
				c[0] = xMin + (xMax-xMin)/(2*n) + j*(xMax-xMin)/n;
				c[1] = yMax - (yMax-yMin)/(2*n) - i*(yMax-yMin)/n;
				zn[0] = c[0];
				zn[1] = c[1];
				iterationsArray[i][j] = isInSet(zn, c, 1);
				if(iterationsArray[i][j] != -1) {
					if(iterationsArray[i][j] < iterationsMin) {
						iterationsMin = iterationsArray[i][j];
					}
				}
			}
		}*/
	//	System.out.println(iterationsMin + " is real iterationsMin");
	/*	int iterationsMin = Integer.MAX_VALUE;
		int scan = 25;
		for(int i = 0; i < scan; ++i) {
			for(int j = 0; j < scan; ++j) {
				c[0] = xMin + (xMax-xMin)/(2*scan) + j*(xMax-xMin)/scan;
				c[1] = yMax - (yMax-yMin)/(2*scan) - i*(yMax-yMin)/scan;
				zn[0] = c[0];
				zn[1] = c[1];
				iterationsArray[i][j] = isInSet(zn, c, 1);
				if(iterationsArray[i][j] != -1) {
					if(iterationsArray[i][j] < iterationsMin) {
						iterationsMin = iterationsArray[i][j];
					}
				}
			}
		}
		if(iterationsMin > maxIterations) {
			
		}*/
		int iterationsMin = Integer.MAX_VALUE;
	// 	System.out.println(iterationsMin + " is fast iterationsMin");
		for(int i = 0; i < n; ++i) {
			for(int j = 0; j < n; ++j) {
				c[0] = xMin + (xMax-xMin)/(2*n) + j*(xMax-xMin)/n;
				c[1] = yMax - (yMax-yMin)/(2*n) - i*(yMax-yMin)/n;
				zn[0] = c[0];
				zn[1] = c[1];
				iterationsArray[i][j] = isInSet(zn, c, 1);
				if(iterationsArray[i][j] == -1) {
					StdDraw.setPenColor();
				}
				else {
					if(iterationsArray[i][j] < iterationsMin) {
						iterationsMin = iterationsArray[i][j];
					}
					StdDraw.setPenColor(0, (int)(((1.0*(iterationsArray[i][j]-iterationsMin))/(maxIterations - 1 - iterationsMin))*255), 50);//use color range for eahc window
				// (int)(((1.0*(iterationsArray[i][j]-iterationsMin))/(maxIterations - 1 - iterationsMin))*255)
				}
				StdDraw.point(c[0], c[1]);
			}
		}
	//	System.out.println(iterationsMax + " is iterationsMax");
	//	System.out.println(iterationsMin + " is iterationsMin");
	}
	
	
	public static void mandelbrotSet() {
		StdDraw.enableDoubleBuffering();
	//	StdDraw.setPenRadius();
	//	StdDraw.setPenRadius(0.00001);
		defaultScale();
		generateBoard();
/*		StdDraw.setPenColor(Color.BLUE);
		StdDraw.filledCircle(1, 0, 0.02);
		StdDraw.filledCircle(0, 0, 0.02);
		StdDraw.setPenColor();
		StdDraw.line(-2, 0, 2, 0);
		StdDraw.line(0, -2, 0, 2);
		StdDraw.text(1, 0.13, "(1, 0)");
		StdDraw.setPenColor(Color.BLUE);
		StdDraw.line(1, -.08, 1, .08);
		StdDraw.filledCircle(0, 0, 0.02);
		StdDraw.line(1, -.08, 1, .08);
		StdDraw.line(-.08, 1, .08, 1);
		StdDraw.line(-1, -.08, -1, .08);
		StdDraw.line(-.08, -1, .08, -1);*/
		StdDraw.show();
		while(true) {
			
			if(StdDraw.mousePressed()) {
				double mouseX = StdDraw.mouseX();
				double mouseY = StdDraw.mouseY();
			//	System.out.println(xRange);
			//	System.out.println(yRange);
				changeScale(mouseX-.1*xRange, mouseX+.1*xRange, mouseY-.1*yRange, mouseY+.1*yRange);
				StdDraw.clear();
				generateBoard();
			}
			if(StdDraw.isKeyPressed(32)) {
				defaultScale();
				StdDraw.clear();
				generateBoard();
			}
			StdDraw.show();
		}
	}
	
	
	
	
	
	
	
	public static void main(String[] args) {
		mandelbrotSet();
		// TODO Auto-generated method stub

	}

}
