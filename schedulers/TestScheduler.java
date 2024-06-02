package schedulers;
import interfaces.InterSchedulerInterface;
import interfaces.NotificationInterface;
import objects.Process;

/**
 * The `TestScheduler` class is responsible for setting up and running integration tests for the `LongTermScheduler` and its
 * associated components.
 *
 * This class creates mock implementations of the `InterSchedulerInterface` and `NotificationInterface`, which are then used
 * to instantiate a `LongTermScheduler` instance. The class then submits a set of test files to the scheduler and observes
 * the results.
 *
 * The main functionality of the `TestScheduler` class includes:
 *
 * 1. **Setting up mock interfaces**: The class creates instances of `MockShortTermScheduler` and `MockNotifier` to simulate
 *    the behavior of the short-term scheduler and notification system, respectively.
 *
 * 2. **Instantiating the `LongTermScheduler`**: The class creates a `LongTermScheduler` instance, passing the mock interfaces
 *    as dependencies.
 *
 * 3. **Submitting test files**: The class defines an array of test file names and submits each file to the `LongTermScheduler`
 *    using the `submitJob()` method.
 *
 * 4. **Displaying the submission queue**: After submitting the test files, the class calls the `displaySubmissionQueue()`
 *    method to print the current state of the submission queue.
 *
 * 5. **Simulating the scheduler's run method**: The class creates a separate thread to run the `LongTermScheduler`'s `run()`
 *    method, allowing the scheduler to process the submission queue.
 *
 * 6. **Stopping the scheduler**: After a short delay, the class calls the `stop()` method to gracefully stop the
 *    `LongTermScheduler` instance.
 *
 * The `TestScheduler` class is designed to provide a comprehensive integration test suite for the `LongTermScheduler` and
 * its related components, ensuring the overall system functions as expected.
 *
 * @author Maria Vitoria
 */
public class TestScheduler {
    public static void main(String[] args) {
        // Setup mock interfaces for testing
        InterSchedulerInterface mockShortTermScheduler = new MockShortTermScheduler();
        NotificationInterface mockNotifier = new MockNotifier();
        
        // Create an instance of LongTermScheduler with mock interfaces
        LongTermScheduler scheduler = new LongTermScheduler(mockShortTermScheduler, mockNotifier);
        
        // Test files
        String[] testFiles = {
            "processo_1.txt",
            "processo_2.txt",
            "processo_erro_bloco.txt",
            "processo_erro_execute.txt",
            "processo_erro_whitespace.txt",
            "processo_3.txt"  // Adicionado o novo arquivo de teste
        };

        // Test process submission
        for (String fileName : testFiles) {
            System.out.println("Submitting file: " + fileName);
            boolean result = scheduler.submitJob(fileName);
            System.out.println("Result: " + (result ? "Success" : "Failed"));
            System.out.println();
        }

        // Display the current submission queue
        System.out.println("Displaying submission queue:");
        scheduler.displaySubmissionQueue();
        System.out.println();

        // Simulate the run method in a separate thread
        Thread schedulerThread = new Thread(scheduler);
        schedulerThread.start();

        // Allow some time for the scheduler to process the queue
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // Stop the scheduler
        scheduler.stop();
        try {
            schedulerThread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}

// Mock implementations for testing
class MockShortTermScheduler implements InterSchedulerInterface {
    private int processLoad = 0;

    @Override
    public void addProcess(Process process) {
        processLoad++;
        System.out.println("Process added to short-term scheduler: " + process.getId());
    }

    @Override
    public int getProcessLoad() {
        return processLoad;
    }
}

class MockNotifier implements NotificationInterface {
    @Override
    public void display(String message) {
        System.out.println(message);
    }
}
