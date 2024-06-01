import java.util.LinkedList;
import java.util.Queue;

/**
 * The `LongTermScheduler` class is a critical component of the project's scheduling system. It is responsible for managing the
 * submission queue of processes and coordinating with the short-term scheduler to ensure efficient resource utilization.
 *
 * The `LongTermScheduler` class implements the `SubmissionInterface` and `Runnable` interfaces. It provides the following
 * functionality:
 *
 * 1. **Process Submission**: The `submitJob()` method allows users to submit new processes to the scheduler. The method
 *    parses the process information from a file using the `Parsing` class and adds the process to the submission queue.
 *
 * 2. **Submission Queue Management**: The `LongTermScheduler` maintains a queue of submitted processes (`submissionQueue`)
 *    and periodically checks the queue to see if any processes can be moved to the short-term scheduler.
 *
 * 3. **Short-Term Scheduler Integration**: The `LongTermScheduler` interacts with the `InterSchedulerInterface` implementation
 *    (provided in the constructor) to add processes to the short-term scheduler when the system load is below the maximum
 *    allowed limit.
 *
 * 4. **Notification**: The `LongTermScheduler` uses the `NotificationInterface` implementation (provided in the constructor)
 *    to display information about the scheduling process, such as when a process is submitted or moved to the short-term
 *    scheduler.
 *
 * 5. **Lifecycle Management**: The `LongTermScheduler` can be stopped by calling the `stop()` method, which sets the `running`
 *    flag to `false` and allows the scheduler to gracefully exit.
 *
 * The `LongTermScheduler` class is designed to be highly configurable and extensible. By using the `InterSchedulerInterface`
 * and `NotificationInterface` abstractions, the class can be easily integrated with different short-term scheduler
 * implementations and notification mechanisms, depending on the requirements of the project.
 *
 * @author Maria Vitória
 */

public class LongTermScheduler implements SubmissionInterface, Runnable {
    /**
     * Constructs a new `LongTermScheduler` instance with the specified short-term scheduler and notification interfaces.
     *
     * @param shortTermScheduler the `InterSchedulerInterface` implementation for the short-term scheduler
     * @param notifier the `NotificationInterface` implementation for displaying notifications
     */
    private Queue<Process> submissionQueue;
    private InterSchedulerInterface shortTermScheduler;
    private boolean running;
    private NotificationInterface notifier;
    private Parsing parser;

    public LongTermScheduler(InterSchedulerInterface shortTermScheduler, NotificationInterface notifier) {
        // Constructor implementation
        this.submissionQueue = new LinkedList<>();
        this.shortTermScheduler = shortTermScheduler;
        this.notifier = notifier;
        this.running = true;
        this.parser = new Parsing();
    }
    /**
     * Submits a new job (process) to the long-term scheduler.
     *
     * @param fileName the name of the file containing the process information
     * @return `true` if the job was successfully submitted, `false` otherwise
     */

    @Override
    public boolean submitJob(String fileName) {
        Process newProcess = parser.parseProcessFromFile(fileName);
        if (newProcess != null) {
            submissionQueue.offer(newProcess);
            notifier.display("Process " + fileName + " submitted.");
            return true;
        }
        notifier.display("Failed to submit process " + fileName + ".");
        return false;
    }

    @Override
    /**
     * Displays the current contents of the submission queue.
     */
    public void displaySubmissionQueue() {
        if (submissionQueue.isEmpty()) {
            notifier.display("Submission queue is empty.");
        } else {
            StringBuilder sb = new StringBuilder("Submission queue:\n");
            for (Process process : submissionQueue) {
                sb.append(process).append("\n");
            }
            notifier.display(sb.toString());
        }
    }
    /**
     * Stops the long-term scheduler, allowing it to gracefully exit.
     */

    public void stop() {
        this.running = false;
    }
    /**
     * The main execution loop of the long-term scheduler.
     * This method is responsible for continuously checking the submission queue and moving processes to the short-term scheduler
     * when the system load is below the maximum allowed limit.
     */

    @Override
    public void run() {
        while (running) {
            if (!submissionQueue.isEmpty() && shortTermScheduler.getProcessLoad() < SchedulerSimulator.MAX_LOAD) {
                Process process = submissionQueue.poll();
                shortTermScheduler.addProcess(process);
                notifier.display("Process " + process + " moved to short-term scheduler.");
            }
            try {
                Thread.sleep(SchedulerSimulator.CHECK_INTERVAL);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

}

