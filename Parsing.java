import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * The `Parsing` class is responsible for parsing process information from text files and creating `Process` objects.
 * The class provides a single method, `parseProcessFromFile()`, which reads a file containing process information and
 * returns a `Process` object representing the parsed process.
 *
 * The file format expected by the `parseProcessFromFile()` method is the bnf form defined on the project pdf instructions
 * If the file is successfully parsed, the `parseProcessFromFile()` method will return a `Process` object containing the
 * process ID and the list of instructions. If there are any errors during the parsing process, the method will return
 * `null`.
 *
 * @author Maria Vitoria
 */

public class Parsing {

    /**
     * Parses a process from the specified file and returns a `Process` object representing the parsed process.
     *
     * @param fileName the name of the file containing the process information
     * @return a `Process` object representing the parsed process, or `null` if there was an error during parsing
     */

    public Process parseProcessFromFile(String fileName) {
        
        try (BufferedReader reader = new BufferedReader(new FileReader("listaDeProcessos/" + fileName))) {
            String line = reader.readLine();
            if (line == null || !line.startsWith("program ")) {
                System.out.println("Error: Missing or invalid program line in " + fileName);
                return null;
            }

            String processId = line.split(" ")[1];
            List<String> instructions = new ArrayList<>();
            boolean inBlock = false;

            while ((line = reader.readLine()) != null) {
                line = line.trim();
                if (line.equals("begin")) {
                    if (inBlock) {
                        System.out.println("Error: Nested begin in " + fileName);
                        return null;
                    }
                    inBlock = true;
                } else if (line.equals("end")) {
                    if (!inBlock) {
                        System.out.println("Error: End without begin in " + fileName);
                        return null;
                    }
                    inBlock = false;
                    break;
                } else if (line.equals("execute")) {
                    if (!inBlock) {
                        System.out.println("Error: execute outside of begin/end block in " + fileName);
                        return null;
                    }
                    instructions.add("execute");
                } else if (line.startsWith("block ")) {
                    if (!inBlock) {
                        System.out.println("Error: block outside of begin/end block in " + fileName);
                        return null;
                    }
                    String instruction = line.substring(6).trim();
                    if (instruction.isEmpty()) {
                        System.out.println("Error: Empty block instruction in " + fileName);
                        return null;
                    }
                    instructions.add("block " + instruction);
                } else if (!line.isEmpty()) {
                    System.out.println("Error: Invalid line in " + fileName + ": " + line);
                    return null;
                }
            }

            if (inBlock) {
                System.out.println("Error: Missing end in " + fileName);
                return null;
            }

            return new Process(processId, instructions);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static void main(String[] args) {
        Parsing parser = new Parsing();

        String[] testFiles = {
            "processo_1.txt",
            "processo_2.txt",
            "processo_erro_bloco.txt",
            "processo_erro_execute.txt",
            "processo_erro_whitespace.txt",
            "processo_3.txt"  // Adicionado o novo arquivo de teste
        };

        for (String fileName : testFiles) {
            System.out.println("Testing file: " + fileName);
            Process process = parser.parseProcessFromFile(fileName);
            if (process != null) {
                System.out.println("Parsed process: " + process);
            }
            System.out.println();
        }
    }
}
