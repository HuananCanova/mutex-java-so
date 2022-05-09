package br.ufsm.csi.so.mutex;

public class Algoritmo2 {

    private boolean CA = false, CB = false;
    private long varGlobal;
    private Algoritmo2.ProcessoA processoA;
    private Algoritmo2.ProcessoB processoB;
    public static void main(String[] args) {
        new Algoritmo2();
    }


    public Algoritmo2() {
        this.processoA = new Algoritmo2.ProcessoA();
        this.processoB = new Algoritmo2.ProcessoB();
        new Thread(processoA).start();
        new Thread(processoB).start();
        new Thread(new Algoritmo2.ConfereConsistencia()).start();
    }

    private class ProcessoA implements Runnable {
        private long varLocal;
        @Override
        public void run() {
            while(true){
                while (CB) { }
                CA = true;
                varGlobal++;
                varLocal++;
                CA = false;
            }
        }
    }


    private class ProcessoB implements Runnable {
        private long varLocal;
        @Override
        public void run() {
            while(true){
                while (CA) { }
                CB = true;
                varGlobal++;
                varLocal++;
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
                    System.out.println("CONSISTENTE");
                }
            }
        }
    }


}
