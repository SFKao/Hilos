package productorConsumidor.explicacion.recursoThreadSafe;

import java.util.Random;

class Valores{
    public static final int MIN_ESPERA_CON = 2;
    public static final int MAX_ESPERA_CON = 5;
    public static final int MIN_ESPERA_PRO = 2;
    public static final int MAX_ESPERA_PRO = 5;
    public static final int NUM_CONSUMIDORES = 2;
    public static final int NUM_PRODUCTORES = 2;
}

public class ProductorConsumidor {
    public static void main(String[] args) {
        Thread[] consumidores = new Consumidor[Valores.NUM_CONSUMIDORES];
        Thread[] productores = new Productor[Valores.NUM_PRODUCTORES];
        Recurso<Integer> recurso = new Recurso<>();
        for(int i = 0; i <consumidores.length; i++)
            consumidores[i] = new Consumidor(String.valueOf(i+1),recurso);

        for(int i = 0; i< productores.length;i++)
            productores[i] = new Productor(String.valueOf(i+1),recurso);

        for (Thread t : consumidores)
            t.start();
        for (Thread t : productores)
            t.start();
    }
}

class Productor extends Thread{
    String nombre;
    final Recurso<Integer> recurso;
    static Random r = new Random();

    public Productor(String nombre, Recurso<Integer> recurso) {
        this.nombre = nombre;
        this.recurso = recurso;
    }

    @Override
    public void run() {
        while(true){
            try {
                Thread.sleep(r.nextInt(Valores.MIN_ESPERA_PRO,Valores.MAX_ESPERA_PRO));
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            int v = r.nextInt(0, 100);
            try {
                recurso.setValor(v);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            System.out.printf("Soy el productor %s y he producido el valor: %d%n",nombre,v);
        }
    }
}

class Consumidor extends Thread{
    String nombre;
    final Recurso<Integer> recurso;
    static Random r = new Random();

    public Consumidor(String nombre, Recurso<Integer> recurso) {
        this.nombre = nombre;
        this.recurso = recurso;
    }

    @Override
    public void run() {
        while(true){
            try {
                Thread.sleep(r.nextInt(Valores.MIN_ESPERA_CON,Valores.MAX_ESPERA_CON));
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            int v = 0;
            try {
                v = recurso.getValor();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            System.out.printf("Soy el consumidor %s y he consumido el valor: %d%n",nombre,v);
        }
    }
}

class Recurso<T> {
    private T valor = null;

    synchronized T getValor() throws InterruptedException {
        synchronized (this){
            while (!datoDisponible()){
                wait();
            }
            T valorDevolver = valor;
            valor = null;
            notifyAll();
            return valorDevolver;
        }
    }

    synchronized void setValor(T v) throws InterruptedException {
        synchronized (this){
            while (datoDisponible())
                wait();
            valor = v;
            notifyAll();
        }
    }

    synchronized boolean datoDisponible(){
        return valor!=null;
    }
}
