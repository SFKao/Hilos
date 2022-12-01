package notifywait.ejercicios;

import java.util.Random;

public class Puentes {

    public static void main(String[] args) {
        Thread[] hilos = new Thread[10];
        Puente p = new Puente();
        for (int i = 0; i < hilos.length; i++) {
            hilos[i] = new Personas(p,i+1);
        }
        for (Thread t: hilos) {
            t.start();
        }
        for (Thread t: hilos) {
            try {
                t.join();
            } catch (InterruptedException e) {}
        }
    }

}


class Personas extends Thread{
    private final Puente puente;
    private int peso;
    private int id;

    public Personas(Puente puente, int id) {
        this.puente = puente;
        this.id = id;
    }

    @Override
    public void run() {
        for(int i = 0; i < 10; i++){
            nuevoPeso();
            try {
                Thread.sleep(new Random().nextInt(10,301));
            } catch (InterruptedException e) {return;}
            synchronized (puente) {
                while (!puente.puedePasar(peso)) {
                    try {
                        System.out.printf("%d no puede pasar, hay %s personas dentro%n", id, puente.getPersonasAcutales());
                        puente.wait();
                    } catch (InterruptedException e) {
                        return;
                    }
                }
                System.out.printf("%d entrando%n", id);
                puente.entrar(peso);
            }
            try {
                System.out.println("Hay " + puente.getPersonasAcutales() + " personas dentro");
                Thread.sleep(new Random().nextInt(100, 501));
            } catch (InterruptedException e) {
                return;
            }
            synchronized (puente) {
                puente.salir(peso);
                puente.notifyAll();
                System.out.printf("%d saliendo%n", id);
            }
        }
    }

    private void nuevoPeso(){
        peso = new Random().nextInt(40,121);
    }
}

class Puente {
    public static final int MAX_PERSONAS = 3;
    public static final int MAX_KILOS = 200;

    private int personasAcutales = 0;
    private int pesoActual = 0;

    public Puente() {
    }

    public synchronized void entrar(int peso){
        personasAcutales++;
        pesoActual += peso;
    }

    public synchronized void salir(int peso){
        personasAcutales--;
        pesoActual -= peso;
    }

    public synchronized boolean puedePasar(int peso){
        if(personasAcutales==MAX_PERSONAS)
            return false;
        return pesoActual+peso <= MAX_KILOS;
    }

    public synchronized int getPersonasAcutales() {
        return personasAcutales;
    }
}
