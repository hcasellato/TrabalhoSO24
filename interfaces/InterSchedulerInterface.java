package interfaces;
import objects.Process;

public interface InterSchedulerInterface {
    void addProcess(Process process);
    int getProcessLoad();
}
