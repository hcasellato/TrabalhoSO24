import java.util.LinkedList;
import java.util.Queue;

public class ShortTermScheduler implements InterSchedulerInterface, Runnable{

    private int processLoad; // Qnt de processos na fila
    private Process runningProcess;

    private boolean running; // flag para programa rodando 

    // Filas de prontos, bloqueados e terminados
    private Queue<Process> readyQueue;
    private Queue<Process> blockedQueue;
    private Queue<Process> finishedQueue;

    private NotificationInterface notifier;

    public ShortTermScheduler(NotificationInterface notifier)
    {
        this.processLoad   = 0;
        this.running = true;
        
        this.readyQueue    = new LinkedList<>();
        this.blockedQueue  = new LinkedList<>();
        this.finishedQueue = new LinkedList<>();

        this.notifier = notifier;

    }
    
    // faz programa parar
    public void stop() {
        this.running = false;
    }

    // Implementação de InterScheduler
    @Override
    public void addProcess(Process process)
    {
        if(processLoad < SchedulerSimulator.MAX_LOAD)
        { // caso processLoad seja menor que o máximo, coloca na fila de prontos
            readyQueue.offer(process); // Coloca processo na fila de prontos, já que foi processado e admitido
            processLoad++;
        } else {
            notifier.display("Não foi possível colocar processo na lista de prontos.");
        }
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

    // RUN
    @Override
    public void run()
    {
        while(running){
            if(!readyQueue.isEmpty())
            {
                runningProcess = readyQueue.poll();

                if(runningProcess.hasMoreInstructions())
                {
                    // Realiza execute ou block do processo em running
                    String instrucao = runningProcess.getNextInstruction(); // capture instrução
                    if(instrucao.equals("execute"))
                    {
                        esperaCronometrada();
                    } else if(instrucao.startsWith("block "))
                    {
                        String[] parts = instrucao.split(" "); // primeiro separa instrução
                        int tamanhoDoBloqueio = Integer.valueOf(parts[1]);
                        int finalDaFila = blockedQueue.size();

                        for(int i = finalDaFila; i < finalDaFila + tamanhoDoBloqueio; i++)
                        {
                            blockedQueue.offer(null);
                        }
                        blockedQueue.offer(runningProcess);

                        esperaCronometrada();
                    }


                    readyQueue.offer(runningProcess); // volta processo para o final da fila de prontos

                } else { // ou seja, não há mais nenhuma instrução
                    finishedQueue.add(runningProcess);
                }
            }
        }
    }
}
