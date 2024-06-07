package app;

import schedulers.ShortTermScheduler;
import schedulers.LongTermScheduler;
import userinterface.UserInterface;
import interfaces.NotificationInterface;

public class SchedulerSimulator {

    public static final int CHECK_INTERVAL = 200; // Exemplo de intervalo de tempo em ms
    public static final int MAX_LOAD = 10; // Exemplo de carga máxima de processos

    public static void main(String[] args) {
        // Inicializa o ShortTermScheduler com uma referência ao NotificationInterface
        NotificationInterface notifier = new UserInterface(null, null); // Temporário, será substituído após a inicialização completa
        ShortTermScheduler shortTermScheduler = new ShortTermScheduler(notifier);

        // Inicializa o LongTermScheduler com uma referência ao ShortTermScheduler e ao NotificationInterface
        LongTermScheduler longTermScheduler = new LongTermScheduler(shortTermScheduler, notifier);

        // Cria a UserInterface com referências ao ControlInterface (ShortTermScheduler) e SubmissionInterface (LongTermScheduler)
        UserInterface ui = new UserInterface(shortTermScheduler, longTermScheduler);

        // Define a UserInterface como o NotificationInterface para os schedulers
        shortTermScheduler.setNotifier(ui);
        longTermScheduler.setNotifier(ui);

        // Inicia as threads para os schedulers
        new Thread(shortTermScheduler).start();
        new Thread(longTermScheduler).start();

        // Inicia a GUI em uma nova thread
        new Thread(ui).start();
    }
}
