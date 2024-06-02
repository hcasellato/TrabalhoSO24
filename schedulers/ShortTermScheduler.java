package schedulers;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;

import javax.naming.ldap.Control;

import app.SchedulerSimulator;
import interfaces.ControlInterface;
import interfaces.InterSchedulerInterface;
import interfaces.NotificationInterface;
import objects.Process;
import services.BlockControlService;

public class ShortTermScheduler implements InterSchedulerInterface, ControlInterface, Runnable {

    private static final int quantum1 = 2;
    private static final int quantum2 = 4;
    private static final int quantum3 = Integer.MAX_VALUE;

    private int processLoad; // Qnt de processos na fila
    private Process runningProcess;

    private boolean running; // flag para programa rodando 

    // Filas de prontos, bloqueados e terminados
    private Queue<Process> readyQueue1;
    private Queue<Process> readyQueue2; 
    private Queue<Process> readyQueue3;
    private Queue<Process> finishedQueue;
    private ArrayList<BlockControlService> blockedList;

    private NotificationInterface notifier;

    public ShortTermScheduler(NotificationInterface notifier)
    {
        this.processLoad   = 0;
        this.running = true;
        
        this.readyQueue    = new LinkedList<>();
        this.finishedQueue = new LinkedList<>();
        this.blockedList = new ArrayList<BlockControlService>();

        this.notifier = notifier;

    }
    

    // Implementação de InterScheduler
    @Override
    public void addProcess(Process process)
    {
        readyQueue.offer(process); // Coloca processo na fila de prontos, já que foi processado e admitido
        processLoad++;
    }

    @Override
    public int getProcessLoad() {
        return processLoad;
    }

    private void esperaCronometrada()
    {
        try{
            wait(SchedulerSimulator.CHECK_INTERVAL); // espera cronometrada
        } catch(InterruptedException e) {
            e.printStackTrace();
        }
    }
    
    // faz programa parar
    public void stop() {
        this.running = false;
    }

    // RUN
    @Override
    public void run()
    {
        while(running){
            if(!readyQueue1.isEmpty())
            {
                runningProcess = readyQueue1.poll();

                if(runningProcess.hasMoreInstructions())
                {
                    // Realiza execute ou block do processo em running
                    String instrucao = runningProcess.getNextInstruction(); // capture instrução
                    if(instrucao.equals("execute"))
                    {
                        esperaCronometrada();
                        if(!blockedList.isEmpty()) {
                            for (BlockControlService bs : blockedList){
                                boolean hasFinished = bs.tick();
                                if (hasFinished) {
                                    int queueToPut = bs.getQueue();
                                    switch(queueToPut) {
                                        case 0:
                                            readyQueue1.offer(bs.getProcess());
                                            break;
                                        case 1:
                                            readyQueue2.offer(bs.getProcess());
                                            break;
                                        case 2:
                                            readyQueue3.offer(bs.getProcess());
                                            break;
                                    }
                                    blockedList.remove(bs);
                                }
                            }
                        }
                    } else if(instrucao.startsWith("block "))
                    {
                    }
                    readyQueue1.offer(runningProcess); // volta processo para o final da fila de prontos
                } else { // ou seja, não há mais nenhuma instrução
                    finishedQueue.add(runningProcess);
                }
            }
        }
    }
}



/*
    // RUN
    @Override
    public void run()
    {
        if(!readyQueue1.isEmpty()){
            int runningQuantum = 0;
            runningProcess = readyQueue1.poll();

            if(runningProcess.hasMoreInstructions())
            {
                // Realiza execute ou block do processo em running
                String instrucao = runningProcess.getNextInstruction(); // capture instrução
                if(instrucao.equals("execute"))
                {
                    esperaCronometrada();
                    runningQuantum++;

                    if (!blockedList.isEmpty()){
                        for (BlockControlService bs : blockedList){
                            boolean hasFinished = bs.tick();
                            if (hasFinished) {
                                int queueToPut = bs.getQueue();
                                switch(queueToPut) {
                                    case 0:
                                        readyQueue1.offer(bs.getProcess());
                                        break;
                                    case 1:
                                        readyQueue2.offer(bs.getProcess());
                                        break;
                                    case 2:
                                        readyQueue3.offer(bs.getProcess());
                                        break;
                                }
                                blockedList.remove(bs);
                            }
                        }
                    }
                
                    if (runningQuantum == quantum1) {
                        readyQueue2.offer(runningProcess);
                    }
                }
                else if(instrucao.startsWith("block "))
                {
                    int timeToBlock = Integer.parseInt(instrucao.strip().split(" ")[1]);
                    if (runningQuantum < quantum1) {
                        BlockControlService bs = new BlockControlService(runningProcess, timeToBlock, 0);
                        blockedList.add(bs);
                    }
                    else {
                        BlockControlService bs = new BlockControlService(runningProcess, timeToBlock, 1);
                        blockedList.add(bs);
                    }
                }
            } else { // ou seja, não há mais nenhuma instrução
            }

            }
    }
*/