package br.ufsm.csi.so.mutex;

import lombok.SneakyThrows;
import java.util.concurrent.Semaphore;

public class Algoritmo4 {


        private Semaphore semaforo = new Semaphore(1);
        private long varGlobal;
        private br.ufsm.csi.so.mutex.Algoritmo4.ProcessoA processoA;
        private br.ufsm.csi.so.mutex.Algoritmo4.ProcessoB processoB;
        public static void main(String[] args) {
            new br.ufsm.csi.so.mutex.Algoritmo4();
        }


        public Algoritmo4() {
            this.processoA = new br.ufsm.csi.so.mutex.Algoritmo4.ProcessoA();
            this.processoB = new br.ufsm.csi.so.mutex.Algoritmo4.ProcessoB();
            new Thread(processoA).start();
            new Thread(processoB).start();
            new Thread(new br.ufsm.csi.so.mutex.Algoritmo4.ConfereConsistencia()).start();
        }

        private class ProcessoA implements Runnable {
            private long varLocal;
            @SneakyThrows
            @Override
            public void run() {
                while(true){
                    semaforo.acquire();
                    varGlobal++;
                    varLocal++;
                    //sai da região critica
                    semaforo.release();
                }
            }
        }


        private class ProcessoB implements Runnable {
            private long varLocal;
            @SneakyThrows
            @Override
            public void run() {
                    while(true){
                        semaforo.acquire();
                        varGlobal++;
                        varLocal++;
                        //sai da região critica
                        semaforo.release();
                    }
            }
        }

        private class ConfereConsistencia implements Runnable{

            @SneakyThrows
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
