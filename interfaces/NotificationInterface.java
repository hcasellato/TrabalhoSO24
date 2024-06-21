package interfaces;

/**
 * The `NotificationInterface` defines the method for displaying notifications or messages in the system.
 *
 * <p>This interface is implemented by classes that provide a mechanism for displaying messages or notifications,
 * such as the `ConsoleNotifier` and `UserInterface` classes.
 *
 * @author Maria Vitoria
 */
public interface NotificationInterface {
    /**
     * Displays the given information or message.
     *
     * @param info the information or message to be displayed
     */
    void display(String info);
}
