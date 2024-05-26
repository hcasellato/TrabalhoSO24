/**
 * The `TestScheduler` class is a utility class used for testing the scheduling functionality of a system.
 * It creates a `LongTermScheduler` instance and submits a set of test files to it, simulating the submission of
 * processes to the scheduler.
 *
 * The class demonstrates the usage of the `LongTermScheduler` class and the associated interfaces, such as
 * `InterSchedulerInterface` and `NotificationInterface`.
 *
 * @author Maria Vitoria
 */
public class TestScheduler {
    /**
     * The main entry point of the `TestScheduler` class.
     *
     * This method creates an instance of the `InterSchedulerInterface` and a `ConsoleNotifier` implementation
     * of the `NotificationInterface`. It then creates a `LongTermScheduler` instance, passing the
     * `InterSchedulerInterface` and `NotificationInterface` implementations as parameters.
     *
     * The method then defines an array of test file names and iterates through them, submitting each file to
     * the `LongTermScheduler` instance. Finally, it starts the `LongTermScheduler` in a separate thread.
     *
     * @param args the command-line arguments (not used in this implementation)
     */

    public static void main(String[] args) {
        InterSchedulerInterface shortTermScheduler = new InterSchedulerInterface() {
            @Override
            public void addProcess(Process process) {
                System.out.println("Process added to short-term scheduler: " + process.getId());
            }

            @Override
            public int getProcessLoad() {
                return 0; // Implementação de exemplo
            }
        };

        NotificationInterface notifier = new ConsoleNotifier();
        LongTermScheduler scheduler = new LongTermScheduler(shortTermScheduler, notifier);

        String[] testFiles = {
            "processo_1.txt",
            "processo_2.txt",
            "processo_erro_bloco.txt",
            "processo_erro_execute.txt",
            "processo_erro_whitespace.txt"
        };

        for (String fileName : testFiles) {
            System.out.println("Testing file: " + fileName);
            scheduler.submitJob(fileName);
            System.out.println();
        }

        new Thread(scheduler).start();
    }
}
