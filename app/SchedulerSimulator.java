package app;

import schedulers.ShortTermScheduler;
import schedulers.LongTermScheduler;
import userinterface.UserInterface;
import interfaces.NotificationInterface;

/**
 * The `SchedulerSimulator` class is the main entry point of the application.
 * It initializes and starts the short-term and long-term schedulers, as well as the user interface.
 *
 * <p>The main responsibilities of this class are:
 * <ul>
 *     <li>Create instances of the `ShortTermScheduler`, `LongTermScheduler`, and `UserInterface` classes</li>
 *     <li>Set up the necessary dependencies and references between the classes</li>
 *     <li>Start the scheduler threads and the user interface thread</li>
 * </ul>
 *
 * <p>This class also defines two constants:
 * <ul>
 *     <li>`CHECK_INTERVAL`: The interval in milliseconds at which the short-term scheduler checks for new processes or blocked processes</li>
 *     <li>`MAX_LOAD`: The maximum number of processes that can be in the system at any given time</li>
 * </ul>
 *
 * @author [Your Name]
 */
public class SchedulerSimulator {

    /**
     * The interval in milliseconds at which the short-term scheduler checks for new processes or blocked processes.
     */
    public static final int CHECK_INTERVAL = 200;

    /**
     * The maximum number of processes that can be in the system at any given time.
     */
    public static final int MAX_LOAD = 10;

    /**
     * The main entry point of the application.
     *
     * @param args Command-line arguments (not used)
     */
    public static void main(String[] args) {
        // Initializes the ShortTermScheduler with a reference to the NotificationInterface
        NotificationInterface notifier = new UserInterface(null, null); // Temporary, will be replaced after full initialization
        ShortTermScheduler shortTermScheduler = new ShortTermScheduler(notifier);

        // Initializes the LongTermScheduler with a reference to the ShortTermScheduler and NotificationInterface
        LongTermScheduler longTermScheduler = new LongTermScheduler(shortTermScheduler, notifier);

        // Creates the UserInterface with references to the ControlInterface (ShortTermScheduler) and SubmissionInterface (LongTermScheduler)
        UserInterface ui = new UserInterface(shortTermScheduler, longTermScheduler);

        // Sets the UserInterface as the NotificationInterface for the schedulers
        shortTermScheduler.setNotifier(ui);
        longTermScheduler.setNotifier(ui);

        // Starts the threads for the schedulers
        new Thread(shortTermScheduler).start();
        new Thread(longTermScheduler).start();

        // Starts the GUI in a new thread
        new Thread(ui).start();
    }
}
