import java.awt.Color;
import java.awt.Event;
import java.awt.Font;
import java.awt.RenderingHints.Key;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Scanner;
import java.util.Set;
import java.util.Iterator;
import org.apache.commons.lang3.StringUtils;

import edu.princeton.cs.introcs.StdDraw;
import java.util.Timer;
public class GameOfLife {

 	static final int SPACEBAR_CODE = 32;
	final double DEFAULT_SCALE;
	final double SCALE_SENSITIVITY;
	final double MOVE_SENSITIVITY;
	final double CELL_RADIUS;
	final Color BACKGROUND_COLOR;
	final Color ALIVE_CELL_COLOR;
	final Color EMPY_CELL_COLOR;
	final Color TEXT_COLOR;
	final int FRAMERATE_MS;
	final int DEFAULT_GAME_DELAY_FACTOR;
	private double xScaleRadius;
	private double yScaleRadius;
	private double[] boardCenter;
	private int gameDelayFactor;
	private HashSet<String> aliveCells;
	private int iterationNum;
	private BoardConfiguration BoardConfig;

	// Game state variables
	private boolean pressingMouse;
	private boolean pressingSpace;
	private boolean pushinP;
	private boolean gameHalted;
	private boolean gameRunning;

	public GameOfLife() {
		DEFAULT_SCALE = 7;
		SCALE_SENSITIVITY = 1.02;
		MOVE_SENSITIVITY = .03;
		CELL_RADIUS = 0.49;
		BACKGROUND_COLOR = new Color(0, 0, 0);
		ALIVE_CELL_COLOR = new Color(0, 255, 0);
		EMPY_CELL_COLOR = new Color(0, 0, 175);
		TEXT_COLOR = new Color(255, 255, 255);
		FRAMERATE_MS = 10;
		DEFAULT_GAME_DELAY_FACTOR = 4;
		xScaleRadius = DEFAULT_SCALE;
		yScaleRadius = DEFAULT_SCALE;
		boardCenter = new double[]{0,0};
		gameDelayFactor = DEFAULT_GAME_DELAY_FACTOR;
		aliveCells = new HashSet<String>(); // i + "" + j
		iterationNum = 0;
		BoardConfig = new BoardConfiguration();
		pressingMouse = false;
		pressingSpace = false;
		pushinP = false;
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
			currentBoardHash.append(cell+":");
		}
		return currentBoardHash.toString();
	}

	public void setAliveUserConfig(String userConfig){
		/*
		* "0,0:0,1:0,2:1,1:-1,2"
		*/
		for(String cord : userConfig.split(":")){
			int[] xy = parseCellKey(cord);
			aliveCells.add(xy[0]+","+xy[1]);
		}
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

		HashSet<String> cellsToLife = new HashSet<String>();
		HashSet<String> cellsToDeath = new HashSet<String>();
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
			int[] cord = parseCellKey(position);
			int numLiveNeighbors = liveNeighbors(cord);
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

	public HashSet<String> getUnexploredNeighbors(int[] cellCord, HashSet<String> exploredCells){
		int x = cellCord[0];
		int y = cellCord[1];
		HashSet<String> potentialNeighbors = new HashSet<String>();
		for(String cell : new String[]{(x-1)+","+(y-1), x+","+(y-1), (x+1)+","+(y-1), (x-1)+","+y, x+","+y, (x+1)+","+y, (x-1)+","+(y+1), x+","+(y+1), (x+1)+","+(y+1)}){
			if(!exploredCells.contains(cell)){
				potentialNeighbors.add(cell);
			}
		};
		return potentialNeighbors;
	}

	public boolean updateCellsV2(){
		// bfs search for all cells that need to be checked
		HashSet<String> updatedCells = new HashSet<String>();
		HashSet<String> cellsToLife = new HashSet<String>();
		HashSet<String> cellsToDeath = new HashSet<String>();
		for(String curCell : aliveCells){
			HashSet<String> uncheckedNeighbors = getUnexploredNeighbors(parseCellKey(curCell), updatedCells);
			if(uncheckedNeighbors.isEmpty()) {
				continue;
			}
			for(String neighbor : uncheckedNeighbors){
				updatedCells.add(neighbor);
				int numLiveNeighbors = liveNeighbors(parseCellKey(neighbor));
				if(aliveCells.contains(neighbor)){
					if(numLiveNeighbors < 2 || numLiveNeighbors > 3) {
						cellsToDeath.add(neighbor);
					}
				}
				else if(numLiveNeighbors == 3) {
					cellsToLife.add(neighbor);
				}
			}
		}

		if(cellsToLife.isEmpty() && cellsToDeath.isEmpty()) {
			return false;
		}
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

	public int liveNeighbors(int[] cord) {
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
			aliveCells.clear();
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


	
	public void flipCell(){
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

	public boolean pPushed(){
		if(!StdDraw.isKeyPressed(KeyEvent.VK_P) && pushinP){
			pushinP = false;
		}
		else if(StdDraw.isKeyPressed(KeyEvent.VK_P) && !pushinP){
			pushinP = true;
			return true;
		}
		return false;
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
		int ticks = 0;
		while(true){
			++ticks;
			StdDraw.clear();
			updateView();
			drawBoard();
			if(spacebarPressed()){
				gameRunning = !gameRunning;
			};
			if(gameRunning){
				StdDraw.setPenColor(TEXT_COLOR);

				//long t = System.nanoTime();
				if(ticks % gameDelayFactor == 0){
					//long t = System.nanoTime();
					gameRunning = updateCellsV2();
					//System.out.println((System.nanoTime() - t));
				}
				//System.out.println((System.nanoTime() - t));
				if(!gameRunning){gameHalted = true;}
				StdDraw.textLeft(boardCenter[0]-.95*xScaleRadius, boardCenter[1]-.75*yScaleRadius, "Game Alive");
				StdDraw.textLeft(boardCenter[0]-.95*xScaleRadius, boardCenter[1]-.65*yScaleRadius, "Speed: " + ((int)(100*DEFAULT_GAME_DELAY_FACTOR/gameDelayFactor))/100.0 + "x");
				if(mousePressed()) {
					aliveCells.clear();
					gameRunning = false;
				}
				if(pPushed()) {
					if(gameDelayFactor <= 1){
						gameDelayFactor = DEFAULT_GAME_DELAY_FACTOR * 4;
					}
					else{
						gameDelayFactor /= 2;
					}
				}
			}
			else{
				StdDraw.setPenColor(TEXT_COLOR);
				if(gameHalted){StdDraw.textLeft(boardCenter[0]-.95*xScaleRadius, boardCenter[1]-.75*yScaleRadius, "Game Halted");}
				else{StdDraw.textLeft(boardCenter[0]-.95*xScaleRadius, boardCenter[1]-.75*yScaleRadius, "Game Paused");}
				if(mousePressed()) {
					flipCell();
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
				StdDraw.textLeft(boardCenter[0]-.95*xScaleRadius, boardCenter[1]-.955*yScaleRadius, "Iteration: " + iterationNum);
			}
			StdDraw.textLeft(boardCenter[0]-.95*xScaleRadius, boardCenter[1]-.85*yScaleRadius, "Alive Cells: " + aliveCells.size());
			StdDraw.pause(FRAMERATE_MS);
			StdDraw.show();
		}
	}

	public void gameOfLife() {
		gameState();
	}

	public static void main(String[] args) {
		new GameOfLife();
		/*String agarHash = BoardConfiguration.ba2;
		GameOfLife game = new GameOfLife();
		game.setAliveUserConfig(agarHash);
		for(int i = 0; i < 1000; ++i){
			long t = System.nanoTime();
			game.updateCells();
			System.out.println((System.nanoTime() - t));
		}*/
	}
}


