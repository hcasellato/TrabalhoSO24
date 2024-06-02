package services;
import objects.Process;


public class BlockControlService {
    private Process process;
    private int remainingQuantums;
    private int queue;

    public BlockControlService(Process process, int remainingQuantums, int queue) {
        this.process = process;
        this.remainingQuantums = remainingQuantums;
        this.queue = queue;
    }

    public Process getProcess() {
        return process;
    }

    public int getRemainingQuantums() {
        return remainingQuantums;
    }

    public int getQueue() {
        return queue;
    }

    public boolean tick() {
        this.remainingQuantums--;
        return (this.remainingQuantums == 0);
    }
}