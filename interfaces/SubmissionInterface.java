package interfaces;

/**
 * The `SubmissionInterface` defines the methods for submitting new jobs or processes to the system.
 *
 * <p>This interface is implemented by the `LongTermScheduler` class and provides methods for submitting a new job
 * from a file and displaying the submission queue.
 *
 * @author [Your Name]
 */
public interface SubmissionInterface {
    /**
     * Submits a new job or process from the specified file.
     *
     * @param fileName the name of the file containing the job or process information
     * @return `true` if the job was submitted successfully, `false` otherwise
     */
    boolean submitJob(String fileName);

    /**
     * Displays the current submission queue of jobs or processes.
     */
    void displaySubmissionQueue();
}
