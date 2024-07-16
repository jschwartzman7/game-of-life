import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Scanner;
import org.apache.commons.lang3.StringUtils;

import edu.princeton.cs.introcs.StdDraw;



public class BoardConfiguration{

    public static final String Glider_Spawner1 = "24,0:22,-1:24,-1:12,-2:13,-2:20,-2:21,-2:34,-2:35,-2:11,-3:15,-3:20,-3:21,-3:34,-3:35,-3:0,-4:1,-4:10,-4:16,-4:20,-4:21,-4:0,-5:1,-5:10,-5:14,-5:16,-5:17,-5:22,-5:24,-5:10,-6:16,-6:24,-6:11,-7:15,-7:12,-8:13,-8";
    public static final String Glider_Spawner2 = "0,0:1,0:7,0:8,0:0,-1:1,-1:7,-1:8,-1:4,-3:5,-3:4,-4:5,-4:22,-9:23,-9:25,-9:26,-9:21,-10:27,-10:21,-11:28,-11:31,-11:32,-11:21,-12:22,-12:23,-12:27,-12:31,-12:32,-12:26,-13:20,-17:21,-17:20,-18:21,-19:22,-19:23,-19:23,-20";
    public static final String Life_Generator = "1,0:2,0:0,-1:1,-1:1,-2";
    public static final String Slow_Extinction = "6,0:0,-1:1,-1:1,-2:5,-2:6,-2:7,-2";
    public static final String Big_Generator = "1,0:3,-1:0,-2:1,-2:4,-2:5,-2:6,-2";
    public static final String[] highlightedPatterns = {"5x5infinitegrowth", "zweiback", "zweiback2", "339p7h1v0", "verylongshipgenerator", "67p222"};
    private boolean useImportedPatterns;
    HashMap<String, String> savedConfigs;
    private Scanner inputScanner;

    public BoardConfiguration(){
        this.inputScanner = new Scanner(System.in);
        this.savedConfigs = new HashMap<String, String>();
        this.savedConfigs.put("Spawner1", Glider_Spawner1);
        this.savedConfigs.put("Spawner2", Glider_Spawner2);
        this.savedConfigs.put("bigGrowth", Life_Generator);
        this.savedConfigs.put("diehard", Slow_Extinction);
        this.savedConfigs.put("acorn", Big_Generator);
        this.useImportedPatterns = true;
        if(useImportedPatterns){
            for(String name : highlightedPatterns){
                try {
                    String fileName = "ImportedPatterns/" + name + ".txt";
                    File myObj = new File(fileName);
                    Scanner fileScanner = new Scanner(myObj);
                    savedConfigs.put(name, convertInputConfigToHash(fileScannerToString(fileScanner)));
                    fileScanner.close();
                } 
                catch (FileNotFoundException e) {
                    System.out.println("An error occurred importing patterns from ImportedPatterns folder at file " + name + ".txt");
                    e.printStackTrace();
                }
            }
        }
    }

    public String fileScannerToString(Scanner fileScanner){
        /*
         * Scanner containing OO...OO.OO..OO file -> String "11000110110011"
         */
        StringBuilder parsedFile = new StringBuilder();
        while(fileScanner.hasNextLine()){
            String currentLine = fileScanner.nextLine();
            currentLine = currentLine.replace("O","1");
            currentLine = currentLine.replace(".","0");
            parsedFile.append(currentLine+",");
        }
        fileScanner.close();
        return parsedFile.toString();
    }

    public static String convertInputConfigToHash(String userConfig){
        /*
         * Ex: "abcdef1,11,a1cde111" -> "6,0:0,-1:1,-1:1,-2:5,-2:6,-2:7,-2"
         * Ex: "0000001,11,00000111" -> "6,0:0,-1:1,-1:1,-2:5,-2:6,-2:7,-2"
         * Ex: fileScannerToString(fileScanner) -> "6,0:0,-1:1,-1:1,-2:5,-2:6,-2:7,-2"
         * 
         * userConfig: board configuration typed by user
         * return: coordinate hash representation of board
         */
        StringBuilder s = new StringBuilder();
        int x = 0;
        int y = 0;
        for(String row : userConfig.split(",")){
            for(char val : row.toCharArray()){
                if(val=='1'){
                    s.append(x+","+y+":");
                }
                ++x;
            }
            --y;
            x = 0;
        }
        return s.toString();
    }

	public String handleConsoleInput(String currentBoardHash){
        System.out.println("A row is a sequence of characters within a linebreak or comma ','");
		System.out.println("'quit' in a row to discard and return");
		System.out.println("'done' in a row to apply input configuration to board");
		System.out.println("'save' in a row to save current board configuration with a name");
		System.out.println("'view' in a row to view saved and preset configurations");
		System.out.println("Input alive cell configuration row by row, or separated by commas");
		System.out.println("Type '1' for alive cells, any other character for empty cells");
        StringBuilder userConfiguration = new StringBuilder();
		while(true){
            System.out.print("> ");
            String inputRow = inputScanner.nextLine();
			String[] splitInputRow = inputRow.split(",");
			for(String row : splitInputRow){
                row = row.toLowerCase();
				if(row.contains("quit")){
					return "";
				}
				else if(row.contains("done")){
					return convertInputConfigToHash(userConfiguration.toString());
				}
                else if(row.contains("save")){
                    if(currentBoardHash.length() > 0){
                        System.out.println("name?");
                        String name = inputScanner.nextLine();
                        savedConfigs.put(name, currentBoardHash);
                    }
                    else{
                        System.out.println("No current configuration to save");
                    }
                }
                
                else if(row.contains("view")){
                    for(String key : savedConfigs.keySet()){
                        System.out.println(key);
                    }
                }
                else{
                    for(String key : savedConfigs.keySet()){
                        if(row.contains(key.toLowerCase())){
                            return savedConfigs.get(key);
                        }
                    }
                    if(useImportedPatterns){
                        File ImportedPatterns = new File("ImportedPatterns");
                        for(File file : ImportedPatterns.listFiles()){
                            if(row.equals(file.getName().replace(".txt","").toLowerCase())){
                                try {
                                    Scanner fileScanner = new Scanner(file);
                                    return convertInputConfigToHash(fileScannerToString(fileScanner));
                                } 
                                catch (FileNotFoundException e) {
                                    System.out.println("An error occurred importing patterns from ImportedPatterns folder at file " + file.getName());
                                    e.printStackTrace();
                                }
                            }
                        }
                    }
				    userConfiguration.append(row+",");
                }
			}
        }
	}


    public static void main(String[] args) {
        //GameOfLife game = new GameOfLife();
        String s = "";
        System.out.println(s.split(":")[1]);
        
    }
}