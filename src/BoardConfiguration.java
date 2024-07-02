import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Scanner;
import org.apache.commons.lang3.StringUtils;

import edu.princeton.cs.introcs.StdDraw;



public class BoardConfiguration{

    public static final String Glider_Spawner1 = "abcdefghijklmnopqrstuvwx1,abcdefghijklmnopqrstuv1x1,abcdefghijkl11opqrst11wxyzabcdefgh11,abcdefghijk1mno1qrst11wxyzabcdefgh11,11cdefghik1mnopq1stu11,11cdefghik1mno1q11tuvw1y1,abcdefghij1lmnop1rstuvwx1,abcdefghijl1nop1,abcdefghijlm11";
    public static final String g1 = "24,0:22,-1:24,-1:12,-2:13,-2:20,-2:21,-2:34,-2:35,-2:11,-3:15,-3:20,-3:21,-3:34,-3:35,-3:0,-4:1,-4:10,-4:16,-4:20,-4:21,-4:0,-5:1,-5:10,-5:14,-5:16,-5:17,-5:22,-5:24,-5:10,-6:16,-6:24,-6:11,-7:15,-7:12,-8:13,-8";
    public static final String Glider_Spawner2 = "11cdefg11,11cdefg11,,abcd11,abcd11,,,,,abcdefghijklmnopqrstuv11y11,abcdefghijklmnopqrstu1wxyza1,abcdefghijklmnopqrstu1wxyzab1de11,abcdefghijklmnopqrstu111yza1cde11,abcdefghijklmnopqrstuvwxyz1,,,,abcdefghijklmnopqrst11,abcdefghijklmnopqrst1,abcdefghijklmnopqrstu111,abcdefghijklmnopqrstuvw1";
    public static final String Life_Generator = "a11,11,a1";
    public static final String Slow_Extinction = "abcdef1,11,a1cde111";
    public static final String Big_Generator = "a1,abc1,11cd111";
    
    HashMap<String, String> preCodedConfigs;
    HashMap<String, String> savedConfigs;
	StringBuilder userConfiguration;
    private Scanner scanner;

    public BoardConfiguration(){
        this.userConfiguration = new StringBuilder();
        this.scanner = new Scanner(System.in);
        this.preCodedConfigs = new HashMap<String, String>();
        this.preCodedConfigs.put("Spawner 1", Glider_Spawner1);
        this.preCodedConfigs.put("Spawner 2", Glider_Spawner2);
        this.preCodedConfigs.put("rpent", Life_Generator);
        this.preCodedConfigs.put("diehard", Slow_Extinction);
        this.preCodedConfigs.put("acorn", Big_Generator);
        this.savedConfigs = new HashMap<String, String>();
    }

    public static String convertInputConfigToHash(String userConfig){
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

	public String handleConsoleInput(String currentConfiguration){
		System.out.println("Input alive cell configuration row by row");
		System.out.println("Type '1' for alive cells, any other character for empty cells");
		System.out.println("Type 'quit' to discard configuration and return");
		System.out.println("Type 'done' to finish and add configuration to grid");
		while(true){
            String inputRow = scanner.nextLine();
			String[] splitInputRow = inputRow.split(",");
			for(String row : splitInputRow){
                row = row.toLowerCase();
				if(row.contains("quit")){
					userConfiguration = new StringBuilder();
					return null;
				}
				if(row.contains("done")){
					return convertInputConfigToHash(userConfiguration.toString());
				}
                if(row.contains("save")){
                    if(currentConfiguration == null)
                        System.out.println("No current configuration to save");
                    else{
                        System.out.println("name?");
                        String name = scanner.nextLine();
                        savedConfigs.put(name, currentConfiguration);
                        return currentConfiguration;
                    }
                }
                for(String key : preCodedConfigs.keySet()){
                    if(row.contains(key.toLowerCase())){
                        return convertInputConfigToHash(preCodedConfigs.get(key));
                    }
                }
                for(String key : savedConfigs.keySet()){
                    if(row.contains(key.toLowerCase())){
                        return savedConfigs.get(key);
                    }
                }
                userConfiguration.append(":");
				userConfiguration.append(row);
			}
        }
	}


    public static void main(String[] args) {
       /*HashSet<String> testSet = new HashSet<String>();
       String s = "abbc";
       testSet.add(s);
       testSet.add("abbc");
       String t = "abbc";
       testSet.add(t);
       System.out.println(testSet.size());*/

       
    
    }


}