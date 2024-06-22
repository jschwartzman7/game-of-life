import java.util.LinkedList;
import java.util.Scanner;
import org.apache.commons.lang3.StringUtils;

import edu.princeton.cs.introcs.StdDraw;

public class ConfigurationInput{

    private LinkedList<String> userConfiguration;
    private Scanner scanner;

    public ConfigurationInput(){
        this.userConfiguration = new LinkedList<String>();
        this.scanner = new Scanner(System.in);
        System.out.println("Type the initial configuration row by row.  Type 'done' when finished");
    }


    public static LinkedList<String> getInputConfig(){
        LinkedList<String> inputConfig = new LinkedList<String>();
        Scanner scanner = new Scanner(System.in);
        System.out.println(scanner.reset());
        System.out.println("Type the initial configuration row by row.  Type 'done' when finished");
        while(true){
            System.out.println("1");
            String rowConfig = scanner.nextLine();
            System.out.println("2");
            if(rowConfig.toLowerCase().contains("done")){
                scanner.close();
                return inputConfig;
            }
            if(StringUtils.containsOnly(rowConfig, "01")){
                inputConfig.add(rowConfig);
            }
            else{
                System.out.println("Line not recognized: invalid character(s)");
            }
            // assert row only contains "1" and "0" or "x" and "o"
            // if row is "done" end
        }
    }

    public LinkedList<String> getUserConfig(){
        String rowConfig = scanner.nextLine();
        if(rowConfig.toLowerCase().contains("done")){
            return userConfiguration;
        }
        if(StringUtils.containsOnly(rowConfig, "01")){
            userConfiguration.add(rowConfig);
        }
        else{
            System.out.println("Line not recognized: invalid character(s)");
        }
        return null;
    }

    public void resetConfig(){
        userConfiguration.clear();
        System.out.println("Type the initial configuration row by row.  Type 'done' when finished");
    }
/*
 * 
 * 0 0 1
 * 1 0 1
 * 1 1 1
 * 
 * 
 * 
 */

    public void main(String[] args) {
        StdDraw.enableDoubleBuffering();
        boolean out = false;
        while(!out){
            StdDraw.clear();
            StdDraw.filledCircle(.5, 0.5, .3);
            StdDraw.filledSquare(StdDraw.mouseX(), StdDraw.mouseY(), .05);
            if(StdDraw.isMousePressed()){
                out = true;
            }
            StdDraw.show();
        }
        System.out.println("Over");
    
    }


}