import java.io.*;
import java.util.*; // Using List, ArrayList, and Arrays

public class Main {
    public static void main(String[] br) throws IOException {

        String commandLine;     //this is the same as my "Scanner something = blah blah", point being that this is where inputs are being placed
        BufferedReader console = new BufferedReader(new InputStreamReader(System.in)); //assuming we are making a Shell then this is for reading console things

        // Keep track of the current directory (starts in where the Java program was run)
        File currentDir = new File(System.getProperty("user.dir"));

        // We break out with <control><C>       //Note this section is from the .java the professor gave us
        while (true) {
            // Read what the user typed
            System.out.print(currentDir.getAbsolutePath() + "> ");  // Shows current working directory like a real shell
            commandLine = console.readLine();                        //user entering something and the "console" reading it

            // If they entered just whitespace or return, skip
            if (commandLine == null || commandLine.trim().equals("")) {
                continue;
            }

            // Split into command and arguments. So, if someone types "assignment Ass.java" will turn into "assignment, Ass.java"
            String[] tokens = commandLine.trim().split("\\s+");

            String command = tokens[0];  //"First token is always the command name" so something like npm run start (Next.js command to run a project) the name is npm    
            List<String> commandList = new ArrayList<>(Arrays.asList(tokens));  //Tokens -> List 

            // Handle "cd" (change directory)
            if (command.equalsIgnoreCase("cd")) {
                if (tokens.length < 2) { // If no directory is provided, show usage
                    System.out.println(currentDir.getAbsolutePath());
                    continue;
                }

                // Create a new path relative to the current directory
                File newDir = new File(currentDir, tokens[1]);

                // Check if the directory exists and is valid
                if (newDir.exists() && newDir.isDirectory()) {
                    // Update the current directory
                    currentDir = newDir.getAbsoluteFile();
                } else {
                    System.out.println("Nah... doesn't exist: " + tokens[1]);
                }
                continue; // Skip creating a ProcessBuilder since "cd" isn't a system command
            }

            try {
                List<String> actualCommandTest = new ArrayList<>();         //I will admit this section I asked ai to help me since I only know Windows commands i asked an ai to config it for me within this project
                actualCommandTest.add("cmd.exe");                       //I will mention this later in the project in a comment but I dont know much about Terminal Commands but I do know three so those are the three here
                actualCommandTest.add("/c");                           // dir, type, and cd. cd is the only one I use lots more than the others but cd allows for the user to move around the project cd main to cd main/folder1
                actualCommandTest.addAll(commandList);                  // dir will show all the stuff in the project that is reach able, so lets say that you have a folder and dont wanna open it you can just type dir and it shows you it (I dont use it unless the project is tiny)
                // actualCommandTest.add(filePath);                    // type will write whatever is inside of the file so lets say you "type test.txt" it'll write whats in it.
                for (int innie = 1; innie< tokens.length; innie++){                         
                    String blah = tokens[innie].replace("/", File.separator);
                    actualCommandTest.add(blah);
                }


                // Build and execute external command
                ProcessBuilder processBuild = new ProcessBuilder(actualCommandTest);
                processBuild.directory(currentDir); // Use our tracked directory, not the system default
                processBuild.redirectErrorStream(true);

                Process process = processBuild.start();

                BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));

                String line;
                while ((line = reader.readLine()) != null) {
                    System.out.println(line);
                }

                process.waitFor();

            } catch (Exception e) {
                System.out.println("Error executing command: " + command);
            }
        }
    }
}



/* 
 * Okay everything is working now
 *  Im not sure if all commands work pre-say but point is that I got the main three that I know and have used for the most part
 * Typing "dir" will give you the directory of the current project and has the little dots that React projects have like: ./blah/another or ../../blah
 * Typing "type test.txt" will show you whats inside of the file. Note that this stuff works with and without running java but if you do run java it will show double
 * Typeing "cd fakePath" will put you inside of the "fakePath" folder. After that you can just write "type test2.txt" and it'll show you the stuff thats in the Test 2 file.
  */