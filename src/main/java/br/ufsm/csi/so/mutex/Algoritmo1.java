package br.ufsm.csi.so.mutex;

public class Algoritmo1 {

    private char vez = 'A';
    private long varGlobal;
    private ProcessoA processoA;
    private ProcessoB processoB;

    public static void main(String[] args) {
        new Algoritmo1();
    }

    public Algoritmo1() {
        this.processoA = new ProcessoA();
        this.processoB = new ProcessoB();
        new Thread(processoA).start();
        new Thread(processoB).start();
        new Thread(new ConfereConsistencia()).start();
    }

    private class ProcessoA implements Runnable {
        private long varLocal;
        @Override
        public void run() {
            while(true){
                while (vez == 'B') { }
                    varGlobal++;
                    varLocal++;
                    vez = 'B';
            }
        }
    }

    private class ProcessoB implements Runnable {
        private long varLocal;
        @Override
        public void run() {
            while(true){
                while (vez == 'A') { }
                    varGlobal++;
                    varLocal++;
                    vez = 'B';
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
                    System.out.println("CONSISTENTE");
                }
            }
        }
    }


}
