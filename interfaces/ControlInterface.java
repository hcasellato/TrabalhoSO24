package interfaces;

/**
 * The `ControlInterface` defines the methods for controlling the simulation of the process scheduler.
 *
 * <p>This interface is implemented by the `ShortTermScheduler` class and provides methods for starting,
 * suspending, resuming, stopping the simulation, and displaying the process queues.
 *
 * @author Maria Vitoria
 */
public interface ControlInterface {
    /**
     * Starts the simulation.
     */
    void startSimulation();

    /**
     * Suspends the simulation.
     */
    void suspendSimulation();

    /**
     * Resumes the suspended simulation.
     */
    void resumeSimulation();

    /**
     * Stops the simulation.
     */
    void stopSimulation();

    /**
     * Displays the current state of the process queues.
     */
    void displayProcessQueues();
}
