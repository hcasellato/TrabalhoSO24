package interfaces;

import objects.Process;

/**
 * The `InterSchedulerInterface` defines the methods for communication between the long-term and short-term schedulers.
 *
 * <p>This interface is implemented by the `ShortTermScheduler` class and provides methods for adding a new process
 * to the system and getting the current process load.
 *
 * @author Maria Vitoria
 */
public interface InterSchedulerInterface {
    /**
     * Adds a new process to the system.
     *
     * @param process the process to be added
     */
    void addProcess(Process process);

    /**
     * Returns the current process load in the system.
     *
     * @return the number of processes currently in the system
     */
    int getProcessLoad();
}
