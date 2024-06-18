    package services;
    import objects.Process;


    public class ProcessControlService {
        private Process process;
        private int remainingQuantums;
        private int queue;
        private int blockedQuantum;

        public ProcessControlService(Process process, int remainingQuantums, int blockedQuantum, int queue) {
            this.process = process;
            this.remainingQuantums = remainingQuantums;
            this.queue = queue;
            this.blockedQuantum = 0;
        }

        public Process getProcess() {
            return process;
        }

        public int getRemainingQuantums() {
            return remainingQuantums;
        }

        public void setRemainingQuantums(int qToGo)
        {
            remainingQuantums = qToGo;
        }

        public int getQueue() {
            return queue;
        }

        public void setQueue(int queueToGo)
        {
            queue = queueToGo;
        }

        public int getBlockedQuantums() {
            return blockedQuantum;
        }

        public void setBlockedQuantums(int qToGo)
        {
            blockedQuantum = qToGo;
        }

        public boolean tick() {
            this.remainingQuantums--;
            return (this.remainingQuantums == 0);
        }

        public boolean blockedTick() {
            this.blockedQuantum--;
            return (this.blockedQuantum == 0);
        }

        @Override
        public String toString() {
            return "Process{id='" + process.getId() + "}";
        }
    }