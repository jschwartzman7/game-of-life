import java.awt.Color;
import java.awt.Event;
import java.awt.RenderingHints.Key;
import java.awt.event.KeyEvent;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Scanner;

import org.apache.commons.lang3.StringUtils;

import edu.princeton.cs.introcs.StdDraw;

public class GameOfLife {
/*
 * User configurability should be available by default, 
 * and it should add the input cells to the board without running the game
 * 
 * 
 */

	final double DEFAULT_SCALE = 5;
	final int SPACEBAR_CODE = 32;
	final double SCALE_SENSITIVITY = 1.1;
	final double MOVE_SENSITIVITY = .4;
	final double CELL_RADIUS = 0.49;
	double xScale = DEFAULT_SCALE;
	double yScale = DEFAULT_SCALE;
	Color backgroundColor = new Color(0, 0, 0);
	Color aliveCellColor = new Color(0, 255, 0);
	Color deadCellColor = new Color(0, 0, 175);
	double[] boardCenter = new double[]{0,0};
	HashMap<String, Boolean> aliveCells = new HashMap<String, Boolean>(); // i + "" + j
	int iterationNum = 0;
	boolean mouseDown = false;
	boolean spacebarDown = false;
	boolean gameHalted = false;
	LinkedList<String> userConfiguration = new LinkedList<String>();
    Scanner scanner = new Scanner(System.in);

	public GameOfLife() {
		StdDraw.enableDoubleBuffering();
	}

	public void resetView() {
		boardCenter[0] = 0;
		boardCenter[1] = 0;
		xScale = DEFAULT_SCALE;
		yScale = DEFAULT_SCALE;
		StdDraw.setXscale(boardCenter[0] - xScale, boardCenter[0] + xScale);
		StdDraw.setYscale(boardCenter[1] - yScale, boardCenter[1] + yScale);
	}

	public double[] parseCellKey(String key){
		String[] cord = key.split(",");
		return new double[]{Double.valueOf(cord[0]), Double.valueOf(cord[1])};
	}

	public boolean updateCells() {
		HashSet<String> checkSet = new HashSet<String>(aliveCells.keySet());
		for(String position : aliveCells.keySet()){
			double[] cord = parseCellKey(position);
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
			if(aliveCells.get(position) != null && (liveNeighbors(position) < 2 || liveNeighbors(position) > 3)) {
				updateAliveSet.add(position);
			}
			else if(aliveCells.get(position) == null && liveNeighbors(position) == 3) {
				updateAliveSet.add(position);
			}
		}
		if(updateAliveSet.isEmpty()) {
			return false;
		}
		else {
			for(String position : updateAliveSet) {
				aliveCells.put(position, aliveCells.get(position) == null ? true : null);
			}
			++iterationNum;
			return true;
		}

	}

	public int liveNeighbors(String position) {
		double[] cord = parseCellKey(position);
		int liveNeighbors = 0;
		for(int x = -1; x <= 1; ++x) {
			for(int y = -1; y <= 1; ++y) {
				if(x != 0 || y != 0) {
					if(aliveCells.get((cord[0] + x) +","+ (cord[1] + y)) != null) {
						++liveNeighbors;
					}
				}
			}
		}
		return liveNeighbors;
	}

	public void updateView() {
		if(StdDraw.isKeyPressed(KeyEvent.VK_R)) {
			resetView();
		}

		if(StdDraw.isKeyPressed(KeyEvent.VK_Q) && xScale > 1 && yScale > 1){ // zoom in
			xScale /= SCALE_SENSITIVITY;
			yScale /= SCALE_SENSITIVITY;
		}
		else if(StdDraw.isKeyPressed(KeyEvent.VK_E)){ // zoom out
			xScale *= SCALE_SENSITIVITY;
			yScale *= SCALE_SENSITIVITY;
		}

		if(StdDraw.isKeyPressed(KeyEvent.VK_DOWN) && yScale > 1){ // zoom y in
			yScale /= SCALE_SENSITIVITY;
		}
		else if(StdDraw.isKeyPressed(KeyEvent.VK_UP)){ // zoom y out
			yScale *= SCALE_SENSITIVITY;
		}

		if(StdDraw.isKeyPressed(KeyEvent.VK_LEFT) && xScale > 1){ // zoom x in
			xScale /= SCALE_SENSITIVITY;
		}
		else if(StdDraw.isKeyPressed(KeyEvent.VK_RIGHT)){ // zoom x out
			xScale *= SCALE_SENSITIVITY;
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

	public void drawBoard() {
		StdDraw.setPenColor(backgroundColor);
		StdDraw.filledSquare(boardCenter[0], boardCenter[1], Math.max(xScale, yScale));
		for(double i = (int)(boardCenter[0] - xScale)-0.5; i <= boardCenter[0] + xScale+0.5; ++i) {
			for(double j = (int)(boardCenter[1] - yScale)-0.5; j <= boardCenter[1] + yScale+0.5; ++j) {
				if(aliveCells.get(i +","+ j) != null) {
					StdDraw.setPenColor(aliveCellColor);
				}
				else {
					StdDraw.setPenColor(deadCellColor);
				}
				StdDraw.filledSquare(i, j, CELL_RADIUS);
			}
		}
	}

	/*public void drawBoardInfo(int state) {
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
	}*/
	
	public void addUserConfig(LinkedList<String> getUserConfig){
		/*
		left justify rows
		* 001
		* 101
		* 1110
		*/
		if(getUserConfig.isEmpty()){
			return;
		}
		int height = getUserConfig.size();
		int length = getUserConfig.get(0).length();
		for(String row : getUserConfig){
			if(row.length() > length){
				length = row.length();
			}
		}
		int x = -(length/2);
		int y = -(height/2);
		for(String row : getUserConfig){
			for(char val : row.toCharArray()){
				if(val=='1'){
					aliveCells.put((x+0.5)+","+(y+0.5), true);
				}
				++x;
			}
			--y;
			x = -(length/2);
		}
	}


	public void flipCells(){
		int flooredX = StdDraw.mouseX() < 0 ? (int)(StdDraw.mouseX() - 1) : (int)(StdDraw.mouseX());
		int flooredY = StdDraw.mouseY() < 0 ? (int)(StdDraw.mouseY() - 1) : (int)(StdDraw.mouseY());
		aliveCells.put((flooredX+.5) +","+ (flooredY+.5), aliveCells.get((flooredX+.5) +","+ (flooredY+.5)) == null ? true : null);
		
	}

	public boolean getUserConfig(){
		while(true){
            String rowConfig = scanner.nextLine();
            if(rowConfig.toLowerCase().contains("done")){
                addUserConfig(userConfiguration);
				userConfiguration.clear();
				return true;
            }
            if(StringUtils.containsOnly(rowConfig, "01")){
                userConfiguration.add(rowConfig);
            }
            else{
                System.out.println("Line not recognized: invalid character(s)");
            }
            // assert row only contains "1" and "0" or "x" and "o"
            // if row is "done" end
        }
	}

	public boolean spacebarPressed(){
		if(!StdDraw.isKeyPressed(SPACEBAR_CODE) && spacebarDown){
			spacebarDown = false;
		}
		else if(StdDraw.isKeyPressed(SPACEBAR_CODE) && !spacebarDown){
			spacebarDown = true;
			return true;
		}
		return false;
	}

	public boolean mousePressed(){
		if(StdDraw.isMousePressed() && !mouseDown){
			mouseDown = true;
			return true;
		}
		else if(!StdDraw.isMousePressed() && mouseDown){
			mouseDown = false;
		}
		return false;
	}


	public void gameRunning(){
		while(updateCells()){
			StdDraw.clear();
			updateView();
			drawBoard();
			if(spacebarPressed()){
				gamePaused();
			};
			if(mousePressed()) {
				resetView();
				aliveCells.clear();
				gamePaused();
			}
			StdDraw.show(100);
		}
		gameHalted();
	}
	
	public void gameHalted(){
		while(true) {
			StdDraw.clear();
			updateView();
			if(mousePressed()) {
				flipCells();
				break;
			}
			if(StdDraw.isKeyPressed(KeyEvent.VK_I)) {
				getUserConfig();
				break;
			}
			
			drawBoard();

			//drawBoardInfo(3);
			StdDraw.show(50);
		}
		gamePaused();
	}

	public void gamePaused(){
		while(true){
			StdDraw.clear();
			
			updateView();
			if(mousePressed()) {
				flipCells();
			}
			if(StdDraw.isKeyPressed(KeyEvent.VK_I)) {
				System.out.println("Type the initial configuration row by row.  Type 'done' when finished	");
				getUserConfig();
			}
			if(StdDraw.isKeyPressed(KeyEvent.VK_I)){
				System.out.println("u should type something");
				String rowConfig = scanner.nextLine();
				System.out.println("u typed: " + rowConfig);
			}
			
			drawBoard();
			
			if(spacebarPressed()){
				break;
			};
			StdDraw.show(100);
		}
		gameRunning();
		

	}

	public void gameOfLife() {
		gamePaused();
		

	}

	public static void main(String[] args) {
		GameOfLife Game = new GameOfLife();
		Game.gameOfLife();
	}
}


