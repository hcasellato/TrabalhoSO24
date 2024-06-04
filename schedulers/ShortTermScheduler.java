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
import services.ProcessControlService;

public class ShortTermScheduler implements InterSchedulerInterface, ControlInterface, Runnable {

    private int processLoad; // Qnt de processos na fila
    private ProcessControlService runningProcess;

    private boolean running; // flag para programa rodando 

    // Filas de prontos, bloqueados e terminados
    private Queue<ProcessControlService> readyQueue1; // fila de quantum 2x
    private Queue<ProcessControlService> readyQueue2; // fila de quantum 4x
    private Queue<ProcessControlService> readyQueue3; // fila de FCFS
    private Queue<ProcessControlService> finishedQueue;
    private ArrayList<ProcessControlService> blockedList;

    private NotificationInterface notifier;

    public ShortTermScheduler(NotificationInterface notifier)
    {
        this.processLoad   = 0;
        this.running = true;
        
        this.finishedQueue = new LinkedList<>();
        this.blockedList = new ArrayList<ProcessControlService>();

        this.notifier = notifier;

    }
    

    // Implementação de InterScheduler
    public void addProcess(Process process)
    {
        ProcessControlService treatedProcess = new ProcessControlService(process, 2, 0, 1);
        readyQueue1.offer(treatedProcess); // Coloca processo na fila de prontos, já que foi processado e admitido
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
                runningProcess = readyQueue1.peek(); // pega primeiro processo da fila
                String instrucao = runningProcess.getProcess().getNextInstruction(); // capture instrução
                
                if(instrucao.equals("execute"))
                {
                    esperaCronometrada();  // executa
                    runningProcess.tick(); // [I] passagem de tempo para processo 

                    if(runningProcess.getRemainingQuantums() == 0)
                    {
                        // Atualização da queue com quantums faltantes (acho que dá pra otimizar)
                        runningProcess.setQueue(2);
                        runningProcess.setRemainingQuantums(4);

                        readyQueue2.offer(readyQueue1.poll());
                    }

                } else if(instrucao.startsWith("block ")) {
                    String[] parts = instrucao.split(" ");
                    runningProcess.setBlockedQuantums(Integer.parseInt(parts[1]));

                    blockedList.add(readyQueue1.poll()); // adiciona processo para blocked, tick vem automaticamente em [II]
                } else { // processo terminou
                    finishedQueue.offer(readyQueue1.poll()); // retira 1o processo de readyQueueX e o coloca em finalizado
                }

            }else if(!readyQueue2.isEmpty())
            {

            }else if(!readyQueue3.isEmpty())
            {
                
            } else if(!blockedList.isEmpty()){
                // ou seja, todas as filas de prontos estão vazias,
                // mas a de bloquados não está, então precisa de uma espera cronometrada!
                esperaCronometrada();

            } else {
                // por fim, todas as listas (prontos e bloqueados) estão vazias
                running = false;
            }

            queueManager(); // [II] passagem de tempo para outros processos (concordando com [I])
                            // direcionamento de bloqueados para fila de prontos
        }
    }

    // Método para gerenciar onde vão os processos entre filas após bloqueio
    private void queueManager()
    {
        // Verificar se tempo de bloqueio acabou
        if(!blockedList.isEmpty())
        {
            for(ProcessControlService blockedProcess : blockedList)
            {
                if(blockedProcess.tick()) // faz o tick e retorna 0 (ou seja, processo mudou de fila)
                {
                    switch (blockedProcess.getQueue()) { // verifica onde (fila) o processo está
                        case 1:
                            runningProcess.setQueue(2);
                            runningProcess.setRemainingQuantums(4);
                            readyQueue2.offer(blockedProcess);
                            break;

                        case 2:
                            runningProcess.setQueue(3);
                            runningProcess.setRemainingQuantums(Integer.MAX_VALUE);
                            readyQueue3.offer(blockedProcess);
                            break;
                    }
                }

                if(blockedProcess.blockedTick()) // faz o blockedTick e retorna 0 (ou seja, processo acabou o bloqueio)
                {
                    switch (blockedProcess.getQueue()) { // verifica onde (fila) o processo está
                        case 1:
                            readyQueue1.offer(blockedProcess);
                            break;

                        case 2:
                            readyQueue2.offer(blockedProcess);
                            break;

                        default:
                            readyQueue3.offer(blockedProcess);
                            break;
                    }
                }

            }
        }
        //blockedList.forEach((blocked) -> blocked.tick()); // para tds os bloquados, faça tick
        
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