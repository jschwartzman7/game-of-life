import java.awt.Color;
import java.awt.Event;
import java.awt.Font;
import java.awt.RenderingHints.Key;
import java.awt.event.KeyEvent;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Scanner;

import org.apache.commons.lang3.StringUtils;

import edu.princeton.cs.introcs.StdDraw;

public class GameOfLife {

 	static final int SPACEBAR_CODE = 32;
	final double DEFAULT_SCALE;
	final double SCALE_SENSITIVITY;
	final double MOVE_SENSITIVITY;
	final double CELL_RADIUS;
	final Color BACKGROUND_COLOR;
	final Color ALIVE_CELL_COLOR;;
	final Color EMPY_CELL_COLOR;
	final Color TEXT_COLOR;
	private double xScaleRadius;
	private double yScaleRadius;
	private double[] boardCenter;
	private int framerateMs;
	private HashSet<String> aliveCells; 
	private HashSet<String> cellsToLife;
	private HashSet<String> cellsToDeath;
	private int iterationNum;
	private BoardConfiguration BoardConfig;

	// Game state variables
	private boolean pressingMouse;
	private boolean pressingSpace;
	private boolean gameHalted;
	private boolean gameRunning;

	public GameOfLife() {
		DEFAULT_SCALE = 7;
		SCALE_SENSITIVITY = 1.1;
		MOVE_SENSITIVITY = .1;
		CELL_RADIUS = 0.49;
		BACKGROUND_COLOR = new Color(0, 0, 0);
		ALIVE_CELL_COLOR = new Color(0, 255, 0);
		EMPY_CELL_COLOR = new Color(0, 0, 175);
		TEXT_COLOR = new Color(255, 255, 255);
		xScaleRadius = DEFAULT_SCALE;
		yScaleRadius = DEFAULT_SCALE;
		boardCenter = new double[]{0,0};
		framerateMs = 50;
		aliveCells = new HashSet<String>(); // i + "" + j
		cellsToLife = new HashSet<String>();
		cellsToDeath = new HashSet<String>();
		iterationNum = 0;
		BoardConfig = new BoardConfiguration();
		pressingMouse = StdDraw.isMousePressed();
		pressingSpace = StdDraw.isKeyPressed(SPACEBAR_CODE);
		gameHalted = false;
		gameRunning = false;
		StdDraw.enableDoubleBuffering();
		StdDraw.setCanvasSize(500, 500);
		StdDraw.setFont(new Font("Arial", Font.PLAIN, 20));
		gameOfLife();
	}

	public int[] parseCellKey(String key){
		String[] cord = key.split(",");
		return new int[]{Integer.parseInt(cord[0]), Integer.parseInt(cord[1])};
	}

	public String hashCurrentConfiguration(){
		if(aliveCells.isEmpty()){
			return null;
		}
		StringBuilder currentBoardHash = new StringBuilder();
		for(String cell : aliveCells){
			currentBoardHash.append(cell);
			currentBoardHash.append(":");
		}
		return currentBoardHash.toString().substring(0, currentBoardHash.length()-1);
	}

	public boolean updateCells() {
		/*
		 * Game of Life Rules
		 * 1. Any live cell with one or zero live neighbors dies, as if by underpopulation.
		 * 2. Any live cell with two or three live neighbors lives on to the next generation.
		 * 3. Any live cell with at least four live neighbors dies, as if by overpopulation.
		 * 4. Any dead cell with exactly three live neighbors becomes a live cell, as if by reproduction.
		 * 
		 * GameCell()
		 *       should be hashable
		 * 		ability to uniquely provide coordinates for each cell
		 * 
		 */

		

		 // check all cells that are alive and their neighbors

		HashSet<String> checkSet = new HashSet<String>();
		
		for(String position : aliveCells){
			int[] cord = parseCellKey(position);
			for(int x = -1; x <= 1; ++x) {
				for(int y = -1; y <= 1; ++y) {
					checkSet.add((cord[0] + x) +","+ (cord[1] + y));
				}
			}
		}

		for(String position : checkSet){
			int numLiveNeighbors = liveNeighbors(position);
			if(aliveCells.contains(position) && (numLiveNeighbors < 2 || numLiveNeighbors > 3)) {
				cellsToDeath.add(position);
			}
			else if(!aliveCells.contains(position) && numLiveNeighbors == 3) {
				cellsToLife.add(position);
			}
		}

		if(cellsToLife.isEmpty() && cellsToDeath.isEmpty()) {return false;}
		else {
			for(String position : cellsToLife) {
				aliveCells.add(position);
			}
			for(String position : cellsToDeath) {
				aliveCells.remove(position);
			}
			cellsToLife.clear();
			cellsToDeath.clear();
			++iterationNum;
			return true;
		}

	}

	public int liveNeighbors(String position) {
		int[] cord = parseCellKey(position);
		int liveNeighbors = 0;
		for(int x = -1; x <= 1; ++x) {
			for(int y = -1; y <= 1; ++y) {
				if(x != 0 || y != 0) {
					if(aliveCells.contains((cord[0] + x) +","+ (cord[1] + y))) {
						++liveNeighbors;
					}
				}
			}
		}
		return liveNeighbors;
	}

	public void updateView() {
		if(StdDraw.isKeyPressed(KeyEvent.VK_R)) {
			boardCenter[0] = 0;
			boardCenter[1] = 0;
			xScaleRadius = DEFAULT_SCALE;
			yScaleRadius = DEFAULT_SCALE;
		}

		if(StdDraw.isKeyPressed(KeyEvent.VK_Q) && xScaleRadius > 1 && yScaleRadius > 1){ // zoom in
			xScaleRadius /= SCALE_SENSITIVITY;
			yScaleRadius /= SCALE_SENSITIVITY;
		}
		else if(StdDraw.isKeyPressed(KeyEvent.VK_E)){ // zoom out
			xScaleRadius *= SCALE_SENSITIVITY;
			yScaleRadius *= SCALE_SENSITIVITY;
		}

		if(StdDraw.isKeyPressed(KeyEvent.VK_DOWN) && yScaleRadius > 1){ // zoom y in
			yScaleRadius /= SCALE_SENSITIVITY;
		}
		else if(StdDraw.isKeyPressed(KeyEvent.VK_UP)){ // zoom y out
			yScaleRadius *= SCALE_SENSITIVITY;
		}

		if(StdDraw.isKeyPressed(KeyEvent.VK_LEFT) && xScaleRadius > 1){ // zoom x in
			xScaleRadius /= SCALE_SENSITIVITY;
		}
		else if(StdDraw.isKeyPressed(KeyEvent.VK_RIGHT)){ // zoom x out
			xScaleRadius *= SCALE_SENSITIVITY;
		}

		if(StdDraw.isKeyPressed(KeyEvent.VK_W)){ // shift up
			boardCenter[1] += Math.min(xScaleRadius, yScaleRadius)*MOVE_SENSITIVITY;
		}
		else if(StdDraw.isKeyPressed(KeyEvent.VK_S)){ // shift down
			boardCenter[1] -= Math.min(xScaleRadius, yScaleRadius)*MOVE_SENSITIVITY;
		}

		if(StdDraw.isKeyPressed(KeyEvent.VK_A)){ // shift left
			boardCenter[0] -= Math.min(xScaleRadius, yScaleRadius)*MOVE_SENSITIVITY;
		}
		else if(StdDraw.isKeyPressed(KeyEvent.VK_D)){ // shift right
			boardCenter[0] += Math.min(xScaleRadius, yScaleRadius)*MOVE_SENSITIVITY;
		}
		StdDraw.setXscale(boardCenter[0] - xScaleRadius, boardCenter[0] + xScaleRadius);
		StdDraw.setYscale(boardCenter[1] - yScaleRadius, boardCenter[1] + yScaleRadius);
	}

	public void drawBoard() {
		StdDraw.setPenColor(BACKGROUND_COLOR);
		StdDraw.filledSquare(boardCenter[0], boardCenter[1], Math.max(xScaleRadius, yScaleRadius));
		for(int i = (int)(boardCenter[0] - xScaleRadius)-1; i <= (int)(boardCenter[0] + xScaleRadius)+1; ++i) {
			for(int j = (int)(boardCenter[1] - yScaleRadius)-1; j <= (int)(boardCenter[1] + yScaleRadius)+1; ++j) {
				if(aliveCells.contains(i+","+j)) {
					StdDraw.setPenColor(ALIVE_CELL_COLOR);
				}
				else {
					StdDraw.setPenColor(EMPY_CELL_COLOR);
				}
				StdDraw.filledSquare(i, j, CELL_RADIUS);
			}
		}
	}


	public void setAliveUserConfig(String userConfig){
		/*
		* "0,0:0,1:0,2:1,1:-1,2"
		*/
		for(String cord : userConfig.split(":")){
			String[] xy = cord.split(",");
			int x = Integer.parseInt(xy[0]);
			int y = Integer.parseInt(xy[1]);
			aliveCells.add(x+","+y);
		}
	}
	
	public void flipCells(){
		int flooredX = (int)Math.round(StdDraw.mouseX());
		int flooredY = (int)Math.round(StdDraw.mouseY());
		/*int flooredX = StdDraw.mouseX() < 0 ? (int)(StdDraw.mouseX() - 1) : (int)(StdDraw.mouseX());
		int flooredY = StdDraw.mouseY() < 0 ? (int)(StdDraw.mouseY() - 1) : (int)(StdDraw.mouseY());*/
		String cellKey = flooredX+","+flooredY;
		if(aliveCells.contains(cellKey)){
			aliveCells.remove(cellKey);
		}
		else{
			aliveCells.add(cellKey);
		}
		
	}

	public boolean spacebarPressed(){
		if(!StdDraw.isKeyPressed(SPACEBAR_CODE) && pressingSpace){
			pressingSpace = false;
		}
		else if(StdDraw.isKeyPressed(SPACEBAR_CODE) && !pressingSpace){
			pressingSpace = true;
			return true;
		}
		return false;
	}

	public boolean mousePressed(){
		if(StdDraw.isMousePressed() && !pressingMouse){
			pressingMouse = true;
			return true;
		}
		else if(!StdDraw.isMousePressed() && pressingMouse){
			pressingMouse = false;
		}
		return false;
	}

	public void gameState(){
		while(true){
			StdDraw.clear();
			updateView();
			drawBoard();
			if(spacebarPressed()){
				gameRunning = !gameRunning;
			};
			if(gameRunning){
				StdDraw.setPenColor(TEXT_COLOR);
				gameRunning = updateCells();
				if(!gameRunning){gameHalted = true;}
				StdDraw.textLeft(boardCenter[0]-.9*xScaleRadius, boardCenter[1]-.85*yScaleRadius, "Game Alive");
				if(mousePressed()) {
					aliveCells.clear();
					gameRunning = false;
				}
			}
			else{
				StdDraw.setPenColor(TEXT_COLOR);
				if(gameHalted){
					StdDraw.textLeft(boardCenter[0]-.9*xScaleRadius, boardCenter[1]-.85*yScaleRadius, "Game Halted");
				}
				else{
					StdDraw.textLeft(boardCenter[0]-.9*xScaleRadius, boardCenter[1]-.85*yScaleRadius, "Game Paused");
				}
				if(mousePressed()) {
					flipCells();
					if(gameHalted){gameHalted = false;}
					iterationNum = 0;
				}
				if(StdDraw.isKeyPressed(KeyEvent.VK_I)) {
					String inputConfig = BoardConfig.handleConsoleInput(hashCurrentConfiguration());
					if(inputConfig != null && inputConfig.length() > 0){
						aliveCells.clear();
						setAliveUserConfig(inputConfig);
						gameHalted = false;
						iterationNum = 0;
					}
				}
			}
			if(iterationNum > 0){
				StdDraw.textLeft(boardCenter[0]-.9*xScaleRadius, boardCenter[1]-.95*yScaleRadius, "Iteration: " + iterationNum);
			}
			StdDraw.textLeft(boardCenter[0]-.9*xScaleRadius, boardCenter[1]-.9*yScaleRadius, "Alive Cells: " + aliveCells.size());
			
			StdDraw.pause(framerateMs);
			StdDraw.show();
		}
	}

	public void gameOfLife() {
		gameState();
	}

	public static void main(String[] args) {
		new GameOfLife();
	}
}


