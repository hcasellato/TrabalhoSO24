import java.util.List;
/**
 * The `Process` class represents a single process in the system. It encapsulates the process identifier and the list of
 * instructions that make up the process.
 *
 * The `Process` class provides the following functionality:
 *
 * 1. **Process Identification**: Each `Process` object has a unique identifier, which can be accessed using the `getId()`
 *    method.
 *
 * 2. **Instruction Management**: The `Process` class maintains a list of instructions that make up the process. These
 *    instructions can be accessed using the `getNextInstruction()` method, which returns the next instruction in the list
 *    and advances the current instruction pointer. The `hasMoreInstructions()` method can be used to check if the process
 *    has any remaining instructions to be executed.
 *
 * 3. **String Representation**: The `toString()` method provides a string representation of the `Process` object, including
 *    the process identifier and the current instruction pointer.
 *
 * The `Process` class is designed to be a simple and lightweight representation of a process in the system. It does not
 * contain any logic related to the execution or scheduling of the process, but rather serves as a data container that can
 * be used by other components of the system, such as the `LongTermScheduler` and `ShortTermScheduler`.
 *
 * The `Process` class is typically created by the `Parsing` class, which reads process information from a file and
 * constructs a `Process` object based on the parsed data.
 *
 * @author Maria Vitoria
 */

public class Process {
    private String id;
    private List<String> instructions;
    private int currentInstruction;

    /**
     * Constructs a new `Process` object with the specified process identifier and list of instructions.
     *
     * @param id          the unique identifier for the process
     * @param instructions the list of instructions that make up the process
     */   

    public Process(String id, List<String> instructions) {
        this.id = id;
        this.instructions = instructions;
        this.currentInstruction = 0;
    }
    /**
     * Returns the unique identifier for the process.
     *
     * @return the process identifier
     */

    public String getId() {
        return id;
    }
    /**
     * Returns the next instruction in the process, and advances the current instruction pointer.
     *
     * @return the next instruction in the process, or `null` if there are no more instructions
     */

    public String getNextInstruction() {
        if (currentInstruction < instructions.size()) {
            return instructions.get(currentInstruction++);
        }
        return null; // Indica que o processo terminou
    }
    /**
     * Checks if the process has any remaining instructions to be executed.
     *
     * @return `true` if the process has more instructions, `false` otherwise
     */

    public boolean hasMoreInstructions() {
        return currentInstruction < instructions.size();
    }
    /**
     * Returns a string representation of the `Process` object, including the process identifier and the current
     * instruction pointer.
     *
     * @return a string representation of the `Process` object
     */

    @Override
    public String toString() {
        return "Process{id='" + id + "', currentInstruction=" + currentInstruction + ", instructions=" + instructions + '}';
    }
}
