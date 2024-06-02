package interfaces;
public interface ControlInterface{
    void startSimulation();
    void suspendSimulation();
    void resumeSimulation();
    void stopSimulation();
    void displayProcessQueues();
}