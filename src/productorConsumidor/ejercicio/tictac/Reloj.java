package productorConsumidor.ejercicio.tictac;

import java.util.Random;

class Valores{
    public static final int MIN_ESPERA_TIC = 2;
    public static final int MAX_ESPERA_TIC = 5;
    public static final int MIN_ESPERA_TAC = 2;
    public static final int MAX_ESPERA_TAC = 5;
    public static final int NUM_TICKERS = 2;
    public static final int NUM_TACKERS = 2;
    public static final int CANTIDAD_DE_TICS_Y_TACKS = 5;

    public static Random r = new Random();
}

public class Reloj {

    public static void main(String[] args) {
        Thread[] tickers = new Ticker[Valores.NUM_TICKERS];
        Thread[] tackers = new Tacker[Valores.NUM_TACKERS];
        Reloj reloj = new Reloj();
        for(int i = 0; i <tickers.length; i++)
            tickers[i] = new Ticker(i+1,reloj);

        for(int i = 0; i< tackers.length;i++)
            tackers[i] = new Tacker(i+1,reloj);

        for (Thread t : tickers)
            t.start();
        for (Thread t : tackers)
            t.start();
    }
    boolean inTic = false;

    public void hacerTic() throws InterruptedException {
        synchronized (this){
            while (!canTic())
                wait();
            System.out.println("TIC");
            inTic = true;
            notifyAll();
        }
    }

    public void hacerTac() throws InterruptedException {
        synchronized (this){
            while (!canTac())
                wait();
            System.out.println("TAC");
            inTic = false;
            notifyAll();
        }

    }

    public synchronized boolean canTic(){
        return !inTic;
    }

    public synchronized boolean canTac(){
        return inTic;
    }
}

class Ticker extends Thread{
    int id;
    Reloj reloj;

    public Ticker(int id, Reloj reloj) {
        this.id = id;
        this.reloj = reloj;
    }

    @Override
    public void run() {
        for (int i = 0; i < Valores.CANTIDAD_DE_TICS_Y_TACKS;i++){
            try {
                Thread.sleep(Valores.r.nextInt(Valores.MIN_ESPERA_TIC,Valores.MAX_ESPERA_TIC));
                reloj.hacerTic();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }
}

class Tacker extends Thread{
    int id;
    Reloj reloj;

    public Tacker(int id, Reloj reloj) {
        this.id = id;
        this.reloj = reloj;
    }

    @Override
    public void run() {
        for (int i = 0; i < Valores.CANTIDAD_DE_TICS_Y_TACKS;i++){
            try {
                Thread.sleep(Valores.r.nextInt(Valores.MIN_ESPERA_TAC,Valores.MAX_ESPERA_TAC));
                reloj.hacerTac();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }
}


