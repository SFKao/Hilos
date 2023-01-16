package productorConsumidor.explicacion.semaforo;

import java.util.concurrent.Semaphore;

class Valores{
    public static final int NUM_CONSUMIDORES = 2;
    public static final int NUM_PRODUCTORES = 2;
    public static final int MAX_HILOS_EN_ALMACEN = 3;
}

public class ProductorConsumidor {
    public static void main(String[] args) {
        Almacen a = new Almacen();
        for (int i = 0; i < Valores.NUM_CONSUMIDORES; i++) {
            new Consumidor(a, String.valueOf(i+1)).start();
        }
        for (int i = 0; i < Valores.NUM_PRODUCTORES; i++) {
            new Productor(a, String.valueOf(i+1)).start();
        }
    }
}

class Productor extends Thread{
    Almacen a;
    String nombre;

    public Productor(Almacen a, String nombre) {
        this.a = a;
        this.nombre = nombre;
    }

    @Override
    public void run() {
        while (true){
            try {
                a.put();
                System.out.println(nombre+" produce");
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }
}

class Consumidor extends Thread{
    Almacen a;
    String nombre;

    public Consumidor(Almacen a, String nombre) {
        this.a = a;
        this.nombre = nombre;
    }

    @Override
    public void run() {
        while (true){
            try {
                a.get();
                System.out.println(nombre+" consume");
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }
}

class Almacen{
    Semaphore semaforoConsumidor = new Semaphore(0);
    Semaphore semaforoProductor = new Semaphore(Valores.MAX_HILOS_EN_ALMACEN);
    Semaphore mutex = new Semaphore(1);
    int cantidad = 0;
    public void get() throws InterruptedException {
        semaforoConsumidor.acquire();
        mutex.acquire();
        --cantidad;
        System.out.println(cantidad);
        mutex.release();
        semaforoProductor.release();
    }

    public void put() throws InterruptedException {
        semaforoProductor.acquire();
        mutex.acquire();
        cantidad++;
        System.out.println(cantidad);
        mutex.release();
        semaforoConsumidor.release();
    }
}