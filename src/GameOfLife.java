import java.awt.Color;
import java.awt.event.KeyEvent;
import java.util.HashMap;
import java.util.HashSet;
import edu.princeton.cs.introcs.StdDraw;

public class GameOfLife {

	static final double DEFAULT_SCALE = 5;
	static final int SPACEBAR_CODE = 32;
	static final double SCALE_SENSITIVITY = .1;
	static final double MOVE_SENSITIVITY = .2;
	static final double CELL_RADIUS = 0.49;
	static double xScale = DEFAULT_SCALE;
	static double yScale = DEFAULT_SCALE;
	static double[] boardCenter = new double[]{0,0};
	static HashMap<String, Boolean> aliveMap = new HashMap<String, Boolean>(); // i + "" + j
	static int iteration = 0;


	/*public static int scale = 5;
	public static double[] center = new double[] {0, 0};
	public static double updateXMin = -scale + .5;
	public static double updateXMax = scale;
	public static double updateYMin = -scale + .5;
	public static double updateYMax = scale;*/
	

	public static void resetView() {
		boardCenter[0] = 0;
		boardCenter[1] = 0;
		xScale = DEFAULT_SCALE;
		yScale = DEFAULT_SCALE;
		StdDraw.setXscale(boardCenter[0] - xScale, boardCenter[0] + xScale);
		StdDraw.setYscale(boardCenter[1] - yScale, boardCenter[1] + yScale);
	}

	public static double[] parseKey(String key){
		String[] cord = key.split(",");
		return new double[]{Double.valueOf(cord[0]), Double.valueOf(cord[1])};
	}

	public static boolean updateCells() {
		HashSet<String> checkSet = new HashSet<String>(aliveMap.keySet());
		for(String position : aliveMap.keySet()){
			double[] cord = parseKey(position);
			for(int x = -1; x <= 1; ++x) {
				for(int y = -1; y <= 1; ++y) {
					if(x != 0 || y != 0) {
						String newPosition = (cord[0] + x) +","+ (cord[1] + y);
						if(!checkSet.contains(newPosition)) {
							checkSet.add(newPosition);
						}
					}
				}
			}
		}
		HashSet<String> updateAliveSet = new HashSet<String>();
		for(String position : checkSet){
			if(aliveMap.get(position) != null && (liveNeighbors(position) < 2 || liveNeighbors(position) > 3)) {
				updateAliveSet.add(position);
			}
			else if(aliveMap.get(position) == null && liveNeighbors(position) == 3) {
				updateAliveSet.add(position);
			}
		}
		if(updateAliveSet.isEmpty()) {
			return false;
		}
		else {
			for(String position : updateAliveSet) {
				aliveMap.put(position, aliveMap.get(position) == null ? true : null);
			}
			++iteration;
			return true;
		}

	}

	public static int liveNeighbors(String position) {
		double[] cord = parseKey(position);
		int liveNeighbors = 0;
		for(int x = -1; x <= 1; ++x) {
			for(int y = -1; y <= 1; ++y) {
				if(x != 0 || y != 0) {
					if(aliveMap.get((cord[0] + x) +","+ (cord[1] + y)) != null) {
						++liveNeighbors;
					}
				}
			}
		}
		return liveNeighbors;
	}

	public static void updateView() {
		if(StdDraw.isKeyPressed(KeyEvent.VK_R)) {
			resetView();
			return;
		}

		if(StdDraw.isKeyPressed(KeyEvent.VK_Q) && xScale > 1 && yScale > 1){ // zoom in
			xScale -= xScale*SCALE_SENSITIVITY;
			yScale -= yScale*SCALE_SENSITIVITY;
		}
		else if(StdDraw.isKeyPressed(KeyEvent.VK_E)){ // zoom out
			xScale += xScale*SCALE_SENSITIVITY;
			yScale += yScale*SCALE_SENSITIVITY;
		}

		if(StdDraw.isKeyPressed(KeyEvent.VK_DOWN) && yScale > 1){ // zoom y in
			yScale -= yScale*SCALE_SENSITIVITY;
		}
		else if(StdDraw.isKeyPressed(KeyEvent.VK_UP)){ // zoom y out
			yScale += yScale*SCALE_SENSITIVITY;;
		}

		if(StdDraw.isKeyPressed(KeyEvent.VK_LEFT) && xScale > 1){ // zoom x in
			xScale -= xScale*SCALE_SENSITIVITY;;
		}
		else if(StdDraw.isKeyPressed(KeyEvent.VK_RIGHT)){ // zoom x out
			xScale += xScale*SCALE_SENSITIVITY;;
		}

		if(StdDraw.isKeyPressed(KeyEvent.VK_W)){ // shift up
			boardCenter[1] += Math.min(xScale, yScale)*MOVE_SENSITIVITY;
		}
		else if(StdDraw.isKeyPressed(KeyEvent.VK_S)){ // shift down
			boardCenter[1] -= Math.min(xScale, yScale)*MOVE_SENSITIVITY;
		}

		if(StdDraw.isKeyPressed(KeyEvent.VK_A)){ // shift left
			boardCenter[0] -= Math.min(xScale, yScale)*MOVE_SENSITIVITY;
		}
		else if(StdDraw.isKeyPressed(KeyEvent.VK_D)){ // shift right
			boardCenter[0] += Math.min(xScale, yScale)*MOVE_SENSITIVITY;
		}
		
		StdDraw.setXscale(boardCenter[0] - xScale, boardCenter[0] + xScale);
		StdDraw.setYscale(boardCenter[1] - yScale, boardCenter[1] + yScale);
	}

	public static void drawBoard() {
		StdDraw.setPenColor(0, 0, 0);
		StdDraw.filledSquare(boardCenter[0], boardCenter[1], Math.max(xScale, yScale));
		for(double i = (int)(boardCenter[0] - xScale)-0.5; i <= boardCenter[0] + xScale+0.5; ++i) {
			for(double j = (int)(boardCenter[1] - yScale)-0.5; j <= boardCenter[1] + yScale+0.5; ++j) {
				if(aliveMap.get(i +","+ j) != null) {
					StdDraw.setPenColor(0, 255, 0);
				}
				else {
					StdDraw.setPenColor(0, 0, 175);
				}
				StdDraw.filledSquare(i, j, CELL_RADIUS);
			}
		}
	}

	public static void drawBoardInfo(int state) {
		StdDraw.setPenColor(Color.WHITE);
		switch (state) {
			case 0: // cell setup
				StdDraw.text(boardCenter[0] - 0.6*xScale, boardCenter[1] - 0.8*yScale, "Click to add/remove cells");
				StdDraw.text(boardCenter[0] - 0.7*xScale, boardCenter[1] - 0.9*yScale, "Press space to start");
				StdDraw.text(boardCenter[0] - 0.7*xScale, boardCenter[1] - 1*yScale, "Press r to reset view");
				break;
			case 1: // game running
				StdDraw.text(boardCenter[0] - 0.9*xScale, boardCenter[1] - 0.7*yScale, "Iteration #: "+iteration);
				StdDraw.text(boardCenter[0] - 0.7*xScale, boardCenter[1] - 0.8*yScale, "Hold Spacebar to pause");
				StdDraw.text(boardCenter[0] - 0.8*xScale, boardCenter[1] - 0.9*yScale, "Click to reset cells");
				break;
			case 2: // game running paused
				StdDraw.text(boardCenter[0] - 0.9*xScale, boardCenter[1] - 0.8*yScale, "Iteration #: "+iteration);
				StdDraw.text(boardCenter[0] - 0.9*xScale, boardCenter[1] - 0.9*yScale, "Release Spacebar to resume");
				break;
			case 3: // game halted
				StdDraw.text(boardCenter[0], boardCenter[1], "Game Halted after " + iteration + " iterations");
				StdDraw.text(boardCenter[0], boardCenter[1] - 0.1*yScale, "Click to reset cells");
				break;
		}
	}

	public static void gameOfLife() {
		StdDraw.enableDoubleBuffering();
		while(true){
			aliveMap.clear();
			iteration = 0;
			resetView();
			drawBoard();
			drawBoardInfo(0);
			StdDraw.show();
			boolean mouseDown = false;

			// Initial cell setup
			while(true) {
				StdDraw.clear();
				if(StdDraw.isKeyPressed(SPACEBAR_CODE)) {
					break;
				}
				if(!StdDraw.isMousePressed()) {mouseDown = false;}
				if(StdDraw.isMousePressed() && !mouseDown) {
					mouseDown = true;
					int flooredX = StdDraw.mouseX() < 0 ? (int)(StdDraw.mouseX() - 1) : (int)(StdDraw.mouseX());
					int flooredY = StdDraw.mouseY() < 0 ? (int)(StdDraw.mouseY() - 1) : (int)(StdDraw.mouseY());
					aliveMap.put((flooredX+.5) +","+ (flooredY+.5), aliveMap.get((flooredX+.5) +","+ (flooredY+.5)) == null ? true : null);
				}
				updateView();
				drawBoard();
				drawBoardInfo(0);
				StdDraw.show(50);
			}
			boolean gameQuit = false;
			while(updateCells()) {
				StdDraw.clear();
				if(StdDraw.isMousePressed()) {
					gameQuit = true;
					break;
				}
				while(StdDraw.isKeyPressed(SPACEBAR_CODE)){
					StdDraw.clear();
					updateView();
					drawBoard();
					drawBoardInfo(2);
					StdDraw.show(100);
				}
				updateView();
				drawBoard();
				drawBoardInfo(1);
				StdDraw.show(100);
			}

			// Game halted
			if(!gameQuit){
				while(!StdDraw.isMousePressed()) {
					StdDraw.clear();
					updateView();
					drawBoard();
					drawBoardInfo(3);
					StdDraw.show(50);
				}
			}
			while(StdDraw.isMousePressed()) {}
		}
	}


	public static void main(String[] args) {
		gameOfLife();
		
	}
}
