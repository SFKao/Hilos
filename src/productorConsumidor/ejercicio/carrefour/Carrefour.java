package productorConsumidor.ejercicio.carrefour;

import java.util.Random;
import java.util.concurrent.Semaphore;

class Valores{
    public static final int NUM_CAJAS = 5;
    public static final int NUM_CLIENTES = 12;
    public static final int MIN_TIEMPO_CAJA = 200;
    public static final int MAX_TIEMPO_CAJA = 1000;
    public static final Random r = new Random();
}

public class Carrefour {
    public static void main(String[] args) {
        FilaDeCajas a = new FilaDeCajas();
        for (int i = 0; i < Valores.NUM_CAJAS; i++)
            new Caja("CAJA "+String.valueOf(i+1), a).start();

        for (int i = 0; i < Valores.NUM_CLIENTES; i++)
            new Cliente("CLIENTE "+String.valueOf(i+1), a).start();
    }

}

class Caja extends Thread{
    String nombre;
    FilaDeCajas filaDeCajas;

    public Caja(String nombre, FilaDeCajas filaDeCajas) {
        this.nombre = nombre;
        this.filaDeCajas = filaDeCajas;
    }

    @Override
    public void run() {
        while (true)
            filaDeCajas.usarCaja(nombre);
    }
}

class Cliente extends Thread{
    String nombre;
    FilaDeCajas filaDeCajas;

    public Cliente(String nombre, FilaDeCajas filaDeCajas) {
        this.nombre = nombre;
        this.filaDeCajas = filaDeCajas;
    }

    @Override
    public void run() {
        while(true)
            filaDeCajas.compra(nombre,Valores.r.nextInt(1000,50000));
    }
}

class FilaDeCajas{
    Semaphore semaforoCliente = new Semaphore(0);
    Semaphore semaforoCajas = new Semaphore(Valores.NUM_CAJAS);
    Semaphore mutex = new Semaphore(1);
    int centimosTotal = 0;
    int cantidad=0;

    public void compra(String nombre, int precioEnCentimos){
        try {
            semaforoCliente.acquire();
            System.out.println(nombre+" entra a las cajas");
            Thread.sleep(Valores.r.nextInt(Valores.MIN_TIEMPO_CAJA,Valores.MAX_TIEMPO_CAJA));
            mutex.acquire();
            centimosTotal +=precioEnCentimos;
            cantidad++;
            mutex.release();
            System.out.println(nombre+" termina la compra");
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }finally {
            semaforoCajas.release();
        }
    }

    public int usarCaja(String nombre){
        try{
            semaforoCajas.acquire();
            System.out.println(nombre+" maneja la caja");
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }finally {
            semaforoCliente.release();
        }
        return 0;
    }

}
