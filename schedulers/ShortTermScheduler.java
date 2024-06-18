package schedulers;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;

import app.SchedulerSimulator;
import interfaces.ControlInterface;
import interfaces.InterSchedulerInterface;
import interfaces.NotificationInterface;
import objects.Process;
import services.ProcessControlService;

public class ShortTermScheduler implements InterSchedulerInterface, ControlInterface, Runnable {

    private int processLoad; // Qnt de processos na fila
    private ProcessControlService runningProcess;

    private boolean running; // flag para programa rodando
    private boolean suspended; // flag para simulação suspensa

    // Filas de prontos, bloqueados e terminados
    private Queue<ProcessControlService> readyQueue1; // fila de quantum 3x
    private Queue<ProcessControlService> readyQueue2; // fila de quantum 6x
    private Queue<ProcessControlService> readyQueue3; // fila de quantum 12x
    private Queue<ProcessControlService> finishedQueue;
    private ArrayList<ProcessControlService> blockedList;

    private NotificationInterface notifier;

    public ShortTermScheduler(NotificationInterface notifier)
    {
        this.processLoad   = 0;
        this.running = true;
        this.suspended = false;

        this.readyQueue1 = new LinkedList<>();
        this.readyQueue2 = new LinkedList<>();
        this.readyQueue3 = new LinkedList<>();
        this.finishedQueue = new LinkedList<>();
        this.blockedList = new ArrayList<ProcessControlService>();

        this.notifier = notifier;
    }

    public void setNotifier(NotificationInterface notifier) {
        this.notifier = notifier;
    }

    // Implementação de InterScheduler
    public synchronized void addProcess(Process process)
    {
        ProcessControlService treatedProcess = new ProcessControlService(process, 3, 0, 1);
        readyQueue1.offer(treatedProcess); // Coloca processo na fila de prontos, já que foi processado e admitido
        processLoad++;
        preemptIfNeeded();
    }

    @Override
    public synchronized int getProcessLoad() {
        return processLoad;
    }

    private void esperaCronometrada()
    {
        try{
            Thread.sleep(SchedulerSimulator.CHECK_INTERVAL); // espera cronometrada
        } catch(InterruptedException e) {
            e.printStackTrace();
        }
    }

    // faz programa parar
    public synchronized void stop() {
        this.running = false;
        notifyAll(); // Notifica todas as threads em espera
    }

    // RUN
    @Override
    public void run()
    {
        while(running){
            synchronized (this) {
                while (suspended) {
                    try {
                        wait(); // Aguarda até que a simulação seja retomada
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }

            if(!readyQueue1.isEmpty())
            {
                runningProcess = readyQueue1.peek(); // pega primeiro processo da fila
                executarProcesso(readyQueue1);

            }else if(!readyQueue2.isEmpty())
            {
                runningProcess = readyQueue2.peek(); // pega primeiro processo da fila
                executarProcesso(readyQueue2);

            }else if(!readyQueue3.isEmpty())
            {
                runningProcess = readyQueue3.peek(); // pega primeiro processo da fila
                executarProcesso(readyQueue3);
                
            } else if(!blockedList.isEmpty()){
                // ou seja, todas as filas de prontos estão vazias,
                // mas a de bloqueados não está, então precisa de uma espera cronometrada!
                esperaCronometrada();

            } else {
                // por fim, todas as listas (prontos e bloqueados) estão vazias
                running = false;
                handleEmptyQueues();
            }

            queueManager(); // [II] passagem de tempo para outros processos (concordando com [I])
                            // direcionamento de bloqueados para fila de prontos
        }
    }

    private void handleEmptyQueues() {
        notifier.display("Todas as filas estão vazias");
        running = false;
    }

    private synchronized void executarProcesso(Queue<ProcessControlService> queue) {
        String instrucao = runningProcess.getProcess().getNextInstruction(); // capture instrução
        
        if(instrucao == null) { // processo terminou
            notifier.display("Process " + runningProcess.getProcess().getId() + " has finished execution");
            finishedQueue.offer(queue.poll()); // retira 1o processo de readyQueueX e o coloca em finalizado
            processLoad--;
        } else if(instrucao.equals("execute"))
        {
            esperaCronometrada();  // executa
            runningProcess.tick(); // [I] passagem de tempo para processo 

            if(runningProcess.getRemainingQuantums() == 0)
            {
                // Rebaixa o processo para a fila de menor prioridade
                int newQueue = runningProcess.getQueue() + 1;
                int newQuantum = newQueue == 2 ? 6 : 12;

                notifier.display("Process " + runningProcess.getProcess().getId() + " moved to queue " + newQueue);

                runningProcess.setQueue(newQueue);
                runningProcess.setRemainingQuantums(newQuantum);

                if(newQueue == 2) {
                    readyQueue2.offer(queue.poll());
                } else {
                    readyQueue3.offer(queue.poll());
                }
                preemptIfNeeded();
            }

        } else if(instrucao.startsWith("block ")) {
            String[] parts = instrucao.split(" ");
            runningProcess.setBlockedQuantums(Integer.parseInt(parts[1]));

            notifier.display("Process " + runningProcess.getProcess().getId() + " is blocked for " + parts[1] + " quantums");

            blockedList.add(queue.poll()); // adiciona processo para blocked
            preemptIfNeeded();
        }
    }

    private synchronized void preemptIfNeeded() {
        // Preempção se um processo de maior prioridade chega enquanto outro de menor prioridade está rodando
        if (runningProcess != null && !readyQueue1.isEmpty() && runningProcess.getQueue() > 1) {
            notifier.display("Process " + runningProcess.getProcess().getId() + " is preempted by a higher priority process");
            if (runningProcess.getQueue() == 2) {
                readyQueue2.offer(runningProcess);
            } else {
                readyQueue3.offer(runningProcess);
            }
            runningProcess = readyQueue1.poll();
        } else if (runningProcess != null && !readyQueue2.isEmpty() && runningProcess.getQueue() == 3) {
            notifier.display("Process " + runningProcess.getProcess().getId() + " is preempted by a higher priority process");
            readyQueue3.offer(runningProcess);
            runningProcess = readyQueue2.poll();
        }
    }

    // Método para gerenciar onde vão os processos entre filas após bloqueio
    private synchronized void queueManager()
    {
        // Verificar se tempo de bloqueio acabou
        if(!blockedList.isEmpty())
        {
            ArrayList<ProcessControlService> readyProcesses = new ArrayList<>();
            for(ProcessControlService blockedProcess : blockedList)
            {
                if(blockedProcess.blockedTick()) // faz o blockedTick e retorna 0 (ou seja, processo acabou o bloqueio)
                {
                    readyProcesses.add(blockedProcess);
                }
            }
            blockedList.removeAll(readyProcesses);
            for(ProcessControlService readyProcess : readyProcesses) {
                if(readyProcess.getQueue() == 3 && readyProcess.getBlockedQuantums() < readyProcess.getRemainingQuantums() / 2) {
                    readyProcess.setQueue(2);
                    readyProcess.setRemainingQuantums(6);
                    notifier.display("Process " + readyProcess.getProcess().getId() + " is removed from blocked and moved to queue 2");
                    readyQueue2.offer(readyProcess);
                } else {
                    notifier.display("Process " + readyProcess.getProcess().getId() + " is removed from blocked and moved to queue " + readyProcess.getQueue());
                    readyQueue3.offer(readyProcess);
                }
            }
            preemptIfNeeded();
        }
    }

    // Implementação de ControlInterface
    @Override
    public synchronized void startSimulation() {
        if (!running) {
            running = true;
            new Thread(this).start();
        } else {
            notifyAll(); // Notifica todas as threads em espera
        }
    }

    @Override
    public synchronized void suspendSimulation() {
        suspended = true;
    }

    @Override
    public synchronized void resumeSimulation() {
        suspended = false;
        notifyAll(); // Notifica todas as threads em espera
    }

    @Override
    public void stopSimulation() {
        stop();
    }

    @Override
    public synchronized void displayProcessQueues() {
        notifier.display("Processos em execução: " + (runningProcess != null ? runningProcess.getProcess().getId() : "Nenhum"));
        notifier.display("Fila de prontos (prioridade 1): " + readyQueue1.toString());
        notifier.display("Fila de prontos (prioridade 2): " + readyQueue2.toString());
        notifier.display("Fila de prontos (prioridade 3): " + readyQueue3.toString());
        notifier.display("Fila de bloqueados: " + blockedList.toString());
        notifier.display("Fila de terminados: " + finishedQueue.toString());
    }
}
