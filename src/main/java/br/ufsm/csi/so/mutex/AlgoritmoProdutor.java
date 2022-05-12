package br.ufsm.csi.so.mutex;

import lombok.SneakyThrows;

import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.Semaphore;

public class AlgoritmoProdutor {

    private final static int TAM_BUFFER = 100;
    private Semaphore vazio = new Semaphore(TAM_BUFFER);
    private Semaphore cheio = new Semaphore(0);
    private Semaphore mutex = new Semaphore(1);
    private final ArrayList<Integer> buffer = new ArrayList<>(TAM_BUFFER);
    private Produtor produtor;
    private Consumidor consumidor;

    public static void main(String[] args) {
        new AlgoritmoProdutor();
    }

    public AlgoritmoProdutor() {
        this.produtor = new Produtor();
        this.consumidor = new Consumidor();
        new Thread(produtor).start();
        new Thread(consumidor).start();
    }

    private class Produtor implements Runnable {

        private Random rnd = new Random();

        @SneakyThrows
        @Override
        public void run() {
            while (true) {
                //produz
                int num = Math.abs(rnd.nextInt(1,2));
                vazio.acquire();
                mutex.acquire();
                System.out.println("Produtor: produziu " + num);
                buffer.add(num);
                mutex.release();
                cheio.release();
            }
        }
    }

    private class Consumidor implements Runnable {

        @SneakyThrows
        @Override
        public void run() {
            while (true) {
                //acesso a regiao critica
                cheio.acquire();
                mutex.acquire();
                int num = buffer.remove(0);
                System.out.println("Consumidor: consumiu " + num);
                mutex.release();
                vazio.release();

            }
        }
    }


}
