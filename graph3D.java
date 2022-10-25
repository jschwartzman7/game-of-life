

import java.awt.Color;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;

import edu.princeton.cs.introcs.StdDraw;
import support.cse131.ArgsProcessor;

public class graph3D {
	
	public static double defaultScale = 22;
	public static double scale = defaultScale;
	public static double defaultRange = 10;
	public static double angle = Math.PI/32;
	public static double[][] pxy = new double[][] {{Math.cos(angle), -Math.sin(angle), 0}, {Math.sin(angle), Math.cos(angle), 0}, {0, 0, 1}};
	public static double[][] nxy = new double[][] {{Math.cos(angle), Math.sin(angle), 0}, {-Math.sin(angle), Math.cos(angle), 0}, {0, 0, 1}};
	public static double[][] pyz = new double[][] {{1, 0, 0}, {0, Math.cos(angle), -Math.sin(angle)}, {0, Math.sin(angle), Math.cos(angle)}};
	public static double[][] nyz = new double[][] {{1, 0, 0}, {0, Math.cos(angle), Math.sin(angle)}, {0, -Math.sin(angle), Math.cos(angle)}};
	public static double[][] pxz = new double[][] {{Math.cos(angle), 0, Math.sin(angle)}, {0, 1, 0}, {-Math.sin(angle), 0, Math.cos(angle)}};
	public static double[][] nxz = new double[][] {{Math.cos(angle), 0, -Math.sin(angle)}, {0, 1, 0}, {Math.sin(angle), 0, Math.cos(angle)}};
	public static double[][] xaxis;
	public static double[][] yaxis;
	public static double[][] zaxis;
	public static LinkedList<double[][]> grid = new LinkedList<double[][]>();
	public static HashSet<double[]> points = new HashSet<double[]>();
	public static ArrayList<double[][]> gridStart = new ArrayList<double[][]>();
	public static ArrayList<double[]> pointsStart = new ArrayList<double[]>();
	public static double[] xEqn = new double[] {0, .2, 0, 0, 2, 0, 0};
	public static double[] yEqn = new double[] {0, 0, 0, 0, .25, 0, 0};
	//                                      t^3, t^2, t, 1, cos, sin, t^.5
	

	public static void rotateGrid() {
		if(StdDraw.isKeyPressed(KeyEvent.VK_D)) {
			for(double[][] line : grid) {
				line[0] = matrix.matrixVectorMultiplication(pxy, line[0]);
				line[1] = matrix.matrixVectorMultiplication(pxy, line[1]);
			}
			for(double[] point : points) {
				double[] newPoint = matrix.matrixVectorMultiplication(pxy, point);
				point[0] = newPoint[0];
				point[1] = newPoint[1];
				point[2] = newPoint[2];
			}
		}
		if(StdDraw.isKeyPressed(KeyEvent.VK_A)) {
			for(double[][] line : grid) {
				line[0] = matrix.matrixVectorMultiplication(nxy, line[0]);
				line[1] = matrix.matrixVectorMultiplication(nxy, line[1]);
			}
			for(double[] point : points) {
				double[] newPoint = matrix.matrixVectorMultiplication(nxy, point);
				point[0] = newPoint[0];
				point[1] = newPoint[1];
				point[2] = newPoint[2];
			}
		}
		if(StdDraw.isKeyPressed(KeyEvent.VK_W)) {
			for(double[][] line : grid) {
				line[0] = matrix.matrixVectorMultiplication(pxz, line[0]);
				line[1] = matrix.matrixVectorMultiplication(pxz, line[1]);
			}
			for(double[] point : points) {
				double[] newPoint = matrix.matrixVectorMultiplication(pxz, point);
				point[0] = newPoint[0];
				point[1] = newPoint[1];
				point[2] = newPoint[2];
			}
		}
		if(StdDraw.isKeyPressed(KeyEvent.VK_S)) {
			for(double[][] line : grid) {
				line[0] = matrix.matrixVectorMultiplication(nxz, line[0]);
				line[1] = matrix.matrixVectorMultiplication(nxz, line[1]);
			}
			for(double[] point : points) {
				double[] newPoint = matrix.matrixVectorMultiplication(nxz, point);
				point[0] = newPoint[0];
				point[1] = newPoint[1];
				point[2] = newPoint[2];
			}
		}
		if(StdDraw.isKeyPressed(KeyEvent.VK_Q)) {
			for(double[][] line : grid) {
				line[0] = matrix.matrixVectorMultiplication(pyz, line[0]);
				line[1] = matrix.matrixVectorMultiplication(pyz, line[1]);
			}
			for(double[] point : points) {
				double[] newPoint = matrix.matrixVectorMultiplication(pyz, point);
				point[0] = newPoint[0];
				point[1] = newPoint[1];
				point[2] = newPoint[2];
			}
		}
		if(StdDraw.isKeyPressed(KeyEvent.VK_E)) {
			for(double[][] line : grid) {
				line[0] = matrix.matrixVectorMultiplication(nyz, line[0]);
				line[1] = matrix.matrixVectorMultiplication(nyz, line[1]);
			}
			for(double[] point : points) {
				double[] newPoint = matrix.matrixVectorMultiplication(nyz, point);
				point[0] = newPoint[0];
				point[1] = newPoint[1];
				point[2] = newPoint[2];
			}
		}
	}
	
	public static double[] to2D(double[] cord) {
		double[] cord2D = new double[] {Math.sqrt(3)/2*(cord[1]-cord[0]), cord[2] - .5*(cord[0]+cord[1])};
	//	StdDraw.filledCircle(cord2D[0], cord2D[1], 0.2);
		return cord2D;
	}
	
	public static void addGridLines() {
		for(double i = -10; i <= 10; i+=2) {
			for(double j = -10; j <= 10; j+=2) {
					grid.add(new double[][] {{-10, i, j}, {10, i, j}});
					grid.add(new double[][] {{i, -10, j}, {i, 10, j}});
					grid.add(new double[][] {{i, j, -10}, {i, j, 10}});
			}
		}
	}
	
	public static void addAxis() {
		xaxis = new double[][] {{-defaultRange, 0, 0}, {defaultRange, 0, 0}};
		yaxis = new double[][] {{0, -defaultRange, 0}, {0,defaultRange, 0}};
		zaxis = new double[][] {{0, 0, -defaultRange}, {0, 0, defaultRange}};
		double[][] xaxisCopy = new double[][] {{-defaultRange, 0, 0}, {defaultRange, 0, 0}};
		double[][] yaxisCopy = new double[][] {{0, -defaultRange, 0}, {0,defaultRange, 0}};
		double[][] zaxisCopy = new double[][] {{0, 0, -defaultRange}, {0, 0, defaultRange}};
		grid.add(xaxis);
		grid.add(yaxis);
		grid.add(zaxis);
		gridStart.add(xaxisCopy);
		gridStart.add(yaxisCopy);
		gridStart.add(zaxisCopy);
	}
	
	
	public static void setAxis(double xMin, double xMax, double yMin, double yMax) {
		xaxis = new double[][] {{xMin, 0, 0}, {xMax, 0, 0}};
		yaxis = new double[][] {{0, yMin, 0}, {0,yMax, 0}};
	}
	
	public static void addEdges() {
		grid.add(new double[][] {{-defaultRange, -defaultRange, -defaultRange}, {-defaultRange, -defaultRange, defaultRange}});
		grid.add(new double[][] {{defaultRange, -defaultRange, -defaultRange}, {defaultRange, -defaultRange, defaultRange}});
		grid.add(new double[][] {{-defaultRange, defaultRange, -defaultRange}, {-defaultRange, defaultRange, defaultRange}});
		grid.add(new double[][] {{defaultRange, defaultRange, -defaultRange}, {defaultRange, defaultRange, defaultRange}});
		
		grid.add(new double[][] {{-defaultRange, -defaultRange, defaultRange}, {defaultRange, -defaultRange, defaultRange}});
		grid.add(new double[][] {{-defaultRange, defaultRange, defaultRange}, {defaultRange, defaultRange, defaultRange}});
		grid.add(new double[][] {{-defaultRange, -defaultRange, defaultRange}, {-defaultRange, defaultRange, defaultRange}});
		grid.add(new double[][] {{defaultRange, -defaultRange, defaultRange}, {defaultRange, defaultRange, defaultRange}});
		
		grid.add(new double[][] {{-defaultRange, -defaultRange, -defaultRange}, {defaultRange, -defaultRange, -defaultRange}});
		grid.add(new double[][] {{-defaultRange, defaultRange, -defaultRange}, {defaultRange, defaultRange, -defaultRange}});
		grid.add(new double[][] {{-defaultRange, -defaultRange, -defaultRange}, {-defaultRange, defaultRange, -defaultRange}});
		grid.add(new double[][] {{defaultRange, -defaultRange, -defaultRange}, {defaultRange, defaultRange, -defaultRange}});
		
		
		gridStart.add(new double[][] {{-defaultRange, -defaultRange, -defaultRange}, {-defaultRange, -defaultRange, defaultRange}});
		gridStart.add(new double[][] {{defaultRange, -defaultRange, -defaultRange}, {defaultRange, -defaultRange, defaultRange}});
		gridStart.add(new double[][] {{-defaultRange, defaultRange, -defaultRange}, {-defaultRange, defaultRange, defaultRange}});
		gridStart.add(new double[][] {{defaultRange, defaultRange, -defaultRange}, {defaultRange, defaultRange, defaultRange}});
		
		gridStart.add(new double[][] {{-defaultRange, -defaultRange, defaultRange}, {defaultRange, -defaultRange, defaultRange}});
		gridStart.add(new double[][] {{-defaultRange, defaultRange, defaultRange}, {defaultRange, defaultRange, defaultRange}});
		gridStart.add(new double[][] {{-defaultRange, -defaultRange, defaultRange}, {-defaultRange, defaultRange, defaultRange}});
		gridStart.add(new double[][] {{defaultRange, -defaultRange, defaultRange}, {defaultRange, defaultRange, defaultRange}});
		
		gridStart.add(new double[][] {{-defaultRange, -defaultRange, -defaultRange}, {defaultRange, -defaultRange, -defaultRange}});
		gridStart.add(new double[][] {{-defaultRange, defaultRange, -defaultRange}, {defaultRange, defaultRange, -defaultRange}});
		gridStart.add(new double[][] {{-defaultRange, -defaultRange, -defaultRange}, {-defaultRange, defaultRange, -defaultRange}});
		gridStart.add(new double[][] {{defaultRange, -defaultRange, -defaultRange}, {defaultRange, defaultRange, -defaultRange}});
	}
	
	public static void setEdges(double xMin, double xMax, double yMin, double yMax) {
		grid.clear();
		xaxis = new double[][] {{xMin, 0, 0}, {xMax, 0, 0}};
		yaxis = new double[][] {{0, yMin, 0}, {0,yMax, 0}};
		zaxis = new double[][] {{0, 0, -defaultRange}, {0, 0, defaultRange}};
		grid.add(xaxis);
		grid.add(yaxis);
		grid.add(zaxis);
	
		grid.add(new double[][] {{xMin, yMin, -defaultRange}, {xMin, yMin, defaultRange}});
		grid.add(new double[][] {{xMax, yMin, -defaultRange}, {xMax, yMin, defaultRange}});
		grid.add(new double[][] {{xMin, yMax, -defaultRange}, {xMin, yMax, defaultRange}});
		grid.add(new double[][] {{xMax, yMax, -defaultRange}, {xMax, yMax, defaultRange}});
		
		grid.add(new double[][] {{xMin, yMin, defaultRange}, {xMax, yMin, defaultRange}});
		grid.add(new double[][] {{xMin, yMax, defaultRange}, {xMax, yMax, defaultRange}});
		grid.add(new double[][] {{xMin, yMin, defaultRange}, {xMin, yMax, defaultRange}});
		grid.add(new double[][] {{xMax, yMin, defaultRange}, {xMax, yMax, defaultRange}});
		
		grid.add(new double[][] {{xMin, yMin, -defaultRange}, {xMax, yMin, -defaultRange}});
		grid.add(new double[][] {{xMin, yMax, -defaultRange}, {xMax, yMax, -defaultRange}});
		grid.add(new double[][] {{xMin, yMin, -defaultRange}, {xMin, yMax, -defaultRange}});
		grid.add(new double[][] {{xMax, yMin, -defaultRange}, {xMax, yMax, -defaultRange}});
		
	}
	public static void addPoints() {
		// x^3, x^2, x, 1, cos(x), sin(x), x^.5
				//  0    1   2  3    4       5      6
		for(double i = -10; i < 10; i += 0.25) {
			for(double j = -10; j < 10; j += 0.25) {
				points.add(new double[] {i, j, xEqn[0]*i*i*i + xEqn[1]*i*i + xEqn[2]*i + xEqn[3] + xEqn[4]*Math.cos(i)+ xEqn[5]*Math.sin(i) + xEqn[6]*Math.pow(Math.abs(i), .5) + yEqn[0]*j*j*j + yEqn[1]*j*j + yEqn[2]*j + yEqn[3] + yEqn[4]*Math.cos(j)+ yEqn[5]*Math.sin(j) + yEqn[6]*Math.pow(Math.abs(j), .5)});
				pointsStart.add(new double[] {i, j, xEqn[0]*i*i*i + xEqn[1]*i*i + xEqn[2]*i + xEqn[3] + xEqn[4]*Math.cos(i)+ xEqn[5]*Math.sin(i) + yEqn[0]*j*j*j + yEqn[1]*j*j + yEqn[2]*j + yEqn[3] + yEqn[4]*Math.cos(j)+ yEqn[5]*Math.sin(j)});			
			}
		}
	}
	
	public static void setPoints(double xMin, double xMax, double yMin, double yMax) {
		points.clear();
		for(double i = xMin; i < xMax; i += 0.25) {
			for(double j = yMin; j < yMax; j += 0.25) {
				points.add(new double[] {i, j, xEqn[0]*i*i*i + xEqn[1]*i*i + xEqn[2]*i + xEqn[3] + xEqn[4]*Math.cos(i)+ xEqn[5]*Math.sin(i) + xEqn[6]*Math.pow(Math.abs(i), .5) + yEqn[0]*j*j*j + yEqn[1]*j*j + yEqn[2]*j + yEqn[3] + yEqn[4]*Math.cos(j)+ yEqn[5]*Math.sin(j) + yEqn[6]*Math.pow(Math.abs(j), .5)});
			}
		}
	}
	
	public static boolean setScale(boolean setScale) {
		StdDraw.setScale();
		StdDraw.setPenColor();
		StdDraw.rectangle(.85, .05, .09, .035);
		StdDraw.text(.85, .05, "Set Scale");
		while(StdDraw.mousePressed() && StdDraw.mouseX()>.76) {// && StdDraw.mouseX()<.94 && StdDraw.mouseY()>.015 && StdDraw.mouseX()<.085) {
			System.out.println(StdDraw.mouseX());
			setScale = true;
		}
		StdDraw.setScale(-scale, scale);
		return setScale;
	}
	
	public static void zoomGraph() {
		if(StdDraw.isKeyPressed(KeyEvent.VK_UP)) { // zoom out
			++scale;
		}
		if(StdDraw.isKeyPressed(KeyEvent.VK_DOWN) && scale > 1) { // zoom in
			--scale;
		}
	}
	
	public static void drawLines() {
		for(double[][] line : grid) {
			StdDraw.setPenRadius(0.0007);
			StdDraw.setPenColor();
			if(line.equals(xaxis) || line.equals(yaxis) || line.equals(zaxis)) {
				StdDraw.setPenRadius(0.004);
				if(line.equals(xaxis)) {
					StdDraw.text(1.1*to2D(line[1])[0], 1.1*to2D(line[1])[1], "x");
					StdDraw.setPenColor(Color.BLUE);
				}
				if(line.equals(yaxis)) {
					StdDraw.text(1.1*to2D(line[1])[0], 1.1*to2D(line[1])[1], "y");
					StdDraw.setPenColor(Color.GREEN);
				}
				if(line.equals(zaxis)) {
					StdDraw.text(1.1*to2D(line[1])[0], 1.1*to2D(line[1])[1], "z");
					StdDraw.setPenColor(Color.RED);
				}
			}
			double[] linePointOne = to2D(line[0]);
			double[] linePointTwo = to2D(line[1]);
			StdDraw.line(linePointOne[0], linePointOne[1], linePointTwo[0], linePointTwo[1]);
		}
	}
	
	public static void drawPoints() {
		for(double[] point : points) {
			double[] point2D = to2D(point);
			StdDraw.filledCircle(point2D[0], point2D[1], 0.1);
	//		threeDimensionalCords.drawCord(point[0], point[1], point[2]);
		}
	}
	
	
	public static void resetOrientation(){
//		grid = gridStart;
//		points = pointsStart;
		points.clear();
		for(double[] point : pointsStart) {
			points.add(new double[] {point[0], point[1], point[2]});
		}
		int i = 0;
		for(double[][] line : grid) {
			line[0][0] = gridStart.get(i)[0][0];
			line[0][1] = gridStart.get(i)[0][1];
			line[0][2] = gridStart.get(i)[0][2];
			line[1][0] = gridStart.get(i)[1][0];
			line[1][1] = gridStart.get(i)[1][1];
			line[1][2] = gridStart.get(i)[1][2];
			++i;
		}
	}
	
	
	public static void main(String[] args) {
		ArgsProcessor ap = new ArgsProcessor(args);
		StdDraw.enableDoubleBuffering();
		StdDraw.setScale(-scale, scale);
		addAxis();
		addEdges();
		addPoints();
//		addGridLines();
		boolean setScale = false;
		while(true) {
			StdDraw.clear();
			StdDraw.setScale(-scale, scale);
			drawLines();
			drawPoints();
			
			if(StdDraw.isKeyPressed(KeyEvent.VK_R)) {
				resetOrientation();
				scale = defaultScale;
			}
			
			zoomGraph();
			
			while(StdDraw.isKeyPressed(KeyEvent.VK_F)) {// && StdDraw.mouseX()<.94 && StdDraw.mouseY()>.015 && StdDraw.mouseX()<.085) {
		//		System.out.println(StdDraw.mouseX());
				setScale = true;
			}
		//	StdDraw.setScale(-scale, scale);
			
			if(setScale) {
				double xMin = ap.nextDouble("xMin");
				double xMax = ap.nextDouble("xMax");
				double yMin = ap.nextDouble("yMin");
				double yMax = ap.nextDouble("yMax");
				setEdges(xMin, xMax, yMin, yMax);
				setPoints(xMin, xMax, yMin, yMax);
				setScale = false;
			}
			
			
			rotateGrid();
			
			
			
			
			StdDraw.show(30);
		}
	}
}
