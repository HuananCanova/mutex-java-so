package br.ufsm.csi.so.mutex;

public class AlgoritmoPeterson {

    private char vez = 'B';
    private boolean CA = false, CB = false;
    private long varGlobal;
    private AlgoritmoPeterson.ProcessoA processoA;
    private AlgoritmoPeterson.ProcessoB processoB;
    public static void main(String[] args) {
        new AlgoritmoPeterson();
    }


    public AlgoritmoPeterson() {
        this.processoA = new AlgoritmoPeterson.ProcessoA();
        this.processoB = new AlgoritmoPeterson.ProcessoB();
        new Thread(processoA).start();
        new Thread(processoB).start();
        new Thread(new AlgoritmoPeterson.ConfereConsistencia()).start();
    }

    private class ProcessoA implements Runnable {
        private long varLocal;
        @Override
        public void run() {
            while(true){
                CA = true;
                vez = 'B';
                while (CB && vez == 'B') { }
                varGlobal++;
                varLocal++;
                //sai da região critica
                CA = false;
            }
        }
    }


    private class ProcessoB implements Runnable {
        private long varLocal;
        @Override
        public void run() {
            while(true){
                CB = true;
                vez = 'A';
                while (CA && vez == 'A') { }
                varGlobal++;
                varLocal++;
                //sai da região critica
                CB = false;
            }
        }
    }

    private class ConfereConsistencia implements Runnable{

        @Override
        public void run() {
            while(true){
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                if (varGlobal != processoA.varLocal + processoB.varLocal){
                    System.out.println("INCONSSISTENTE " + (varGlobal- (processoA.varLocal + processoB.varLocal)));
                } else {
                    System.out.println("CONSISTENTE varglobal: "+ varGlobal);
                }
            }
        }
    }


}
